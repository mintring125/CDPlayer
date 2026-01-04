package com.example.cdplayer.ui.screens.home;

import com.example.cdplayer.data.repository.AudioRepository;
import com.example.cdplayer.player.MusicPlayerManager;
import com.example.cdplayer.util.MediaScanner;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<AudioRepository> audioRepositoryProvider;

  private final Provider<MediaScanner> mediaScannerProvider;

  private final Provider<MusicPlayerManager> musicPlayerManagerProvider;

  public HomeViewModel_Factory(Provider<AudioRepository> audioRepositoryProvider,
      Provider<MediaScanner> mediaScannerProvider,
      Provider<MusicPlayerManager> musicPlayerManagerProvider) {
    this.audioRepositoryProvider = audioRepositoryProvider;
    this.mediaScannerProvider = mediaScannerProvider;
    this.musicPlayerManagerProvider = musicPlayerManagerProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(audioRepositoryProvider.get(), mediaScannerProvider.get(), musicPlayerManagerProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<AudioRepository> audioRepositoryProvider,
      Provider<MediaScanner> mediaScannerProvider,
      Provider<MusicPlayerManager> musicPlayerManagerProvider) {
    return new HomeViewModel_Factory(audioRepositoryProvider, mediaScannerProvider, musicPlayerManagerProvider);
  }

  public static HomeViewModel newInstance(AudioRepository audioRepository,
      MediaScanner mediaScanner, MusicPlayerManager musicPlayerManager) {
    return new HomeViewModel(audioRepository, mediaScanner, musicPlayerManager);
  }
}
