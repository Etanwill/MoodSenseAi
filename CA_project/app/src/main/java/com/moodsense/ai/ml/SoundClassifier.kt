package com.moodsense.ai.ml

import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import com.moodsense.ai.domain.model.SoundResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.tensorflow.lite.support.audio.TensorAudio
import org.tensorflow.lite.task.audio.classifier.AudioClassifier
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.coroutineContext
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * On-device sound classifier using TensorFlow Lite with YAMNet model.
 * Classifies ambient sounds into categories like speech, music, traffic, etc.
 */
@Singleton
class SoundClassifier @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val MODEL_FILE = "yamnet.tflite"
    private val SAMPLE_RATE = 16000
    private val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
    private val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT

    private var audioClassifier: AudioClassifier? = null
    private var audioRecord: AudioRecord? = null
    private var classificationJob: Job? = null

    private val _soundResult = MutableStateFlow<SoundResult?>(null)
    val soundResult: StateFlow<SoundResult?> = _soundResult

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private fun initClassifier(): Boolean {
        return try {
            val assetList = context.assets.list("") ?: emptyArray()
            if (MODEL_FILE !in assetList) {
                Log.w("SoundClassifier", "YAMNet model not found. Using mock classification.")
                return false
            }

            val options = AudioClassifier.AudioClassifierOptions.builder()
                .setMaxResults(5)
                .setScoreThreshold(0.3f)
                .build()

            audioClassifier = AudioClassifier.createFromFileAndOptions(context, MODEL_FILE, options)
            true
        } catch (e: Exception) {
            Log.e("SoundClassifier", "Failed to load YAMNet model: ${e.message}")
            false
        }
    }

    fun startClassification() {
        if (_isRunning.value) return
        val hasModel = initClassifier()
        _isRunning.value = true

        classificationJob = scope.launch {
            if (hasModel && audioClassifier != null) {
                classifyWithModel()
            } else {
                mockClassification()
            }
        }
    }

    private suspend fun classifyWithModel() {
        val classifier = audioClassifier ?: return
        try {
            val tensorAudio = classifier.createInputTensorAudio()
            val bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT)

            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                CHANNEL_CONFIG,
                AUDIO_FORMAT,
                bufferSize * 2
            )

            audioRecord?.startRecording()

            while (_isRunning.value && coroutineContext.isActive) {
                tensorAudio.load(audioRecord)
                val results = classifier.classify(tensorAudio)
                val topResults = results.firstOrNull()?.categories ?: continue

                if (topResults.isNotEmpty()) {
                    val top = topResults.first()
                    val intensity = calculateIntensity(audioRecord)

                    _soundResult.value = SoundResult(
                        category = mapYamNetCategory(top.label),
                        confidence = top.score,
                        intensity = intensity
                    )
                }
                delay(500)
            }
        } catch (e: Exception) {
            Log.e("SoundClassifier", "Classification error: ${e.message}")
        } finally {
            audioRecord?.stop()
            audioRecord?.release()
            audioRecord = null
        }
    }

    private suspend fun mockClassification() {
        val categories = listOf("Speech", "Lofi Music", "Silence", "City Traffic", "Nature Sounds")
        var index = 0
        while (_isRunning.value && coroutineContext.isActive) {
            _soundResult.value = SoundResult(
                category = categories[index % categories.size],
                confidence = 0.7f + (Math.random() * 0.3f).toFloat(),
                intensity = (Math.random() * 0.8f).toFloat()
            )
            index++
            delay(2000)
        }
    }

    private fun calculateIntensity(audioRecord: AudioRecord?): Float {
        audioRecord ?: return 0f
        val buffer = ShortArray(1024)
        val read = audioRecord.read(buffer, 0, buffer.size)
        if (read <= 0) return 0f

        var sum = 0.0
        for (i in 0 until read) {
            sum += (buffer[i] * buffer[i]).toDouble()
        }
        val rms = sqrt(sum / read)
        return (rms / Short.MAX_VALUE).toFloat().coerceIn(0f, 1f)
    }

    /**
     * More flexible mapping to show specific instruments or genres
     */
    private fun mapYamNetCategory(yamnetLabel: String): String {
        val label = yamnetLabel.lowercase()
        return when {
            label.contains("speech") || label.contains("voice") -> "Speech"
            label.contains("traffic") || label.contains("vehicle") -> "Traffic"
            label.contains("nature") || label.contains("bird") || label.contains("wind") -> "Nature"
            label.contains("silence") -> "Silence"
            
            // If it's music, try to be more specific than just "Music"
            label.contains("music") || label.contains("song") || label.contains("instrument") -> {
                if (label == "music") "Music"
                else yamnetLabel.split(",").first().trim().replaceFirstChar { it.uppercase() }
            }

            else -> yamnetLabel.split(",").first().trim().replaceFirstChar { it.uppercase() }
        }
    }

    fun stopClassification() {
        _isRunning.value = false
        classificationJob?.cancel()
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }

    fun release() {
        stopClassification()
        audioClassifier?.close()
        scope.cancel()
    }
}
