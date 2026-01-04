package com.example.cdplayer.ui.screens.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cdplayer.data.repository.PlaylistRepository
import com.example.cdplayer.domain.model.AudioFile
import com.example.cdplayer.domain.model.PlaylistWithTracks
import com.example.cdplayer.player.MusicPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val musicPlayerManager: MusicPlayerManager
) : ViewModel() {

    private var currentPlaylistId: Long = -1

    private val _playlistWithTracks = MutableStateFlow<PlaylistWithTracks?>(null)
    val playlistWithTracks: StateFlow<PlaylistWithTracks?> = _playlistWithTracks.asStateFlow()

    val playbackState = musicPlayerManager.playbackState

    fun loadPlaylist(playlistId: Long) {
        currentPlaylistId = playlistId
        viewModelScope.launch {
            playlistRepository.getPlaylistWithTracks(playlistId).collect { data ->
                _playlistWithTracks.value = data
            }
        }
    }

    fun playTrack(index: Int) {
        val tracks = _playlistWithTracks.value?.tracks ?: return
        musicPlayerManager.playQueue(tracks, index)
    }

    fun playAll() {
        val tracks = _playlistWithTracks.value?.tracks ?: return
        if (tracks.isNotEmpty()) {
            musicPlayerManager.playQueue(tracks, 0)
        }
    }

    fun shufflePlay() {
        val tracks = _playlistWithTracks.value?.tracks?.shuffled() ?: return
        if (tracks.isNotEmpty()) {
            musicPlayerManager.playQueue(tracks, 0)
        }
    }

    fun removeTrack(audioFileId: Long) {
        viewModelScope.launch {
            playlistRepository.removeTrackFromPlaylist(currentPlaylistId, audioFileId)
        }
    }

    fun addToQueue(audioFile: AudioFile) {
        musicPlayerManager.addToQueue(audioFile)
    }
}

@HiltViewModel
class CreatePlaylistViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val _isCreated = MutableStateFlow(false)
    val isCreated: StateFlow<Boolean> = _isCreated.asStateFlow()

    fun createPlaylist(name: String, description: String?) {
        if (name.isBlank()) return

        viewModelScope.launch {
            playlistRepository.createPlaylist(name, description)
            _isCreated.value = true
        }
    }
}
