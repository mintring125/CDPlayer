package com.example.cdplayer.di;

import com.example.cdplayer.data.remote.api.LastFmApi;
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
public final class AppModule_ProvideLastFmApiFactory implements Factory<LastFmApi> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  public AppModule_ProvideLastFmApiFactory(Provider<OkHttpClient> okHttpClientProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public LastFmApi get() {
    return provideLastFmApi(okHttpClientProvider.get());
  }

  public static AppModule_ProvideLastFmApiFactory create(
      Provider<OkHttpClient> okHttpClientProvider) {
    return new AppModule_ProvideLastFmApiFactory(okHttpClientProvider);
  }

  public static LastFmApi provideLastFmApi(OkHttpClient okHttpClient) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideLastFmApi(okHttpClient));
  }
}
