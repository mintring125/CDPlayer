package com.example.cdplayer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pdf_books")
data class PdfBookEntity(
    @PrimaryKey
    val filePath: String,
    val fileName: String,
    val lastPage: Int = 1,
    val totalPages: Int = 0,
    val lastReadAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val coverPath: String? = null
)

