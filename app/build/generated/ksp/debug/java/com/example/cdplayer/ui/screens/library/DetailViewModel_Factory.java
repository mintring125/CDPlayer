package com.example.cdplayer.ui.screens.library;

import androidx.lifecycle.SavedStateHandle;
import com.example.cdplayer.data.repository.AudioRepository;
import com.example.cdplayer.player.MusicPlayerManager;
import com.example.cdplayer.util.Id3TagWriter;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class DetailViewModel_Factory implements Factory<DetailViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<AudioRepository> audioRepositoryProvider;

  private final Provider<MusicPlayerManager> musicPlayerManagerProvider;

  private final Provider<Id3TagWriter> id3TagWriterProvider;

  public DetailViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<AudioRepository> audioRepositoryProvider,
      Provider<MusicPlayerManager> musicPlayerManagerProvider,
      Provider<Id3TagWriter> id3TagWriterProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.audioRepositoryProvider = audioRepositoryProvider;
    this.musicPlayerManagerProvider = musicPlayerManagerProvider;
    this.id3TagWriterProvider = id3TagWriterProvider;
  }

  @Override
  public DetailViewModel get() {
    return newInstance(savedStateHandleProvider.get(), audioRepositoryProvider.get(), musicPlayerManagerProvider.get(), id3TagWriterProvider.get());
  }

  public static DetailViewModel_Factory create(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<AudioRepository> audioRepositoryProvider,
      Provider<MusicPlayerManager> musicPlayerManagerProvider,
      Provider<Id3TagWriter> id3TagWriterProvider) {
    return new DetailViewModel_Factory(savedStateHandleProvider, audioRepositoryProvider, musicPlayerManagerProvider, id3TagWriterProvider);
  }

  public static DetailViewModel newInstance(SavedStateHandle savedStateHandle,
      AudioRepository audioRepository, MusicPlayerManager musicPlayerManager,
      Id3TagWriter id3TagWriter) {
    return new DetailViewModel(savedStateHandle, audioRepository, musicPlayerManager, id3TagWriter);
  }
}
