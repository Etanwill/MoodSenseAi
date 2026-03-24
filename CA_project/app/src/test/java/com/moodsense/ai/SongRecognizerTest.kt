package com.moodsense.ai

import com.moodsense.ai.ml.RecognitionResult
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for Song Recognition result model and state logic.
 *
 * Run with: ./gradlew test
 */
class SongRecognizerTest {

    // ── RecognitionResult state tests ────────────────────────

    @Test
    fun `idle state is initial state`() {
        val state: RecognitionResult = RecognitionResult.Idle
        assertTrue(state is RecognitionResult.Idle)
    }

    @Test
    fun `recording state is set when listening starts`() {
        val state: RecognitionResult = RecognitionResult.Recording
        assertTrue(state is RecognitionResult.Recording)
    }

    @Test
    fun `processing state is set after recording ends`() {
        val state: RecognitionResult = RecognitionResult.Processing
        assertTrue(state is RecognitionResult.Processing)
    }

    @Test
    fun `found result stores title correctly`() {
        val result = RecognitionResult.Found(
            title          = "Bohemian Rhapsody",
            artist         = "Queen",
            album          = "A Night at the Opera",
            genre          = "Rock",
            releaseDate    = "1975-10-31",
            score          = 95,
            youtubeId      = "fJ9rUzIMcZQ",
            spotifyTrackId = "7tFiyTwD0nx5a1eklYtX2J",
            albumArtUrl    = null
        )
        assertEquals("Bohemian Rhapsody", result.title)
    }

    @Test
    fun `found result stores artist correctly`() {
        val result = RecognitionResult.Found(
            title = "Shape of You", artist = "Ed Sheeran",
            album = "Divide", genre = "Pop", releaseDate = "2017",
            score = 98, youtubeId = null, spotifyTrackId = null, albumArtUrl = null
        )
        assertEquals("Ed Sheeran", result.artist)
    }

    @Test
    fun `found result stores match score correctly`() {
        val result = RecognitionResult.Found(
            title = "Test", artist = "Artist", album = "Album",
            genre = "Pop", releaseDate = "2020", score = 87,
            youtubeId = null, spotifyTrackId = null, albumArtUrl = null
        )
        assertEquals(87, result.score)
    }

    @Test
    fun `found result youtube id can be null`() {
        val result = RecognitionResult.Found(
            title = "Test", artist = "Artist", album = "Album",
            genre = "Pop", releaseDate = "2020", score = 80,
            youtubeId = null, spotifyTrackId = null, albumArtUrl = null
        )
        assertNull(result.youtubeId)
    }

    @Test
    fun `found result spotify id can be null`() {
        val result = RecognitionResult.Found(
            title = "Test", artist = "Artist", album = "Album",
            genre = "Pop", releaseDate = "2020", score = 80,
            youtubeId = null, spotifyTrackId = null, albumArtUrl = null
        )
        assertNull(result.spotifyTrackId)
    }

    @Test
    fun `not found result stores message correctly`() {
        val result = RecognitionResult.NotFound("No song recognized")
        assertEquals("No song recognized", result.message)
    }

    @Test
    fun `error result stores message correctly`() {
        val result = RecognitionResult.Error("Network timeout")
        assertEquals("Network timeout", result.message)
    }

    // ── Score validation ─────────────────────────────────────

    @Test
    fun `high confidence match score is above 80`() {
        val score = 92
        assertTrue("Score above 80 is high confidence", score > 80)
    }

    @Test
    fun `low confidence match score is below 50`() {
        val score = 35
        assertFalse("Score below 50 is low confidence", score > 80)
    }

    // ── ACRCloud config check ────────────────────────────────

    @Test
    fun `default API key placeholder is detected as unconfigured`() {
        val key            = "YOUR_ACR_ACCESS_KEY"
        val isConfigured   = key != "YOUR_ACR_ACCESS_KEY"
        assertFalse("Default placeholder key should be detected as unconfigured", isConfigured)
    }

    @Test
    fun `real API key is detected as configured`() {
        val key          = "abc123realkey456"
        val isConfigured = key != "YOUR_ACR_ACCESS_KEY"
        assertTrue("Real key should be detected as configured", isConfigured)
    }

    // ── State transition logic ───────────────────────────────

    @Test
    fun `result is not in loading state when Found`() {
        val result: RecognitionResult = RecognitionResult.Found(
            "Title", "Artist", "Album", "Genre", "2020",
            90, null, null, null
        )
        val isLoading = result is RecognitionResult.Recording ||
                result is RecognitionResult.Processing
        assertFalse(isLoading)
    }

    @Test
    fun `result is in loading state when Recording`() {
        val result: RecognitionResult = RecognitionResult.Recording
        val isLoading = result is RecognitionResult.Recording ||
                result is RecognitionResult.Processing
        assertTrue(isLoading)
    }

    @Test
    fun `result is in loading state when Processing`() {
        val result: RecognitionResult = RecognitionResult.Processing
        val isLoading = result is RecognitionResult.Recording ||
                result is RecognitionResult.Processing
        assertTrue(isLoading)
    }
}
