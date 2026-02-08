package com.example.cdplayer.ui.screens.stamp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cdplayer.data.local.dao.StampDao
import com.example.cdplayer.data.local.entity.StampEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random
import java.util.Calendar

data class StampUiState(
    val currentBoardNumber: Int = 1,
    val stamps: List<StampEntity> = emptyList(),
    val totalStamps: Int = 0,
    val isCharging: Boolean = false,
    val chargeProgress: Float = 0f,
    val justCollectedStamp: StampEntity? = null,
    val showBigReveal: Boolean = false,
    val showCompletionCelebration: Boolean = false,
    val canCollectStamp: Boolean = true,
    val alreadyCollectedToday: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class StampViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val stampDao: StampDao
) : ViewModel() {

    companion object {
        const val TOTAL_STAMP_IMAGES = 28
        const val STAMPS_PER_BOARD = 12
        const val CHARGE_DURATION_MS = 2000L  // 2 seconds long press
        const val CHARGE_TICK_MS = 16L         // ~60fps update
    }

    private val _uiState = MutableStateFlow(StampUiState())
    val uiState: StateFlow<StampUiState> = _uiState.asStateFlow()

    private var chargeJob: Job? = null

    private fun getStartOfToday(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    private suspend fun checkTodayStamp() {
        val count = stampDao.getStampsCollectedSince(getStartOfToday())
        val collectedToday = count > 0
        _uiState.update { it.copy(
            alreadyCollectedToday = collectedToday,
            canCollectStamp = !collectedToday && it.stamps.size < STAMPS_PER_BOARD
        ) }
    }

    init {
        viewModelScope.launch {
            val maxBoard = stampDao.getMaxBoardNumber()
            val initialBoard = if (maxBoard == 0) 1 else {
                val count = stampDao.getStampCountForBoard(maxBoard)
                if (count >= STAMPS_PER_BOARD) maxBoard + 1 else maxBoard
            }
            _uiState.update { it.copy(currentBoardNumber = initialBoard) }
            checkTodayStamp()
        }

        viewModelScope.launch {
            _uiState.map { it.currentBoardNumber }
                .distinctUntilChanged()
                .flatMapLatest { boardNum ->
                    stampDao.getStampsByBoard(boardNum)
                }
                .collect { stamps ->
                    val boardFull = stamps.size >= STAMPS_PER_BOARD
                    _uiState.update { it.copy(
                        stamps = stamps,
                        canCollectStamp = !boardFull && !it.alreadyCollectedToday
                    ) }
                }
        }

        viewModelScope.launch {
            stampDao.getTotalStampCount().collect { total ->
                _uiState.update { it.copy(totalStamps = total) }
            }
        }
    }

    fun onChargeStart() {
        val state = _uiState.value
        if (!state.canCollectStamp) return

        chargeJob?.cancel()
        _uiState.update { it.copy(isCharging = true, chargeProgress = 0f, justCollectedStamp = null) }

        chargeJob = viewModelScope.launch {
            var elapsed = 0L
            while (elapsed < CHARGE_DURATION_MS) {
                delay(CHARGE_TICK_MS)
                elapsed += CHARGE_TICK_MS
                val progress = (elapsed.toFloat() / CHARGE_DURATION_MS).coerceAtMost(1f)
                _uiState.update { it.copy(chargeProgress = progress) }
            }
            // Charge complete - collect stamp!
            collectStamp()
        }
    }

    fun onChargeEnd() {
        // If charge wasn't complete, cancel
        if (_uiState.value.chargeProgress < 1f) {
            chargeJob?.cancel()
            _uiState.update { it.copy(isCharging = false, chargeProgress = 0f) }
        }
    }

    private suspend fun collectStamp() {
        val currentBoard = _uiState.value.currentBoardNumber
        val currentCount = stampDao.getStampCountForBoard(currentBoard)

        if (currentCount >= STAMPS_PER_BOARD) {
            // Board is full, shouldn't happen but handle gracefully
            _uiState.update { it.copy(isCharging = false, chargeProgress = 0f) }
            return
        }

        // Pick random stamp image
        val randomImageNum = Random.nextInt(1, TOTAL_STAMP_IMAGES + 1)
        val imageName = "stamp_%02d.png".format(randomImageNum)

        val newStamp = StampEntity(
            stampImageName = imageName,
            boardNumber = currentBoard,
            position = currentCount // next available position
        )

        stampDao.insertStamp(newStamp)

        _uiState.update { it.copy(
            isCharging = false,
            chargeProgress = 0f,
            justCollectedStamp = newStamp,
            showBigReveal = true,
            alreadyCollectedToday = true,
            canCollectStamp = false
        ) }

        // Check if board is now complete
        val newCount = currentCount + 1
        if (newCount >= STAMPS_PER_BOARD) {
            delay(3500) // Let the big reveal + slam animation play
            _uiState.update { it.copy(showCompletionCelebration = true, canCollectStamp = false) }
        }
    }

    fun onStampAnimationComplete() {
        _uiState.update { it.copy(justCollectedStamp = null) }
    }

    fun onBigRevealComplete() {
        _uiState.update { it.copy(showBigReveal = false) }
    }

    fun onCompletionCelebrationDismissed() {
        _uiState.update { it.copy(showCompletionCelebration = false) }
        // Start a new board
        viewModelScope.launch {
            val newBoardNumber = _uiState.value.currentBoardNumber + 1
            _uiState.update { it.copy(
                currentBoardNumber = newBoardNumber,
                canCollectStamp = true
            ) }
        }
    }

    fun navigateToBoard(boardNumber: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(currentBoardNumber = boardNumber) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        chargeJob?.cancel()
    }
}
