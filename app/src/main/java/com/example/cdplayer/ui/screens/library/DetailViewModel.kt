package com.example.cdplayer.ui.screens.library

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cdplayer.data.repository.AudioRepository
import com.example.cdplayer.domain.model.AudioFile
import com.example.cdplayer.player.MusicPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

import com.example.cdplayer.util.Id3TagWriter
import kotlinx.coroutines.launch

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val audioRepository: AudioRepository,
    private val musicPlayerManager: MusicPlayerManager,
    private val id3TagWriter: Id3TagWriter
) : ViewModel() {

    private val albumName: String? = savedStateHandle.get<String>("albumName")
    private val artistName: String? = savedStateHandle.get<String>("artistName")

    val title: String = albumName ?: artistName ?: ""

    val tracks: StateFlow<List<AudioFile>> = when {
        albumName != null -> audioRepository.getAudioFilesByAlbum(albumName)
        artistName != null -> audioRepository.getAudioFilesByArtist(artistName)
        else -> kotlinx.coroutines.flow.flowOf(emptyList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    val playbackState = musicPlayerManager.playbackState

    fun updateAlbumName(newName: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            val currentTracks = tracks.value
            currentTracks.forEach { track ->
                // 태그 업데이트
                id3TagWriter.writeMetadata(
                    filePath = track.path,
                    title = track.title,
                    artist = track.artist,
                    album = newName,
                    genre = track.genre,
                    year = track.year,
                    trackNumber = track.trackNumber
                )
                // DB 업데이트
                audioRepository.updateAudioFile(track.copy(album = newName))
            }
            onComplete()
        }
    }

    fun playTrack(track: AudioFile) {
        musicPlayerManager.play(track)
    }
    
    fun playTracks(tracks: List<AudioFile>, startIndex: Int) {
        musicPlayerManager.playQueue(tracks, startIndex)
    }

    fun addToQueue(track: AudioFile) {
        musicPlayerManager.addToQueue(track)
    }
}
