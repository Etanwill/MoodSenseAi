package com.moodsense.ai.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents a detected emotion from facial analysis
 */
enum class Emotion(val displayName: String, val emoji: String) {
    HAPPY("Happy", "😊"),
    SAD("Sad", "😢"),
    ANGRY("Angry", "😠"),
    FEAR("Fear", "😨"),
    SURPRISE("Surprise", "😲"),
    DISGUST("Disgust", "🤢"),
    NEUTRAL("Neutral", "😐"),
    UNKNOWN("Unknown", "❓")
}

/**
 * Result from facial emotion detection
 */
data class EmotionResult(
    val emotion: Emotion,
    val confidence: Float,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Result from sound classification
 */
data class SoundResult(
    val category: String,
    val confidence: Float,
    val intensity: Float,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Typing statistics tracked during keyboard usage
 */
data class TypingStats(
    val wordsPerMinute: Float,
    val backspaceFrequency: Float,
    val averagePauseDuration: Long,
    val totalCharacters: Int,
    val sessionDuration: Long
)

/**
 * Mood tag options for journal entries
 */
enum class MoodTag(val label: String) {
    HAPPY("Happy"),
    SAD("Sad"),
    ANXIOUS("Anxious"),
    EXCITED("Excited"),
    TIRED("Tired"),
    CALM("Calm"),
    STRESSED("Stressed"),
    GRATEFUL("Grateful"),
    ANGRY("Angry"),
    CONFUSED("Confused"),
    FOCUSED("Focused"),
    LONELY("Lonely")
}

/**
 * Insight pattern detected from user data
 */
data class MoodInsight(
    val title: String,
    val description: String,
    val emoji: String,
    val type: InsightType
)

enum class InsightType {
    EMOTION_PATTERN,
    TYPING_PATTERN,
    TIME_PATTERN,
    MOOD_TREND
}

/**
 * Music track from iTunes Search API.
 * Free to use, no API key required, returns album art + 30s preview.
 */
@Parcelize
data class MusicTrack(
    val id: String,
    val name: String,
    val artistName: String,
    val albumName: String,
    val albumArtUrl: String?,
    val trackUrl: String,       // Direct iTunes/Apple Music link
    val previewUrl: String?     // 30-second preview MP3 URL
) : Parcelable