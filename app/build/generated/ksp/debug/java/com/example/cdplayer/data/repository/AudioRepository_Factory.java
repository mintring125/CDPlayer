package com.example.cdplayer.data.repository;

import com.example.cdplayer.data.local.dao.AudioFileDao;
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
public final class AudioRepository_Factory implements Factory<AudioRepository> {
  private final Provider<AudioFileDao> audioFileDaoProvider;

  public AudioRepository_Factory(Provider<AudioFileDao> audioFileDaoProvider) {
    this.audioFileDaoProvider = audioFileDaoProvider;
  }

  @Override
  public AudioRepository get() {
    return newInstance(audioFileDaoProvider.get());
  }

  public static AudioRepository_Factory create(Provider<AudioFileDao> audioFileDaoProvider) {
    return new AudioRepository_Factory(audioFileDaoProvider);
  }

  public static AudioRepository newInstance(AudioFileDao audioFileDao) {
    return new AudioRepository(audioFileDao);
  }
}
