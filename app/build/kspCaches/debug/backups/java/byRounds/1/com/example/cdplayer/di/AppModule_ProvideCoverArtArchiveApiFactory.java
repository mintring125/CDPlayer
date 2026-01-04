package com.example.cdplayer.di;

import com.example.cdplayer.data.remote.api.CoverArtArchiveApi;
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
public final class AppModule_ProvideCoverArtArchiveApiFactory implements Factory<CoverArtArchiveApi> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  public AppModule_ProvideCoverArtArchiveApiFactory(Provider<OkHttpClient> okHttpClientProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public CoverArtArchiveApi get() {
    return provideCoverArtArchiveApi(okHttpClientProvider.get());
  }

  public static AppModule_ProvideCoverArtArchiveApiFactory create(
      Provider<OkHttpClient> okHttpClientProvider) {
    return new AppModule_ProvideCoverArtArchiveApiFactory(okHttpClientProvider);
  }

  public static CoverArtArchiveApi provideCoverArtArchiveApi(OkHttpClient okHttpClient) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideCoverArtArchiveApi(okHttpClient));
  }
}
