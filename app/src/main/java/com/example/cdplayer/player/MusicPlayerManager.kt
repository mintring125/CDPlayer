package com.example.cdplayer.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.cdplayer.data.repository.AudioRepository
import com.example.cdplayer.domain.model.AudioFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicPlayerManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val audioRepository: AudioRepository
) {
    private var exoPlayer: ExoPlayer? = null

    private val _playbackState = MutableStateFlow(PlaybackState())
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var positionUpdateJob: Job? = null
    private var sleepTimerJob: Job? = null

    private val playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _playbackState.update { it.copy(isPlaying = isPlaying) }
            if (isPlaying) {
                startPositionUpdates()
            } else {
                stopPositionUpdates()
            }
        }

        override fun onPlaybackStateChanged(state: Int) {
            when (state) {
                Player.STATE_READY -> {
                    _playbackState.update {
                        it.copy(
                            duration = exoPlayer?.duration ?: 0,
                            isLoading = false
                        )
                    }
                }
                Player.STATE_BUFFERING -> {
                    _playbackState.update { it.copy(isLoading = true) }
                }
                Player.STATE_ENDED -> {
                    handlePlaybackEnded()
                }
                Player.STATE_IDLE -> {
                    _playbackState.update { it.copy(isLoading = false) }
                }
            }
        }

        override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
            updateCurrentTrackFromMediaItem(mediaItem)
        }

        override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
            _playbackState.update { it.copy(error = error.message, isLoading = false) }
        }
    }

    fun initialize() {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build().apply {
                addListener(playerListener)
            }
        }
    }

    fun release() {
        stopPositionUpdates()
        sleepTimerJob?.cancel()
        exoPlayer?.removeListener(playerListener)
        exoPlayer?.release()
        exoPlayer = null
    }

    fun getPlayer(): ExoPlayer? = exoPlayer

    fun play(audioFile: AudioFile) {
        val queue = listOf(audioFile)
        playQueue(queue, 0)
    }

    fun playQueue(queue: List<AudioFile>, startIndex: Int = 0) {
        exoPlayer?.let { player ->
            val mediaItems = queue.map { audio ->
                MediaItem.Builder()
                    .setUri(audio.path)
                    .setMediaId(audio.id.toString())
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle(audio.title)
                            .setArtist(audio.artist)
                            .setAlbumTitle(audio.album)
                            .build()
                    )
                    .build()
            }

            player.setMediaItems(mediaItems, startIndex, 0)
            player.prepare()
            player.play()

            _playbackState.update {
                it.copy(
                    queue = queue,
                    currentQueueIndex = startIndex,
                    currentTrack = queue.getOrNull(startIndex),
                    isLoading = true,
                    error = null
                )
            }
        }
    }

    fun addToQueue(audioFile: AudioFile) {
        exoPlayer?.let { player ->
            val mediaItem = MediaItem.Builder()
                .setUri(audioFile.path)
                .setMediaId(audioFile.id.toString())
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(audioFile.title)
                        .setArtist(audioFile.artist)
                        .setAlbumTitle(audioFile.album)
                        .build()
                )
                .build()

            player.addMediaItem(mediaItem)

            _playbackState.update {
                it.copy(queue = it.queue + audioFile)
            }
        }
    }

    fun playPause() {
        exoPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }
    }

    fun pause() {
        exoPlayer?.pause()
    }

    fun resume() {
        exoPlayer?.play()
    }

    fun stop() {
        exoPlayer?.stop()
        stopPositionUpdates()
        _playbackState.update { PlaybackState() }
    }

    fun seekTo(position: Long) {
        exoPlayer?.seekTo(position)
        _playbackState.update { it.copy(currentPosition = position) }
    }

    fun seekToPercent(percent: Float) {
        val duration = _playbackState.value.duration
        if (duration > 0) {
            val position = (duration * percent).toLong()
            seekTo(position)
        }
    }

    fun skipForward(seconds: Int = 10) {
        exoPlayer?.let { player ->
            val newPosition = (player.currentPosition + seconds * 1000).coerceAtMost(player.duration)
            seekTo(newPosition)
        }
    }

    fun skipBackward(seconds: Int = 10) {
        exoPlayer?.let { player ->
            val newPosition = (player.currentPosition - seconds * 1000).coerceAtLeast(0)
            seekTo(newPosition)
        }
    }

    fun next() {
        exoPlayer?.let { player ->
            if (player.hasNextMediaItem()) {
                player.seekToNextMediaItem()
            } else if (_playbackState.value.repeatMode == RepeatMode.ALL && _playbackState.value.queue.isNotEmpty()) {
                player.seekTo(0, 0)
            }
        }
    }

    fun previous() {
        exoPlayer?.let { player ->
            if (player.currentPosition > 3000) {
                // 3초 이상 재생했으면 처음으로
                player.seekTo(0)
            } else if (player.hasPreviousMediaItem()) {
                player.seekToPreviousMediaItem()
            } else if (_playbackState.value.repeatMode == RepeatMode.ALL && _playbackState.value.queue.isNotEmpty()) {
                player.seekTo(player.mediaItemCount - 1, 0)
            }
        }
    }

    fun setRepeatMode(mode: RepeatMode) {
        exoPlayer?.repeatMode = when (mode) {
            RepeatMode.OFF -> Player.REPEAT_MODE_OFF
            RepeatMode.ONE -> Player.REPEAT_MODE_ONE
            RepeatMode.ALL -> Player.REPEAT_MODE_ALL
        }
        _playbackState.update { it.copy(repeatMode = mode) }
    }

    fun toggleRepeatMode() {
        val currentMode = _playbackState.value.repeatMode
        val nextMode = when (currentMode) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
        setRepeatMode(nextMode)
    }

    fun setShuffleEnabled(enabled: Boolean) {
        exoPlayer?.shuffleModeEnabled = enabled
        _playbackState.update { it.copy(shuffleEnabled = enabled) }
    }

    fun toggleShuffle() {
        setShuffleEnabled(!_playbackState.value.shuffleEnabled)
    }

    fun setPlaybackSpeed(speed: Float) {
        exoPlayer?.playbackParameters = PlaybackParameters(speed)
        _playbackState.update { it.copy(playbackSpeed = speed) }
    }

    fun setSleepTimer(minutes: Int) {
        sleepTimerJob?.cancel()
        if (minutes > 0) {
            sleepTimerJob = coroutineScope.launch {
                delay(minutes * 60 * 1000L)
                pause()
            }
        }
    }

    fun cancelSleepTimer() {
        sleepTimerJob?.cancel()
        sleepTimerJob = null
    }

    private fun startPositionUpdates() {
        positionUpdateJob?.cancel()
        positionUpdateJob = coroutineScope.launch {
            while (true) {
                exoPlayer?.let { player ->
                    val position = player.currentPosition
                    _playbackState.update { it.copy(currentPosition = position) }

                    // 재생 위치 저장
                    _playbackState.value.currentTrack?.let { track ->
                        audioRepository.updatePlaybackPosition(track.id, position)
                    }
                }
                delay(1000)
            }
        }
    }

    private fun stopPositionUpdates() {
        positionUpdateJob?.cancel()
        positionUpdateJob = null
    }

    private fun handlePlaybackEnded() {
        when (_playbackState.value.repeatMode) {
            RepeatMode.ONE -> {
                exoPlayer?.seekTo(0)
                exoPlayer?.play()
            }
            RepeatMode.ALL -> {
                if (!_playbackState.value.hasNext) {
                    exoPlayer?.seekTo(0, 0)
                    exoPlayer?.play()
                }
            }
            RepeatMode.OFF -> {
                // 재생 종료
            }
        }
    }

    private fun updateCurrentTrackFromMediaItem(mediaItem: MediaItem?) {
        if (mediaItem == null) {
            _playbackState.update { it.copy(currentTrack = null, currentQueueIndex = -1) }
            return
        }

        val mediaId = mediaItem.mediaId.toLongOrNull()
        val queue = _playbackState.value.queue
        val index = queue.indexOfFirst { it.id == mediaId }

        if (index >= 0) {
            _playbackState.update {
                it.copy(
                    currentTrack = queue[index],
                    currentQueueIndex = index
                )
            }
        }
    }

    fun clearError() {
        _playbackState.update { it.copy(error = null) }
    }
}
