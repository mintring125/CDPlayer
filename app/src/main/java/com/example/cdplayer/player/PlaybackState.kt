package com.example.cdplayer.player

import com.example.cdplayer.domain.model.AudioFile

data class PlaybackState(
    val currentTrack: AudioFile? = null,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0,
    val duration: Long = 0,
    val playbackSpeed: Float = 1.0f,
    val repeatMode: RepeatMode = RepeatMode.OFF,
    val shuffleEnabled: Boolean = false,
    val queue: List<AudioFile> = emptyList(),
    val currentQueueIndex: Int = -1,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val progress: Float
        get() = if (duration > 0) currentPosition.toFloat() / duration else 0f

    val hasNext: Boolean
        get() = when (repeatMode) {
            RepeatMode.ALL -> queue.isNotEmpty()
            else -> currentQueueIndex < queue.size - 1
        }

    val hasPrevious: Boolean
        get() = when (repeatMode) {
            RepeatMode.ALL -> queue.isNotEmpty()
            else -> currentQueueIndex > 0
        }

    val formattedPosition: String
        get() = formatTime(currentPosition)

    val formattedDuration: String
        get() = formatTime(duration)

    private fun formatTime(millis: Long): String {
        val totalSeconds = millis / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (hours > 0) {
            String.format("%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%d:%02d", minutes, seconds)
        }
    }
}

enum class RepeatMode {
    OFF,        // 반복 없음
    ONE,        // 한 곡 반복
    ALL         // 전체 반복
}

sealed class PlayerEvent {
    data class TrackChanged(val track: AudioFile?) : PlayerEvent()
    data class PlaybackStateChanged(val isPlaying: Boolean) : PlayerEvent()
    data class PositionChanged(val position: Long) : PlayerEvent()
    data class DurationChanged(val duration: Long) : PlayerEvent()
    data class Error(val message: String) : PlayerEvent()
    object PlaybackCompleted : PlayerEvent()
}
