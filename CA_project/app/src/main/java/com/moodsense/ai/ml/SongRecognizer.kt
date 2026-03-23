package com.moodsense.ai.ml

import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

// ─────────────────────────────────────────────────────────────
// ACRCloud response models
// ─────────────────────────────────────────────────────────────

data class AcrCloudResponse(
    val status: AcrStatus,
    val metadata: AcrMetadata?
)

data class AcrStatus(
    val code: Int,
    val msg: String
)

data class AcrMetadata(
    val music: List<AcrMusic>?
)

data class AcrMusic(
    val title: String,
    @SerializedName("artists") val artists: List<AcrArtist>,
    val album: AcrAlbum?,
    val genres: List<AcrGenre>?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("score") val score: Int,
    @SerializedName("external_metadata") val externalMetadata: AcrExternalMetadata?
)

data class AcrArtist(val name: String)
data class AcrAlbum(val name: String)
data class AcrGenre(val name: String)

data class AcrExternalMetadata(
    val spotify: AcrSpotifyMeta?,
    val youtube: AcrYoutubeMeta?
)

data class AcrSpotifyMeta(
    val track: AcrSpotifyTrack?,
    val artists: List<AcrSpotifyArtist>?,
    val album: AcrSpotifyAlbum?
)

data class AcrSpotifyTrack(val id: String?, val name: String?)
data class AcrSpotifyArtist(val id: String?, val name: String?)
data class AcrSpotifyAlbum(val id: String?, val name: String?)
data class AcrYoutubeMeta(val vid: String?)

// ─────────────────────────────────────────────────────────────
// Result sealed class
// ─────────────────────────────────────────────────────────────

sealed class RecognitionResult {
    object Idle : RecognitionResult()
    object Recording : RecognitionResult()
    object Processing : RecognitionResult()
    data class Found(
        val title: String,
        val artist: String,
        val album: String,
        val genre: String,
        val releaseDate: String,
        val score: Int,
        val youtubeId: String?,
        val spotifyTrackId: String?,
        val albumArtUrl: String?
    ) : RecognitionResult()
    data class NotFound(val message: String) : RecognitionResult()
    data class Error(val message: String) : RecognitionResult()
}

// ─────────────────────────────────────────────────────────────
// SongRecognizer — records audio & calls ACRCloud
// ─────────────────────────────────────────────────────────────

@Singleton
class SongRecognizer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "SongRecognizer"
        private const val SAMPLE_RATE    = 16000 // Higher sample rate for better matching
        private const val RECORD_SECONDS = 12    // Longer duration for better fingerprinting
        private const val ACR_HOST       = "identify-eu-west-1.acrcloud.com"

        const val ACR_ACCESS_KEY    = "a2ccc2f6bc2ab9d4a7edaacfdec417a2"
        const val ACR_ACCESS_SECRET = "kILPNzKOY42ofuEc5fKmmn8ZR1SM7GVMfYoBuPxm"
    }

    private val client = OkHttpClient.Builder()
        .connectTimeout(25, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _result = MutableStateFlow<RecognitionResult>(RecognitionResult.Idle)
    val result: StateFlow<RecognitionResult> = _result

    fun identify() {
        if (_result.value is RecognitionResult.Recording ||
            _result.value is RecognitionResult.Processing) return

        scope.launch {
            try {
                _result.value = RecognitionResult.Recording
                val rawAudio = recordAudio()
                
                // Wrap in WAV header so ACRCloud knows the sample rate
                val wavAudio = addWavHeader(rawAudio, SAMPLE_RATE)

                _result.value = RecognitionResult.Processing
                val response = sendToAcrCloud(wavAudio)

                _result.value = parseResponse(response)

            } catch (e: Exception) {
                Log.e(TAG, "Recognition error: ${e.message}")
                _result.value = RecognitionResult.Error("${e.message}")
            }
        }
    }

    fun reset() {
        _result.value = RecognitionResult.Idle
    }

    private suspend fun recordAudio(): ByteArray {
        val bufferSize = AudioRecord.getMinBufferSize(
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        )

        val recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            SAMPLE_RATE,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize * 4
        )

        val totalSamples = SAMPLE_RATE * RECORD_SECONDS
        val audioBuffer  = ByteArray(totalSamples * 2) 
        var offset = 0

        if (recorder.state != AudioRecord.STATE_INITIALIZED) {
            throw Exception("Microphone initialization failed. Check permissions.")
        }

        recorder.startRecording()
        try {
            val startTime = System.currentTimeMillis()
            // Record until buffer is full or timeout (15s)
            while (offset < audioBuffer.size && (System.currentTimeMillis() - startTime) < 15000) {
                val read = recorder.read(audioBuffer, offset, audioBuffer.size - offset)
                if (read > 0) {
                    offset += read
                } else if (read < 0) {
                    break 
                }
                yield() // Cooperate with other coroutines
            }
        } finally {
            recorder.stop()
            recorder.release()
        }

        return audioBuffer
    }

    /**
     * Adds a standard 44-byte WAV header to raw PCM data.
     * This ensures the server knows the sample rate and bit depth.
     */
    private fun addWavHeader(pcmData: ByteArray, sampleRate: Int): ByteArray {
        val header = ByteArray(44)
        val totalDataLen = pcmData.size + 36
        val byteRate = sampleRate * 2 

        header[0] = 'R'.toByte(); header[1] = 'I'.toByte(); header[2] = 'F'.toByte(); header[3] = 'F'.toByte()
        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = ((totalDataLen shr 8) and 0xff).toByte()
        header[6] = ((totalDataLen shr 16) and 0xff).toByte()
        header[7] = ((totalDataLen shr 24) and 0xff).toByte()
        header[8] = 'W'.toByte(); header[9] = 'A'.toByte(); header[10] = 'V'.toByte(); header[11] = 'E'.toByte()
        header[12] = 'f'.toByte(); header[13] = 'm'.toByte(); header[14] = 't'.toByte(); header[15] = ' '.toByte()
        header[16] = 16; header[17] = 0; header[18] = 0; header[19] = 0 // Header size
        header[20] = 1; header[21] = 0 // PCM format
        header[22] = 1; header[23] = 0 // Mono
        header[24] = (sampleRate and 0xff).toByte()
        header[25] = ((sampleRate shr 8) and 0xff).toByte()
        header[26] = ((sampleRate shr 16) and 0xff).toByte()
        header[27] = ((sampleRate shr 24) and 0xff).toByte()
        header[28] = (byteRate and 0xff).toByte()
        header[29] = ((byteRate shr 8) and 0xff).toByte()
        header[30] = ((byteRate shr 16) and 0xff).toByte()
        header[31] = ((byteRate shr 24) and 0xff).toByte()
        header[32] = 2; header[33] = 0 // Block align
        header[34] = 16; header[35] = 0 // Bits per sample
        header[36] = 'd'.toByte(); header[37] = 'a'.toByte(); header[38] = 't'.toByte(); header[39] = 'a'.toByte()
        header[40] = (pcmData.size and 0xff).toByte()
        header[41] = ((pcmData.size shr 8) and 0xff).toByte()
        header[42] = ((pcmData.size shr 16) and 0xff).toByte()
        header[43] = ((pcmData.size shr 24) and 0xff).toByte()

        return header + pcmData
    }

    private fun sendToAcrCloud(audioData: ByteArray): String {
        val timestamp  = (System.currentTimeMillis() / 1000).toString()
        val dataType   = "audio"
        val sigVersion = "1"
        val httpMethod = "POST"
        val httpUri    = "/v1/identify"

        val stringToSign = "$httpMethod\n$httpUri\n$ACR_ACCESS_KEY\n$dataType\n$sigVersion\n$timestamp"
        val signature    = hmacSha1(stringToSign, ACR_ACCESS_SECRET)

        val body = FormBody.Builder()
            .add("access_key",        ACR_ACCESS_KEY)
            .add("data_type",         dataType)
            .add("signature_version", sigVersion)
            .add("signature",         signature)
            .add("sample_bytes",      audioData.size.toString())
            .add("timestamp",         timestamp)
            .add("sample",            Base64.encodeToString(audioData, Base64.NO_WRAP))
            .build()

        val request = Request.Builder()
            .url("https://$ACR_HOST/v1/identify")
            .post(body)
            .build()

        val response = client.newCall(request).execute()
        return response.body?.string() ?: ""
    }

    private fun parseResponse(json: String): RecognitionResult {
        return try {
            val resp = gson.fromJson(json, AcrCloudResponse::class.java)

            when (resp.status.code) {
                0 -> {
                    val music = resp.metadata?.music?.firstOrNull()
                        ?: return RecognitionResult.NotFound("Match found, but no metadata available.")

                    RecognitionResult.Found(
                        title       = music.title,
                        artist      = music.artists.joinToString(", ") { it.name },
                        album       = music.album?.name ?: "",
                        genre       = music.genres?.firstOrNull()?.name ?: "Unknown",
                        releaseDate = music.releaseDate ?: "",
                        score       = music.score,
                        youtubeId   = music.externalMetadata?.youtube?.vid,
                        spotifyTrackId = music.externalMetadata?.spotify?.track?.id,
                        albumArtUrl = null
                    )
                }
                1001 -> RecognitionResult.NotFound("No song identified. Try a louder part of the song.")
                2004 -> RecognitionResult.Error("Audio error (2004). Try holding the phone closer.")
                else -> RecognitionResult.Error("ACRCloud Error ${resp.status.code}: ${resp.status.msg}")
            }
        } catch (e: Exception) {
            RecognitionResult.Error("Failed to parse results.")
        }
    }

    private fun hmacSha1(data: String, key: String): String {
        val mac = Mac.getInstance("HmacSHA1")
        mac.init(SecretKeySpec(key.toByteArray(), "HmacSHA1"))
        return Base64.encodeToString(mac.doFinal(data.toByteArray()), Base64.NO_WRAP)
    }
}
