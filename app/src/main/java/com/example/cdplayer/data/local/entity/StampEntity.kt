package com.example.cdplayer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stamps")
data class StampEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val stampImageName: String,     // e.g. "stamp_01.png" - asset filename
    val boardNumber: Int,           // which board (1, 2, 3, ...) - rotates every 12
    val position: Int,              // 0-11 position on the board
    val collectedAt: Long = System.currentTimeMillis()
)
