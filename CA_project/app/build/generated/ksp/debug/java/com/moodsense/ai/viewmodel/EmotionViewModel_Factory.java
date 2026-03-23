package com.moodsense.ai.viewmodel;

import com.moodsense.ai.data.repository.EmotionHistoryRepository;
import com.moodsense.ai.ml.FaceEmotionAnalyzer;
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
public final class EmotionViewModel_Factory implements Factory<EmotionViewModel> {
  private final Provider<FaceEmotionAnalyzer> faceEmotionAnalyzerProvider;

  private final Provider<EmotionHistoryRepository> emotionHistoryRepositoryProvider;

  public EmotionViewModel_Factory(Provider<FaceEmotionAnalyzer> faceEmotionAnalyzerProvider,
      Provider<EmotionHistoryRepository> emotionHistoryRepositoryProvider) {
    this.faceEmotionAnalyzerProvider = faceEmotionAnalyzerProvider;
    this.emotionHistoryRepositoryProvider = emotionHistoryRepositoryProvider;
  }

  @Override
  public EmotionViewModel get() {
    return newInstance(faceEmotionAnalyzerProvider.get(), emotionHistoryRepositoryProvider.get());
  }

  public static EmotionViewModel_Factory create(
      Provider<FaceEmotionAnalyzer> faceEmotionAnalyzerProvider,
      Provider<EmotionHistoryRepository> emotionHistoryRepositoryProvider) {
    return new EmotionViewModel_Factory(faceEmotionAnalyzerProvider, emotionHistoryRepositoryProvider);
  }

  public static EmotionViewModel newInstance(FaceEmotionAnalyzer faceEmotionAnalyzer,
      EmotionHistoryRepository emotionHistoryRepository) {
    return new EmotionViewModel(faceEmotionAnalyzer, emotionHistoryRepository);
  }
}
