package com.moodsense.ai.data.repository;

import com.moodsense.ai.data.local.dao.TypingStatsDao;
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
public final class TypingStatsRepository_Factory implements Factory<TypingStatsRepository> {
  private final Provider<TypingStatsDao> typingStatsDaoProvider;

  public TypingStatsRepository_Factory(Provider<TypingStatsDao> typingStatsDaoProvider) {
    this.typingStatsDaoProvider = typingStatsDaoProvider;
  }

  @Override
  public TypingStatsRepository get() {
    return newInstance(typingStatsDaoProvider.get());
  }

  public static TypingStatsRepository_Factory create(
      Provider<TypingStatsDao> typingStatsDaoProvider) {
    return new TypingStatsRepository_Factory(typingStatsDaoProvider);
  }

  public static TypingStatsRepository newInstance(TypingStatsDao typingStatsDao) {
    return new TypingStatsRepository(typingStatsDao);
  }
}
