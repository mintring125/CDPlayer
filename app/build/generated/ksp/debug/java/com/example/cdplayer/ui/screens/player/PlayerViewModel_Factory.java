package com.example.cdplayer.ui.screens.player;

import com.example.cdplayer.data.repository.BookmarkRepository;
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
public final class PlayerViewModel_Factory implements Factory<PlayerViewModel> {
  private final Provider<MusicPlayerManager> musicPlayerManagerProvider;

  private final Provider<BookmarkRepository> bookmarkRepositoryProvider;

  public PlayerViewModel_Factory(Provider<MusicPlayerManager> musicPlayerManagerProvider,
      Provider<BookmarkRepository> bookmarkRepositoryProvider) {
    this.musicPlayerManagerProvider = musicPlayerManagerProvider;
    this.bookmarkRepositoryProvider = bookmarkRepositoryProvider;
  }

  @Override
  public PlayerViewModel get() {
    return newInstance(musicPlayerManagerProvider.get(), bookmarkRepositoryProvider.get());
  }

  public static PlayerViewModel_Factory create(
      Provider<MusicPlayerManager> musicPlayerManagerProvider,
      Provider<BookmarkRepository> bookmarkRepositoryProvider) {
    return new PlayerViewModel_Factory(musicPlayerManagerProvider, bookmarkRepositoryProvider);
  }

  public static PlayerViewModel newInstance(MusicPlayerManager musicPlayerManager,
      BookmarkRepository bookmarkRepository) {
    return new PlayerViewModel(musicPlayerManager, bookmarkRepository);
  }
}
