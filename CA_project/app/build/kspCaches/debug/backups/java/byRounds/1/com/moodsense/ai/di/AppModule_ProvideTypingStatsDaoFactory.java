package com.moodsense.ai.di;

import com.moodsense.ai.data.local.MoodSenseDatabase;
import com.moodsense.ai.data.local.dao.TypingStatsDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideTypingStatsDaoFactory implements Factory<TypingStatsDao> {
  private final Provider<MoodSenseDatabase> dbProvider;

  public AppModule_ProvideTypingStatsDaoFactory(Provider<MoodSenseDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public TypingStatsDao get() {
    return provideTypingStatsDao(dbProvider.get());
  }

  public static AppModule_ProvideTypingStatsDaoFactory create(
      Provider<MoodSenseDatabase> dbProvider) {
    return new AppModule_ProvideTypingStatsDaoFactory(dbProvider);
  }

  public static TypingStatsDao provideTypingStatsDao(MoodSenseDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideTypingStatsDao(db));
  }
}
