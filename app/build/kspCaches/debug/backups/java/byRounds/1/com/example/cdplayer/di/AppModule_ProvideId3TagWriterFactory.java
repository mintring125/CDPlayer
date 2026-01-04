package com.example.cdplayer.di;

import android.content.Context;
import com.example.cdplayer.util.Id3TagWriter;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideId3TagWriterFactory implements Factory<Id3TagWriter> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideId3TagWriterFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public Id3TagWriter get() {
    return provideId3TagWriter(contextProvider.get());
  }

  public static AppModule_ProvideId3TagWriterFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideId3TagWriterFactory(contextProvider);
  }

  public static Id3TagWriter provideId3TagWriter(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideId3TagWriter(context));
  }
}
