package com.moodsense.ai.viewmodel;

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
public final class JournalViewModel_Factory implements Factory<JournalViewModel> {
  private final Provider<MoodJournalRepository> moodJournalRepositoryProvider;

  private final Provider<TypingStatsRepository> typingStatsRepositoryProvider;

  public JournalViewModel_Factory(Provider<MoodJournalRepository> moodJournalRepositoryProvider,
      Provider<TypingStatsRepository> typingStatsRepositoryProvider) {
    this.moodJournalRepositoryProvider = moodJournalRepositoryProvider;
    this.typingStatsRepositoryProvider = typingStatsRepositoryProvider;
  }

  @Override
  public JournalViewModel get() {
    return newInstance(moodJournalRepositoryProvider.get(), typingStatsRepositoryProvider.get());
  }

  public static JournalViewModel_Factory create(
      Provider<MoodJournalRepository> moodJournalRepositoryProvider,
      Provider<TypingStatsRepository> typingStatsRepositoryProvider) {
    return new JournalViewModel_Factory(moodJournalRepositoryProvider, typingStatsRepositoryProvider);
  }

  public static JournalViewModel newInstance(MoodJournalRepository moodJournalRepository,
      TypingStatsRepository typingStatsRepository) {
    return new JournalViewModel(moodJournalRepository, typingStatsRepository);
  }
}
