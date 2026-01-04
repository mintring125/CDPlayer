package com.example.cdplayer.ui.screens.edit;

import android.content.Context;
import androidx.lifecycle.SavedStateHandle;
import com.example.cdplayer.data.repository.AudioRepository;
import com.example.cdplayer.util.Id3TagWriter;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class EditMetadataViewModel_Factory implements Factory<EditMetadataViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<AudioRepository> audioRepositoryProvider;

  private final Provider<Id3TagWriter> id3TagWriterProvider;

  private final Provider<Context> contextProvider;

  public EditMetadataViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<AudioRepository> audioRepositoryProvider,
      Provider<Id3TagWriter> id3TagWriterProvider, Provider<Context> contextProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.audioRepositoryProvider = audioRepositoryProvider;
    this.id3TagWriterProvider = id3TagWriterProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public EditMetadataViewModel get() {
    return newInstance(savedStateHandleProvider.get(), audioRepositoryProvider.get(), id3TagWriterProvider.get(), contextProvider.get());
  }

  public static EditMetadataViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<AudioRepository> audioRepositoryProvider,
      Provider<Id3TagWriter> id3TagWriterProvider, Provider<Context> contextProvider) {
    return new EditMetadataViewModel_Factory(savedStateHandleProvider, audioRepositoryProvider, id3TagWriterProvider, contextProvider);
  }

  public static EditMetadataViewModel newInstance(SavedStateHandle savedStateHandle,
      AudioRepository audioRepository, Id3TagWriter id3TagWriter, Context context) {
    return new EditMetadataViewModel(savedStateHandle, audioRepository, id3TagWriter, context);
  }
}
