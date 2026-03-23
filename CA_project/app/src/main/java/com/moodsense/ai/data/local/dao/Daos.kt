package com.moodsense.ai.data.local.dao

import androidx.room.*
import com.moodsense.ai.data.local.entities.*
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for mood journal entries
 */
@Dao
interface MoodJournalDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: MoodJournalEntity): Long

    @Update
    suspend fun updateEntry(entry: MoodJournalEntity)

    @Delete
    suspend fun deleteEntry(entry: MoodJournalEntity)

    @Query("SELECT * FROM mood_journal ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<MoodJournalEntity>>

    @Query("SELECT * FROM mood_journal WHERE timestamp >= :startTime AND timestamp <= :endTime ORDER BY timestamp DESC")
    fun getEntriesInRange(startTime: Long, endTime: Long): Flow<List<MoodJournalEntity>>

    @Query("SELECT * FROM mood_journal ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestEntry(): MoodJournalEntity?

    @Query("SELECT COUNT(*) FROM mood_journal")
    suspend fun getEntryCount(): Int
}

/**
 * Data Access Object for typing statistics
 */
@Dao
interface TypingStatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStats(stats: TypingStatsEntity): Long

    @Query("SELECT * FROM typing_stats ORDER BY timestamp DESC LIMIT 50")
    fun getRecentStats(): Flow<List<TypingStatsEntity>>

    @Query("SELECT AVG(wordsPerMinute) FROM typing_stats WHERE timestamp >= :startTime")
    suspend fun getAverageWpm(startTime: Long): Float?

    @Query("SELECT AVG(backspaceFrequency) FROM typing_stats WHERE timestamp >= :startTime")
    suspend fun getAverageBackspaceFrequency(startTime: Long): Float?

    @Query("SELECT * FROM typing_stats ORDER BY timestamp DESC")
    fun getAllStats(): Flow<List<TypingStatsEntity>>
}

/**
 * Data Access Object for emotion history
 */
@Dao
interface EmotionHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmotion(emotion: EmotionHistoryEntity): Long

    @Query("SELECT * FROM emotion_history ORDER BY timestamp DESC LIMIT 100")
    fun getRecentEmotions(): Flow<List<EmotionHistoryEntity>>

    @Query("SELECT emotion, COUNT(*) as count FROM emotion_history WHERE timestamp >= :startTime GROUP BY emotion ORDER BY count DESC")
    suspend fun getEmotionFrequency(startTime: Long): List<EmotionFrequency>

    @Query("SELECT * FROM emotion_history WHERE timestamp >= :startTime AND timestamp <= :endTime ORDER BY timestamp ASC")
    suspend fun getEmotionsInRange(startTime: Long, endTime: Long): List<EmotionHistoryEntity>

    @Query("DELETE FROM emotion_history WHERE timestamp < :beforeTime")
    suspend fun deleteOldEntries(beforeTime: Long)
}

/**
 * Helper data class for emotion frequency queries
 */
data class EmotionFrequency(
    val emotion: String,
    val count: Int
)

/**
 * Data Access Object for sound history
 */
@Dao
interface SoundHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSound(sound: SoundHistoryEntity): Long

    @Query("SELECT * FROM sound_history ORDER BY timestamp DESC LIMIT 50")
    fun getRecentSounds(): Flow<List<SoundHistoryEntity>>

    @Query("SELECT * FROM sound_history ORDER BY timestamp DESC")
    fun getAllSounds(): Flow<List<SoundHistoryEntity>>
}
