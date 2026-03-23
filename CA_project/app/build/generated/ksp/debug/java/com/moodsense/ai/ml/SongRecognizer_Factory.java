package com.moodsense.ai.ml;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class SongRecognizer_Factory implements Factory<SongRecognizer> {
  private final Provider<Context> contextProvider;

  public SongRecognizer_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SongRecognizer get() {
    return newInstance(contextProvider.get());
  }

  public static SongRecognizer_Factory create(Provider<Context> contextProvider) {
    return new SongRecognizer_Factory(contextProvider);
  }

  public static SongRecognizer newInstance(Context context) {
    return new SongRecognizer(context);
  }
}
