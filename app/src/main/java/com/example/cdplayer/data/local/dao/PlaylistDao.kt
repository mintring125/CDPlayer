package com.example.cdplayer.data.local.dao

import androidx.room.*
import com.example.cdplayer.data.local.entity.AudioFileEntity
import com.example.cdplayer.data.local.entity.PlaylistEntity
import com.example.cdplayer.data.local.entity.PlaylistTrackCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Query("SELECT * FROM playlists ORDER BY modifiedDate DESC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE id = :id")
    suspend fun getPlaylistById(id: Long): PlaylistEntity?

    @Query("SELECT * FROM playlists WHERE id = :id")
    fun getPlaylistByIdFlow(id: Long): Flow<PlaylistEntity?>

    @Query("""
        SELECT af.* FROM audio_files af
        INNER JOIN playlist_tracks pt ON af.id = pt.audioFileId
        WHERE pt.playlistId = :playlistId
        ORDER BY pt.orderIndex ASC
    """)
    fun getTracksForPlaylist(playlistId: Long): Flow<List<AudioFileEntity>>

    @Query("SELECT COUNT(*) FROM playlist_tracks WHERE playlistId = :playlistId")
    suspend fun getTrackCountForPlaylist(playlistId: Long): Int

    @Query("""
        SELECT COALESCE(SUM(af.duration), 0) FROM audio_files af
        INNER JOIN playlist_tracks pt ON af.id = pt.audioFileId
        WHERE pt.playlistId = :playlistId
    """)
    suspend fun getTotalDurationForPlaylist(playlistId: Long): Long

    @Query("SELECT * FROM playlist_tracks WHERE playlistId = :playlistId ORDER BY orderIndex ASC")
    suspend fun getPlaylistTracks(playlistId: Long): List<PlaylistTrackCrossRef>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlist: PlaylistEntity): Long

    @Update
    suspend fun update(playlist: PlaylistEntity)

    @Delete
    suspend fun delete(playlist: PlaylistEntity)

    @Query("DELETE FROM playlists WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToPlaylist(crossRef: PlaylistTrackCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTracksToPlaylist(crossRefs: List<PlaylistTrackCrossRef>)

    @Query("DELETE FROM playlist_tracks WHERE playlistId = :playlistId AND audioFileId = :audioFileId")
    suspend fun removeTrackFromPlaylist(playlistId: Long, audioFileId: Long)

    @Query("DELETE FROM playlist_tracks WHERE playlistId = :playlistId")
    suspend fun clearPlaylist(playlistId: Long)

    @Query("SELECT MAX(orderIndex) FROM playlist_tracks WHERE playlistId = :playlistId")
    suspend fun getMaxOrderIndex(playlistId: Long): Int?

    @Query("""
        UPDATE playlist_tracks
        SET orderIndex = :newIndex
        WHERE playlistId = :playlistId AND audioFileId = :audioFileId
    """)
    suspend fun updateTrackOrder(playlistId: Long, audioFileId: Long, newIndex: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM playlist_tracks WHERE playlistId = :playlistId AND audioFileId = :audioFileId)")
    suspend fun isTrackInPlaylist(playlistId: Long, audioFileId: Long): Boolean
}
