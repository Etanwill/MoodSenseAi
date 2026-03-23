package com.moodsense.ai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moodsense.ai.data.repository.EmotionHistoryRepository
import com.moodsense.ai.data.repository.MoodJournalRepository
import com.moodsense.ai.domain.model.Emotion
import com.moodsense.ai.domain.model.EmotionResult
import com.moodsense.ai.domain.model.SoundResult
import com.moodsense.ai.ml.SoundClassifier
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Home Dashboard screen.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val soundClassifier: SoundClassifier,
    private val emotionHistoryRepository: EmotionHistoryRepository,
    private val moodJournalRepository: MoodJournalRepository
) : ViewModel() {

    private val _currentEmotion = MutableStateFlow<EmotionResult?>(null)
    val currentEmotion: StateFlow<EmotionResult?> = _currentEmotion.asStateFlow()

    val soundResult: StateFlow<SoundResult?> = soundClassifier.soundResult

    val recentEmotions = emotionHistoryRepository.getRecentEmotions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val journalEntries = moodJournalRepository.getAllEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        soundClassifier.startClassification()
    }

    fun updateCurrentEmotion(result: EmotionResult) {
        _currentEmotion.value = result
        // Auto-save detected emotions periodically
        viewModelScope.launch {
            if (result.emotion != Emotion.UNKNOWN && result.confidence > 0.5f) {
                emotionHistoryRepository.saveEmotion(result)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        soundClassifier.stopClassification()
    }
}
