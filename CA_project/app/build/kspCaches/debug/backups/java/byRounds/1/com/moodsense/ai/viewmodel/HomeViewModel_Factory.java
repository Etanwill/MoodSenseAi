package com.moodsense.ai.viewmodel;

import com.moodsense.ai.data.repository.EmotionHistoryRepository;
import com.moodsense.ai.data.repository.MoodJournalRepository;
import com.moodsense.ai.ml.SoundClassifier;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<SoundClassifier> soundClassifierProvider;

  private final Provider<EmotionHistoryRepository> emotionHistoryRepositoryProvider;

  private final Provider<MoodJournalRepository> moodJournalRepositoryProvider;

  public HomeViewModel_Factory(Provider<SoundClassifier> soundClassifierProvider,
      Provider<EmotionHistoryRepository> emotionHistoryRepositoryProvider,
      Provider<MoodJournalRepository> moodJournalRepositoryProvider) {
    this.soundClassifierProvider = soundClassifierProvider;
    this.emotionHistoryRepositoryProvider = emotionHistoryRepositoryProvider;
    this.moodJournalRepositoryProvider = moodJournalRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(soundClassifierProvider.get(), emotionHistoryRepositoryProvider.get(), moodJournalRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<SoundClassifier> soundClassifierProvider,
      Provider<EmotionHistoryRepository> emotionHistoryRepositoryProvider,
      Provider<MoodJournalRepository> moodJournalRepositoryProvider) {
    return new HomeViewModel_Factory(soundClassifierProvider, emotionHistoryRepositoryProvider, moodJournalRepositoryProvider);
  }

  public static HomeViewModel newInstance(SoundClassifier soundClassifier,
      EmotionHistoryRepository emotionHistoryRepository,
      MoodJournalRepository moodJournalRepository) {
    return new HomeViewModel(soundClassifier, emotionHistoryRepository, moodJournalRepository);
  }
}
