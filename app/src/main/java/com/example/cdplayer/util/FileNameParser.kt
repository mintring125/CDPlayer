package com.example.cdplayer.util

data class ParsedFileName(
    val title: String?,
    val artist: String?,
    val album: String?,
    val trackNumber: Int?
)

object FileNameParser {

    // 다양한 파일명 패턴 정의
    private val patterns = listOf(
        // "Artist - Album - 01 - Title.mp3"
        Regex("^(.+?)\\s*-\\s*(.+?)\\s*-\\s*(\\d+)\\s*-\\s*(.+)$"),

        // "Artist - Album - 01. Title.mp3"
        Regex("^(.+?)\\s*-\\s*(.+?)\\s*-\\s*(\\d+)\\.\\s*(.+)$"),

        // "Artist - 01 - Title.mp3"
        Regex("^(.+?)\\s*-\\s*(\\d+)\\s*-\\s*(.+)$"),

        // "Artist - Title.mp3"
        Regex("^(.+?)\\s*-\\s*(.+)$"),

        // "01. Title.mp3"
        Regex("^(\\d+)\\.\\s*(.+)$"),

        // "01 - Title.mp3"
        Regex("^(\\d+)\\s*-\\s*(.+)$"),

        // "Track 01 - Title.mp3"
        Regex("^[Tt]rack\\s*(\\d+)\\s*-\\s*(.+)$"),

        // Audiobook patterns
        // "Book Title - Chapter 01.mp3"
        Regex("^(.+?)\\s*-\\s*[Cc]hapter\\s*(\\d+)$"),

        // "Book Title - Part 01.mp3"
        Regex("^(.+?)\\s*-\\s*[Pp]art\\s*(\\d+)$")
    )

    fun parse(fileName: String): ParsedFileName {
        // 확장자 제거
        val nameWithoutExt = fileName.substringBeforeLast(".")

        // 패턴 매칭 시도
        for (pattern in patterns) {
            val match = pattern.matchEntire(nameWithoutExt)
            if (match != null) {
                return parseMatch(pattern, match)
            }
        }

        // 패턴 매칭 실패 시 파일명 그대로 반환
        return ParsedFileName(
            title = nameWithoutExt,
            artist = null,
            album = null,
            trackNumber = null
        )
    }

    private fun parseMatch(pattern: Regex, match: MatchResult): ParsedFileName {
        val groups = match.groupValues

        return when {
            // "Artist - Album - 01 - Title.mp3" or "Artist - Album - 01. Title.mp3"
            groups.size == 5 && groups[3].toIntOrNull() != null -> {
                ParsedFileName(
                    title = groups[4].trim(),
                    artist = groups[1].trim(),
                    album = groups[2].trim(),
                    trackNumber = groups[3].toIntOrNull()
                )
            }

            // "Artist - 01 - Title.mp3"
            groups.size == 4 && groups[2].toIntOrNull() != null -> {
                ParsedFileName(
                    title = groups[3].trim(),
                    artist = groups[1].trim(),
                    album = null,
                    trackNumber = groups[2].toIntOrNull()
                )
            }

            // "Artist - Title.mp3"
            groups.size == 3 && groups[1].toIntOrNull() == null && groups[2].toIntOrNull() == null -> {
                ParsedFileName(
                    title = groups[2].trim(),
                    artist = groups[1].trim(),
                    album = null,
                    trackNumber = null
                )
            }

            // "01. Title.mp3" or "01 - Title.mp3" or "Track 01 - Title.mp3"
            groups.size == 3 && groups[1].toIntOrNull() != null -> {
                ParsedFileName(
                    title = groups[2].trim(),
                    artist = null,
                    album = null,
                    trackNumber = groups[1].toIntOrNull()
                )
            }

            // "Book Title - Chapter 01.mp3" or "Book Title - Part 01.mp3"
            groups.size == 3 -> {
                ParsedFileName(
                    title = groups[1].trim(),
                    artist = null,
                    album = groups[1].trim(),
                    trackNumber = groups[2].toIntOrNull()
                )
            }

            else -> {
                ParsedFileName(
                    title = match.value,
                    artist = null,
                    album = null,
                    trackNumber = null
                )
            }
        }
    }

    fun parseAlbumFromFolder(path: String): String? {
        val parts = path.replace("\\", "/").split("/")
        return if (parts.size >= 2) {
            parts[parts.size - 2]
        } else {
            null
        }
    }

    fun parseArtistFromFolder(path: String): String? {
        val parts = path.replace("\\", "/").split("/")
        return if (parts.size >= 3) {
            parts[parts.size - 3]
        } else {
            null
        }
    }
}
