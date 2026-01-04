package com.example.cdplayer.di;

import android.content.Context;
import com.example.cdplayer.util.Id3TagReader;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class AppModule_ProvideId3TagReaderFactory implements Factory<Id3TagReader> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideId3TagReaderFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public Id3TagReader get() {
    return provideId3TagReader(contextProvider.get());
  }

  public static AppModule_ProvideId3TagReaderFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideId3TagReaderFactory(contextProvider);
  }

  public static Id3TagReader provideId3TagReader(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideId3TagReader(context));
  }
}
