package com.moodsense.ai.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Room entity for mood journal entries
 */
@Entity(tableName = "mood_journal")
data class MoodJournalEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val note: String,
    val tags: String, // JSON array of MoodTag names
    val detectedEmotion: String?,
    val timestamp: Long = System.currentTimeMillis(),
    val mood: Int = 5 // 1-10 scale
)

/**
 * Room entity for typing statistics
 */
@Entity(tableName = "typing_stats")
data class TypingStatsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val wordsPerMinute: Float,
    val backspaceFrequency: Float,
    val averagePauseDuration: Long,
    val totalCharacters: Int,
    val sessionDuration: Long,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Room entity for emotion history
 */
@Entity(tableName = "emotion_history")
data class EmotionHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val emotion: String,
    val confidence: Float,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Room entity for sound classification history
 */
@Entity(tableName = "sound_history")
data class SoundHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val confidence: Float,
    val intensity: Float,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Type converters for Room to handle complex types
 */
class StringListConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }
}
