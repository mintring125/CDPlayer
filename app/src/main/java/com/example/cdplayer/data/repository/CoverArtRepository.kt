package com.example.cdplayer.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.cdplayer.data.remote.api.GoogleBooksApi
import com.example.cdplayer.data.remote.api.LastFmApi
import com.example.cdplayer.data.remote.api.MusicBrainzApi
import com.example.cdplayer.domain.model.AudioFile
import com.example.cdplayer.domain.model.AudioType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

data class CoverArtResult(
    val imageUrl: String?,
    val source: String,
    val confidence: Float
)

@Singleton
class CoverArtRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val lastFmApi: LastFmApi,
    private val googleBooksApi: GoogleBooksApi,
    private val musicBrainzApi: MusicBrainzApi,
    private val okHttpClient: OkHttpClient
) {
    private val coverArtDir = File(context.filesDir, "cover_art")

    init {
        if (!coverArtDir.exists()) {
            coverArtDir.mkdirs()
        }
    }

    suspend fun searchCoverArt(audioFile: AudioFile): CoverArtResult? = withContext(Dispatchers.IO) {
        return@withContext when (audioFile.type) {
            AudioType.MUSIC -> searchMusicCoverArt(audioFile)
            AudioType.AUDIOBOOK -> searchAudiobookCoverArt(audioFile)
        }
    }

    private suspend fun searchMusicCoverArt(audioFile: AudioFile): CoverArtResult? {
        val artist = audioFile.artist ?: return null
        val album = audioFile.album

        // 1. Last.fm API 시도
        try {
            if (album != null) {
                val response = lastFmApi.getAlbumInfo(
                    artist = artist,
                    album = album,
                    apiKey = LastFmApi.API_KEY
                )
                response.album?.getLargestImageUrl()?.let { url ->
                    return CoverArtResult(url, "Last.fm", 0.9f)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 2. MusicBrainz API 시도
        try {
            val query = buildString {
                append("artist:\"$artist\"")
                if (album != null) {
                    append(" AND release:\"$album\"")
                }
            }
            val response = musicBrainzApi.searchRelease(query)
            response.releases?.firstOrNull()?.getCoverArtUrl()?.let { url ->
                return CoverArtResult(url, "MusicBrainz", 0.8f)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 3. Last.fm 앨범 검색 시도
        try {
            val searchQuery = if (album != null) "$artist $album" else artist
            val response = lastFmApi.searchAlbum(
                album = searchQuery,
                apiKey = LastFmApi.API_KEY
            )
            response.results?.albumMatches?.albums?.firstOrNull()?.getLargestImageUrl()?.let { url ->
                return CoverArtResult(url, "Last.fm Search", 0.6f)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    private suspend fun searchAudiobookCoverArt(audioFile: AudioFile): CoverArtResult? {
        val title = audioFile.album ?: audioFile.title
        val author = audioFile.artist

        // 1. Google Books API 시도
        try {
            val query = buildString {
                append("intitle:$title")
                if (author != null) {
                    append("+inauthor:$author")
                }
            }
            val response = googleBooksApi.searchBooks(query)
            response.items?.firstOrNull()?.volumeInfo?.getBestImageUrl()?.let { url ->
                return CoverArtResult(url, "Google Books", 0.85f)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 2. 제목만으로 다시 시도
        try {
            val response = googleBooksApi.searchBooks(title)
            response.items?.firstOrNull()?.volumeInfo?.getBestImageUrl()?.let { url ->
                return CoverArtResult(url, "Google Books (title only)", 0.6f)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    suspend fun downloadAndSaveCoverArt(audioFileId: Long, imageUrl: String): String? = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(imageUrl)
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) return@withContext null

            val bytes = response.body?.bytes() ?: return@withContext null
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size) ?: return@withContext null

            val outputFile = File(coverArtDir, "cover_$audioFileId.jpg")
            FileOutputStream(outputFile).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
            }
            bitmap.recycle()

            outputFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getCachedCoverArtPath(audioFileId: Long): String? {
        val file = File(coverArtDir, "cover_$audioFileId.jpg")
        return if (file.exists()) file.absolutePath else null
    }

    suspend fun deleteCoverArt(audioFileId: Long) = withContext(Dispatchers.IO) {
        val file = File(coverArtDir, "cover_$audioFileId.jpg")
        if (file.exists()) {
            file.delete()
        }
    }

    suspend fun clearAllCoverArt() = withContext(Dispatchers.IO) {
        coverArtDir.listFiles()?.forEach { it.delete() }
    }
}
