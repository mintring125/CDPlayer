package com.example.cdplayer.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cdplayer.data.repository.AudioRepository
import com.example.cdplayer.domain.model.AudioFile
import com.example.cdplayer.player.MusicPlayerManager
import com.example.cdplayer.util.MediaScanner
import com.example.cdplayer.util.ScanProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.cdplayer.ui.components.SelectionState
import com.example.cdplayer.domain.model.AudioType

data class HomeUiState(
    val isScanning: Boolean = false,
    val scanProgress: ScanProgress? = null,
    val error: String? = null
)

data class AudiobookAlbum(
    val albumName: String,
    val coverArtPath: String?,
    val coverArtUri: String?,
    val trackCount: Int,
    val tracks: List<AudioFile>
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val audioRepository: AudioRepository,
    private val mediaScanner: MediaScanner,
    private val musicPlayerManager: MusicPlayerManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    val recentlyPlayed = audioRepository.getRecentlyPlayed(10)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allMusic = audioRepository.getAllMusic()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allAudiobooks = audioRepository.getAllAudiobooks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Audiobooks grouped by album
    val audiobookAlbums: StateFlow<List<AudiobookAlbum>> = audioRepository.getAllAudiobooks()
        .map { audiobooks ->
            audiobooks.groupBy { it.album ?: "알 수 없는 앨범" }
                .map { (albumName, tracks) ->
                    val tracksSorted = tracks.sortedBy { it.trackNumber ?: Int.MAX_VALUE }
                    // 앨범 내의 트랙 중 커버 이미지가 있는 첫 번째 트랙을 찾음
                    val trackWithCover = tracksSorted.find { !it.coverArtPath.isNullOrBlank() || !it.coverArtUri.isNullOrBlank() }
                    
                    AudiobookAlbum(
                        albumName = albumName,
                        coverArtPath = trackWithCover?.coverArtPath,
                        coverArtUri = trackWithCover?.coverArtUri,
                        trackCount = tracks.size,
                        tracks = tracksSorted
                    )
                }
                .sortedBy { it.albumName }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val favorites = audioRepository.getFavorites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val playbackState = musicPlayerManager.playbackState

    private val _selectionState = MutableStateFlow(SelectionState())
    val selectionState: StateFlow<SelectionState> = _selectionState.asStateFlow()

    init {
        // 앱 시작 시 미디어 스캔
        scanMedia()
    }

    fun scanMedia() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isScanning = true, error = null)

            try {
                mediaScanner.scanMediaFiles().collect { progress ->
                    _uiState.value = _uiState.value.copy(scanProgress = progress)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            } finally {
                _uiState.value = _uiState.value.copy(isScanning = false, scanProgress = null)
            }
        }
    }

    fun playTrack(audioFile: AudioFile) {
        musicPlayerManager.play(audioFile)
    }

    fun playTracks(tracks: List<AudioFile>, startIndex: Int = 0) {
        musicPlayerManager.playQueue(tracks, startIndex)
    }

    fun addToQueue(audioFile: AudioFile) {
        musicPlayerManager.addToQueue(audioFile)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun toggleSelection(audioId: Long) {
        _selectionState.update { it.toggle(audioId) }
    }

    fun clearSelection() {
        _selectionState.update { it.clear() }
    }

    fun toggleFavorite(audioId: Long) {
        viewModelScope.launch {
            val audio = audioRepository.getAudioFileById(audioId)
            audio?.let {
                audioRepository.updateAudioFile(it.copy(isFavorite = !it.isFavorite))
            }
        }
    }

    fun moveSelectedFiles(targetPath: String) {
        val selectedIds = _selectionState.value.selectedIds.toList()
        if (selectedIds.isEmpty()) return

        viewModelScope.launch {
            try {
                // allMusic and allAudiobooks are StateFlows of lists.
                // We need to look in both because user might be in All Music or Audiobooks tab.
                // Or rather, just query repository by ID, but we have the list in memory.
                val currentFiles = allMusic.value + allAudiobooks.value
                
                selectedIds.forEach { id ->
                    val audioFile = currentFiles.find { it.id == id }
                    if (audioFile != null) {
                        val sourceFile = java.io.File(audioFile.path)
                        val targetDir = java.io.File(targetPath)
                        val targetFile = java.io.File(targetDir, sourceFile.name)

                        // 타겟 폴더가 없으면 생성
                        if (!targetDir.exists()) targetDir.mkdirs()

                        if (sourceFile.exists()) {
                            // 같은 이름의 파일이 있으면 건너뛰거나 이름 변경 (여기선 건너뜀)
                            if (targetFile.exists()) return@forEach
                            
                            val success = sourceFile.renameTo(targetFile)
                            if (success) {
                                // DB 업데이트
                                audioRepository.updateAudioFile(audioFile.copy(path = targetFile.absolutePath))
                            }
                        }
                    }
                }
                clearSelection()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "파일 이동 중 오류 발생: ${e.message}")
            }
        }
    }

    fun changeSelectedAudioType(newType: com.example.cdplayer.domain.model.AudioType) {
        val selectedIds = _selectionState.value.selectedIds.toList()
        if (selectedIds.isEmpty()) return

        viewModelScope.launch {
            try {
                val currentFiles = allMusic.value + allAudiobooks.value
                selectedIds.forEach { id ->
                    val audioFile = currentFiles.find { it.id == id }
                    audioFile?.let {
                        audioRepository.updateAudioFile(it.copy(type = newType))
                    }
                }
                clearSelection()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "유형 변경 중 오류 발생: ${e.message}") }
            }
        }
    }

    fun deleteSelectedFiles() {
        val selectedIds = _selectionState.value.selectedIds.toList()
        if (selectedIds.isEmpty()) return

        viewModelScope.launch {
            try {
                // 개별 삭제 (일괄 삭제 API가 없으므로)
                selectedIds.forEach { id ->
                    audioRepository.deleteAudioFileById(id)
                }
                clearSelection()
                // 미디어 스캔 다시 돌려서 파일 동기화
                // scanMedia() // Optional, DB sync might be enough
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "파일 삭제 중 오류 발생: ${e.message}")
            }
        }
    }

    fun addSelectedToPlaylist(playlistId: Long) {
        // TODO: Implement playlist add logic
         viewModelScope.launch {
            clearSelection()
        }
    }

    fun moveAlbumToAudiobook(album: AudioFile) {
        viewModelScope.launch {
            try {
                if (album.album != null) {
                    audioRepository.moveAlbumToType(album.album, AudioType.AUDIOBOOK)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "오디오북으로 이동 중 오류 발생: ${e.message}") }
            }
        }
    }

    fun moveAlbumToMusic(album: AudioFile) {
        viewModelScope.launch {
            try {
                if (album.album != null) {
                    audioRepository.moveAlbumToType(album.album, AudioType.MUSIC)
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "음악으로 이동 중 오류 발생: ${e.message}") }
            }
        }
    }

    fun removeFromAlbum(audioFile: AudioFile) {
        viewModelScope.launch {
            try {
                // Set album to the track title to effectively "remove" it from the album
                audioRepository.updateAlbumForIds(listOf(audioFile.id), audioFile.title)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "앨범에서 빼기 중 오류 발생: ${e.message}") }
            }
        }
    }

    // Audiobook selection state for multi-select album grouping
    private val _audiobookSelectionState = MutableStateFlow(SelectionState())
    val audiobookSelectionState: StateFlow<SelectionState> = _audiobookSelectionState.asStateFlow()

    fun toggleAudiobookSelection(audioId: Long) {
        _audiobookSelectionState.update { it.toggle(audioId) }
    }

    fun clearAudiobookSelection() {
        _audiobookSelectionState.update { it.clear() }
    }

    fun groupSelectedAsAlbum(newAlbumName: String) {
        val selectedIds = _audiobookSelectionState.value.selectedIds.toList()
        if (selectedIds.isEmpty() || newAlbumName.isBlank()) return

        viewModelScope.launch {
            try {
                audioRepository.updateAlbumForIds(selectedIds, newAlbumName)
                clearAudiobookSelection()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "앨범 그룹화 중 오류 발생: ${e.message}") }
            }
        }
    }

    fun deleteSelectedAudiobooks() {
        val selectedIds = _audiobookSelectionState.value.selectedIds.toList()
        if (selectedIds.isEmpty()) return

        viewModelScope.launch {
            try {
                val currentAudiobooks = allAudiobooks.value
                selectedIds.forEach { id ->
                    val audioFile = currentAudiobooks.find { it.id == id }
                    if (audioFile != null) {
                        // 실제 파일 삭제
                        val file = java.io.File(audioFile.path)
                        if (file.exists()) {
                            file.delete()
                        }
                        // DB에서 삭제
                        audioRepository.deleteAudioFileById(id)
                    }
                }
                clearAudiobookSelection()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "파일 삭제 중 오류 발생: ${e.message}") }
            }
        }
    }

    fun renameAlbum(album: AudiobookAlbum, newName: String) {
        if (newName.isBlank()) return
        val trackIds = album.tracks.map { it.id }
        viewModelScope.launch {
            try {
                audioRepository.updateAlbumForIds(trackIds, newName)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "앨범 이름 변경 중 오류 발생: ${e.message}") }
            }
        }
    }

    fun renameSelectedAudiobooks(newName: String) {
        val selectedIds = _audiobookSelectionState.value.selectedIds.toList()
        if (selectedIds.isEmpty() || newName.isBlank()) return

        viewModelScope.launch {
            try {
                audioRepository.updateAlbumForIds(selectedIds, newName)
                clearAudiobookSelection()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "앨범 이름 변경 중 오류 발생: ${e.message}") }
            }
        }
    }
}

