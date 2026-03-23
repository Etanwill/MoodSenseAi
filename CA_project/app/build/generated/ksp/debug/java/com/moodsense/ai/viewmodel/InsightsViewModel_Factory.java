package com.moodsense.ai.viewmodel;

import com.moodsense.ai.data.repository.EmotionHistoryRepository;
import com.moodsense.ai.data.repository.MoodJournalRepository;
import com.moodsense.ai.data.repository.TypingStatsRepository;
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
public final class InsightsViewModel_Factory implements Factory<InsightsViewModel> {
  private final Provider<EmotionHistoryRepository> emotionHistoryRepositoryProvider;

  private final Provider<TypingStatsRepository> typingStatsRepositoryProvider;

  private final Provider<MoodJournalRepository> moodJournalRepositoryProvider;

  public InsightsViewModel_Factory(
      Provider<EmotionHistoryRepository> emotionHistoryRepositoryProvider,
      Provider<TypingStatsRepository> typingStatsRepositoryProvider,
      Provider<MoodJournalRepository> moodJournalRepositoryProvider) {
    this.emotionHistoryRepositoryProvider = emotionHistoryRepositoryProvider;
    this.typingStatsRepositoryProvider = typingStatsRepositoryProvider;
    this.moodJournalRepositoryProvider = moodJournalRepositoryProvider;
  }

  @Override
  public InsightsViewModel get() {
    return newInstance(emotionHistoryRepositoryProvider.get(), typingStatsRepositoryProvider.get(), moodJournalRepositoryProvider.get());
  }

  public static InsightsViewModel_Factory create(
      Provider<EmotionHistoryRepository> emotionHistoryRepositoryProvider,
      Provider<TypingStatsRepository> typingStatsRepositoryProvider,
      Provider<MoodJournalRepository> moodJournalRepositoryProvider) {
    return new InsightsViewModel_Factory(emotionHistoryRepositoryProvider, typingStatsRepositoryProvider, moodJournalRepositoryProvider);
  }

  public static InsightsViewModel newInstance(EmotionHistoryRepository emotionHistoryRepository,
      TypingStatsRepository typingStatsRepository, MoodJournalRepository moodJournalRepository) {
    return new InsightsViewModel(emotionHistoryRepository, typingStatsRepository, moodJournalRepository);
  }
}
