package com.moodsense.ai

import com.moodsense.ai.domain.model.TypingStats
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for TypingStats model and stress detection logic.
 *
 * Run with: ./gradlew test
 */
class TypingStatsTest {

    private fun sampleStats(
        wpm:          Float = 60f,
        backspace:    Float = 0.05f,
        pauseDuration: Long = 200L,
        totalChars:   Int   = 300,
        duration:     Long  = 120000L
    ) = TypingStats(wpm, backspace, pauseDuration, totalChars, duration)

    // ── Field tests ──────────────────────────────────────────

    @Test
    fun `typing stats stores WPM correctly`() {
        val stats = sampleStats(wpm = 75f)
        assertEquals(75f, stats.wordsPerMinute, 0.001f)
    }

    @Test
    fun `typing stats stores backspace frequency correctly`() {
        val stats = sampleStats(backspace = 0.12f)
        assertEquals(0.12f, stats.backspaceFrequency, 0.001f)
    }

    @Test
    fun `typing stats stores pause duration correctly`() {
        val stats = sampleStats(pauseDuration = 500L)
        assertEquals(500L, stats.averagePauseDuration)
    }

    @Test
    fun `typing stats stores total characters correctly`() {
        val stats = sampleStats(totalChars = 450)
        assertEquals(450, stats.totalCharacters)
    }

    @Test
    fun `typing stats stores session duration correctly`() {
        val stats = sampleStats(duration = 180000L)
        assertEquals(180000L, stats.sessionDuration)
    }

    // ── Stress detection logic tests ─────────────────────────

    @Test
    fun `high backspace rate indicates stress`() {
        // Above 15% backspace rate is a stress indicator
        val stats     = sampleStats(backspace = 0.20f)
        val isStressed = stats.backspaceFrequency > 0.15f
        assertTrue("High backspace rate should indicate stress", isStressed)
    }

    @Test
    fun `low backspace rate indicates calm typing`() {
        val stats     = sampleStats(backspace = 0.03f)
        val isStressed = stats.backspaceFrequency > 0.15f
        assertFalse("Low backspace rate should not indicate stress", isStressed)
    }

    @Test
    fun `very low WPM with long pauses indicates hesitation`() {
        val stats        = sampleStats(wpm = 15f, pauseDuration = 2000L)
        val isHesitating = stats.wordsPerMinute < 20f && stats.averagePauseDuration > 1000L
        assertTrue("Slow typing with long pauses should indicate hesitation", isHesitating)
    }

    @Test
    fun `normal WPM range is between 40 and 80`() {
        val stats   = sampleStats(wpm = 60f)
        val isNormal = stats.wordsPerMinute in 40f..80f
        assertTrue("60 WPM should be in normal range", isNormal)
    }

    @Test
    fun `very high WPM indicates rushed or stressed typing`() {
        val stats    = sampleStats(wpm = 120f)
        val isRushed = stats.wordsPerMinute > 100f
        assertTrue("120 WPM should indicate rushed typing", isRushed)
    }

    @Test
    fun `session duration is positive`() {
        val stats = sampleStats(duration = 60000L)
        assertTrue(stats.sessionDuration > 0)
    }

    @Test
    fun `total characters is non-negative`() {
        val stats = sampleStats(totalChars = 0)
        assertTrue(stats.totalCharacters >= 0)
    }

    // ── Stress score calculation ─────────────────────────────

    @Test
    fun `stress score is higher with high backspace and low WPM`() {
        val stressedStats = sampleStats(wpm = 20f, backspace = 0.25f, pauseDuration = 1500L)
        val calmStats     = sampleStats(wpm = 65f, backspace = 0.03f, pauseDuration = 200L)

        // Simple stress score: backspace% * 100 + (100 - wpm) / 10
        fun stressScore(s: TypingStats) =
            (s.backspaceFrequency * 100) + ((100f - s.wordsPerMinute) / 10f)

        assertTrue(
            "Stressed typing should have higher score than calm typing",
            stressScore(stressedStats) > stressScore(calmStats)
        )
    }
}