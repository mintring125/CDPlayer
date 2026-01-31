package com.example.cdplayer.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.Tag
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.Charset
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

data class AudioMetadata(
    val title: String?,
    val artist: String?,
    val album: String?,
    val albumArtist: String?,
    val genre: String?,
    val year: Int?,
    val trackNumber: Int?,
    val discNumber: Int?,
    val duration: Long?,
    val bitrate: Int?,
    val sampleRate: Int?,
    val hasEmbeddedArt: Boolean
)

@Singleton
class Id3TagReader @Inject constructor(
    private val context: Context
) {
    companion object {
        // 한국어 인코딩
        private val EUC_KR = try {
            Charset.forName("EUC-KR")
        } catch (e: Exception) {
            Charset.forName("ISO-8859-1")
        }
        
        private val CP949 = try {
            Charset.forName("x-windows-949")
        } catch (e: Exception) {
            EUC_KR
        }
        
        private val LATIN_1 = Charset.forName("ISO-8859-1")
    }

    init {
        // JAudioTagger 로그 비활성화
        Logger.getLogger("org.jaudiotagger").level = Level.OFF
    }

    fun readMetadata(filePath: String): AudioMetadata? {
        return try {
            val file = File(filePath)
            if (!file.exists()) return null

            val audioFile = AudioFileIO.read(file)
            val tag = audioFile.tag
            val audioHeader = audioFile.audioHeader

            AudioMetadata(
                title = fixKoreanEncoding(tag?.getFirst(FieldKey.TITLE)?.takeIf { it.isNotBlank() }),
                artist = fixKoreanEncoding(tag?.getFirst(FieldKey.ARTIST)?.takeIf { it.isNotBlank() }),
                album = fixKoreanEncoding(tag?.getFirst(FieldKey.ALBUM)?.takeIf { it.isNotBlank() }),
                albumArtist = fixKoreanEncoding(tag?.getFirst(FieldKey.ALBUM_ARTIST)?.takeIf { it.isNotBlank() }),
                genre = fixKoreanEncoding(tag?.getFirst(FieldKey.GENRE)?.takeIf { it.isNotBlank() }),
                year = tag?.getFirst(FieldKey.YEAR)?.toIntOrNull(),
                trackNumber = parseTrackNumber(tag?.getFirst(FieldKey.TRACK)),
                discNumber = parseTrackNumber(tag?.getFirst(FieldKey.DISC_NO)),
                duration = (audioHeader.trackLength * 1000).toLong(),
                bitrate = audioHeader.bitRateAsNumber.toInt(),
                sampleRate = audioHeader.sampleRateAsNumber,
                hasEmbeddedArt = tag?.firstArtwork != null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 한국어 인코딩 문제를 수정합니다.
     * EUC-KR/CP949로 인코딩된 텍스트가 Latin-1으로 잘못 읽힌 경우를 감지하고 수정합니다.
     */
    private fun fixKoreanEncoding(text: String?): String? {
        if (text.isNullOrBlank()) return text
        
        // 이미 정상적인 한글이 포함되어 있으면 그대로 반환
        if (containsKorean(text)) {
            return text
        }
        
        // 깨진 문자 패턴 감지 (Latin-1로 잘못 읽힌 EUC-KR 텍스트)
        if (looksLikeMojibake(text)) {
            // EUC-KR로 재변환 시도
            try {
                val bytes = text.toByteArray(LATIN_1)
                val eucKrDecoded = String(bytes, EUC_KR)
                if (containsKorean(eucKrDecoded) && !looksLikeMojibake(eucKrDecoded)) {
                    return eucKrDecoded
                }
                
                // CP949로 재변환 시도
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
    
    /**
     * 문자열에 한글이 포함되어 있는지 확인
     */
    private fun containsKorean(text: String): Boolean {
        return text.any { char ->
            val code = char.code
            // 한글 유니코드 범위: 가-힣 (0xAC00-0xD7A3) + 한글 자모
            (code in 0xAC00..0xD7A3) || (code in 0x1100..0x11FF) || (code in 0x3130..0x318F)
        }
    }
    
    /**
     * 텍스트가 mojibake(깨진 문자)처럼 보이는지 감지
     * Latin-1로 잘못 읽힌 EUC-KR 텍스트는 특정 패턴의 문자를 가짐
     */
    private fun looksLikeMojibake(text: String): Boolean {
        var highByteCount = 0
        var totalChars = 0
        
        for (char in text) {
            val code = char.code
            totalChars++
            // Latin-1 확장 영역 (0x80-0xFF) - EUC-KR 바이트가 Latin-1로 읽힌 경우의 특징
            if (code in 0x80..0xFF) {
                highByteCount++
            }
        }
        
        // 전체 문자의 30% 이상이 high byte이면 mojibake로 판단
        return totalChars > 0 && (highByteCount.toFloat() / totalChars) > 0.3f
    }

    fun extractEmbeddedArt(filePath: String): ByteArray? {
        return try {
            val file = File(filePath)
            if (!file.exists()) return null

            val audioFile = AudioFileIO.read(file)
            audioFile.tag?.firstArtwork?.binaryData
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun saveEmbeddedArtToFile(filePath: String, audioFileId: Long): String? {
        return try {
            val artData = extractEmbeddedArt(filePath) ?: return null

            val coverArtDir = File(context.filesDir, "cover_art")
            if (!coverArtDir.exists()) {
                coverArtDir.mkdirs()
            }

            val outputFile = File(coverArtDir, "cover_$audioFileId.jpg")

            // 이미지를 JPEG으로 변환하여 저장
            val bitmap = BitmapFactory.decodeByteArray(artData, 0, artData.size)
            if (bitmap != null) {
                FileOutputStream(outputFile).use { fos ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
                }
                bitmap.recycle()
                outputFile.absolutePath
            } else {
                // 비트맵 변환 실패 시 원본 데이터 그대로 저장
                outputFile.writeBytes(artData)
                outputFile.absolutePath
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun readMetadataFromUri(uri: Uri): AudioMetadata? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("temp_audio", ".mp3", context.cacheDir)
            tempFile.deleteOnExit()

            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            inputStream.close()

            val result = readMetadata(tempFile.absolutePath)
            tempFile.delete()
            result
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun parseTrackNumber(trackStr: String?): Int? {
        if (trackStr.isNullOrBlank()) return null
        // "1/10" 형식 처리
        val parts = trackStr.split("/")
        return parts[0].trim().toIntOrNull()
    }

    fun findExternalCoverArt(audioFilePath: String): String? {
        return try {
            val audioFile = File(audioFilePath)
            val parentDir = audioFile.parentFile ?: return null
            
            // 검색할 파일명 우선순위
            val coverNames = listOf(
                "cover.jpg", "cover.jpeg", "cover.png",
                "album.jpg", "album.jpeg", "album.png",
                "folder.jpg", "folder.jpeg", "folder.png",
                "front.jpg", "front.jpeg", "front.png"
            )

            // 1. 일반적인 이름의 파일 검색
            for (name in coverNames) {
                val coverFile = File(parentDir, name)
                if (coverFile.exists()) {
                    return coverFile.absolutePath
                }
                
                // 대소문자 구분 없이 검색 (File.exists()는 대소문자 구분함)
                val matchingFile = parentDir.listFiles()?.find { 
                    it.name.equals(name, ignoreCase = true) 
                }
                if (matchingFile != null) {
                    return matchingFile.absolutePath
                }
            }
            
            // 2. 폴더 내의 이미지 파일 검색 (표준 이름이 없는 경우)
            // 이미지가 하나만 있거나, 혹은 가장 큰 이미지 파일을 선택
            val imageFiles = parentDir.listFiles { file ->
                val name = file.name.lowercase()
                file.isFile && (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"))
            }

            if (!imageFiles.isNullOrEmpty()) {
                // 이미지가 하나뿐이면 그것을 사용
                if (imageFiles.size == 1) {
                    return imageFiles[0].absolutePath
                }
                
                // 여러 개라면, 이름에 "cover", "album", "front" 등이 포함된 것 우선
                val bestMatch = imageFiles.find { file -> 
                    val name = file.name.lowercase()
                    name.contains("cover") || name.contains("album") || name.contains("front")
                }
                if (bestMatch != null) return bestMatch.absolutePath
                
                // 그래도 없으면, 가장 파일 크기가 큰 이미지를 사용 (고화질 커버일 확률 높음)
                return imageFiles.maxByOrNull { it.length() }?.absolutePath
            }
            
            null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}


