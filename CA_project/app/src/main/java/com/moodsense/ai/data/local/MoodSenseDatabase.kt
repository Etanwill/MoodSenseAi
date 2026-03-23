package com.moodsense.ai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.moodsense.ai.data.local.dao.*
import com.moodsense.ai.data.local.entities.*

/**
 * Main Room database for MoodSense AI.
 * Contains all tables for offline-first data storage.
 */
@Database(
    entities = [
        MoodJournalEntity::class,
        TypingStatsEntity::class,
        EmotionHistoryEntity::class,
        SoundHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class MoodSenseDatabase : RoomDatabase() {

    abstract fun moodJournalDao(): MoodJournalDao
    abstract fun typingStatsDao(): TypingStatsDao
    abstract fun emotionHistoryDao(): EmotionHistoryDao
    abstract fun soundHistoryDao(): SoundHistoryDao

    companion object {
        const val DATABASE_NAME = "moodsense_db"
    }
}
