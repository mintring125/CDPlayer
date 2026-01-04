package com.example.cdplayer.di;

import com.example.cdplayer.data.local.AppDatabase;
import com.example.cdplayer.data.local.dao.AudioFileDao;
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
public final class AppModule_ProvideAudioFileDaoFactory implements Factory<AudioFileDao> {
  private final Provider<AppDatabase> databaseProvider;

  public AppModule_ProvideAudioFileDaoFactory(Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public AudioFileDao get() {
    return provideAudioFileDao(databaseProvider.get());
  }

  public static AppModule_ProvideAudioFileDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new AppModule_ProvideAudioFileDaoFactory(databaseProvider);
  }

  public static AudioFileDao provideAudioFileDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideAudioFileDao(database));
  }
}
