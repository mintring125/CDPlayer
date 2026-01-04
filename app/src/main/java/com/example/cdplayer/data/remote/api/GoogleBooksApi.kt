package com.example.cdplayer.data.remote.api

import com.example.cdplayer.data.remote.dto.GoogleBooksResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleBooksApi {

    @GET("books/v1/volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 5,
        @Query("printType") printType: String = "books"
    ): GoogleBooksResponse

    @GET("books/v1/volumes")
    suspend fun searchAudiobooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 5,
        @Query("filter") filter: String = "ebooks"
    ): GoogleBooksResponse

    companion object {
        const val BASE_URL = "https://www.googleapis.com/"
    }
}
