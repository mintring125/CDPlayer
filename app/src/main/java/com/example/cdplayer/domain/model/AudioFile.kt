package com.example.cdplayer.domain.model

data class AudioFile(
    val id: Long,
    val title: String,
    val artist: String?,
    val album: String?,
    val albumArtist: String?,
    val duration: Long,
    val path: String,
    val type: AudioType,
    val coverArtPath: String?,
    val coverArtUri: String?,
    val lastPlayedPosition: Long = 0,
    val lastPlayedAt: Long? = null,
    val addedDate: Long,
    val genre: String?,
    val trackNumber: Int?,
    val discNumber: Int?,
    val year: Int?,
    val bitrate: Int?,
    val sampleRate: Int?,
    val fileSize: Long?,
    val mimeType: String?,
    val isFavorite: Boolean = false
) {
    val displayTitle: String
        get() = title.ifBlank { path.substringAfterLast("/").substringBeforeLast(".") }

    val displayArtist: String
        get() = artist ?: "알 수 없는 아티스트"

    val displayAlbum: String
        get() = album ?: "알 수 없는 앨범"

    val formattedDuration: String
        get() {
            val totalSeconds = duration / 1000
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            val seconds = totalSeconds % 60

            return if (hours > 0) {
                String.format("%d:%02d:%02d", hours, minutes, seconds)
            } else {
                String.format("%d:%02d", minutes, seconds)
            }
        }

    val isAudiobook: Boolean
        get() = type == AudioType.AUDIOBOOK
}
