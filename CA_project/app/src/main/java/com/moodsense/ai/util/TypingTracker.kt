package com.moodsense.ai.util

import com.moodsense.ai.domain.model.TypingStats

/**
 * Tracks keyboard dynamics for behavioral analysis.
 * Measures typing speed, backspace frequency, and pause patterns.
 */
class TypingTracker {
    private val keyPressTimestamps = mutableListOf<Long>()
    private var backspaceCount = 0
    private var totalCharCount = 0
    private var sessionStartTime = 0L
    private var lastKeyPressTime = 0L
    private val pauseDurations = mutableListOf<Long>()

    companion object {
        private const val PAUSE_THRESHOLD_MS = 1500L // 1.5 seconds = significant pause
        private const val MIN_SESSION_MS = 5000L // Minimum 5 seconds for valid stats
    }

    /**
     * Call this when the user starts typing
     */
    fun startSession() {
        sessionStartTime = System.currentTimeMillis()
        keyPressTimestamps.clear()
        backspaceCount = 0
        totalCharCount = 0
        lastKeyPressTime = 0L
        pauseDurations.clear()
    }

    /**
     * Record a key press event
     * @param isBackspace true if the key was a backspace/delete
     */
    fun onKeyPress(isBackspace: Boolean) {
        val now = System.currentTimeMillis()

        if (sessionStartTime == 0L) startSession()

        // Track pause duration
        if (lastKeyPressTime > 0) {
            val pauseDuration = now - lastKeyPressTime
            if (pauseDuration > PAUSE_THRESHOLD_MS) {
                pauseDurations.add(pauseDuration)
            }
        }

        lastKeyPressTime = now
        keyPressTimestamps.add(now)

        if (isBackspace) {
            backspaceCount++
        } else {
            totalCharCount++
        }
    }

    /**
     * Get current typing statistics
     */
    fun getStats(): TypingStats? {
        val sessionDuration = System.currentTimeMillis() - sessionStartTime
        if (sessionDuration < MIN_SESSION_MS || totalCharCount < 5) return null

        val sessionMinutes = sessionDuration / 60000f
        val wordsPerMinute = if (sessionMinutes > 0) {
            // Average word length ~5 chars
            (totalCharCount / 5f) / sessionMinutes
        } else 0f

        val backspaceFrequency = if (totalCharCount > 0) {
            backspaceCount.toFloat() / totalCharCount.toFloat()
        } else 0f

        val avgPauseDuration = if (pauseDurations.isNotEmpty()) {
            pauseDurations.average().toLong()
        } else 0L

        return TypingStats(
            wordsPerMinute = wordsPerMinute.coerceIn(0f, 200f),
            backspaceFrequency = backspaceFrequency.coerceIn(0f, 1f),
            averagePauseDuration = avgPauseDuration,
            totalCharacters = totalCharCount,
            sessionDuration = sessionDuration
        )
    }

    /**
     * Get typing speed category for UI display
     */
    fun getSpeedCategory(): SpeedCategory {
        val wpm = getStats()?.wordsPerMinute ?: return SpeedCategory.UNKNOWN
        return when {
            wpm < 20f -> SpeedCategory.SLOW
            wpm < 40f -> SpeedCategory.MODERATE
            wpm < 60f -> SpeedCategory.AVERAGE
            wpm < 80f -> SpeedCategory.FAST
            else -> SpeedCategory.VERY_FAST
        }
    }

    enum class SpeedCategory(val label: String) {
        UNKNOWN("Unknown"),
        SLOW("Slow"),
        MODERATE("Moderate"),
        AVERAGE("Average"),
        FAST("Fast"),
        VERY_FAST("Very Fast")
    }
}
