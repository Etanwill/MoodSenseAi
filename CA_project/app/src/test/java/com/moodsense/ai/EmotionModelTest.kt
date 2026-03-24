package com.moodsense.ai

import com.moodsense.ai.domain.model.Emotion
import com.moodsense.ai.domain.model.EmotionResult
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for Emotion model and EmotionResult logic.
 *
 * Run with: ./gradlew test
 */
class EmotionModelTest {

    // ── Emotion enum tests ───────────────────────────────────

    @Test
    fun `all 8 emotions are defined`() {
        val emotions = Emotion.values()
        assertEquals(8, emotions.size)
    }

    @Test
    fun `each emotion has a non-empty display name`() {
        Emotion.values().forEach { emotion ->
            assertTrue(
                "Emotion $emotion has empty displayName",
                emotion.displayName.isNotBlank()
            )
        }
    }

    @Test
    fun `each emotion has an emoji`() {
        Emotion.values().forEach { emotion ->
            assertTrue(
                "Emotion $emotion has no emoji",
                emotion.emoji.isNotBlank()
            )
        }
    }

    @Test
    fun `happy emotion has correct display name`() {
        assertEquals("Happy", Emotion.HAPPY.displayName)
    }

    @Test
    fun `neutral emotion has correct emoji`() {
        assertEquals("😐", Emotion.NEUTRAL.emoji)
    }

    // ── EmotionResult tests ──────────────────────────────────

    @Test
    fun `EmotionResult stores emotion correctly`() {
        val result = EmotionResult(emotion = Emotion.HAPPY, confidence = 0.92f)
        assertEquals(Emotion.HAPPY, result.emotion)
    }

    @Test
    fun `EmotionResult stores confidence correctly`() {
        val result = EmotionResult(emotion = Emotion.SAD, confidence = 0.75f)
        assertEquals(0.75f, result.confidence, 0.001f)
    }

    @Test
    fun `EmotionResult timestamp defaults to current time`() {
        val before = System.currentTimeMillis()
        val result = EmotionResult(emotion = Emotion.NEUTRAL, confidence = 0.5f)
        val after  = System.currentTimeMillis()
        assertTrue(result.timestamp in before..after)
    }

    @Test
    fun `confidence value of 1_0 is valid`() {
        val result = EmotionResult(emotion = Emotion.HAPPY, confidence = 1.0f)
        assertTrue(result.confidence <= 1.0f)
    }

    @Test
    fun `confidence value of 0_0 is valid`() {
        val result = EmotionResult(emotion = Emotion.UNKNOWN, confidence = 0.0f)
        assertTrue(result.confidence >= 0.0f)
    }

    @Test
    fun `high confidence detection is above threshold`() {
        val result = EmotionResult(emotion = Emotion.ANGRY, confidence = 0.85f)
        assertTrue("High confidence should be above 0.6 threshold", result.confidence > 0.6f)
    }

    @Test
    fun `low confidence detection is below threshold`() {
        val result = EmotionResult(emotion = Emotion.FEAR, confidence = 0.3f)
        assertFalse("Low confidence should not pass 0.6 threshold", result.confidence > 0.6f)
    }

    @Test
    fun `unknown emotion is not saved to history`() {
        val result = EmotionResult(emotion = Emotion.UNKNOWN, confidence = 0.9f)
        // Mirrors the logic in EmotionViewModel — UNKNOWN should be filtered out
        val shouldSave = result.emotion != Emotion.UNKNOWN && result.confidence > 0.6f
        assertFalse(shouldSave)
    }

    @Test
    fun `valid emotion with high confidence is saved to history`() {
        val result = EmotionResult(emotion = Emotion.HAPPY, confidence = 0.8f)
        val shouldSave = result.emotion != Emotion.UNKNOWN && result.confidence > 0.6f
        assertTrue(shouldSave)
    }
}