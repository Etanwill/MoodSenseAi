package com.moodsense.ai.data.repository;

import com.moodsense.ai.data.local.dao.MoodJournalDao;
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
public final class MoodJournalRepository_Factory implements Factory<MoodJournalRepository> {
  private final Provider<MoodJournalDao> moodJournalDaoProvider;

  public MoodJournalRepository_Factory(Provider<MoodJournalDao> moodJournalDaoProvider) {
    this.moodJournalDaoProvider = moodJournalDaoProvider;
  }

  @Override
  public MoodJournalRepository get() {
    return newInstance(moodJournalDaoProvider.get());
  }

  public static MoodJournalRepository_Factory create(
      Provider<MoodJournalDao> moodJournalDaoProvider) {
    return new MoodJournalRepository_Factory(moodJournalDaoProvider);
  }

  public static MoodJournalRepository newInstance(MoodJournalDao moodJournalDao) {
    return new MoodJournalRepository(moodJournalDao);
  }
}
