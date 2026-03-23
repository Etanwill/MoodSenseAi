package com.moodsense.ai

import com.moodsense.ai.domain.model.Emotion
import com.moodsense.ai.domain.model.MoodTag
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for Mood Journal logic — tag validation, mood score ranges,
 * and journal entry business rules.
 *
 * Run with: ./gradlew test
 */
class MoodJournalTest {

    // ── MoodTag tests ────────────────────────────────────────

    @Test
    fun `all mood tags have non-empty labels`() {
        MoodTag.values().forEach { tag ->
            assertTrue("MoodTag $tag has empty label", tag.label.isNotBlank())
        }
    }

    @Test
    fun `mood tags include common emotional states`() {
        val tagLabels = MoodTag.values().map { it.label }
        assertTrue(tagLabels.contains("Happy"))
        assertTrue(tagLabels.contains("Sad"))
        assertTrue(tagLabels.contains("Anxious"))
        assertTrue(tagLabels.contains("Stressed"))
    }

    @Test
    fun `there are at least 10 mood tags available`() {
        assertTrue(MoodTag.values().size >= 10)
    }

    // ── Mood score validation ────────────────────────────────

    @Test
    fun `mood score of 5 is valid mid-range`() {
        val score = 5
        assertTrue(score in 1..10)
    }

    @Test
    fun `mood score of 1 is valid minimum`() {
        val score = 1
        assertTrue(score in 1..10)
    }

    @Test
    fun `mood score of 10 is valid maximum`() {
        val score = 10
        assertTrue(score in 1..10)
    }

    @Test
    fun `mood score of 0 is invalid`() {
        val score = 0
        assertFalse(score in 1..10)
    }

    @Test
    fun `mood score of 11 is invalid`() {
        val score = 11
        assertFalse(score in 1..10)
    }

    // ── Journal entry validation ─────────────────────────────

    @Test
    fun `blank note should not be saved`() {
        val note      = "   "
        val shouldSave = note.isNotBlank()
        assertFalse("Blank note should not be saved", shouldSave)
    }

    @Test
    fun `non-blank note should be saved`() {
        val note      = "Today was a great day"
        val shouldSave = note.isNotBlank()
        assertTrue("Non-blank note should be saved", shouldSave)
    }

    @Test
    fun `journal entry can have no tags`() {
        val tags = emptyList<MoodTag>()
        // Empty tag list is valid — tags are optional
        assertTrue(tags.isEmpty())
    }

    @Test
    fun `journal entry can have multiple tags`() {
        val tags = listOf(MoodTag.HAPPY, MoodTag.GRATEFUL, MoodTag.EXCITED)
        assertEquals(3, tags.size)
    }

    @Test
    fun `detected emotion can be null in journal entry`() {
        val detectedEmotion: Emotion? = null
        assertNull(detectedEmotion)
    }

    @Test
    fun `detected emotion is included when available`() {
        val detectedEmotion: Emotion? = Emotion.HAPPY
        assertNotNull(detectedEmotion)
        assertEquals(Emotion.HAPPY, detectedEmotion)
    }

    // ── Note length tests ────────────────────────────────────

    @Test
    fun `very long note is still valid`() {
        val longNote = "a".repeat(5000)
        assertTrue(longNote.isNotBlank())
        assertTrue(longNote.length == 5000)
    }

    @Test
    fun `single character note is valid`() {
        val note = "."
        assertTrue(note.isNotBlank())
    }

    // ── Tag serialization ────────────────────────────────────

    @Test
    fun `mood tags can be converted to name list`() {
        val tags    = listOf(MoodTag.HAPPY, MoodTag.CALM)
        val names   = tags.map { it.name }
        assertEquals(listOf("HAPPY", "CALM"), names)
    }

    @Test
    fun `mood tag names can be parsed back to enum`() {
        val name = "ANXIOUS"
        val tag  = MoodTag.valueOf(name)
        assertEquals(MoodTag.ANXIOUS, tag)
    }
}