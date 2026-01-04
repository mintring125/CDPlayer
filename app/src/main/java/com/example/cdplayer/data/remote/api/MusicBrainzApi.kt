package com.example.cdplayer.data.remote.api

import com.example.cdplayer.data.remote.dto.CoverArtArchiveResponse
import com.example.cdplayer.data.remote.dto.MusicBrainzReleaseSearchResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MusicBrainzApi {

    @Headers("User-Agent: CDPlayer/1.0 (contact@example.com)")
    @GET("ws/2/release/")
    suspend fun searchRelease(
        @Query("query") query: String,
        @Query("fmt") format: String = "json",
        @Query("limit") limit: Int = 5
    ): MusicBrainzReleaseSearchResponse

    companion object {
        const val BASE_URL = "https://musicbrainz.org/"
    }
}

interface CoverArtArchiveApi {

    @GET("release/{mbid}")
    suspend fun getCoverArt(
        @Path("mbid") mbid: String
    ): CoverArtArchiveResponse

    @GET("release/{mbid}/front")
    suspend fun getFrontCover(
        @Path("mbid") mbid: String
    ): okhttp3.ResponseBody

    companion object {
        const val BASE_URL = "https://coverartarchive.org/"
    }
}
