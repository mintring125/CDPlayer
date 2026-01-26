package com.example.cdplayer.util

import com.example.cdplayer.domain.model.Chapter
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.audio.mp4.Mp4AudioHeader
import org.jaudiotagger.audio.mp4.Mp4TagReader
import java.io.File
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

/**
 * M4B 오디오북 파일에서 챕터 정보를 추출합니다.
 * MP4/M4B 파일의 챕터는 'chpl' atom에 저장됩니다.
 */
@Singleton
class M4bChapterReader @Inject constructor() {

    init {
        Logger.getLogger("org.jaudiotagger").level = Level.OFF
    }

    /**
     * M4B 파일에서 챕터 목록을 추출합니다.
     */
    fun extractChapters(filePath: String): List<Chapter> {
        val file = File(filePath)
        if (!file.exists() || !isM4bFile(filePath)) {
            return emptyList()
        }

        return try {
            // 먼저 chpl atom에서 챕터 추출 시도
            val chapters = extractChaptersFromChplAtom(file)
            if (chapters.isNotEmpty()) {
                return chapters
            }

            // chpl이 없으면 nero 챕터 형식 시도
            extractChaptersFromNeroFormat(file)
        } catch (e: Exception) {
            android.util.Log.e("M4bChapterReader", "Failed to extract chapters from $filePath", e)
            emptyList()
        }
    }

    /**
     * 파일이 M4B 형식인지 확인합니다.
     */
    private fun isM4bFile(filePath: String): Boolean {
        val lowerPath = filePath.lowercase()
        return lowerPath.endsWith(".m4b") ||
               lowerPath.endsWith(".m4a") ||
               lowerPath.endsWith(".mp4")
    }

    /**
     * MP4 파일의 'chpl' (chapter list) atom에서 챕터를 추출합니다.
     * iTunes/QuickTime 스타일의 챕터 형식입니다.
     */
    private fun extractChaptersFromChplAtom(file: File): List<Chapter> {
        val chapters = mutableListOf<Chapter>()

        RandomAccessFile(file, "r").use { raf ->
            val chplOffset = findAtom(raf, "moov", "udta", "chpl")
            if (chplOffset < 0) return emptyList()

            raf.seek(chplOffset)

            // chpl atom 크기 읽기
            val atomSize = raf.readInt()
            val atomType = readAtomType(raf)

            if (atomType != "chpl") return emptyList()

            // version (1 byte) + flags (3 bytes)
            raf.skipBytes(4)

            // reserved (4 bytes) - 일부 버전에서 사용
            val version = raf.readInt()

            // 챕터 개수 (1 byte 또는 4 bytes, 버전에 따라 다름)
            val chapterCount = if (version == 0x01000000) {
                raf.readByte().toInt() and 0xFF
            } else {
                raf.seek(chplOffset + 8) // atom header 다음으로
                raf.skipBytes(4) // version + flags
                raf.skipBytes(4) // reserved
                raf.readByte().toInt() and 0xFF
            }

            for (i in 0 until chapterCount) {
                try {
                    // 시작 시간 (100나노초 단위, 8 bytes)
                    val startTime100ns = raf.readLong()
                    val startTimeMs = startTime100ns / 10000 // 100ns -> ms

                    // 챕터 이름 길이 (1 byte)
                    val nameLength = raf.readByte().toInt() and 0xFF

                    // 챕터 이름
                    val nameBytes = ByteArray(nameLength)
                    raf.readFully(nameBytes)
                    val title = String(nameBytes, StandardCharsets.UTF_8)

                    chapters.add(
                        Chapter(
                            index = i,
                            title = title.ifBlank { "Chapter ${i + 1}" },
                            startTimeMs = startTimeMs,
                            endTimeMs = 0 // 나중에 계산
                        )
                    )
                } catch (e: Exception) {
                    break
                }
            }
        }

        // endTimeMs 계산 (다음 챕터의 시작 시간 또는 파일 길이)
        return calculateEndTimes(chapters, file)
    }

    /**
     * Nero 스타일 챕터 형식에서 챕터를 추출합니다.
     * 'chap' atom을 참조하는 트랙에서 추출합니다.
     */
    private fun extractChaptersFromNeroFormat(file: File): List<Chapter> {
        // JAudioTagger를 사용하여 파일 정보 읽기
        return try {
            val audioFile = AudioFileIO.read(file)
            val header = audioFile.audioHeader as? Mp4AudioHeader
            val durationMs = (header?.trackLength ?: 0) * 1000L

            // Nero 챕터는 trak/tref/chap 구조로 참조됨
            // 여기서는 기본적인 fallback으로 빈 리스트 반환
            emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * 중첩된 atom을 찾습니다.
     */
    private fun findAtom(raf: RandomAccessFile, vararg atomPath: String): Long {
        var currentOffset = 0L
        var searchEnd = raf.length()

        for (targetAtom in atomPath) {
            val found = findAtomInRange(raf, targetAtom, currentOffset, searchEnd)
            if (found < 0) return -1

            // 찾은 atom 내부로 이동
            raf.seek(found)
            val atomSize = raf.readInt().toLong()
            raf.skipBytes(4) // atom type

            currentOffset = found + 8 // atom header 다음
            searchEnd = found + atomSize
        }

        return currentOffset - 8 // atom 시작 위치 반환
    }

    /**
     * 지정된 범위 내에서 atom을 찾습니다.
     */
    private fun findAtomInRange(
        raf: RandomAccessFile,
        targetAtom: String,
        start: Long,
        end: Long
    ): Long {
        var offset = start

        while (offset < end - 8) {
            raf.seek(offset)

            val size = raf.readInt().toLong()
            val type = readAtomType(raf)

            if (size < 8) break // 잘못된 atom

            if (type == targetAtom) {
                return offset
            }

            offset += if (size == 1L) {
                // extended size
                raf.readLong()
            } else {
                size
            }
        }

        return -1
    }

    /**
     * 4바이트 atom type을 문자열로 읽습니다.
     */
    private fun readAtomType(raf: RandomAccessFile): String {
        val bytes = ByteArray(4)
        raf.readFully(bytes)
        return String(bytes, StandardCharsets.US_ASCII)
    }

    /**
     * 각 챕터의 종료 시간을 계산합니다.
     */
    private fun calculateEndTimes(chapters: List<Chapter>, file: File): List<Chapter> {
        if (chapters.isEmpty()) return chapters

        // 파일 전체 길이 가져오기
        val totalDurationMs = try {
            val audioFile = AudioFileIO.read(file)
            audioFile.audioHeader.trackLength * 1000L
        } catch (e: Exception) {
            chapters.lastOrNull()?.startTimeMs?.plus(60000) ?: 0L
        }

        return chapters.mapIndexed { index, chapter ->
            val endTimeMs = if (index < chapters.size - 1) {
                chapters[index + 1].startTimeMs
            } else {
                totalDurationMs
            }
            chapter.copy(endTimeMs = endTimeMs)
        }
    }

    /**
     * 오디오 파일의 전체 재생 시간을 가져옵니다 (밀리초).
     */
    fun getFileDuration(filePath: String): Long {
        return try {
            val file = File(filePath)
            if (!file.exists()) return 0L

            val audioFile = AudioFileIO.read(file)
            audioFile.audioHeader.trackLength * 1000L
        } catch (e: Exception) {
            0L
        }
    }
}
