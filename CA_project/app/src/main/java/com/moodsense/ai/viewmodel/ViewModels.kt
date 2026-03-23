package com.moodsense.ai.viewmodel

import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.moodsense.ai.data.local.entities.EmotionHistoryEntity
import com.moodsense.ai.data.local.entities.MoodJournalEntity
import com.moodsense.ai.data.local.entities.TypingStatsEntity
import com.moodsense.ai.data.repository.*
import com.moodsense.ai.domain.model.*
import com.moodsense.ai.ml.FaceEmotionAnalyzer
import com.moodsense.ai.ml.SoundClassifier
import com.moodsense.ai.util.InsightsAnalyzer
import com.moodsense.ai.util.TypingTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

// ============================================================
// Emotion Camera ViewModel
// ============================================================

/**
 * ViewModel for facial emotion detection screen
 */
@HiltViewModel
class EmotionViewModel @Inject constructor(
    private val faceEmotionAnalyzer: FaceEmotionAnalyzer,
    private val emotionHistoryRepository: EmotionHistoryRepository
) : ViewModel() {

    private val _emotionResult = MutableStateFlow<EmotionResult?>(null)
    val emotionResult: StateFlow<EmotionResult?> = _emotionResult.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    /**
     * Analyze a camera frame for emotions
     */
    fun analyzeFrame(imageProxy: ImageProxy) {
        if (_isProcessing.value) {
            imageProxy.close()
            return
        }

        viewModelScope.launch {
            _isProcessing.value = true
            try {
                val inputImage = InputImage.fromMediaImage(
                    imageProxy.image!!,
                    imageProxy.imageInfo.rotationDegrees
                )
                val result = faceEmotionAnalyzer.analyzeImage(inputImage)
                _emotionResult.value = result

                // Save significant detections
                if (result.emotion != Emotion.UNKNOWN && result.confidence > 0.6f) {
                    emotionHistoryRepository.saveEmotion(result)
                }
            } catch (e: Exception) {
                // Silently handle analysis errors
            } finally {
                _isProcessing.value = false
                imageProxy.close()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        faceEmotionAnalyzer.close()
    }
}

// ============================================================
// Sound Monitor ViewModel
// ============================================================

/**
 * ViewModel for the sound monitor / song recognition screen
 */
@HiltViewModel
class SoundViewModel @Inject constructor(
    private val soundClassifier: SoundClassifier,
    private val songRecognizer: com.moodsense.ai.ml.SongRecognizer
) : ViewModel() {

    val soundResult:       StateFlow<SoundResult?> = soundClassifier.soundResult
    val isRunning:         StateFlow<Boolean>       = soundClassifier.isRunning
    val recognitionResult                           = songRecognizer.result

    fun startMonitoring()  = soundClassifier.startClassification()
    fun stopMonitoring()   = soundClassifier.stopClassification()
    fun identifySong()     = songRecognizer.identify()
    fun resetRecognition() = songRecognizer.reset()

    override fun onCleared() {
        super.onCleared()
        soundClassifier.stopClassification()
    }
}

// ============================================================
// Mood Journal ViewModel
// ============================================================

data class JournalUiState(
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null,
    val entries: List<MoodJournalEntity> = emptyList()
)

/**
 * ViewModel for the mood journal screen
 */
@HiltViewModel
class JournalViewModel @Inject constructor(
    private val moodJournalRepository: MoodJournalRepository,
    private val typingStatsRepository: TypingStatsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(JournalUiState())
    val uiState: StateFlow<JournalUiState> = _uiState.asStateFlow()

    val entries = moodJournalRepository.getAllEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val typingTracker = TypingTracker()

    fun onTextFieldFocused() = typingTracker.startSession()

    fun onKeyPress(isBackspace: Boolean) = typingTracker.onKeyPress(isBackspace)

    /**
     * Save a new journal entry
     */
    fun saveEntry(
        note: String,
        selectedTags: List<MoodTag>,
        detectedEmotion: Emotion?,
        mood: Int
    ) {
        if (note.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Please write something before saving")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                moodJournalRepository.saveEntry(note, selectedTags, detectedEmotion, mood)

                // Save typing stats if available
                typingTracker.getStats()?.let { stats ->
                    typingStatsRepository.saveStats(stats)
                }

                _uiState.value = _uiState.value.copy(isLoading = false, isSaved = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to save: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetSavedState() {
        _uiState.value = _uiState.value.copy(isSaved = false)
    }

    fun deleteEntry(entry: MoodJournalEntity) {
        viewModelScope.launch {
            moodJournalRepository.deleteEntry(entry)
        }
    }
}

// ============================================================
// Insights ViewModel
// ============================================================

data class InsightsUiState(
    val isLoading: Boolean = true,
    val insights: List<MoodInsight> = emptyList(),
    val emotionData: List<EmotionHistoryEntity> = emptyList(),
    val typingData: List<TypingStatsEntity> = emptyList(),
    val journalData: List<MoodJournalEntity> = emptyList()
)

/**
 * ViewModel for the insights and analytics screen
 */
@HiltViewModel
class InsightsViewModel @Inject constructor(
    private val emotionHistoryRepository: EmotionHistoryRepository,
    private val typingStatsRepository: TypingStatsRepository,
    private val moodJournalRepository: MoodJournalRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsightsUiState())
    val uiState: StateFlow<InsightsUiState> = _uiState.asStateFlow()

    init {
        loadInsights()
    }

    private fun loadInsights() {
        viewModelScope.launch {
            combine(
                emotionHistoryRepository.getRecentEmotions(),
                typingStatsRepository.getAllStats(),
                moodJournalRepository.getAllEntries()
            ) { emotions, typingStats, journals ->
                Triple(emotions, typingStats, journals)
            }.collect { (emotions, typingStats, journals) ->
                val insights = InsightsAnalyzer.generateInsights(emotions, journals, typingStats)
                _uiState.value = InsightsUiState(
                    isLoading = false,
                    insights = insights,
                    emotionData = emotions,
                    typingData = typingStats,
                    journalData = journals
                )
            }
        }
    }
}

// ============================================================
// Music Recommendation ViewModel
// ============================================================

data class MusicUiState(
    val isLoading: Boolean = false,
    val tracks: List<MusicTrack> = emptyList(),
    val error: String? = null,
    val currentMood: Emotion = Emotion.NEUTRAL
)

/**
 * ViewModel for music recommendations powered by iTunes Search API.
 * Free, no API key needed, works out of the box.
 */
@HiltViewModel
class MusicViewModel @Inject constructor(
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MusicUiState())
    val uiState: StateFlow<MusicUiState> = _uiState.asStateFlow()

    fun loadRecommendations(emotion: Emotion = Emotion.NEUTRAL) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading   = true,
                currentMood = emotion,
                error       = null,
                tracks      = emptyList()
            )

            val result = musicRepository.searchTracksByMood(emotion)
            result.fold(
                onSuccess = { tracks ->
                    _uiState.value = _uiState.value.copy(isLoading = false, tracks = tracks)
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Unknown error"
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

// ============================================================
// Onboarding ViewModel
// ============================================================

/**
 * ViewModel for onboarding flow
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {
    private val _onboardingComplete = MutableStateFlow(false)
    val onboardingComplete: StateFlow<Boolean> = _onboardingComplete.asStateFlow()

    fun completeOnboarding() {
        _onboardingComplete.value = true
    }
}