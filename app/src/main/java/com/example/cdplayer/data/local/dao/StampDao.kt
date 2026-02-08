package com.example.cdplayer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cdplayer.data.local.entity.StampEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StampDao {
    @Query("SELECT * FROM stamps WHERE boardNumber = :boardNumber ORDER BY position ASC")
    fun getStampsByBoard(boardNumber: Int): Flow<List<StampEntity>>

    @Query("SELECT COALESCE(MAX(boardNumber), 1) FROM stamps")
    fun getLatestBoardNumber(): Flow<Int>

    @Query("SELECT COUNT(*) FROM stamps WHERE boardNumber = :boardNumber")
    suspend fun getStampCountForBoard(boardNumber: Int): Int

    @Query("SELECT COUNT(*) FROM stamps")
    fun getTotalStampCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStamp(stamp: StampEntity)

    @Query("SELECT * FROM stamps ORDER BY collectedAt DESC")
    fun getAllStamps(): Flow<List<StampEntity>>

    @Query("SELECT COALESCE(MAX(boardNumber), 0) FROM stamps")
    suspend fun getMaxBoardNumber(): Int

    @Query("SELECT COUNT(*) FROM stamps WHERE boardNumber = :boardNumber")
    fun getStampCountForBoardFlow(boardNumber: Int): Flow<Int>

    @Query("SELECT COUNT(*) FROM stamps WHERE collectedAt >= :startOfDay")
    suspend fun getStampsCollectedSince(startOfDay: Long): Int
}
