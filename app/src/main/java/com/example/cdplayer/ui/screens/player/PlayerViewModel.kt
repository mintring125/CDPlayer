package com.example.cdplayer.ui.screens.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cdplayer.data.repository.BookmarkRepository
import com.example.cdplayer.data.repository.DictionaryRepository
import com.example.cdplayer.domain.model.Bookmark
import com.example.cdplayer.player.MusicPlayerManager
import com.example.cdplayer.player.PlaybackState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerUiState(
    val showSpeedDialog: Boolean = false,
    val showSleepTimerDialog: Boolean = false,
    val showQueueSheet: Boolean = false,
    val showBookmarkDialog: Boolean = false,
    val showDictionaryDialog: Boolean = false,
    val dictionaryResult: DictionaryResult? = null,
    val isDictionaryLoading: Boolean = false
)

sealed class DictionaryResult {
    data class Translation(val original: String, val translated: String) : DictionaryResult()
    data class Error(val message: String) : DictionaryResult()
}

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val musicPlayerManager: MusicPlayerManager,
    private val bookmarkRepository: BookmarkRepository,
    private val dictionaryRepository: DictionaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    val playbackState: StateFlow<PlaybackState> = musicPlayerManager.playbackState

    val bookmarks = playbackState
        .flatMapLatest { state ->
            state.currentTrack?.let { track ->
                bookmarkRepository.getBookmarksForAudio(track.id)
            } ?: flowOf(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun playPause() {
        musicPlayerManager.playPause()
    }

    fun next() {
        musicPlayerManager.next()
    }

    fun previous() {
        musicPlayerManager.previous()
    }

    fun seekTo(position: Long) {
        musicPlayerManager.seekTo(position)
    }

    fun skipForward(seconds: Int = 10) {
        musicPlayerManager.skipForward(seconds)
    }

    fun skipBackward(seconds: Int = 10) {
        musicPlayerManager.skipBackward(seconds)
    }

    fun toggleRepeatMode() {
        musicPlayerManager.toggleRepeatMode()
    }

    fun toggleShuffle() {
        musicPlayerManager.toggleShuffle()
    }

    fun setPlaybackSpeed(speed: Float) {
        musicPlayerManager.setPlaybackSpeed(speed)
        hideSpeedDialog()
    }

    fun setSleepTimer(minutes: Int) {
        musicPlayerManager.setSleepTimer(minutes)
        hideSleepTimerDialog()
    }

    fun cancelSleepTimer() {
        musicPlayerManager.cancelSleepTimer()
    }

    fun showSpeedDialog() {
        _uiState.value = _uiState.value.copy(showSpeedDialog = true)
    }

    fun hideSpeedDialog() {
        _uiState.value = _uiState.value.copy(showSpeedDialog = false)
    }

    fun showSleepTimerDialog() {
        _uiState.value = _uiState.value.copy(showSleepTimerDialog = true)
    }

    fun hideSleepTimerDialog() {
        _uiState.value = _uiState.value.copy(showSleepTimerDialog = false)
    }

    fun showQueueSheet() {
        _uiState.value = _uiState.value.copy(showQueueSheet = true)
    }

    fun hideQueueSheet() {
        _uiState.value = _uiState.value.copy(showQueueSheet = false)
    }

    fun showBookmarkDialog() {
        _uiState.value = _uiState.value.copy(showBookmarkDialog = true)
    }

    fun hideBookmarkDialog() {
        _uiState.value = _uiState.value.copy(showBookmarkDialog = false)
    }

    fun addBookmark(note: String? = null) {
        viewModelScope.launch {
            val state = playbackState.value
            state.currentTrack?.let { track ->
                bookmarkRepository.createBookmark(
                    audioFileId = track.id,
                    position = state.currentPosition,
                    note = note
                )
            }
            hideBookmarkDialog()
        }
    }

    fun deleteBookmark(bookmarkId: Long) {
        viewModelScope.launch {
            bookmarkRepository.deleteBookmark(bookmarkId)
        }
    }

    fun seekToBookmark(bookmark: Bookmark) {
        seekTo(bookmark.position)
    }

    // Dictionary
    fun showDictionaryDialog() {
        _uiState.value = _uiState.value.copy(showDictionaryDialog = true)
    }

    fun hideDictionaryDialog() {
        _uiState.value = _uiState.value.copy(
            showDictionaryDialog = false,
            dictionaryResult = null,
            isDictionaryLoading = false
        )
    }

    fun searchWord(text: String) {
        if (text.isBlank()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isDictionaryLoading = true,
                dictionaryResult = null
            )

            // 단어든 문장이든 모두 Google 번역
            val result = dictionaryRepository.translate(text)
            _uiState.value = _uiState.value.copy(
                isDictionaryLoading = false,
                dictionaryResult = result.fold(
                    onSuccess = { DictionaryResult.Translation(it.first, it.second) },
                    onFailure = { DictionaryResult.Error(it.message ?: "알 수 없는 오류") }
                )
            )
        }
    }

    fun clearDictionaryResult() {
        _uiState.value = _uiState.value.copy(dictionaryResult = null)
    }
}
