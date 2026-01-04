package com.example.cdplayer.data.repository;

import com.example.cdplayer.data.local.dao.PlaylistDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class PlaylistRepository_Factory implements Factory<PlaylistRepository> {
  private final Provider<PlaylistDao> playlistDaoProvider;

  public PlaylistRepository_Factory(Provider<PlaylistDao> playlistDaoProvider) {
    this.playlistDaoProvider = playlistDaoProvider;
  }

  @Override
  public PlaylistRepository get() {
    return newInstance(playlistDaoProvider.get());
  }

  public static PlaylistRepository_Factory create(Provider<PlaylistDao> playlistDaoProvider) {
    return new PlaylistRepository_Factory(playlistDaoProvider);
  }

  public static PlaylistRepository newInstance(PlaylistDao playlistDao) {
    return new PlaylistRepository(playlistDao);
  }
}
