package com.moodsense.ai.viewmodel;

import com.moodsense.ai.data.repository.EmotionHistoryRepository;
import com.moodsense.ai.data.repository.GroqApi;
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
public final class TherapistViewModel_Factory implements Factory<TherapistViewModel> {
  private final Provider<EmotionHistoryRepository> emotionRepoProvider;

  private final Provider<MoodJournalRepository> journalRepoProvider;

  private final Provider<TypingStatsRepository> typingRepoProvider;

  private final Provider<GroqApi> groqApiProvider;

  public TherapistViewModel_Factory(Provider<EmotionHistoryRepository> emotionRepoProvider,
      Provider<MoodJournalRepository> journalRepoProvider,
      Provider<TypingStatsRepository> typingRepoProvider, Provider<GroqApi> groqApiProvider) {
    this.emotionRepoProvider = emotionRepoProvider;
    this.journalRepoProvider = journalRepoProvider;
    this.typingRepoProvider = typingRepoProvider;
    this.groqApiProvider = groqApiProvider;
  }

  @Override
  public TherapistViewModel get() {
    return newInstance(emotionRepoProvider.get(), journalRepoProvider.get(), typingRepoProvider.get(), groqApiProvider.get());
  }

  public static TherapistViewModel_Factory create(
      Provider<EmotionHistoryRepository> emotionRepoProvider,
      Provider<MoodJournalRepository> journalRepoProvider,
      Provider<TypingStatsRepository> typingRepoProvider, Provider<GroqApi> groqApiProvider) {
    return new TherapistViewModel_Factory(emotionRepoProvider, journalRepoProvider, typingRepoProvider, groqApiProvider);
  }

  public static TherapistViewModel newInstance(EmotionHistoryRepository emotionRepo,
      MoodJournalRepository journalRepo, TypingStatsRepository typingRepo, GroqApi groqApi) {
    return new TherapistViewModel(emotionRepo, journalRepo, typingRepo, groqApi);
  }
}
