package com.example.cdplayer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.cdplayer.domain.model.Playlist

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String?,
    val createdDate: Long,
    val modifiedDate: Long,
    val coverArtPath: String?
) {
    fun toDomain(trackCount: Int, totalDuration: Long): Playlist {
        return Playlist(
            id = id,
            name = name,
            description = description,
            createdDate = createdDate,
            modifiedDate = modifiedDate,
            trackCount = trackCount,
            totalDuration = totalDuration,
            coverArtPath = coverArtPath
        )
    }

    companion object {
        fun fromDomain(playlist: Playlist): PlaylistEntity {
            return PlaylistEntity(
                id = playlist.id,
                name = playlist.name,
                description = playlist.description,
                createdDate = playlist.createdDate,
                modifiedDate = playlist.modifiedDate,
                coverArtPath = playlist.coverArtPath
            )
        }
    }
}

@Entity(
    tableName = "playlist_tracks",
    primaryKeys = ["playlistId", "audioFileId"]
)
data class PlaylistTrackCrossRef(
    val playlistId: Long,
    val audioFileId: Long,
    val orderIndex: Int,
    val addedDate: Long
)
