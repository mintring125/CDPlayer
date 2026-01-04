package com.example.cdplayer.ui.screens.search;

import com.example.cdplayer.data.repository.AudioRepository;
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
public final class SearchViewModel_Factory implements Factory<SearchViewModel> {
  private final Provider<AudioRepository> audioRepositoryProvider;

  private final Provider<MusicPlayerManager> musicPlayerManagerProvider;

  public SearchViewModel_Factory(Provider<AudioRepository> audioRepositoryProvider,
      Provider<MusicPlayerManager> musicPlayerManagerProvider) {
    this.audioRepositoryProvider = audioRepositoryProvider;
    this.musicPlayerManagerProvider = musicPlayerManagerProvider;
  }

  @Override
  public SearchViewModel get() {
    return newInstance(audioRepositoryProvider.get(), musicPlayerManagerProvider.get());
  }

  public static SearchViewModel_Factory create(Provider<AudioRepository> audioRepositoryProvider,
      Provider<MusicPlayerManager> musicPlayerManagerProvider) {
    return new SearchViewModel_Factory(audioRepositoryProvider, musicPlayerManagerProvider);
  }

  public static SearchViewModel newInstance(AudioRepository audioRepository,
      MusicPlayerManager musicPlayerManager) {
    return new SearchViewModel(audioRepository, musicPlayerManager);
  }
}
