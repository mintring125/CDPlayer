package com.example.cdplayer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cdplayer.data.local.dao.AudioFileDao
import com.example.cdplayer.data.local.dao.BookmarkDao
import com.example.cdplayer.data.local.dao.PdfBookDao
import com.example.cdplayer.data.local.dao.PlaylistDao
import com.example.cdplayer.data.local.dao.StampDao
import com.example.cdplayer.data.local.entity.AudioFileEntity
import com.example.cdplayer.data.local.entity.BookmarkEntity
import com.example.cdplayer.data.local.entity.PdfBookEntity
import com.example.cdplayer.data.local.entity.PlaylistEntity
import com.example.cdplayer.data.local.entity.PlaylistTrackCrossRef
import com.example.cdplayer.data.local.entity.StampEntity

@Database(
    entities = [
        AudioFileEntity::class,
        PlaylistEntity::class,
        PlaylistTrackCrossRef::class,
        BookmarkEntity::class,
        PdfBookEntity::class,
        StampEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun audioFileDao(): AudioFileDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun pdfBookDao(): PdfBookDao
    abstract fun stampDao(): StampDao

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

        val MIGRATION_4_5 = object : androidx.room.migration.Migration(4, 5) {
            override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                // Add rating column (0.5 increments, max 5.0)
                database.execSQL("ALTER TABLE pdf_books ADD COLUMN rating REAL NOT NULL DEFAULT 0")
            }
        }

        val MIGRATION_5_6 = object : androidx.room.migration.Migration(5, 6) {
            override fun migrate(database: androidx.sqlite.db.SupportSQLiteDatabase) {
                // Create stamps table
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS stamps (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        stampImageName TEXT NOT NULL,
                        boardNumber INTEGER NOT NULL,
                        position INTEGER NOT NULL,
                        collectedAt INTEGER NOT NULL
                    )
                """.trimIndent())
            }
        }
    }
}

