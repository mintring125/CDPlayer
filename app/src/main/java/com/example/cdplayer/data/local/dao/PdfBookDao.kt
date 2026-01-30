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

    @Query("SELECT * FROM pdf_books WHERE filePath = :filePath LIMIT 1")
    suspend fun getBook(filePath: String): PdfBookEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(book: PdfBookEntity)

    @Query("UPDATE pdf_books SET lastPage = :page, lastReadAt = :time WHERE filePath = :filePath")
    suspend fun updateLastPage(filePath: String, page: Int, time: Long = System.currentTimeMillis())

    @Query("DELETE FROM pdf_books WHERE filePath = :filePath")
    suspend fun delete(filePath: String)
}
