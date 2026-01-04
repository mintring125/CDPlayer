package com.example.cdplayer;

import com.example.cdplayer.player.MusicPlayerManager;
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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<MusicPlayerManager> musicPlayerManagerProvider;

  public MainActivity_MembersInjector(Provider<MusicPlayerManager> musicPlayerManagerProvider) {
    this.musicPlayerManagerProvider = musicPlayerManagerProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<MusicPlayerManager> musicPlayerManagerProvider) {
    return new MainActivity_MembersInjector(musicPlayerManagerProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectMusicPlayerManager(instance, musicPlayerManagerProvider.get());
  }

  @InjectedFieldSignature("com.example.cdplayer.MainActivity.musicPlayerManager")
  public static void injectMusicPlayerManager(MainActivity instance,
      MusicPlayerManager musicPlayerManager) {
    instance.musicPlayerManager = musicPlayerManager;
  }
}
