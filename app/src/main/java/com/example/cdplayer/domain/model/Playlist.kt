package com.example.cdplayer.domain.model

data class Playlist(
    val id: Long,
    val name: String,
    val description: String?,
    val createdDate: Long,
    val modifiedDate: Long,
    val trackCount: Int,
    val totalDuration: Long,
    val coverArtPath: String?
) {
    val formattedDuration: String
        get() {
            val totalSeconds = totalDuration / 1000
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60

            return if (hours > 0) {
                "${hours}시간 ${minutes}분"
            } else {
                "${minutes}분"
            }
        }
}

data class PlaylistWithTracks(
    val playlist: Playlist,
    val tracks: List<AudioFile>
)
