package com.example.cdplayer.di;

import com.example.cdplayer.data.remote.api.MusicBrainzApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class AppModule_ProvideMusicBrainzApiFactory implements Factory<MusicBrainzApi> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  public AppModule_ProvideMusicBrainzApiFactory(Provider<OkHttpClient> okHttpClientProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public MusicBrainzApi get() {
    return provideMusicBrainzApi(okHttpClientProvider.get());
  }

  public static AppModule_ProvideMusicBrainzApiFactory create(
      Provider<OkHttpClient> okHttpClientProvider) {
    return new AppModule_ProvideMusicBrainzApiFactory(okHttpClientProvider);
  }

  public static MusicBrainzApi provideMusicBrainzApi(OkHttpClient okHttpClient) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideMusicBrainzApi(okHttpClient));
  }
}
