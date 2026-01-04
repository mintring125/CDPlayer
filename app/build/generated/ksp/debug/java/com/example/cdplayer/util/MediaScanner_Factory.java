package com.example.cdplayer.util;

import android.content.Context;
import com.example.cdplayer.data.repository.AudioRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class MediaScanner_Factory implements Factory<MediaScanner> {
  private final Provider<Context> contextProvider;

  private final Provider<Id3TagReader> id3TagReaderProvider;

  private final Provider<AudioRepository> audioRepositoryProvider;

  public MediaScanner_Factory(Provider<Context> contextProvider,
      Provider<Id3TagReader> id3TagReaderProvider,
      Provider<AudioRepository> audioRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.id3TagReaderProvider = id3TagReaderProvider;
    this.audioRepositoryProvider = audioRepositoryProvider;
  }

  @Override
  public MediaScanner get() {
    return newInstance(contextProvider.get(), id3TagReaderProvider.get(), audioRepositoryProvider.get());
  }

  public static MediaScanner_Factory create(Provider<Context> contextProvider,
      Provider<Id3TagReader> id3TagReaderProvider,
      Provider<AudioRepository> audioRepositoryProvider) {
    return new MediaScanner_Factory(contextProvider, id3TagReaderProvider, audioRepositoryProvider);
  }

  public static MediaScanner newInstance(Context context, Id3TagReader id3TagReader,
      AudioRepository audioRepository) {
    return new MediaScanner(context, id3TagReader, audioRepository);
  }
}
