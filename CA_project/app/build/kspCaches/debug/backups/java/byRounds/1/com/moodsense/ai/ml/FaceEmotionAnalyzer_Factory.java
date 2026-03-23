package com.moodsense.ai.ml;

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
public final class FaceEmotionAnalyzer_Factory implements Factory<FaceEmotionAnalyzer> {
  @Override
  public FaceEmotionAnalyzer get() {
    return newInstance();
  }

  public static FaceEmotionAnalyzer_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static FaceEmotionAnalyzer newInstance() {
    return new FaceEmotionAnalyzer();
  }

  private static final class InstanceHolder {
    private static final FaceEmotionAnalyzer_Factory INSTANCE = new FaceEmotionAnalyzer_Factory();
  }
}
