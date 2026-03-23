package com.moodsense.ai.di;

import com.moodsense.ai.data.local.MoodSenseDatabase;
import com.moodsense.ai.data.local.dao.SoundHistoryDao;
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
public final class AppModule_ProvideSoundHistoryDaoFactory implements Factory<SoundHistoryDao> {
  private final Provider<MoodSenseDatabase> dbProvider;

  public AppModule_ProvideSoundHistoryDaoFactory(Provider<MoodSenseDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public SoundHistoryDao get() {
    return provideSoundHistoryDao(dbProvider.get());
  }

  public static AppModule_ProvideSoundHistoryDaoFactory create(
      Provider<MoodSenseDatabase> dbProvider) {
    return new AppModule_ProvideSoundHistoryDaoFactory(dbProvider);
  }

  public static SoundHistoryDao provideSoundHistoryDao(MoodSenseDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideSoundHistoryDao(db));
  }
}
