package com.example.cdplayer.util

import com.example.cdplayer.domain.model.AudioType

object AudioTypeDetector {

    private val audiobookFolderPatterns = listOf(
        "audiobook", "audiobooks", "오디오북", "audio book", "audio books",
        "podcast", "podcasts", "팟캐스트",
        "spoken", "speech", "lecture", "lectures", "강의"
    )

    private val audiobookGenrePatterns = listOf(
        "audiobook", "audio book", "오디오북",
        "spoken", "speech", "spoken word", "spoken & audio",
        "podcast", "팟캐스트",
        "lecture", "강의", "books & spoken"
    )

    private val audiobookTitlePatterns = listOf(
        Regex("chapter\\s*\\d+", RegexOption.IGNORE_CASE),
        Regex("챕터\\s*\\d+"),
        Regex("ch\\.?\\s*\\d+", RegexOption.IGNORE_CASE),
        Regex("part\\s*\\d+", RegexOption.IGNORE_CASE),
        Regex("파트\\s*\\d+"),
        Regex("episode\\s*\\d+", RegexOption.IGNORE_CASE),
        Regex("에피소드\\s*\\d+"),
        Regex("disc\\s*\\d+.*track\\s*\\d+", RegexOption.IGNORE_CASE)
    )

    // 30분 이상이면 오디오북으로 간주
    private const val AUDIOBOOK_DURATION_THRESHOLD = 30 * 60 * 1000L

    private fun isAudiobookExtension(lowerPath: String): Boolean {
        return lowerPath.endsWith(".mp3") || lowerPath.endsWith(".m4b")
    }

    fun detectAudioType(
        path: String,
        genre: String?,
        title: String?,
        duration: Long
    ): AudioType {
        val lowerPath = path.lowercase()

        // 오디오북은 mp3, m4b 확장자만 허용
        if (!isAudiobookExtension(lowerPath)) {
            return AudioType.MUSIC
        }

        // 0. M4B 확장자는 항상 오디오북
        if (lowerPath.endsWith(".m4b")) {
            return AudioType.AUDIOBOOK
        }

        // 1. 폴더 경로로 판단
        if (audiobookFolderPatterns.any { lowerPath.contains(it) }) {
            return AudioType.AUDIOBOOK
        }
        if (lowerPath.contains("/music/") || lowerPath.contains("\\music\\")) {
            return AudioType.MUSIC
        }

        // 2. 장르 태그로 판단
        genre?.let { g ->
            val lowerGenre = g.lowercase()
            if (audiobookGenrePatterns.any { lowerGenre.contains(it) }) {
                return AudioType.AUDIOBOOK
            }
        }

        // 3. 파일명/제목 패턴으로 판단
        title?.let { t ->
            if (audiobookTitlePatterns.any { it.containsMatchIn(t) }) {
                return AudioType.AUDIOBOOK
            }
        }

        // 4. 파일명에서 직접 확인
        val fileName = path.substringAfterLast("/").substringAfterLast("\\")
        if (audiobookTitlePatterns.any { it.containsMatchIn(fileName) }) {
            return AudioType.AUDIOBOOK
        }

        // 5. 재생 시간으로 판단 (오디오북은 보통 30분 이상)
        if (duration > AUDIOBOOK_DURATION_THRESHOLD) {
            return AudioType.AUDIOBOOK
        }

        // 6. 기본값은 음악
        return AudioType.MUSIC
    }
}
