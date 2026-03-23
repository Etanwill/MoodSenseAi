package com.moodsense.ai.di;

import android.content.Context;
import com.moodsense.ai.ml.SongRecognizer;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AppModule_ProvideSongRecognizerFactory implements Factory<SongRecognizer> {
  private final Provider<Context> contextProvider;

  public AppModule_ProvideSongRecognizerFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SongRecognizer get() {
    return provideSongRecognizer(contextProvider.get());
  }

  public static AppModule_ProvideSongRecognizerFactory create(Provider<Context> contextProvider) {
    return new AppModule_ProvideSongRecognizerFactory(contextProvider);
  }

  public static SongRecognizer provideSongRecognizer(Context context) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideSongRecognizer(context));
  }
}
