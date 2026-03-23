package com.moodsense.ai.di;

import com.moodsense.ai.data.local.MoodSenseDatabase;
import com.moodsense.ai.data.local.dao.MoodJournalDao;
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
public final class AppModule_ProvideMoodJournalDaoFactory implements Factory<MoodJournalDao> {
  private final Provider<MoodSenseDatabase> dbProvider;

  public AppModule_ProvideMoodJournalDaoFactory(Provider<MoodSenseDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public MoodJournalDao get() {
    return provideMoodJournalDao(dbProvider.get());
  }

  public static AppModule_ProvideMoodJournalDaoFactory create(
      Provider<MoodSenseDatabase> dbProvider) {
    return new AppModule_ProvideMoodJournalDaoFactory(dbProvider);
  }

  public static MoodJournalDao provideMoodJournalDao(MoodSenseDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideMoodJournalDao(db));
  }
}
