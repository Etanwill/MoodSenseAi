package com.moodsense.ai.di;

import com.moodsense.ai.data.repository.MusicRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class AppModule_ProvideMusicRepositoryFactory implements Factory<MusicRepository> {
  @Override
  public MusicRepository get() {
    return provideMusicRepository();
  }

  public static AppModule_ProvideMusicRepositoryFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MusicRepository provideMusicRepository() {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideMusicRepository());
  }

  private static final class InstanceHolder {
    private static final AppModule_ProvideMusicRepositoryFactory INSTANCE = new AppModule_ProvideMusicRepositoryFactory();
  }
}
