package com.example.cdplayer.data.repository

import com.example.cdplayer.data.local.dao.PlaylistDao
import com.example.cdplayer.data.local.entity.PlaylistEntity
import com.example.cdplayer.data.local.entity.PlaylistTrackCrossRef
import com.example.cdplayer.domain.model.AudioFile
import com.example.cdplayer.domain.model.Playlist
import com.example.cdplayer.domain.model.PlaylistWithTracks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistRepository @Inject constructor(
    private val playlistDao: PlaylistDao
) {

    fun getAllPlaylists(): Flow<List<Playlist>> =
        playlistDao.getAllPlaylists().map { entities ->
            entities.map { entity ->
                val trackCount = playlistDao.getTrackCountForPlaylist(entity.id)
                val totalDuration = playlistDao.getTotalDurationForPlaylist(entity.id)
                entity.toDomain(trackCount, totalDuration)
            }
        }

    suspend fun getPlaylistById(id: Long): Playlist? {
        val entity = playlistDao.getPlaylistById(id) ?: return null
        val trackCount = playlistDao.getTrackCountForPlaylist(id)
        val totalDuration = playlistDao.getTotalDurationForPlaylist(id)
        return entity.toDomain(trackCount, totalDuration)
    }

    fun getPlaylistByIdFlow(id: Long): Flow<Playlist?> =
        playlistDao.getPlaylistByIdFlow(id).map { entity ->
            entity?.let {
                val trackCount = playlistDao.getTrackCountForPlaylist(id)
                val totalDuration = playlistDao.getTotalDurationForPlaylist(id)
                it.toDomain(trackCount, totalDuration)
            }
        }

    fun getPlaylistWithTracks(id: Long): Flow<PlaylistWithTracks?> {
        return combine(
            getPlaylistByIdFlow(id),
            getTracksForPlaylist(id)
        ) { playlist, tracks ->
            playlist?.let { PlaylistWithTracks(it, tracks) }
        }
    }

    fun getTracksForPlaylist(playlistId: Long): Flow<List<AudioFile>> =
        playlistDao.getTracksForPlaylist(playlistId).map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun createPlaylist(name: String, description: String? = null): Long {
        val now = System.currentTimeMillis()
        val entity = PlaylistEntity(
            name = name,
            description = description,
            createdDate = now,
            modifiedDate = now,
            coverArtPath = null
        )
        return playlistDao.insert(entity)
    }

    suspend fun updatePlaylist(playlist: Playlist) {
        val entity = PlaylistEntity.fromDomain(playlist).copy(
            modifiedDate = System.currentTimeMillis()
        )
        playlistDao.update(entity)
    }

    suspend fun deletePlaylist(id: Long) =
        playlistDao.deleteById(id)

    suspend fun addTrackToPlaylist(playlistId: Long, audioFileId: Long) {
        val maxIndex = playlistDao.getMaxOrderIndex(playlistId) ?: -1
        val crossRef = PlaylistTrackCrossRef(
            playlistId = playlistId,
            audioFileId = audioFileId,
            orderIndex = maxIndex + 1,
            addedDate = System.currentTimeMillis()
        )
        playlistDao.addTrackToPlaylist(crossRef)
    }

    suspend fun addTracksToPlaylist(playlistId: Long, audioFileIds: List<Long>) {
        var maxIndex = playlistDao.getMaxOrderIndex(playlistId) ?: -1
        val crossRefs = audioFileIds.map { audioFileId ->
            maxIndex++
            PlaylistTrackCrossRef(
                playlistId = playlistId,
                audioFileId = audioFileId,
                orderIndex = maxIndex,
                addedDate = System.currentTimeMillis()
            )
        }
        playlistDao.addTracksToPlaylist(crossRefs)
    }

    suspend fun removeTrackFromPlaylist(playlistId: Long, audioFileId: Long) =
        playlistDao.removeTrackFromPlaylist(playlistId, audioFileId)

    suspend fun clearPlaylist(playlistId: Long) =
        playlistDao.clearPlaylist(playlistId)

    suspend fun isTrackInPlaylist(playlistId: Long, audioFileId: Long): Boolean =
        playlistDao.isTrackInPlaylist(playlistId, audioFileId)

    suspend fun reorderTracks(playlistId: Long, fromIndex: Int, toIndex: Int) {
        val tracks = playlistDao.getPlaylistTracks(playlistId).toMutableList()
        if (fromIndex < 0 || fromIndex >= tracks.size || toIndex < 0 || toIndex >= tracks.size) {
            return
        }
        val movedTrack = tracks.removeAt(fromIndex)
        tracks.add(toIndex, movedTrack)
        tracks.forEachIndexed { index, track ->
            playlistDao.updateTrackOrder(playlistId, track.audioFileId, index)
        }
    }
}
