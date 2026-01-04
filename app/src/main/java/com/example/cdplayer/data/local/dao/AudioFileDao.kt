package com.example.cdplayer.data.local.dao

import androidx.room.*
import com.example.cdplayer.data.local.entity.AudioFileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AudioFileDao {

    @Query("SELECT * FROM audio_files ORDER BY title ASC")
    fun getAllAudioFiles(): Flow<List<AudioFileEntity>>

    @Query("SELECT * FROM audio_files WHERE type = :type ORDER BY title ASC")
    fun getAudioFilesByType(type: String): Flow<List<AudioFileEntity>>
    
    @Query("SELECT * FROM audio_files WHERE isFavorite = 1 ORDER BY addedDate DESC")
    fun getFavorites(): Flow<List<AudioFileEntity>>

    @Query("SELECT * FROM audio_files WHERE type = 'MUSIC' ORDER BY title ASC")
    fun getAllMusic(): Flow<List<AudioFileEntity>>

    @Query("SELECT * FROM audio_files WHERE type = 'AUDIOBOOK' ORDER BY title ASC")
    fun getAllAudiobooks(): Flow<List<AudioFileEntity>>

    @Query("SELECT * FROM audio_files WHERE id = :id")
    suspend fun getAudioFileById(id: Long): AudioFileEntity?

    @Query("SELECT * FROM audio_files WHERE id = :id")
    fun getAudioFileByIdFlow(id: Long): Flow<AudioFileEntity?>

    @Query("SELECT * FROM audio_files WHERE path = :path")
    suspend fun getAudioFileByPath(path: String): AudioFileEntity?

    @Query("SELECT * FROM audio_files WHERE lastPlayedAt IS NOT NULL ORDER BY lastPlayedAt DESC LIMIT :limit")
    fun getRecentlyPlayed(limit: Int = 20): Flow<List<AudioFileEntity>>

    @Query("SELECT DISTINCT artist FROM audio_files WHERE artist IS NOT NULL ORDER BY artist ASC")
    fun getAllArtists(): Flow<List<String>>

    @Query("SELECT * FROM audio_files WHERE artist = :artist ORDER BY album, trackNumber")
    fun getAudioFilesByArtist(artist: String): Flow<List<AudioFileEntity>>

    @Query("SELECT DISTINCT album FROM audio_files WHERE album IS NOT NULL ORDER BY album ASC")
    fun getAllAlbums(): Flow<List<String>>

    @Query("SELECT * FROM audio_files WHERE album = :album ORDER BY trackNumber ASC")
    fun getAudioFilesByAlbum(album: String): Flow<List<AudioFileEntity>>

    @Query("SELECT DISTINCT genre FROM audio_files WHERE genre IS NOT NULL ORDER BY genre ASC")
    fun getAllGenres(): Flow<List<String>>

    @Query("SELECT * FROM audio_files WHERE genre = :genre ORDER BY title ASC")
    fun getAudioFilesByGenre(genre: String): Flow<List<AudioFileEntity>>

    @Query("""
        SELECT * FROM audio_files
        WHERE title LIKE '%' || :query || '%'
        OR artist LIKE '%' || :query || '%'
        OR album LIKE '%' || :query || '%'
        ORDER BY title ASC
    """)
    fun searchAudioFiles(query: String): Flow<List<AudioFileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(audioFile: AudioFileEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(audioFiles: List<AudioFileEntity>)

    @Update
    suspend fun update(audioFile: AudioFileEntity)

    @Query("UPDATE audio_files SET lastPlayedPosition = :position, lastPlayedAt = :playedAt WHERE id = :id")
    suspend fun updatePlaybackPosition(id: Long, position: Long, playedAt: Long)

    @Query("UPDATE audio_files SET coverArtPath = :coverArtPath WHERE id = :id")
    suspend fun updateCoverArt(id: Long, coverArtPath: String?)

    @Delete
    suspend fun delete(audioFile: AudioFileEntity)

    @Query("DELETE FROM audio_files WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM audio_files WHERE path NOT IN (:existingPaths)")
    suspend fun deleteNotInPaths(existingPaths: List<String>)

    @Query("SELECT COUNT(*) FROM audio_files")
    suspend fun getCount(): Int

    @Query("SELECT COUNT(*) FROM audio_files WHERE type = :type")
    suspend fun getCountByType(type: String): Int
}
