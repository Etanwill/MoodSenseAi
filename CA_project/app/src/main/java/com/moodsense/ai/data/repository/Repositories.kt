package com.moodsense.ai.data.repository

import com.google.gson.Gson
import com.moodsense.ai.data.local.dao.*
import com.moodsense.ai.data.local.entities.*
import com.moodsense.ai.domain.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for mood journal data.
 */
@Singleton
class MoodJournalRepository @Inject constructor(
    private val moodJournalDao: MoodJournalDao
) {
    fun getAllEntries(): Flow<List<MoodJournalEntity>> = moodJournalDao.getAllEntries()

    suspend fun saveEntry(
        note: String,
        tags: List<MoodTag>,
        detectedEmotion: Emotion?,
        mood: Int
    ): Long {
        val tagsJson = Gson().toJson(tags.map { it.name })
        val entity = MoodJournalEntity(
            note = note,
            tags = tagsJson,
            detectedEmotion = detectedEmotion?.name,
            mood = mood
        )
        return moodJournalDao.insertEntry(entity)
    }

    suspend fun deleteEntry(entry: MoodJournalEntity) = moodJournalDao.deleteEntry(entry)
    suspend fun getLatestEntry() = moodJournalDao.getLatestEntry()
}

/**
 * Repository for typing statistics
 */
@Singleton
class TypingStatsRepository @Inject constructor(
    private val typingStatsDao: TypingStatsDao
) {
    fun getRecentStats(): Flow<List<TypingStatsEntity>> = typingStatsDao.getRecentStats()
    fun getAllStats():    Flow<List<TypingStatsEntity>> = typingStatsDao.getAllStats()

    suspend fun saveStats(stats: TypingStats): Long {
        val entity = TypingStatsEntity(
            wordsPerMinute       = stats.wordsPerMinute,
            backspaceFrequency   = stats.backspaceFrequency,
            averagePauseDuration = stats.averagePauseDuration,
            totalCharacters      = stats.totalCharacters,
            sessionDuration      = stats.sessionDuration
        )
        return typingStatsDao.insertStats(entity)
    }
}

/**
 * Repository for emotion detection history
 */
@Singleton
class EmotionHistoryRepository @Inject constructor(
    private val emotionHistoryDao: EmotionHistoryDao
) {
    fun getRecentEmotions(): Flow<List<EmotionHistoryEntity>> =
        emotionHistoryDao.getRecentEmotions()

    suspend fun saveEmotion(result: EmotionResult): Long {
        val entity = EmotionHistoryEntity(
            emotion    = result.emotion.name,
            confidence = result.confidence,
            timestamp  = result.timestamp
        )
        return emotionHistoryDao.insertEmotion(entity)
    }

    suspend fun getEmotionFrequency(startTime: Long) =
        emotionHistoryDao.getEmotionFrequency(startTime)

    suspend fun getEmotionsInRange(startTime: Long, endTime: Long) =
        emotionHistoryDao.getEmotionsInRange(startTime, endTime)
}

/**
 * Repository for music recommendations using the iTunes Search API.
 */
@Singleton
class MusicRepository @Inject constructor() {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    companion object {
        private const val TAG = "MusicRepository"
    }

    /**
     * Search iTunes for tracks matching the given emotion.
     */
    suspend fun searchTracksByMood(emotion: Emotion): Result<List<MusicTrack>> =
        withContext(Dispatchers.IO) {
            try {
                val term = getMoodSearchTerm(emotion)

                val url = HttpUrl.Builder()
                    .scheme("https")
                    .host("itunes.apple.com")
                    .addPathSegment("search")
                    .addQueryParameter("term",      term)
                    .addQueryParameter("media",     "music")
                    .addQueryParameter("entity",    "song")
                    .addQueryParameter("limit",     "100") // increased limit to 100 for more variety
                    .addQueryParameter("explicit",  "No")
                    .build()

                val request = Request.Builder()
                    .url(url)
                    .get()
                    .header("Accept", "application/json")
                    .build()

                val response = client.newCall(request).execute()
                val body = response.body?.string() ?: ""

                if (!response.isSuccessful) {
                    return@withContext Result.failure(
                        Exception("iTunes error: HTTP ${response.code}")
                    )
                }

                val parsed = gson.fromJson(body, ItunesSearchResponse::class.java)
                val tracks = parsed.results
                    .filter { it.kind == "song" && it.trackName != null }
                    .shuffled() // Shuffle to ensure variety on every click
                    .map { dto ->
                        MusicTrack(
                            id          = dto.trackId.toString(),
                            name        = dto.trackName    ?: "Unknown",
                            artistName  = dto.artistName   ?: "Unknown Artist",
                            albumName   = dto.collectionName ?: "",
                            albumArtUrl = dto.artworkUrl100?.replace("100x100", "400x400"), 
                            trackUrl    = dto.trackViewUrl ?: "",
                            previewUrl  = dto.previewUrl
                        )
                    }

                Result.success(tracks)

            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * Refined search terms to favor trending/popular tracks.
     */
    private fun getMoodSearchTerm(emotion: Emotion): String = when (emotion) {
        Emotion.HAPPY    -> "top hits 2024 upbeat pop"
        Emotion.SAD      -> "popular emotional ballads"
        Emotion.ANGRY    -> "trending hard rock metal"
        Emotion.FEAR     -> "popular relaxing acoustic"
        Emotion.SURPRISE -> "top charts dance hits"
        Emotion.DISGUST  -> "popular chill lo-fi"
        Emotion.NEUTRAL  -> "today's top mid-tempo"
        Emotion.UNKNOWN  -> "global top 100"
    }
}
