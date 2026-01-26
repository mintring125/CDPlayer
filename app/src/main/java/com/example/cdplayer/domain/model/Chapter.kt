package com.example.cdplayer.domain.model

/**
 * M4B 오디오북의 챕터 정보를 나타냅니다.
 */
data class Chapter(
    val index: Int,
    val title: String,
    val startTimeMs: Long,
    val endTimeMs: Long
) {
    val durationMs: Long
        get() = endTimeMs - startTimeMs

    fun formatStartTime(): String {
        val totalSeconds = startTimeMs / 1000
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
