package com.example.cdplayer.ui.screens.playlist;

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
public final class PlaylistViewModel_Factory implements Factory<PlaylistViewModel> {
  private final Provider<PlaylistRepository> playlistRepositoryProvider;

  private final Provider<MusicPlayerManager> musicPlayerManagerProvider;

  public PlaylistViewModel_Factory(Provider<PlaylistRepository> playlistRepositoryProvider,
      Provider<MusicPlayerManager> musicPlayerManagerProvider) {
    this.playlistRepositoryProvider = playlistRepositoryProvider;
    this.musicPlayerManagerProvider = musicPlayerManagerProvider;
  }

  @Override
  public PlaylistViewModel get() {
    return newInstance(playlistRepositoryProvider.get(), musicPlayerManagerProvider.get());
  }

  public static PlaylistViewModel_Factory create(
      Provider<PlaylistRepository> playlistRepositoryProvider,
      Provider<MusicPlayerManager> musicPlayerManagerProvider) {
    return new PlaylistViewModel_Factory(playlistRepositoryProvider, musicPlayerManagerProvider);
  }

  public static PlaylistViewModel newInstance(PlaylistRepository playlistRepository,
      MusicPlayerManager musicPlayerManager) {
    return new PlaylistViewModel(playlistRepository, musicPlayerManager);
  }
}
