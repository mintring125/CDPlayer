package com.example.cdplayer;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.example.cdplayer.data.local.AppDatabase;
import com.example.cdplayer.data.local.dao.AudioFileDao;
import com.example.cdplayer.data.local.dao.BookmarkDao;
import com.example.cdplayer.data.local.dao.PlaylistDao;
import com.example.cdplayer.data.repository.AudioRepository;
import com.example.cdplayer.data.repository.BookmarkRepository;
import com.example.cdplayer.data.repository.PlaylistRepository;
import com.example.cdplayer.di.AppModule;
import com.example.cdplayer.di.AppModule_ProvideAppDatabaseFactory;
import com.example.cdplayer.di.AppModule_ProvideAudioFileDaoFactory;
import com.example.cdplayer.di.AppModule_ProvideBookmarkDaoFactory;
import com.example.cdplayer.di.AppModule_ProvideId3TagReaderFactory;
import com.example.cdplayer.di.AppModule_ProvideId3TagWriterFactory;
import com.example.cdplayer.di.AppModule_ProvidePlaylistDaoFactory;
import com.example.cdplayer.player.MusicPlayerManager;
import com.example.cdplayer.player.PlaybackService;
import com.example.cdplayer.player.PlaybackService_MembersInjector;
import com.example.cdplayer.ui.screens.edit.EditMetadataViewModel;
import com.example.cdplayer.ui.screens.edit.EditMetadataViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.cdplayer.ui.screens.home.HomeViewModel;
import com.example.cdplayer.ui.screens.home.HomeViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.cdplayer.ui.screens.library.DetailViewModel;
import com.example.cdplayer.ui.screens.library.DetailViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.cdplayer.ui.screens.library.LibraryViewModel;
import com.example.cdplayer.ui.screens.library.LibraryViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.cdplayer.ui.screens.player.PlayerViewModel;
import com.example.cdplayer.ui.screens.player.PlayerViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.cdplayer.ui.screens.playlist.CreatePlaylistViewModel;
import com.example.cdplayer.ui.screens.playlist.CreatePlaylistViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.cdplayer.ui.screens.playlist.PlaylistViewModel;
import com.example.cdplayer.ui.screens.playlist.PlaylistViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.cdplayer.ui.screens.search.SearchViewModel;
import com.example.cdplayer.ui.screens.search.SearchViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.cdplayer.ui.screens.settings.SettingsViewModel;
import com.example.cdplayer.ui.screens.settings.SettingsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.example.cdplayer.util.Id3TagReader;
import com.example.cdplayer.util.Id3TagWriter;
import com.example.cdplayer.util.MediaScanner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.flags.HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class DaggerCDPlayerApp_HiltComponents_SingletonC {
  private DaggerCDPlayerApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder appModule(AppModule appModule) {
      Preconditions.checkNotNull(appModule);
      return this;
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    /**
     * @deprecated This module is declared, but an instance is not used in the component. This method is a no-op. For more, see https://dagger.dev/unused-modules.
     */
    @Deprecated
    public Builder hiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule(
        HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule hiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule) {
      Preconditions.checkNotNull(hiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule);
      return this;
    }

    public CDPlayerApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements CDPlayerApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public CDPlayerApp_HiltComponents.ActivityRetainedC build() {
      return new ActivityRetainedCImpl(singletonCImpl);
    }
  }

  private static final class ActivityCBuilder implements CDPlayerApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public CDPlayerApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements CDPlayerApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public CDPlayerApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements CDPlayerApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public CDPlayerApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements CDPlayerApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public CDPlayerApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements CDPlayerApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public CDPlayerApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements CDPlayerApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public CDPlayerApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends CDPlayerApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends CDPlayerApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends CDPlayerApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends CDPlayerApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Set<String> getViewModelKeys() {
      return ImmutableSet.<String>of(CreatePlaylistViewModel_HiltModules_KeyModule_ProvideFactory.provide(), DetailViewModel_HiltModules_KeyModule_ProvideFactory.provide(), EditMetadataViewModel_HiltModules_KeyModule_ProvideFactory.provide(), HomeViewModel_HiltModules_KeyModule_ProvideFactory.provide(), LibraryViewModel_HiltModules_KeyModule_ProvideFactory.provide(), PlayerViewModel_HiltModules_KeyModule_ProvideFactory.provide(), PlaylistViewModel_HiltModules_KeyModule_ProvideFactory.provide(), SearchViewModel_HiltModules_KeyModule_ProvideFactory.provide(), SettingsViewModel_HiltModules_KeyModule_ProvideFactory.provide());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectMusicPlayerManager(instance, singletonCImpl.musicPlayerManagerProvider.get());
      return instance;
    }
  }

  private static final class ViewModelCImpl extends CDPlayerApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<CreatePlaylistViewModel> createPlaylistViewModelProvider;

    private Provider<DetailViewModel> detailViewModelProvider;

    private Provider<EditMetadataViewModel> editMetadataViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<LibraryViewModel> libraryViewModelProvider;

    private Provider<PlayerViewModel> playerViewModelProvider;

    private Provider<PlaylistViewModel> playlistViewModelProvider;

    private Provider<SearchViewModel> searchViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.createPlaylistViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.detailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.editMetadataViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.libraryViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.playerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.playlistViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.searchViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
    }

    @Override
    public Map<String, Provider<ViewModel>> getHiltViewModelMap() {
      return ImmutableMap.<String, Provider<ViewModel>>builderWithExpectedSize(9).put("com.example.cdplayer.ui.screens.playlist.CreatePlaylistViewModel", ((Provider) createPlaylistViewModelProvider)).put("com.example.cdplayer.ui.screens.library.DetailViewModel", ((Provider) detailViewModelProvider)).put("com.example.cdplayer.ui.screens.edit.EditMetadataViewModel", ((Provider) editMetadataViewModelProvider)).put("com.example.cdplayer.ui.screens.home.HomeViewModel", ((Provider) homeViewModelProvider)).put("com.example.cdplayer.ui.screens.library.LibraryViewModel", ((Provider) libraryViewModelProvider)).put("com.example.cdplayer.ui.screens.player.PlayerViewModel", ((Provider) playerViewModelProvider)).put("com.example.cdplayer.ui.screens.playlist.PlaylistViewModel", ((Provider) playlistViewModelProvider)).put("com.example.cdplayer.ui.screens.search.SearchViewModel", ((Provider) searchViewModelProvider)).put("com.example.cdplayer.ui.screens.settings.SettingsViewModel", ((Provider) settingsViewModelProvider)).build();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.example.cdplayer.ui.screens.playlist.CreatePlaylistViewModel 
          return (T) new CreatePlaylistViewModel(singletonCImpl.playlistRepositoryProvider.get());

          case 1: // com.example.cdplayer.ui.screens.library.DetailViewModel 
          return (T) new DetailViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.audioRepositoryProvider.get(), singletonCImpl.musicPlayerManagerProvider.get(), singletonCImpl.provideId3TagWriterProvider.get());

          case 2: // com.example.cdplayer.ui.screens.edit.EditMetadataViewModel 
          return (T) new EditMetadataViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.audioRepositoryProvider.get(), singletonCImpl.provideId3TagWriterProvider.get(), ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.example.cdplayer.ui.screens.home.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.audioRepositoryProvider.get(), singletonCImpl.mediaScannerProvider.get(), singletonCImpl.musicPlayerManagerProvider.get());

          case 4: // com.example.cdplayer.ui.screens.library.LibraryViewModel 
          return (T) new LibraryViewModel(singletonCImpl.audioRepositoryProvider.get(), singletonCImpl.playlistRepositoryProvider.get(), singletonCImpl.musicPlayerManagerProvider.get());

          case 5: // com.example.cdplayer.ui.screens.player.PlayerViewModel 
          return (T) new PlayerViewModel(singletonCImpl.musicPlayerManagerProvider.get(), singletonCImpl.bookmarkRepositoryProvider.get());

          case 6: // com.example.cdplayer.ui.screens.playlist.PlaylistViewModel 
          return (T) new PlaylistViewModel(singletonCImpl.playlistRepositoryProvider.get(), singletonCImpl.musicPlayerManagerProvider.get());

          case 7: // com.example.cdplayer.ui.screens.search.SearchViewModel 
          return (T) new SearchViewModel(singletonCImpl.audioRepositoryProvider.get(), singletonCImpl.musicPlayerManagerProvider.get());

          case 8: // com.example.cdplayer.ui.screens.settings.SettingsViewModel 
          return (T) new SettingsViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends CDPlayerApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;

      initialize();

    }

    @SuppressWarnings("unchecked")
    private void initialize() {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends CDPlayerApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }

    @Override
    public void injectPlaybackService(PlaybackService playbackService) {
      injectPlaybackService2(playbackService);
    }

    private PlaybackService injectPlaybackService2(PlaybackService instance) {
      PlaybackService_MembersInjector.injectMusicPlayerManager(instance, singletonCImpl.musicPlayerManagerProvider.get());
      return instance;
    }
  }

  private static final class SingletonCImpl extends CDPlayerApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<AppDatabase> provideAppDatabaseProvider;

    private Provider<AudioFileDao> provideAudioFileDaoProvider;

    private Provider<AudioRepository> audioRepositoryProvider;

    private Provider<MusicPlayerManager> musicPlayerManagerProvider;

    private Provider<PlaylistDao> providePlaylistDaoProvider;

    private Provider<PlaylistRepository> playlistRepositoryProvider;

    private Provider<Id3TagWriter> provideId3TagWriterProvider;

    private Provider<Id3TagReader> provideId3TagReaderProvider;

    private Provider<MediaScanner> mediaScannerProvider;

    private Provider<BookmarkDao> provideBookmarkDaoProvider;

    private Provider<BookmarkRepository> bookmarkRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideAppDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<AppDatabase>(singletonCImpl, 3));
      this.provideAudioFileDaoProvider = DoubleCheck.provider(new SwitchingProvider<AudioFileDao>(singletonCImpl, 2));
      this.audioRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<AudioRepository>(singletonCImpl, 1));
      this.musicPlayerManagerProvider = DoubleCheck.provider(new SwitchingProvider<MusicPlayerManager>(singletonCImpl, 0));
      this.providePlaylistDaoProvider = DoubleCheck.provider(new SwitchingProvider<PlaylistDao>(singletonCImpl, 5));
      this.playlistRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<PlaylistRepository>(singletonCImpl, 4));
      this.provideId3TagWriterProvider = DoubleCheck.provider(new SwitchingProvider<Id3TagWriter>(singletonCImpl, 6));
      this.provideId3TagReaderProvider = DoubleCheck.provider(new SwitchingProvider<Id3TagReader>(singletonCImpl, 8));
      this.mediaScannerProvider = DoubleCheck.provider(new SwitchingProvider<MediaScanner>(singletonCImpl, 7));
      this.provideBookmarkDaoProvider = DoubleCheck.provider(new SwitchingProvider<BookmarkDao>(singletonCImpl, 10));
      this.bookmarkRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<BookmarkRepository>(singletonCImpl, 9));
    }

    @Override
    public void injectCDPlayerApp(CDPlayerApp cDPlayerApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return ImmutableSet.<Boolean>of();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.example.cdplayer.player.MusicPlayerManager 
          return (T) new MusicPlayerManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.audioRepositoryProvider.get());

          case 1: // com.example.cdplayer.data.repository.AudioRepository 
          return (T) new AudioRepository(singletonCImpl.provideAudioFileDaoProvider.get());

          case 2: // com.example.cdplayer.data.local.dao.AudioFileDao 
          return (T) AppModule_ProvideAudioFileDaoFactory.provideAudioFileDao(singletonCImpl.provideAppDatabaseProvider.get());

          case 3: // com.example.cdplayer.data.local.AppDatabase 
          return (T) AppModule_ProvideAppDatabaseFactory.provideAppDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 4: // com.example.cdplayer.data.repository.PlaylistRepository 
          return (T) new PlaylistRepository(singletonCImpl.providePlaylistDaoProvider.get());

          case 5: // com.example.cdplayer.data.local.dao.PlaylistDao 
          return (T) AppModule_ProvidePlaylistDaoFactory.providePlaylistDao(singletonCImpl.provideAppDatabaseProvider.get());

          case 6: // com.example.cdplayer.util.Id3TagWriter 
          return (T) AppModule_ProvideId3TagWriterFactory.provideId3TagWriter(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 7: // com.example.cdplayer.util.MediaScanner 
          return (T) new MediaScanner(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.provideId3TagReaderProvider.get(), singletonCImpl.audioRepositoryProvider.get());

          case 8: // com.example.cdplayer.util.Id3TagReader 
          return (T) AppModule_ProvideId3TagReaderFactory.provideId3TagReader(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 9: // com.example.cdplayer.data.repository.BookmarkRepository 
          return (T) new BookmarkRepository(singletonCImpl.provideBookmarkDaoProvider.get());

          case 10: // com.example.cdplayer.data.local.dao.BookmarkDao 
          return (T) AppModule_ProvideBookmarkDaoFactory.provideBookmarkDao(singletonCImpl.provideAppDatabaseProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
