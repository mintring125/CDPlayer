package com.example.cdplayer.player;

import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class PlaybackService_MembersInjector implements MembersInjector<PlaybackService> {
  private final Provider<MusicPlayerManager> musicPlayerManagerProvider;

  public PlaybackService_MembersInjector(Provider<MusicPlayerManager> musicPlayerManagerProvider) {
    this.musicPlayerManagerProvider = musicPlayerManagerProvider;
  }

  public static MembersInjector<PlaybackService> create(
      Provider<MusicPlayerManager> musicPlayerManagerProvider) {
    return new PlaybackService_MembersInjector(musicPlayerManagerProvider);
  }

  @Override
  public void injectMembers(PlaybackService instance) {
    injectMusicPlayerManager(instance, musicPlayerManagerProvider.get());
  }

  @InjectedFieldSignature("com.example.cdplayer.player.PlaybackService.musicPlayerManager")
  public static void injectMusicPlayerManager(PlaybackService instance,
      MusicPlayerManager musicPlayerManager) {
    instance.musicPlayerManager = musicPlayerManager;
  }
}
