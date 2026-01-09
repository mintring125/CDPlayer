package com.example.cdplayer.ui.screens.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cdplayer.data.repository.AudioRepository
import com.example.cdplayer.data.repository.PlaylistRepository
import com.example.cdplayer.domain.model.AudioFile
import com.example.cdplayer.domain.model.Playlist
import com.example.cdplayer.player.MusicPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.cdplayer.ui.components.SelectionState
import com.example.cdplayer.domain.model.AudioType

enum class LibraryTab {
    MUSIC, AUDIOBOOKS, ARTISTS, ALBUMS, PLAYLISTS
}

data class LibraryUiState(
    val selectedTab: LibraryTab = LibraryTab.MUSIC,
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

    private val _selectionState = MutableStateFlow(SelectionState())
    val selectionState: StateFlow<SelectionState> = _selectionState.asStateFlow()

    fun toggleSelection(audioId: Long) {
        _selectionState.update { it.toggle(audioId) }
    }

    fun clearSelection() {
        _selectionState.update { it.clear() }
    }

    fun selectTab(tab: LibraryTab) {
        clearSelection()
        _uiState.value = _uiState.value.copy(selectedTab = tab)
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

    fun moveTrackToType(audioFile: AudioFile, type: com.example.cdplayer.domain.model.AudioType) {
        viewModelScope.launch {
            audioRepository.updateAudioFile(audioFile.copy(type = type))
        }
    }

    fun moveAlbumToType(albumName: String, type: com.example.cdplayer.domain.model.AudioType) {
        viewModelScope.launch {
            audioRepository.moveAlbumToType(albumName, type)
        }
    }

    fun toggleFavorite(audioFile: AudioFile) {
        viewModelScope.launch {
            audioRepository.updateAudioFile(audioFile.copy(isFavorite = !audioFile.isFavorite))
        }
    }

    fun changeSelectedToType(type: AudioType) {
        val selectedIds = _selectionState.value.selectedIds.toList()
        if (selectedIds.isEmpty()) return

        viewModelScope.launch {
            val currentFiles = allMusic.value + allAudiobooks.value
            selectedIds.forEach { id ->
                val audioFile = currentFiles.find { it.id == id }
                audioFile?.let {
                    audioRepository.updateAudioFile(it.copy(type = type))
                }
            }
            clearSelection()
        }
    }

    fun deleteSelected() {
        val selectedIds = _selectionState.value.selectedIds.toList()
        if (selectedIds.isEmpty()) return

        viewModelScope.launch {
            selectedIds.forEach { id ->
                audioRepository.deleteAudioFileById(id)
            }
            clearSelection()
        }
    }
}
