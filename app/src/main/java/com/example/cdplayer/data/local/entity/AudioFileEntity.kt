package com.example.cdplayer.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.cdplayer.domain.model.AudioFile
import com.example.cdplayer.domain.model.AudioType

@Entity(
    tableName = "audio_files",
    indices = [
        Index(value = ["path"], unique = true),
        Index(value = ["artist"]),
        Index(value = ["album"]),
        Index(value = ["type"]),
        Index(value = ["lastPlayedAt"])
    ]
)
data class AudioFileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val artist: String?,
    val album: String?,
    val albumArtist: String?,
    val duration: Long,
    val path: String,
    val type: String,
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
    fun toDomain(): AudioFile {
        return AudioFile(
            id = id,
            title = title,
            artist = artist,
            album = album,
            albumArtist = albumArtist,
            duration = duration,
            path = path,
            type = AudioType.fromString(type),
            coverArtPath = coverArtPath,
            coverArtUri = coverArtUri,
            lastPlayedPosition = lastPlayedPosition,
            lastPlayedAt = lastPlayedAt,
            addedDate = addedDate,
            genre = genre,
            trackNumber = trackNumber,
            discNumber = discNumber,
            year = year,
            bitrate = bitrate,
            sampleRate = sampleRate,
            fileSize = fileSize,
            mimeType = mimeType,
            isFavorite = isFavorite
        )
    }

    companion object {
        fun fromDomain(audioFile: AudioFile): AudioFileEntity {
            return AudioFileEntity(
                id = audioFile.id,
                title = audioFile.title,
                artist = audioFile.artist,
                album = audioFile.album,
                albumArtist = audioFile.albumArtist,
                duration = audioFile.duration,
                path = audioFile.path,
                type = audioFile.type.name,
                coverArtPath = audioFile.coverArtPath,
                coverArtUri = audioFile.coverArtUri,
                lastPlayedPosition = audioFile.lastPlayedPosition,
                lastPlayedAt = audioFile.lastPlayedAt,
                addedDate = audioFile.addedDate,
                genre = audioFile.genre,
                trackNumber = audioFile.trackNumber,
                discNumber = audioFile.discNumber,
                year = audioFile.year,
                bitrate = audioFile.bitrate,
                sampleRate = audioFile.sampleRate,
                fileSize = audioFile.fileSize,
                mimeType = audioFile.mimeType,
                isFavorite = audioFile.isFavorite
            )
        }
    }
}
