package com.example.cdplayer.data.repository;

import android.content.Context;
import com.example.cdplayer.data.remote.api.GoogleBooksApi;
import com.example.cdplayer.data.remote.api.LastFmApi;
import com.example.cdplayer.data.remote.api.MusicBrainzApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class CoverArtRepository_Factory implements Factory<CoverArtRepository> {
  private final Provider<Context> contextProvider;

  private final Provider<LastFmApi> lastFmApiProvider;

  private final Provider<GoogleBooksApi> googleBooksApiProvider;

  private final Provider<MusicBrainzApi> musicBrainzApiProvider;

  private final Provider<OkHttpClient> okHttpClientProvider;

  public CoverArtRepository_Factory(Provider<Context> contextProvider,
      Provider<LastFmApi> lastFmApiProvider, Provider<GoogleBooksApi> googleBooksApiProvider,
      Provider<MusicBrainzApi> musicBrainzApiProvider,
      Provider<OkHttpClient> okHttpClientProvider) {
    this.contextProvider = contextProvider;
    this.lastFmApiProvider = lastFmApiProvider;
    this.googleBooksApiProvider = googleBooksApiProvider;
    this.musicBrainzApiProvider = musicBrainzApiProvider;
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public CoverArtRepository get() {
    return newInstance(contextProvider.get(), lastFmApiProvider.get(), googleBooksApiProvider.get(), musicBrainzApiProvider.get(), okHttpClientProvider.get());
  }

  public static CoverArtRepository_Factory create(Provider<Context> contextProvider,
      Provider<LastFmApi> lastFmApiProvider, Provider<GoogleBooksApi> googleBooksApiProvider,
      Provider<MusicBrainzApi> musicBrainzApiProvider,
      Provider<OkHttpClient> okHttpClientProvider) {
    return new CoverArtRepository_Factory(contextProvider, lastFmApiProvider, googleBooksApiProvider, musicBrainzApiProvider, okHttpClientProvider);
  }

  public static CoverArtRepository newInstance(Context context, LastFmApi lastFmApi,
      GoogleBooksApi googleBooksApi, MusicBrainzApi musicBrainzApi, OkHttpClient okHttpClient) {
    return new CoverArtRepository(context, lastFmApi, googleBooksApi, musicBrainzApi, okHttpClient);
  }
}
