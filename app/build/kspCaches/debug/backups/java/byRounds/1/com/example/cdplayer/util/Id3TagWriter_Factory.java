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
public final class Id3TagWriter_Factory implements Factory<Id3TagWriter> {
  private final Provider<Context> contextProvider;

  public Id3TagWriter_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public Id3TagWriter get() {
    return newInstance(contextProvider.get());
  }

  public static Id3TagWriter_Factory create(Provider<Context> contextProvider) {
    return new Id3TagWriter_Factory(contextProvider);
  }

  public static Id3TagWriter newInstance(Context context) {
    return new Id3TagWriter(context);
  }
}
