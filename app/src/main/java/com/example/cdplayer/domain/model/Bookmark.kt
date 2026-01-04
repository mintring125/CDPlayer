package com.example.cdplayer.domain.model

data class Bookmark(
    val id: Long,
    val audioFileId: Long,
    val position: Long,
    val note: String?,
    val createdDate: Long
) {
    val formattedPosition: String
        get() {
            val totalSeconds = position / 1000
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

data class BookmarkWithAudio(
    val bookmark: Bookmark,
    val audioFile: AudioFile
)
