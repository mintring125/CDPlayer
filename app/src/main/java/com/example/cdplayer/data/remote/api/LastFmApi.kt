package com.example.cdplayer.data.remote.api

import com.example.cdplayer.data.remote.dto.LastFmAlbumInfoResponse
import com.example.cdplayer.data.remote.dto.LastFmAlbumSearchResponse
import com.example.cdplayer.data.remote.dto.LastFmArtistInfoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmApi {

    @GET("2.0/")
    suspend fun getAlbumInfo(
        @Query("method") method: String = "album.getinfo",
        @Query("artist") artist: String,
        @Query("album") album: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json"
    ): LastFmAlbumInfoResponse

    @GET("2.0/")
    suspend fun searchAlbum(
        @Query("method") method: String = "album.search",
        @Query("album") album: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 5
    ): LastFmAlbumSearchResponse

    @GET("2.0/")
    suspend fun getArtistInfo(
        @Query("method") method: String = "artist.getinfo",
        @Query("artist") artist: String,
        @Query("api_key") apiKey: String,
        @Query("format") format: String = "json"
    ): LastFmArtistInfoResponse

    companion object {
        const val BASE_URL = "https://ws.audioscrobbler.com/"
        // My daugheter player - Last.fm API credentials
        const val API_KEY = "807fe8e8c07804af9c6a94d715b36c95"
    }
}
