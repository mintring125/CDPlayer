package com.example.cdplayer.data.repository

import com.example.cdplayer.data.local.dao.BookmarkDao
import com.example.cdplayer.data.local.entity.BookmarkEntity
import com.example.cdplayer.domain.model.Bookmark
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkRepository @Inject constructor(
    private val bookmarkDao: BookmarkDao
) {

    fun getAllBookmarks(): Flow<List<Bookmark>> =
        bookmarkDao.getAllBookmarks().map { entities ->
            entities.map { it.toDomain() }
        }

    fun getBookmarksForAudio(audioFileId: Long): Flow<List<Bookmark>> =
        bookmarkDao.getBookmarksForAudio(audioFileId).map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun getBookmarkById(id: Long): Bookmark? =
        bookmarkDao.getBookmarkById(id)?.toDomain()

    suspend fun createBookmark(audioFileId: Long, position: Long, note: String? = null): Long {
        val entity = BookmarkEntity(
            audioFileId = audioFileId,
            position = position,
            note = note,
            createdDate = System.currentTimeMillis()
        )
        return bookmarkDao.insert(entity)
    }

    suspend fun updateBookmark(bookmark: Bookmark) =
        bookmarkDao.update(BookmarkEntity.fromDomain(bookmark))

    suspend fun deleteBookmark(id: Long) =
        bookmarkDao.deleteById(id)

    suspend fun deleteAllBookmarksForAudio(audioFileId: Long) =
        bookmarkDao.deleteAllForAudio(audioFileId)

    suspend fun getBookmarkCountForAudio(audioFileId: Long): Int =
        bookmarkDao.getCountForAudio(audioFileId)
}
