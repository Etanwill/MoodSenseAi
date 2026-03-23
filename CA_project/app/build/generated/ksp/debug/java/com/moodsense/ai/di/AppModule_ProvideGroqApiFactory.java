package com.moodsense.ai.di;

import com.moodsense.ai.data.repository.GroqApi;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

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
public final class AppModule_ProvideGroqApiFactory implements Factory<GroqApi> {
  private final Provider<OkHttpClient> okHttpClientProvider;

  public AppModule_ProvideGroqApiFactory(Provider<OkHttpClient> okHttpClientProvider) {
    this.okHttpClientProvider = okHttpClientProvider;
  }

  @Override
  public GroqApi get() {
    return provideGroqApi(okHttpClientProvider.get());
  }

  public static AppModule_ProvideGroqApiFactory create(
      Provider<OkHttpClient> okHttpClientProvider) {
    return new AppModule_ProvideGroqApiFactory(okHttpClientProvider);
  }

  public static GroqApi provideGroqApi(OkHttpClient okHttpClient) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideGroqApi(okHttpClient));
  }
}
