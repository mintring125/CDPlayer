package com.example.cdplayer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cdplayer.data.local.entity.PdfBookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PdfBookDao {

    @Query("SELECT * FROM pdf_books ORDER BY lastReadAt DESC")
    fun getAllBooks(): Flow<List<PdfBookEntity>>

    @Query("SELECT * FROM pdf_books WHERE isFavorite = 1 ORDER BY lastReadAt DESC")
    fun getFavorites(): Flow<List<PdfBookEntity>>

    @Query("SELECT * FROM pdf_books WHERE filePath = :filePath LIMIT 1")
    suspend fun getBook(filePath: String): PdfBookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(book: PdfBookEntity)

    @Query("UPDATE pdf_books SET lastPage = :page, lastReadAt = :time WHERE filePath = :filePath")
    suspend fun updateLastPage(filePath: String, page: Int, time: Long = System.currentTimeMillis())

    @Query("UPDATE pdf_books SET isFavorite = :isFavorite WHERE filePath = :filePath")
    suspend fun updateFavorite(filePath: String, isFavorite: Boolean)

    @Query("UPDATE pdf_books SET coverPath = :coverPath WHERE filePath = :filePath")
    suspend fun updateCoverPath(filePath: String, coverPath: String)

    @Query("DELETE FROM pdf_books WHERE filePath = :filePath")
    suspend fun delete(filePath: String)

    @Query("UPDATE pdf_books SET rating = :rating WHERE filePath = :filePath")
    suspend fun updateRating(filePath: String, rating: Float)

    @Query("SELECT rating FROM pdf_books WHERE filePath = :filePath")
    suspend fun getRating(filePath: String): Float?
}

