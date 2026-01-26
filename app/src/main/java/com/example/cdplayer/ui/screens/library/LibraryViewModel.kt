package com.example.cdplayer.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cdplayer.data.repository.AudioRepository
import com.example.cdplayer.data.repository.PlaylistRepository
import com.example.cdplayer.domain.model.AudioFile
import com.example.cdplayer.domain.model.Playlist
import com.example.cdplayer.player.MusicPlayerManager
import com.example.cdplayer.ui.components.SelectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class LibraryTab {
    MUSIC, AUDIOBOOKS, ARTISTS, ALBUMS, PLAYLISTS
}

enum class SortOption(val displayName: String) {
    ADDED_DATE_DESC("추가된 날짜 (최신순)"),
    ADDED_DATE_ASC("추가된 날짜 (오래된순)"),
    TITLE_ASC("제목 (가나다순)"),
    TITLE_DESC("제목 (역순)"),
    ARTIST_ASC("아티스트 (가나다순)"),
    DURATION_DESC("재생시간 (긴순)"),
    DURATION_ASC("재생시간 (짧은순)"),
    LAST_PLAYED("최근 재생순")
}

data class LibraryUiState(
    val selectedTab: LibraryTab = LibraryTab.MUSIC,
    val sortOption: SortOption = SortOption.ADDED_DATE_DESC,
    val isLoading: Boolean = false
)

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val audioRepository: AudioRepository,
    private val playlistRepository: PlaylistRepository,
    private val musicPlayerManager: MusicPlayerManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.ADDED_DATE_DESC)

    val allMusic = combine(
        audioRepository.getAllMusic(),
        _sortOption
    ) { music, sort ->
        sortAudioFiles(music, sort)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allAudiobooks = combine(
        audioRepository.getAllAudiobooks(),
        _sortOption
    ) { audiobooks, sort ->
        sortAudioFiles(audiobooks, sort)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private fun sortAudioFiles(files: List<AudioFile>, sortOption: SortOption): List<AudioFile> {
        return when (sortOption) {
            SortOption.ADDED_DATE_DESC -> files.sortedByDescending { it.addedDate }
            SortOption.ADDED_DATE_ASC -> files.sortedBy { it.addedDate }
            SortOption.TITLE_ASC -> files.sortedBy { it.title.lowercase() }
            SortOption.TITLE_DESC -> files.sortedByDescending { it.title.lowercase() }
            SortOption.ARTIST_ASC -> files.sortedBy { (it.artist ?: "").lowercase() }
            SortOption.DURATION_DESC -> files.sortedByDescending { it.duration }
            SortOption.DURATION_ASC -> files.sortedBy { it.duration }
            SortOption.LAST_PLAYED -> files.sortedByDescending { it.lastPlayedAt ?: 0L }
        }
    }

    val allArtists = audioRepository.getAllArtists()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allAlbums = audioRepository.getAllAlbums()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allPlaylists = playlistRepository.getAllPlaylists()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val playbackState = musicPlayerManager.playbackState

    // Selection state for multi-select
    private val _selectionState = MutableStateFlow(SelectionState())
    val selectionState: StateFlow<SelectionState> = _selectionState.asStateFlow()

    fun toggleSelection(audioId: Long) {
        _selectionState.update { it.toggle(audioId) }
    }

    fun clearSelection() {
        _selectionState.update { it.clear() }
    }

    fun groupSelectedAsAlbum(newAlbumName: String) {
        val selectedIds = _selectionState.value.selectedIds.toList()
        if (selectedIds.isEmpty() || newAlbumName.isBlank()) return

        viewModelScope.launch {
            audioRepository.updateAlbumForIds(selectedIds, newAlbumName)
            clearSelection()
        }
    }

    fun removeFromAlbum(audioFile: AudioFile) {
        viewModelScope.launch {
            // Set album to the track title to effectively "remove" it from the album
            audioRepository.updateAlbumForIds(listOf(audioFile.id), audioFile.title)
        }
    }

    fun selectTab(tab: LibraryTab) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
        _uiState.value = _uiState.value.copy(sortOption = option)
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

    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            playlistRepository.deletePlaylist(playlistId)
        }
    }

    fun moveAlbumToType(album: AudioFile, type: com.example.cdplayer.domain.model.AudioType) {
        viewModelScope.launch {
            if (album.album != null) {
                audioRepository.moveAlbumToType(album.album, type)
            }
        }
    }

    fun moveAlbumToType(albumName: String, type: com.example.cdplayer.domain.model.AudioType) {
        viewModelScope.launch {
            audioRepository.moveAlbumToType(albumName, type)
        }
    }

    fun toggleFavorite(audioId: Long) {
        viewModelScope.launch {
            val audio = audioRepository.getAudioFileById(audioId)
            audio?.let {
                audioRepository.updateAudioFile(it.copy(isFavorite = !it.isFavorite))
            }
        }
    }

    fun deleteSelectedFiles() {
        val selectedIds = _selectionState.value.selectedIds.toList()
        if (selectedIds.isEmpty()) return

        viewModelScope.launch {
            try {
                selectedIds.forEach { id ->
                    deleteFileById(id)
                }
                clearSelection()
            } catch (e: Exception) {
                // 에러 처리 (필요시 UI에 표시)
            }
        }
    }

    fun deleteFile(audioFile: AudioFile) {
        viewModelScope.launch {
            try {
                // 실제 파일 삭제
                val file = java.io.File(audioFile.path)
                if (file.exists()) {
                    file.delete()
                }
                // DB에서 삭제
                audioRepository.deleteAudioFileById(audioFile.id)
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }

    private suspend fun deleteFileById(id: Long) {
        val audioFile = audioRepository.getAudioFileById(id)
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
}
