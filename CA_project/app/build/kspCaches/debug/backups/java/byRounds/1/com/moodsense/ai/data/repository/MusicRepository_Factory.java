package com.moodsense.ai.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class MusicRepository_Factory implements Factory<MusicRepository> {
  @Override
  public MusicRepository get() {
    return newInstance();
  }

  public static MusicRepository_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MusicRepository newInstance() {
    return new MusicRepository();
  }

  private static final class InstanceHolder {
    private static final MusicRepository_Factory INSTANCE = new MusicRepository_Factory();
  }
}
