package com.example.cdplayer.player;

import android.content.Context;
import com.example.cdplayer.data.repository.AudioRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class MusicPlayerManager_Factory implements Factory<MusicPlayerManager> {
  private final Provider<Context> contextProvider;

  private final Provider<AudioRepository> audioRepositoryProvider;

  public MusicPlayerManager_Factory(Provider<Context> contextProvider,
      Provider<AudioRepository> audioRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.audioRepositoryProvider = audioRepositoryProvider;
  }

  @Override
  public MusicPlayerManager get() {
    return newInstance(contextProvider.get(), audioRepositoryProvider.get());
  }

  public static MusicPlayerManager_Factory create(Provider<Context> contextProvider,
      Provider<AudioRepository> audioRepositoryProvider) {
    return new MusicPlayerManager_Factory(contextProvider, audioRepositoryProvider);
  }

  public static MusicPlayerManager newInstance(Context context, AudioRepository audioRepository) {
    return new MusicPlayerManager(context, audioRepository);
  }
}
