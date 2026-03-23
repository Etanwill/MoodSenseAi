package com.moodsense.ai.di;

import com.moodsense.ai.data.local.MoodSenseDatabase;
import com.moodsense.ai.data.local.dao.EmotionHistoryDao;
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
public final class AppModule_ProvideEmotionHistoryDaoFactory implements Factory<EmotionHistoryDao> {
  private final Provider<MoodSenseDatabase> dbProvider;

  public AppModule_ProvideEmotionHistoryDaoFactory(Provider<MoodSenseDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public EmotionHistoryDao get() {
    return provideEmotionHistoryDao(dbProvider.get());
  }

  public static AppModule_ProvideEmotionHistoryDaoFactory create(
      Provider<MoodSenseDatabase> dbProvider) {
    return new AppModule_ProvideEmotionHistoryDaoFactory(dbProvider);
  }

  public static EmotionHistoryDao provideEmotionHistoryDao(MoodSenseDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideEmotionHistoryDao(db));
  }
}
