package com.example.cdplayer.util

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.cdplayer.data.repository.AudioRepository
import com.example.cdplayer.domain.model.AudioFile
import com.example.cdplayer.domain.model.AudioType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

data class ScanProgress(
    val current: Int,
    val total: Int,
    val currentFile: String?
)

@Singleton
class MediaScanner @Inject constructor(
    @ApplicationContext private val context: Context,
    private val id3TagReader: Id3TagReader,
    private val audioRepository: AudioRepository
) {
    private val audioCollection: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }

    private val projection = arrayOf(
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.MIME_TYPE,
        MediaStore.Audio.Media.SIZE,
        MediaStore.Audio.Media.DATE_ADDED,
        MediaStore.Audio.Media.TRACK
    )

    private val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0 OR ${MediaStore.Audio.Media.DURATION} > 60000"

    fun scanMediaFiles(): Flow<ScanProgress> = flow {
        val audioFiles = mutableListOf<AudioFile>()
        val existingPaths = mutableListOf<String>()

        // 1. DB에서 기존 경로를 HashSet으로 한번에 로드 (N+1 쿼리 제거)
        val dbPaths = audioRepository.getAllPaths().toHashSet()

        context.contentResolver.query(
            audioCollection,
            projection,
            selection,
            null,
            "${MediaStore.Audio.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
            val dateAddedColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
            val trackColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)

            val total = cursor.count
            var current = 0

            while (cursor.moveToNext()) {
                current++
                val path = cursor.getString(dataColumn)

                // m4a 확장자 및 통화녹음 제외
                val lowerPath = path.lowercase()
                if (lowerPath.endsWith(".m4a") || isCallRecording(lowerPath)) {
                    continue
                }

                existingPaths.add(path)

                emit(ScanProgress(current, total, path))

                // 2. HashSet으로 O(1) 존재 확인 (개별 DB 쿼리 제거)
                if (dbPaths.contains(path)) {
                    // 인코딩 수정이 필요한지는 MediaStore 데이터로 간접 확인
                    val msTitle = cursor.getString(titleColumn)
                    val msArtist = cursor.getString(artistColumn)
                    val msAlbum = cursor.getString(albumColumn)
                    if (needsEncodingFix(msTitle) || needsEncodingFix(msArtist) || needsEncodingFix(msAlbum)) {
                        // 인코딩 수정이 필요한 경우만 개별 조회 후 삭제/재등록
                        val existingAudio = audioRepository.getAudioFileByPath(path)
                        if (existingAudio != null) {
                            if (!needsEncodingFix(existingAudio.title) &&
                                !needsEncodingFix(existingAudio.artist) &&
                                !needsEncodingFix(existingAudio.album)) {
                                continue
                            }
                            audioRepository.deleteAudioFileById(existingAudio.id)
                        }
                    } else {
                        continue
                    }
                }

                val mediaStoreTitle = fixKoreanEncodingStatic(cursor.getString(titleColumn)) ?: ""
                val mediaStoreArtist = fixKoreanEncodingStatic(cursor.getString(artistColumn))?.takeIf { it != "<unknown>" }
                val mediaStoreAlbum = fixKoreanEncodingStatic(cursor.getString(albumColumn))?.takeIf { it != "<unknown>" }
                val albumId = cursor.getLong(albumIdColumn)
                val duration = cursor.getLong(durationColumn)
                val mimeType = cursor.getString(mimeTypeColumn)
                val size = cursor.getLong(sizeColumn)
                val dateAdded = cursor.getLong(dateAddedColumn) * 1000
                val trackNumber = cursor.getInt(trackColumn)

                // ID3 태그 읽기 시도 (커버아트 데이터도 함께 읽음)
                val metadata = id3TagReader.readMetadata(path)

                // 파일명 파싱 (ID3 태그가 없을 경우 fallback)
                val fileName = path.substringAfterLast("/")
                val parsedFileName = FileNameParser.parse(fileName)

                // 최종 메타데이터 결정 (우선순위: ID3 > MediaStore > 파일명 파싱 > 폴더 구조)
                val title = metadata?.title
                    ?: mediaStoreTitle.takeIf { it.isNotBlank() }
                    ?: parsedFileName.title
                    ?: fileName.substringBeforeLast(".")

                val artist = metadata?.artist
                    ?: mediaStoreArtist
                    ?: parsedFileName.artist
                    ?: FileNameParser.parseArtistFromFolder(path)

                val album = metadata?.album
                    ?: mediaStoreAlbum
                    ?: parsedFileName.album
                    ?: FileNameParser.parseAlbumFromFolder(path)

                val genre = metadata?.genre

                // 오디오 타입 감지
                val audioType = AudioTypeDetector.detectAudioType(
                    path = path,
                    genre = genre,
                    title = title,
                    duration = duration
                )

                // 앨범 아트 URI 생성
                val albumArtUri = ContentUris.withAppendedId(
                    Uri.parse("content://media/external/audio/albumart"),
                    albumId
                ).toString()

                // 커버아트는 null로 저장 (후처리에서 일괄 처리)
                val audioFile = AudioFile(
                    id = 0, // Room에서 자동 생성
                    title = title,
                    artist = artist,
                    album = album,
                    albumArtist = metadata?.albumArtist,
                    duration = duration,
                    path = path,
                    type = audioType,
                    coverArtPath = null,
                    coverArtUri = albumArtUri,
                    lastPlayedPosition = 0,
                    lastPlayedAt = null,
                    addedDate = dateAdded,
                    genre = genre,
                    trackNumber = metadata?.trackNumber ?: parsedFileName.trackNumber ?: trackNumber.takeIf { it > 0 },
                    discNumber = metadata?.discNumber,
                    year = metadata?.year,
                    bitrate = metadata?.bitrate,
                    sampleRate = metadata?.sampleRate,
                    fileSize = size,
                    mimeType = mimeType,
                    isFavorite = album?.contains("Biscuit", ignoreCase = true) == true
                )

                audioFiles.add(audioFile)

                // 배치 저장 (100개씩)
                if (audioFiles.size >= 100) {
                    audioRepository.insertAudioFiles(audioFiles)
                    audioFiles.clear()
                }
            }
        }

        // 남은 파일 저장
        if (audioFiles.isNotEmpty()) {
            audioRepository.insertAudioFiles(audioFiles)
        }

        // 더 이상 존재하지 않는 파일 삭제
        audioRepository.deleteNotInPaths(existingPaths)

        // 커버아트 후처리 (메인 스캔 완료 후)
        processCoverArt()

    }.flowOn(Dispatchers.IO)

    /**
     * coverArtPath가 null인 파일들에 대해 커버아트를 일괄 처리.
     * 디렉토리별 캐시를 사용하여 같은 폴더 내 파일들의 중복 탐색을 방지.
     */
    private suspend fun processCoverArt() {
        val filesWithoutCover = audioRepository.getFilesWithoutCoverArt()
        if (filesWithoutCover.isEmpty()) return

        // 디렉토리 → 외부 커버아트 경로 캐시
        val dirCoverCache = mutableMapOf<String, String?>()

        for (audioFile in filesWithoutCover) {
            val path = audioFile.path
            val dir = path.substringBeforeLast("/")

            // 1. 임베디드 아트 확인 (ID3 태그 재읽기)
            val metadata = id3TagReader.readMetadata(path)
            if (metadata?.embeddedArtData != null) {
                val coverPath = id3TagReader.saveEmbeddedArtFromData(metadata.embeddedArtData, audioFile.id)
                if (coverPath != null) {
                    audioRepository.updateCoverArt(audioFile.id, coverPath)
                    continue
                }
            }

            // 2. 외부 파일 검색 (디렉토리 캐시 활용)
            val externalCover = dirCoverCache.getOrPut(dir) {
                id3TagReader.findExternalCoverArt(path)
            }
            if (externalCover != null) {
                audioRepository.updateCoverArt(audioFile.id, externalCover)
            }
        }
    }

    suspend fun scanSingleFile(path: String): AudioFile? = withContext(Dispatchers.IO) {
        val metadata = id3TagReader.readMetadata(path)

        val fileName = path.substringAfterLast("/")
        val parsedFileName = FileNameParser.parse(fileName)

        val title = metadata?.title ?: parsedFileName.title ?: fileName.substringBeforeLast(".")
        val artist = metadata?.artist ?: parsedFileName.artist
        val album = metadata?.album ?: parsedFileName.album ?: FileNameParser.parseAlbumFromFolder(path)
        val duration = metadata?.duration ?: 0L

        val audioType = AudioTypeDetector.detectAudioType(
            path = path,
            genre = metadata?.genre,
            title = title,
            duration = duration
        )

        AudioFile(
            id = 0,
            title = title,
            artist = artist,
            album = album,
            albumArtist = metadata?.albumArtist,
            duration = duration,
            path = path,
            type = audioType,
            coverArtPath = if (metadata?.hasEmbeddedArt == true) {
                // 임시 ID (0)를 사용하여 저장 시도. 단, 파일명 충돌 주의 필요.
                // 여기서는 single scan이므로 hashcode 등을 이용하거나 findExternalCoverArt 우선 시도 가능
                id3TagReader.findExternalCoverArt(path)
            } else {
                id3TagReader.findExternalCoverArt(path)
            },
            coverArtUri = null,
            lastPlayedPosition = 0,
            lastPlayedAt = null,
            addedDate = System.currentTimeMillis(),
            genre = metadata?.genre,
            trackNumber = metadata?.trackNumber ?: parsedFileName.trackNumber,
            discNumber = metadata?.discNumber,
            year = metadata?.year,
            bitrate = metadata?.bitrate,
            sampleRate = metadata?.sampleRate,
            fileSize = java.io.File(path).length(),
            mimeType = null,
            isFavorite = album?.contains("Biscuit", ignoreCase = true) == true
        )
    }

    companion object {
        private val EUC_KR = try {
            java.nio.charset.Charset.forName("EUC-KR")
        } catch (e: Exception) {
            java.nio.charset.Charset.forName("ISO-8859-1")
        }

        private val CP949 = try {
            java.nio.charset.Charset.forName("x-windows-949")
        } catch (e: Exception) {
            EUC_KR
        }

        private val LATIN_1 = java.nio.charset.Charset.forName("ISO-8859-1")

        /**
         * 텍스트가 깨진 인코딩(mojibake)인지 확인
         */
        fun needsEncodingFix(text: String?): Boolean {
            if (text.isNullOrBlank()) return false
            
            // 이미 한글이 포함되어 있으면 수정 불필요
            if (containsKorean(text)) return false
            
            // mojibake 패턴 감지
            return looksLikeMojibake(text)
        }

        /**
         * 한국어 인코딩 문제를 수정합니다.
         */
        fun fixKoreanEncodingStatic(text: String?): String? {
            if (text.isNullOrBlank()) return text

            // 이미 정상적인 한글이 포함되어 있으면 그대로 반환
            if (containsKorean(text)) {
                return text
            }

            // 깨진 문자 패턴 감지 (Latin-1로 잘못 읽힌 EUC-KR 텍스트)
            if (looksLikeMojibake(text)) {
                try {
                    val bytes = text.toByteArray(LATIN_1)
                    val eucKrDecoded = String(bytes, EUC_KR)
                    if (containsKorean(eucKrDecoded) && !looksLikeMojibake(eucKrDecoded)) {
                        return eucKrDecoded
                    }

                    val cp949Decoded = String(bytes, CP949)
                    if (containsKorean(cp949Decoded) && !looksLikeMojibake(cp949Decoded)) {
                        return cp949Decoded
                    }
                } catch (e: Exception) {
                    // 변환 실패 시 원본 반환
                }
            }

            return text
        }

        private val callRecordingPatterns = listOf(
            "call", "recorder", "recording", "통화녹음", "통화 녹음",
            "callrecorder", "callrecording", "voicerecorder"
        )

        fun isCallRecording(lowerPath: String): Boolean {
            return callRecordingPatterns.any { lowerPath.contains(it) }
        }

        private fun containsKorean(text: String): Boolean {
            return text.any { char ->
                val code = char.code
                (code in 0xAC00..0xD7A3) || (code in 0x1100..0x11FF) || (code in 0x3130..0x318F)
            }
        }

        private fun looksLikeMojibake(text: String): Boolean {
            var highByteCount = 0
            var totalChars = 0

            for (char in text) {
                val code = char.code
                totalChars++
                if (code in 0x80..0xFF) {
                    highByteCount++
                }
            }

            return totalChars > 0 && (highByteCount.toFloat() / totalChars) > 0.3f
        }
    }
}
