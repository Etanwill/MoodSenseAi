package com.moodsense.ai.data.repository;

import com.moodsense.ai.data.local.dao.EmotionHistoryDao;
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
public final class EmotionHistoryRepository_Factory implements Factory<EmotionHistoryRepository> {
  private final Provider<EmotionHistoryDao> emotionHistoryDaoProvider;

  public EmotionHistoryRepository_Factory(Provider<EmotionHistoryDao> emotionHistoryDaoProvider) {
    this.emotionHistoryDaoProvider = emotionHistoryDaoProvider;
  }

  @Override
  public EmotionHistoryRepository get() {
    return newInstance(emotionHistoryDaoProvider.get());
  }

  public static EmotionHistoryRepository_Factory create(
      Provider<EmotionHistoryDao> emotionHistoryDaoProvider) {
    return new EmotionHistoryRepository_Factory(emotionHistoryDaoProvider);
  }

  public static EmotionHistoryRepository newInstance(EmotionHistoryDao emotionHistoryDao) {
    return new EmotionHistoryRepository(emotionHistoryDao);
  }
}
