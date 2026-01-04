package com.example.cdplayer.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cdplayer.data.repository.AudioRepository
import com.example.cdplayer.domain.model.AudioFile
import com.example.cdplayer.player.MusicPlayerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val audioRepository: AudioRepository,
    private val musicPlayerManager: MusicPlayerManager
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val searchResults = _query
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) {
                flowOf(emptyList())
            } else {
                audioRepository.searchAudioFiles(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val playbackState = musicPlayerManager.playbackState

    fun updateQuery(query: String) {
        _query.value = query
    }

    fun clearQuery() {
        _query.value = ""
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
}
