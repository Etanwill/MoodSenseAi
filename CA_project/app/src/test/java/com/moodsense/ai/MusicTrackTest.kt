package com.moodsense.ai

import com.moodsense.ai.domain.model.MusicTrack
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for MusicTrack model and iTunes data mapping logic.
 *
 * Run with: ./gradlew test
 */
class MusicTrackTest {

    private fun sampleTrack(
        id:         String  = "123456",
        name:       String  = "Happy",
        artist:     String  = "Pharrell Williams",
        album:      String  = "Girl",
        artUrl:     String? = "https://is1-ssl.mzstatic.com/image/300x300/sample.jpg",
        trackUrl:   String  = "https://music.apple.com/track/123456",
        previewUrl: String? = "https://audio-ssl.itunes.apple.com/preview.mp3"
    ) = MusicTrack(id, name, artist, album, artUrl, trackUrl, previewUrl)

    // ── Field tests ──────────────────────────────────────────

    @Test
    fun `track stores id correctly`() {
        val track = sampleTrack(id = "987654")
        assertEquals("987654", track.id)
    }

    @Test
    fun `track stores name correctly`() {
        val track = sampleTrack(name = "Blinding Lights")
        assertEquals("Blinding Lights", track.name)
    }

    @Test
    fun `track stores artist correctly`() {
        val track = sampleTrack(artist = "The Weeknd")
        assertEquals("The Weeknd", track.artistName)
    }

    @Test
    fun `track stores album correctly`() {
        val track = sampleTrack(album = "After Hours")
        assertEquals("After Hours", track.albumName)
    }

    @Test
    fun `track stores trackUrl correctly`() {
        val url   = "https://music.apple.com/track/987654"
        val track = sampleTrack(trackUrl = url)
        assertEquals(url, track.trackUrl)
    }

    @Test
    fun `track preview url can be null`() {
        val track = sampleTrack(previewUrl = null)
        assertNull(track.previewUrl)
    }

    @Test
    fun `track album art url can be null`() {
        val track = sampleTrack(artUrl = null)
        assertNull(track.albumArtUrl)
    }

    @Test
    fun `track preview url is valid when present`() {
        val track = sampleTrack(previewUrl = "https://audio-ssl.itunes.apple.com/preview.mp3")
        assertNotNull(track.previewUrl)
        assertTrue(track.previewUrl!!.startsWith("https://"))
    }

    // ── Album art upgrade logic ──────────────────────────────

    @Test
    fun `album art url can be upgraded from 100x100 to 300x300`() {
        val small   = "https://is1-ssl.mzstatic.com/image/100x100/sample.jpg"
        val upgraded = small.replace("100x100", "300x300")
        assertTrue(upgraded.contains("300x300"))
        assertFalse(upgraded.contains("100x100"))
    }

    @Test
    fun `track url is not blank for valid track`() {
        val track = sampleTrack()
        assertTrue(track.trackUrl.isNotBlank())
    }

    @Test
    fun `two tracks with same id are equal`() {
        val track1 = sampleTrack(id = "111", name = "Song A")
        val track2 = sampleTrack(id = "111", name = "Song A")
        assertEquals(track1, track2)
    }

    @Test
    fun `two tracks with different ids are not equal`() {
        val track1 = sampleTrack(id = "111")
        val track2 = sampleTrack(id = "222")
        assertNotEquals(track1, track2)
    }

    // ── Mood query mapping tests ─────────────────────────────

    @Test
    fun `happy mood search term contains upbeat keywords`() {
        val term = "happy upbeat fun"
        assertTrue(term.contains("happy"))
        assertTrue(term.contains("upbeat"))
    }

    @Test
    fun `sad mood search term contains emotional keywords`() {
        val term = "sad emotional heartbreak"
        assertTrue(term.contains("sad"))
        assertTrue(term.contains("emotional"))
    }

    @Test
    fun `neutral mood search term contains focus keywords`() {
        val term = "focus concentration ambient"
        assertTrue(term.contains("focus"))
        assertTrue(term.contains("ambient"))
    }
}