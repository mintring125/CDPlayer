package com.example.cdplayer.data.local.dao

import androidx.room.*
import com.example.cdplayer.data.local.entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmarks ORDER BY createdDate DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmarks WHERE audioFileId = :audioFileId ORDER BY position ASC")
    fun getBookmarksForAudio(audioFileId: Long): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmarks WHERE id = :id")
    suspend fun getBookmarkById(id: Long): BookmarkEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookmark: BookmarkEntity): Long

    @Update
    suspend fun update(bookmark: BookmarkEntity)

    @Delete
    suspend fun delete(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM bookmarks WHERE audioFileId = :audioFileId")
    suspend fun deleteAllForAudio(audioFileId: Long)

    @Query("SELECT COUNT(*) FROM bookmarks WHERE audioFileId = :audioFileId")
    suspend fun getCountForAudio(audioFileId: Long): Int
}
