package com.example.cdplayer.util;

import android.content.Context;
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
public final class Id3TagReader_Factory implements Factory<Id3TagReader> {
  private final Provider<Context> contextProvider;

  public Id3TagReader_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public Id3TagReader get() {
    return newInstance(contextProvider.get());
  }

  public static Id3TagReader_Factory create(Provider<Context> contextProvider) {
    return new Id3TagReader_Factory(contextProvider);
  }

  public static Id3TagReader newInstance(Context context) {
    return new Id3TagReader(context);
  }
}
