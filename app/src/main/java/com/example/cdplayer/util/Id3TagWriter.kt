package com.example.cdplayer.util

import android.content.Context
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.Tag
import org.jaudiotagger.tag.id3.ID3v24Tag
import java.io.File
import java.util.logging.Level
import java.util.logging.Logger
import javax.inject.Inject
import javax.inject.Singleton

/**
 * ID3 태그 쓰기 유틸리티
 */
@Singleton
class Id3TagWriter @Inject constructor(
    private val context: Context
) {
    init {
        // JAudioTagger 로그 비활성화
        Logger.getLogger("org.jaudiotagger").level = Level.OFF
    }

    /**
     * 오디오 파일의 ID3 태그를 업데이트합니다.
     * 
     * @return 성공 여부
     */
    fun writeMetadata(
        filePath: String,
        title: String,
        artist: String?,
        album: String?,
        genre: String?,
        year: Int?,
        trackNumber: Int?
    ): Boolean {
        return try {
            val file = File(filePath)
            if (!file.exists()) {
                return false
            }

            val audioFile = AudioFileIO.read(file)
            
            // 기존 태그가 없으면 새로 생성
            var tag: Tag? = audioFile.tag
            if (tag == null) {
                tag = ID3v24Tag()
                audioFile.tag = tag
            }

            // 태그 필드 업데이트
            if (title.isNotBlank()) {
                tag.setField(FieldKey.TITLE, title)
            }

            if (artist != null) {
                if (artist.isNotBlank()) {
                    tag.setField(FieldKey.ARTIST, artist)
                } else {
                    tag.deleteField(FieldKey.ARTIST)
                }
            }

            if (album != null) {
                if (album.isNotBlank()) {
                    tag.setField(FieldKey.ALBUM, album)
                } else {
                    tag.deleteField(FieldKey.ALBUM)
                }
            }

            if (genre != null) {
                if (genre.isNotBlank()) {
                    tag.setField(FieldKey.GENRE, genre)
                } else {
                    tag.deleteField(FieldKey.GENRE)
                }
            }

            if (year != null) {
                tag.setField(FieldKey.YEAR, year.toString())
            }

            if (trackNumber != null) {
                tag.setField(FieldKey.TRACK, trackNumber.toString())
            }

            // 앨범 아트 업데이트 (artworkPath가 제공된 경우)
             // 이 함수 시그니처를 변경해야 함. 아래와 같이 오버로딩하거나 인자를 추가해야 함.
             // 현재는 writeMetadata 시그니처를 변경하지 않고 내부에서 처리할 수 없음.
             // 따라서 별도의 메서드를 추가하거나 인자를 추가해야 함.
             // 여기서는 인자를 추가하는 방식으로 수정 진행.

            // 파일에 저장
            audioFile.commit()
            
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 오디오 파일의 ID3 태그를 업데이트합니다. (앨범 아트 포함)
     */
    fun writeMetadata(
        filePath: String,
        title: String,
        artist: String?,
        album: String?,
        genre: String?,
        year: Int?,
        trackNumber: Int?,
        artworkFile: File? = null
    ): Boolean {
        return try {
            val file = File(filePath)
            if (!file.exists()) {
                return false
            }

            val audioFile = AudioFileIO.read(file)
            
            // 기존 태그가 없으면 새로 생성
            var tag: Tag? = audioFile.tag
            if (tag == null) {
                tag = ID3v24Tag()
                audioFile.tag = tag
            }

            // 태그 필드 업데이트
            if (title.isNotBlank()) {
                tag.setField(FieldKey.TITLE, title)
            }

            if (artist != null) {
                if (artist.isNotBlank()) {
                    tag.setField(FieldKey.ARTIST, artist)
                } else {
                    tag.deleteField(FieldKey.ARTIST)
                }
            }

            if (album != null) {
                if (album.isNotBlank()) {
                    tag.setField(FieldKey.ALBUM, album)
                } else {
                    tag.deleteField(FieldKey.ALBUM)
                }
            }

            if (genre != null) {
                if (genre.isNotBlank()) {
                    tag.setField(FieldKey.GENRE, genre)
                } else {
                    tag.deleteField(FieldKey.GENRE)
                }
            }

            if (year != null) {
                tag.setField(FieldKey.YEAR, year.toString())
            }

            if (trackNumber != null) {
                tag.setField(FieldKey.TRACK, trackNumber.toString())
            }

            // 앨범 아트 처리
            if (artworkFile != null && artworkFile.exists()) {
                // 기존 아트워크 삭제
                tag.deleteArtworkField()
                
                // 새 아트워크 생성 및 추가
                val artwork = org.jaudiotagger.tag.images.StandardArtwork.createArtworkFromFile(artworkFile)
                tag.setField(artwork)
            }

            // 파일에 저장
            audioFile.commit()
            
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 파일명을 안전하게 변경합니다.
     * 
     * @return 새 파일 경로, 실패 시 null
     */
    fun renameFile(oldPath: String, newFileName: String): String? {
        return try {
            val oldFile = File(oldPath)
            if (!oldFile.exists()) return null

            val extension = oldPath.substringAfterLast(".")
            val parentDir = oldPath.substringBeforeLast("/")
            val newPath = "$parentDir/$newFileName.$extension"
            val newFile = File(newPath)

            if (newFile.exists()) return null

            if (oldFile.renameTo(newFile)) {
                newPath
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
