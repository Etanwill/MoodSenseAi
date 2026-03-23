package com.moodsense.ai;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.moodsense.ai.data.local.MoodSenseDatabase;
import com.moodsense.ai.data.local.dao.EmotionHistoryDao;
import com.moodsense.ai.data.local.dao.MoodJournalDao;
import com.moodsense.ai.data.local.dao.TypingStatsDao;
import com.moodsense.ai.data.repository.EmotionHistoryRepository;
import com.moodsense.ai.data.repository.GroqApi;
import com.moodsense.ai.data.repository.MoodJournalRepository;
import com.moodsense.ai.data.repository.MusicRepository;
import com.moodsense.ai.data.repository.TypingStatsRepository;
import com.moodsense.ai.di.AppModule;
import com.moodsense.ai.di.AppModule_ProvideDatabaseFactory;
import com.moodsense.ai.di.AppModule_ProvideEmotionHistoryDaoFactory;
import com.moodsense.ai.di.AppModule_ProvideGroqApiFactory;
import com.moodsense.ai.di.AppModule_ProvideMoodJournalDaoFactory;
import com.moodsense.ai.di.AppModule_ProvideMusicRepositoryFactory;
import com.moodsense.ai.di.AppModule_ProvideOkHttpClientFactory;
import com.moodsense.ai.di.AppModule_ProvideSongRecognizerFactory;
import com.moodsense.ai.di.AppModule_ProvideTypingStatsDaoFactory;
import com.moodsense.ai.ml.FaceEmotionAnalyzer;
import com.moodsense.ai.ml.SongRecognizer;
import com.moodsense.ai.ml.SoundClassifier;
import com.moodsense.ai.viewmodel.EmotionViewModel;
import com.moodsense.ai.viewmodel.EmotionViewModel_HiltModules_KeyModule_ProvideFactory;
import com.moodsense.ai.viewmodel.HomeViewModel;
import com.moodsense.ai.viewmodel.HomeViewModel_HiltModules_KeyModule_ProvideFactory;
import com.moodsense.ai.viewmodel.InsightsViewModel;
import com.moodsense.ai.viewmodel.InsightsViewModel_HiltModules_KeyModule_ProvideFactory;
import com.moodsense.ai.viewmodel.JournalViewModel;
import com.moodsense.ai.viewmodel.JournalViewModel_HiltModules_KeyModule_ProvideFactory;
import com.moodsense.ai.viewmodel.MusicViewModel;
import com.moodsense.ai.viewmodel.MusicViewModel_HiltModules_KeyModule_ProvideFactory;
import com.moodsense.ai.viewmodel.OnboardingViewModel;
import com.moodsense.ai.viewmodel.OnboardingViewModel_HiltModules_KeyModule_ProvideFactory;
import com.moodsense.ai.viewmodel.SoundViewModel;
import com.moodsense.ai.viewmodel.SoundViewModel_HiltModules_KeyModule_ProvideFactory;
import com.moodsense.ai.viewmodel.TherapistViewModel;
import com.moodsense.ai.viewmodel.TherapistViewModel_HiltModules_KeyModule_ProvideFactory;
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
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.SetBuilder;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

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
public final class DaggerMoodSenseApp_HiltComponents_SingletonC {
  private DaggerMoodSenseApp_HiltComponents_SingletonC() {
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

    public MoodSenseApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements MoodSenseApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public MoodSenseApp_HiltComponents.ActivityRetainedC build() {
      return new ActivityRetainedCImpl(singletonCImpl);
    }
  }

  private static final class ActivityCBuilder implements MoodSenseApp_HiltComponents.ActivityC.Builder {
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
    public MoodSenseApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements MoodSenseApp_HiltComponents.FragmentC.Builder {
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
    public MoodSenseApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements MoodSenseApp_HiltComponents.ViewWithFragmentC.Builder {
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
    public MoodSenseApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements MoodSenseApp_HiltComponents.ViewC.Builder {
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
    public MoodSenseApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements MoodSenseApp_HiltComponents.ViewModelC.Builder {
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
    public MoodSenseApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements MoodSenseApp_HiltComponents.ServiceC.Builder {
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
    public MoodSenseApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends MoodSenseApp_HiltComponents.ViewWithFragmentC {
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

  private static final class FragmentCImpl extends MoodSenseApp_HiltComponents.FragmentC {
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

  private static final class ViewCImpl extends MoodSenseApp_HiltComponents.ViewC {
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

  private static final class ActivityCImpl extends MoodSenseApp_HiltComponents.ActivityC {
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
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Set<String> getViewModelKeys() {
      return SetBuilder.<String>newSetBuilder(8).add(EmotionViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(HomeViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(InsightsViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(JournalViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(MusicViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(OnboardingViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(SoundViewModel_HiltModules_KeyModule_ProvideFactory.provide()).add(TherapistViewModel_HiltModules_KeyModule_ProvideFactory.provide()).build();
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
  }

  private static final class ViewModelCImpl extends MoodSenseApp_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<EmotionViewModel> emotionViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<InsightsViewModel> insightsViewModelProvider;

    private Provider<JournalViewModel> journalViewModelProvider;

    private Provider<MusicViewModel> musicViewModelProvider;

    private Provider<OnboardingViewModel> onboardingViewModelProvider;

    private Provider<SoundViewModel> soundViewModelProvider;

    private Provider<TherapistViewModel> therapistViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.emotionViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.insightsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.journalViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.musicViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.onboardingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.soundViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.therapistViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
    }

    @Override
    public Map<String, Provider<ViewModel>> getHiltViewModelMap() {
      return MapBuilder.<String, Provider<ViewModel>>newMapBuilder(8).put("com.moodsense.ai.viewmodel.EmotionViewModel", ((Provider) emotionViewModelProvider)).put("com.moodsense.ai.viewmodel.HomeViewModel", ((Provider) homeViewModelProvider)).put("com.moodsense.ai.viewmodel.InsightsViewModel", ((Provider) insightsViewModelProvider)).put("com.moodsense.ai.viewmodel.JournalViewModel", ((Provider) journalViewModelProvider)).put("com.moodsense.ai.viewmodel.MusicViewModel", ((Provider) musicViewModelProvider)).put("com.moodsense.ai.viewmodel.OnboardingViewModel", ((Provider) onboardingViewModelProvider)).put("com.moodsense.ai.viewmodel.SoundViewModel", ((Provider) soundViewModelProvider)).put("com.moodsense.ai.viewmodel.TherapistViewModel", ((Provider) therapistViewModelProvider)).build();
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
          case 0: // com.moodsense.ai.viewmodel.EmotionViewModel 
          return (T) new EmotionViewModel(singletonCImpl.faceEmotionAnalyzerProvider.get(), singletonCImpl.emotionHistoryRepositoryProvider.get());

          case 1: // com.moodsense.ai.viewmodel.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.soundClassifierProvider.get(), singletonCImpl.emotionHistoryRepositoryProvider.get(), singletonCImpl.moodJournalRepositoryProvider.get());

          case 2: // com.moodsense.ai.viewmodel.InsightsViewModel 
          return (T) new InsightsViewModel(singletonCImpl.emotionHistoryRepositoryProvider.get(), singletonCImpl.typingStatsRepositoryProvider.get(), singletonCImpl.moodJournalRepositoryProvider.get());

          case 3: // com.moodsense.ai.viewmodel.JournalViewModel 
          return (T) new JournalViewModel(singletonCImpl.moodJournalRepositoryProvider.get(), singletonCImpl.typingStatsRepositoryProvider.get());

          case 4: // com.moodsense.ai.viewmodel.MusicViewModel 
          return (T) new MusicViewModel(singletonCImpl.provideMusicRepositoryProvider.get());

          case 5: // com.moodsense.ai.viewmodel.OnboardingViewModel 
          return (T) new OnboardingViewModel();

          case 6: // com.moodsense.ai.viewmodel.SoundViewModel 
          return (T) new SoundViewModel(singletonCImpl.soundClassifierProvider.get(), singletonCImpl.provideSongRecognizerProvider.get());

          case 7: // com.moodsense.ai.viewmodel.TherapistViewModel 
          return (T) new TherapistViewModel(singletonCImpl.emotionHistoryRepositoryProvider.get(), singletonCImpl.moodJournalRepositoryProvider.get(), singletonCImpl.typingStatsRepositoryProvider.get(), singletonCImpl.provideGroqApiProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends MoodSenseApp_HiltComponents.ActivityRetainedC {
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

  private static final class ServiceCImpl extends MoodSenseApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends MoodSenseApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<FaceEmotionAnalyzer> faceEmotionAnalyzerProvider;

    private Provider<MoodSenseDatabase> provideDatabaseProvider;

    private Provider<EmotionHistoryRepository> emotionHistoryRepositoryProvider;

    private Provider<SoundClassifier> soundClassifierProvider;

    private Provider<MoodJournalRepository> moodJournalRepositoryProvider;

    private Provider<TypingStatsRepository> typingStatsRepositoryProvider;

    private Provider<MusicRepository> provideMusicRepositoryProvider;

    private Provider<SongRecognizer> provideSongRecognizerProvider;

    private Provider<OkHttpClient> provideOkHttpClientProvider;

    private Provider<GroqApi> provideGroqApiProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private EmotionHistoryDao emotionHistoryDao() {
      return AppModule_ProvideEmotionHistoryDaoFactory.provideEmotionHistoryDao(provideDatabaseProvider.get());
    }

    private MoodJournalDao moodJournalDao() {
      return AppModule_ProvideMoodJournalDaoFactory.provideMoodJournalDao(provideDatabaseProvider.get());
    }

    private TypingStatsDao typingStatsDao() {
      return AppModule_ProvideTypingStatsDaoFactory.provideTypingStatsDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.faceEmotionAnalyzerProvider = DoubleCheck.provider(new SwitchingProvider<FaceEmotionAnalyzer>(singletonCImpl, 0));
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<MoodSenseDatabase>(singletonCImpl, 2));
      this.emotionHistoryRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<EmotionHistoryRepository>(singletonCImpl, 1));
      this.soundClassifierProvider = DoubleCheck.provider(new SwitchingProvider<SoundClassifier>(singletonCImpl, 3));
      this.moodJournalRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<MoodJournalRepository>(singletonCImpl, 4));
      this.typingStatsRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<TypingStatsRepository>(singletonCImpl, 5));
      this.provideMusicRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<MusicRepository>(singletonCImpl, 6));
      this.provideSongRecognizerProvider = DoubleCheck.provider(new SwitchingProvider<SongRecognizer>(singletonCImpl, 7));
      this.provideOkHttpClientProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 9));
      this.provideGroqApiProvider = DoubleCheck.provider(new SwitchingProvider<GroqApi>(singletonCImpl, 8));
    }

    @Override
    public void injectMoodSenseApp(MoodSenseApp moodSenseApp) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
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
          case 0: // com.moodsense.ai.ml.FaceEmotionAnalyzer 
          return (T) new FaceEmotionAnalyzer();

          case 1: // com.moodsense.ai.data.repository.EmotionHistoryRepository 
          return (T) new EmotionHistoryRepository(singletonCImpl.emotionHistoryDao());

          case 2: // com.moodsense.ai.data.local.MoodSenseDatabase 
          return (T) AppModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.moodsense.ai.ml.SoundClassifier 
          return (T) new SoundClassifier(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 4: // com.moodsense.ai.data.repository.MoodJournalRepository 
          return (T) new MoodJournalRepository(singletonCImpl.moodJournalDao());

          case 5: // com.moodsense.ai.data.repository.TypingStatsRepository 
          return (T) new TypingStatsRepository(singletonCImpl.typingStatsDao());

          case 6: // com.moodsense.ai.data.repository.MusicRepository 
          return (T) AppModule_ProvideMusicRepositoryFactory.provideMusicRepository();

          case 7: // com.moodsense.ai.ml.SongRecognizer 
          return (T) AppModule_ProvideSongRecognizerFactory.provideSongRecognizer(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 8: // com.moodsense.ai.data.repository.GroqApi 
          return (T) AppModule_ProvideGroqApiFactory.provideGroqApi(singletonCImpl.provideOkHttpClientProvider.get());

          case 9: // okhttp3.OkHttpClient 
          return (T) AppModule_ProvideOkHttpClientFactory.provideOkHttpClient();

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
