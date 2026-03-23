package com.moodsense.ai.di

import android.content.Context
import androidx.room.Room
import com.moodsense.ai.data.local.MoodSenseDatabase
import com.moodsense.ai.data.local.dao.*
import com.moodsense.ai.data.repository.MusicRepository
import com.moodsense.ai.data.repository.GroqApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // ============================================================
    // Database
    // ============================================================

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MoodSenseDatabase {
        return Room.databaseBuilder(
            context,
            MoodSenseDatabase::class.java,
            MoodSenseDatabase.DATABASE_NAME
        ).build()
    }

    @Provides fun provideMoodJournalDao(db: MoodSenseDatabase): MoodJournalDao     = db.moodJournalDao()
    @Provides fun provideTypingStatsDao(db: MoodSenseDatabase): TypingStatsDao     = db.typingStatsDao()
    @Provides fun provideEmotionHistoryDao(db: MoodSenseDatabase): EmotionHistoryDao = db.emotionHistoryDao()
    @Provides fun provideSoundHistoryDao(db: MoodSenseDatabase): SoundHistoryDao   = db.soundHistoryDao()

    // ============================================================
    // Groq AI API
    // ============================================================

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideGroqApi(okHttpClient: OkHttpClient): GroqApi {
        return Retrofit.Builder()
            .baseUrl("https://api.groq.com/openai/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GroqApi::class.java)
    }

    // ============================================================
    // Music — iTunes Search API (free, no key needed)
    // ============================================================

    @Provides
    @Singleton
    fun provideMusicRepository(): MusicRepository = MusicRepository()

    // ============================================================
    // Song Recognition — ACRCloud (Shazam-like)
    // ============================================================

    @Provides
    @Singleton
    fun provideSongRecognizer(@dagger.hilt.android.qualifiers.ApplicationContext context: android.content.Context): com.moodsense.ai.ml.SongRecognizer =
        com.moodsense.ai.ml.SongRecognizer(context)
}
