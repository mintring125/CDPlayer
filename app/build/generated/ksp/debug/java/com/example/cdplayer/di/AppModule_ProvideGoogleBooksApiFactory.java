package com.example.cdplayer.di;

import com.example.cdplayer.data.remote.api.GoogleBooksApi;
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
public final class AppModule_ProvideGoogleBooksApiFactory implements Factory<GoogleBooksApi> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  public AppModule_ProvideGoogleBooksApiFactory(Provider<OkHttpClient> okHttpClientProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public GoogleBooksApi get() {
    return provideGoogleBooksApi(okHttpClientProvider.get());
  }

  public static AppModule_ProvideGoogleBooksApiFactory create(
      Provider<OkHttpClient> okHttpClientProvider) {
    return new AppModule_ProvideGoogleBooksApiFactory(okHttpClientProvider);
  }

  public static GoogleBooksApi provideGoogleBooksApi(OkHttpClient okHttpClient) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideGoogleBooksApi(okHttpClient));
  }
}
