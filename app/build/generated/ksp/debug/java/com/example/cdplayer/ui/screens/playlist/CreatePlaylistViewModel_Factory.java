package com.example.cdplayer.ui.screens.playlist;

import com.example.cdplayer.data.repository.PlaylistRepository;
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
public final class CreatePlaylistViewModel_Factory implements Factory<CreatePlaylistViewModel> {
  private final Provider<PlaylistRepository> playlistRepositoryProvider;

  public CreatePlaylistViewModel_Factory(Provider<PlaylistRepository> playlistRepositoryProvider) {
    this.playlistRepositoryProvider = playlistRepositoryProvider;
  }

  @Override
  public CreatePlaylistViewModel get() {
    return newInstance(playlistRepositoryProvider.get());
  }

  public static CreatePlaylistViewModel_Factory create(
      Provider<PlaylistRepository> playlistRepositoryProvider) {
    return new CreatePlaylistViewModel_Factory(playlistRepositoryProvider);
  }

  public static CreatePlaylistViewModel newInstance(PlaylistRepository playlistRepository) {
    return new CreatePlaylistViewModel(playlistRepository);
  }
}
