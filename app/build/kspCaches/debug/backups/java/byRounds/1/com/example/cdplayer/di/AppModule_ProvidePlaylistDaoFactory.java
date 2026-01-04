package com.example.cdplayer.di;

import com.example.cdplayer.data.local.AppDatabase;
import com.example.cdplayer.data.local.dao.PlaylistDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvidePlaylistDaoFactory implements Factory<PlaylistDao> {
  private final Provider<AppDatabase> databaseProvider;

  public AppModule_ProvidePlaylistDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public PlaylistDao get() {
    return providePlaylistDao(databaseProvider.get());
  }

  public static AppModule_ProvidePlaylistDaoFactory create(Provider<AppDatabase> databaseProvider) {
    return new AppModule_ProvidePlaylistDaoFactory(databaseProvider);
  }

  public static PlaylistDao providePlaylistDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.providePlaylistDao(database));
  }
}
