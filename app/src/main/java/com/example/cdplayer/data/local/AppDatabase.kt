package com.example.cdplayer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cdplayer.data.local.dao.AudioFileDao
import com.example.cdplayer.data.local.dao.BookmarkDao
import com.example.cdplayer.data.local.dao.PdfBookDao
import com.example.cdplayer.data.local.dao.PlaylistDao
import com.example.cdplayer.data.local.entity.AudioFileEntity
import com.example.cdplayer.data.local.entity.BookmarkEntity
import com.example.cdplayer.data.local.entity.PdfBookEntity
import com.example.cdplayer.data.local.entity.PlaylistEntity
import com.example.cdplayer.data.local.entity.PlaylistTrackCrossRef

@Database(
    entities = [
        AudioFileEntity::class,
        PlaylistEntity::class,
        PlaylistTrackCrossRef::class,
        BookmarkEntity::class,
        PdfBookEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun audioFileDao(): AudioFileDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun pdfBookDao(): PdfBookDao

    companion object {
        const val DATABASE_NAME = "cdplayer_db"

        val MIGRATION_3_4 = object : androidx.room.migration.Migration(3, 4) {
            override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                // Add isFavorite column
                database.execSQL("ALTER TABLE pdf_books ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0")
                // Add coverPath column
                database.execSQL("ALTER TABLE pdf_books ADD COLUMN coverPath TEXT")
            }
        }
    }
}

