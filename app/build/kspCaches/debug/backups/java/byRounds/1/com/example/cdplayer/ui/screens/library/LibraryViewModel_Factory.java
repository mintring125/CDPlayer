package com.example.cdplayer.ui.screens.library;

import com.example.cdplayer.data.repository.AudioRepository;
import com.example.cdplayer.data.repository.PlaylistRepository;
import com.example.cdplayer.player.MusicPlayerManager;
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
public final class LibraryViewModel_Factory implements Factory<LibraryViewModel> {
  private final Provider<AudioRepository> audioRepositoryProvider;

  private final Provider<PlaylistRepository> playlistRepositoryProvider;

  private final Provider<MusicPlayerManager> musicPlayerManagerProvider;

  public LibraryViewModel_Factory(Provider<AudioRepository> audioRepositoryProvider,
      Provider<PlaylistRepository> playlistRepositoryProvider,
      Provider<MusicPlayerManager> musicPlayerManagerProvider) {
    this.audioRepositoryProvider = audioRepositoryProvider;
    this.playlistRepositoryProvider = playlistRepositoryProvider;
    this.musicPlayerManagerProvider = musicPlayerManagerProvider;
  }

  @Override
  public LibraryViewModel get() {
    return newInstance(audioRepositoryProvider.get(), playlistRepositoryProvider.get(), musicPlayerManagerProvider.get());
  }

  public static LibraryViewModel_Factory create(Provider<AudioRepository> audioRepositoryProvider,
      Provider<PlaylistRepository> playlistRepositoryProvider,
      Provider<MusicPlayerManager> musicPlayerManagerProvider) {
    return new LibraryViewModel_Factory(audioRepositoryProvider, playlistRepositoryProvider, musicPlayerManagerProvider);
  }

  public static LibraryViewModel newInstance(AudioRepository audioRepository,
      PlaylistRepository playlistRepository, MusicPlayerManager musicPlayerManager) {
    return new LibraryViewModel(audioRepository, playlistRepository, musicPlayerManager);
  }
}
