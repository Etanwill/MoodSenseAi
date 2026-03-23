package com.moodsense.ai.viewmodel;

import com.moodsense.ai.ml.SongRecognizer;
import com.moodsense.ai.ml.SoundClassifier;
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
public final class SoundViewModel_Factory implements Factory<SoundViewModel> {
  private final Provider<SoundClassifier> soundClassifierProvider;

  private final Provider<SongRecognizer> songRecognizerProvider;

  public SoundViewModel_Factory(Provider<SoundClassifier> soundClassifierProvider,
      Provider<SongRecognizer> songRecognizerProvider) {
    this.soundClassifierProvider = soundClassifierProvider;
    this.songRecognizerProvider = songRecognizerProvider;
  }

  @Override
  public SoundViewModel get() {
    return newInstance(soundClassifierProvider.get(), songRecognizerProvider.get());
  }

  public static SoundViewModel_Factory create(Provider<SoundClassifier> soundClassifierProvider,
      Provider<SongRecognizer> songRecognizerProvider) {
    return new SoundViewModel_Factory(soundClassifierProvider, songRecognizerProvider);
  }

  public static SoundViewModel newInstance(SoundClassifier soundClassifier,
      SongRecognizer songRecognizer) {
    return new SoundViewModel(soundClassifier, songRecognizer);
  }
}
