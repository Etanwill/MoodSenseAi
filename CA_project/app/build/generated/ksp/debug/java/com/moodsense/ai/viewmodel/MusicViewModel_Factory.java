package com.moodsense.ai.viewmodel;

import com.moodsense.ai.data.repository.MusicRepository;
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
public final class MusicViewModel_Factory implements Factory<MusicViewModel> {
  private final Provider<MusicRepository> musicRepositoryProvider;

  public MusicViewModel_Factory(Provider<MusicRepository> musicRepositoryProvider) {
    this.musicRepositoryProvider = musicRepositoryProvider;
  }

  @Override
  public MusicViewModel get() {
    return newInstance(musicRepositoryProvider.get());
  }

  public static MusicViewModel_Factory create(Provider<MusicRepository> musicRepositoryProvider) {
    return new MusicViewModel_Factory(musicRepositoryProvider);
  }

  public static MusicViewModel newInstance(MusicRepository musicRepository) {
    return new MusicViewModel(musicRepository);
  }
}
