package com.example.cdplayer.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.cdplayer.domain.model.Bookmark

@Entity(
    tableName = "bookmarks",
    foreignKeys = [
        ForeignKey(
            entity = AudioFileEntity::class,
            parentColumns = ["id"],
            childColumns = ["audioFileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["audioFileId"])]
)
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val audioFileId: Long,
    val position: Long,
    val note: String?,
    val createdDate: Long
) {
    fun toDomain(): Bookmark {
        return Bookmark(
            id = id,
            audioFileId = audioFileId,
            position = position,
            note = note,
            createdDate = createdDate
        )
    }

    companion object {
        fun fromDomain(bookmark: Bookmark): BookmarkEntity {
            return BookmarkEntity(
                id = bookmark.id,
                audioFileId = bookmark.audioFileId,
                position = bookmark.position,
                note = bookmark.note,
                createdDate = bookmark.createdDate
            )
        }
    }
}
