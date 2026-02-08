package com.example.cdplayer.di

import android.content.Context
import androidx.room.Room
import com.example.cdplayer.data.local.AppDatabase
import com.example.cdplayer.data.local.dao.AudioFileDao
import com.example.cdplayer.data.local.dao.BookmarkDao
import com.example.cdplayer.data.local.dao.PdfBookDao
import com.example.cdplayer.data.local.dao.PlaylistDao
import com.example.cdplayer.data.local.dao.StampDao
import com.example.cdplayer.data.remote.api.CoverArtArchiveApi
import com.example.cdplayer.data.remote.api.DictionaryApi
import com.example.cdplayer.data.remote.api.GoogleTranslateApi
import com.example.cdplayer.data.remote.api.GoogleBooksApi
import com.example.cdplayer.data.remote.api.LastFmApi
import com.example.cdplayer.data.remote.api.MusicBrainzApi
import com.example.cdplayer.util.Id3TagReader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Database
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .addMigrations(AppDatabase.MIGRATION_3_4, AppDatabase.MIGRATION_4_5, AppDatabase.MIGRATION_5_6)
            .build()
    }

    @Provides
    @Singleton
    fun provideAudioFileDao(database: AppDatabase): AudioFileDao {
        return database.audioFileDao()
    }

    @Provides
    @Singleton
    fun providePlaylistDao(database: AppDatabase): PlaylistDao {
        return database.playlistDao()
    }

    @Provides
    @Singleton
    fun provideBookmarkDao(database: AppDatabase): BookmarkDao {
        return database.bookmarkDao()
    }

    @Provides
    @Singleton
    fun providePdfBookDao(database: AppDatabase): PdfBookDao {
        return database.pdfBookDao()
    }

    @Provides
    @Singleton
    fun provideStampDao(database: AppDatabase): StampDao {
        return database.stampDao()
    }

    // Network
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideLastFmApi(okHttpClient: OkHttpClient): LastFmApi {
        return Retrofit.Builder()
            .baseUrl(LastFmApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LastFmApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGoogleBooksApi(okHttpClient: OkHttpClient): GoogleBooksApi {
        return Retrofit.Builder()
            .baseUrl(GoogleBooksApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GoogleBooksApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMusicBrainzApi(okHttpClient: OkHttpClient): MusicBrainzApi {
        return Retrofit.Builder()
            .baseUrl(MusicBrainzApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MusicBrainzApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCoverArtArchiveApi(okHttpClient: OkHttpClient): CoverArtArchiveApi {
        return Retrofit.Builder()
            .baseUrl(CoverArtArchiveApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CoverArtArchiveApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDictionaryApi(okHttpClient: OkHttpClient): DictionaryApi {
        return Retrofit.Builder()
            .baseUrl(DictionaryApi.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DictionaryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGoogleTranslateApi(okHttpClient: OkHttpClient): GoogleTranslateApi {
        return Retrofit.Builder()
            .baseUrl(GoogleTranslateApi.BASE_URL)
            .client(okHttpClient)
            .build()
            .create(GoogleTranslateApi::class.java)
    }

    // Utils
    @Provides
    @Singleton
    fun provideId3TagReader(@ApplicationContext context: Context): Id3TagReader {
        return Id3TagReader(context)
    }

    @Provides
    @Singleton
    fun provideId3TagWriter(@ApplicationContext context: Context): com.example.cdplayer.util.Id3TagWriter {
        return com.example.cdplayer.util.Id3TagWriter(context)
    }
}
