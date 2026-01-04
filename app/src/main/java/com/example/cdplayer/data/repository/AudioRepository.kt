package com.example.cdplayer.data.repository

import com.example.cdplayer.data.local.dao.AudioFileDao
import com.example.cdplayer.data.local.entity.AudioFileEntity
import com.example.cdplayer.domain.model.AudioFile
import com.example.cdplayer.domain.model.AudioType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioRepository @Inject constructor(
    private val audioFileDao: AudioFileDao
) {

    fun getAllAudioFiles(): Flow<List<AudioFile>> =
        audioFileDao.getAllAudioFiles().map { entities ->
            entities.map { it.toDomain() }
        }

    fun getAudioFilesByType(type: AudioType): Flow<List<AudioFile>> =
        audioFileDao.getAudioFilesByType(type.name).map { entities ->
            entities.map { it.toDomain() }
        }

    fun getFavorites(): Flow<List<AudioFile>> =
        audioFileDao.getFavorites().map { entities ->
            entities.map { it.toDomain() }
        }

    fun getAllMusic(): Flow<List<AudioFile>> =
        audioFileDao.getAllMusic().map { entities ->
            entities.map { it.toDomain() }
        }

    fun getAllAudiobooks(): Flow<List<AudioFile>> =
        audioFileDao.getAllAudiobooks().map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun getAudioFileById(id: Long): AudioFile? =
        audioFileDao.getAudioFileById(id)?.toDomain()

    fun getAudioFileByIdFlow(id: Long): Flow<AudioFile?> =
        audioFileDao.getAudioFileByIdFlow(id).map { it?.toDomain() }

    suspend fun getAudioFileByPath(path: String): AudioFile? =
        audioFileDao.getAudioFileByPath(path)?.toDomain()

    fun getRecentlyPlayed(limit: Int = 20): Flow<List<AudioFile>> =
        audioFileDao.getRecentlyPlayed(limit).map { entities ->
            entities.map { it.toDomain() }
        }

    fun getAllArtists(): Flow<List<String>> =
        audioFileDao.getAllArtists()

    fun getAudioFilesByArtist(artist: String): Flow<List<AudioFile>> =
        audioFileDao.getAudioFilesByArtist(artist).map { entities ->
            entities.map { it.toDomain() }
        }

    fun getAllAlbums(): Flow<List<String>> =
        audioFileDao.getAllAlbums()

    fun getAudioFilesByAlbum(album: String): Flow<List<AudioFile>> =
        audioFileDao.getAudioFilesByAlbum(album).map { entities ->
            entities.map { it.toDomain() }
        }

    fun getAllGenres(): Flow<List<String>> =
        audioFileDao.getAllGenres()

    fun getAudioFilesByGenre(genre: String): Flow<List<AudioFile>> =
        audioFileDao.getAudioFilesByGenre(genre).map { entities ->
            entities.map { it.toDomain() }
        }

    fun searchAudioFiles(query: String): Flow<List<AudioFile>> =
        audioFileDao.searchAudioFiles(query).map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun insertAudioFile(audioFile: AudioFile): Long =
        audioFileDao.insert(AudioFileEntity.fromDomain(audioFile))

    suspend fun insertAudioFiles(audioFiles: List<AudioFile>) =
        audioFileDao.insertAll(audioFiles.map { AudioFileEntity.fromDomain(it) })

    suspend fun updateAudioFile(audioFile: AudioFile) =
        audioFileDao.update(AudioFileEntity.fromDomain(audioFile))

    suspend fun updatePlaybackPosition(id: Long, position: Long) =
        audioFileDao.updatePlaybackPosition(id, position, System.currentTimeMillis())

    suspend fun updateCoverArt(id: Long, coverArtPath: String?) =
        audioFileDao.updateCoverArt(id, coverArtPath)

    suspend fun moveAlbumToType(albumName: String, type: AudioType) =
        audioFileDao.updateTypeByAlbum(albumName, type.name)

    suspend fun deleteAudioFile(audioFile: AudioFile) =
        audioFileDao.delete(AudioFileEntity.fromDomain(audioFile))

    suspend fun deleteAudioFileById(id: Long) =
        audioFileDao.deleteById(id)

    suspend fun deleteNotInPaths(existingPaths: List<String>) =
        audioFileDao.deleteNotInPaths(existingPaths)

    suspend fun getCount(): Int =
        audioFileDao.getCount()

    suspend fun getCountByType(type: AudioType): Int =
        audioFileDao.getCountByType(type.name)
}
