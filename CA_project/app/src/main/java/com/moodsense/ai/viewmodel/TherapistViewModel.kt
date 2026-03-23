package com.moodsense.ai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moodsense.ai.data.local.entities.EmotionHistoryEntity
import com.moodsense.ai.data.local.entities.MoodJournalEntity
import com.moodsense.ai.data.local.entities.TypingStatsEntity
import com.moodsense.ai.data.repository.EmotionHistoryRepository
import com.moodsense.ai.data.repository.MoodJournalRepository
import com.moodsense.ai.data.repository.TypingStatsRepository
import com.moodsense.ai.data.repository.GroqApi
import com.moodsense.ai.data.repository.GroqChatRequest
import com.moodsense.ai.data.repository.GroqMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

data class ChatMessage(
    val id:        String = UUID.randomUUID().toString(),
    val text:      String,
    val isUser:    Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class TherapistUiState(
    val messages:    List<ChatMessage> = emptyList(),
    val isTyping:    Boolean           = false,
    val error:       String?           = null,
    val suggestions: List<String>      = emptyList()
)

@HiltViewModel
class TherapistViewModel @Inject constructor(
    private val emotionRepo: EmotionHistoryRepository,
    private val journalRepo: MoodJournalRepository,
    private val typingRepo:  TypingStatsRepository,
    private val groqApi: GroqApi
) : ViewModel() {

    private val _uiState = MutableStateFlow(TherapistUiState())
    val uiState: StateFlow<TherapistUiState> = _uiState.asStateFlow()

    private var isInitializing = false
    
    // Groq API Key
    private val GROQ_API_KEY = "Bearer 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"

    // History for the model (OpenAI/Groq style)
    private val conversationHistory = mutableListOf<GroqMessage>()

    /**
     * Optimized Session Start using Groq (Llama 3)
     */
    fun startSession() {
        if (isInitializing || _uiState.value.messages.isNotEmpty()) return
        isInitializing = true
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isTyping = true, error = null)
            try {
                val summary = buildDailySummary()
                val systemPrompt = buildSystemPrompt(summary)

                // Initialize history with system prompt
                conversationHistory.clear()
                conversationHistory.add(GroqMessage(role = "system", content = systemPrompt))
                
                // Add first user prompt internally to get a greeting
                val firstPrompt = "Please introduce yourself as MoodSense AI and start our wellness session with a warm greeting based on my daily data."
                conversationHistory.add(GroqMessage(role = "user", content = firstPrompt))

                val response = retryWithBackoff {
                    groqApi.getChatCompletion(
                        apiKey = GROQ_API_KEY,
                        request = GroqChatRequest(
                            model = "llama-3.1-8b-instant", // Using updated model
                            messages = conversationHistory
                        )
                    )
                }
                
                val reply = response.choices.firstOrNull()?.message?.content 
                    ?: "Hello! I'm MoodSense AI. How are you feeling today?"

                // Save reply to history
                conversationHistory.add(GroqMessage(role = "assistant", content = reply))

                _uiState.value = _uiState.value.copy(
                    isTyping    = false,
                    messages    = listOf(ChatMessage(text = reply, isUser = false)),
                    suggestions = listOf("How does my day look?", "Help me relax", "I'm feeling stressed"),
                    error = null
                )
            } catch (e: Exception) {
                handleAiError(e)
            } finally {
                isInitializing = false
            }
        }
    }

    private fun handleAiError(e: Exception) {
        android.util.Log.e("TherapistVM", "AI Error: ${e.message}", e)
        
        // Detailed logging for HTTP errors
        if (e is retrofit2.HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            android.util.Log.e("TherapistVM", "HTTP Error Body: $errorBody")
        }

        val errorMsg = when {
            e.message?.contains("401") == true -> "AI Error: Unauthorized. Please check your API key."
            e.message?.contains("429") == true -> "Rate limit reached. Please wait a moment."
            e.message?.contains("400") == true -> "AI Error: Bad Request. The API parameters may be incorrect."
            else -> "AI Error: Unable to connect. Please check your internet."
        }
        _uiState.value = _uiState.value.copy(isTyping = false, error = errorMsg)
    }

    fun sendMessage(text: String) {
        if (_uiState.value.isTyping || text.isBlank()) return

        val userMsg = ChatMessage(text = text, isUser = true)
        _uiState.value = _uiState.value.copy(
            messages = _uiState.value.messages + userMsg,
            isTyping = true,
            error = null
        )

        // Add to history
        conversationHistory.add(GroqMessage(role = "user", content = text))

        viewModelScope.launch {
            try {
                val response = retryWithBackoff {
                    groqApi.getChatCompletion(
                        apiKey = GROQ_API_KEY,
                        request = GroqChatRequest(
                            model = "llama-3.1-8b-instant",
                            messages = conversationHistory
                        )
                    )
                }
                
                val reply = response.choices.firstOrNull()?.message?.content ?: "I'm listening..."
                
                // Add to history
                conversationHistory.add(GroqMessage(role = "assistant", content = reply))

                _uiState.value = _uiState.value.copy(
                    isTyping = false,
                    messages = _uiState.value.messages + ChatMessage(text = reply, isUser = false),
                    suggestions = generateFollowUpSuggestions(text)
                )
            } catch (e: Exception) {
                handleAiError(e)
            }
        }
    }

    private suspend fun <T> retryWithBackoff(
        maxRetries: Int = 3,
        initialDelay: Long = 2000,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelay
        repeat(maxRetries - 1) {
            try {
                return block()
            } catch (e: Exception) {
                // Don't retry on Auth or Bad Request errors
                if (e.message?.contains("401") == true || e.message?.contains("400") == true) throw e

                if (e.message?.contains("429") == true) {
                    delay(currentDelay)
                    currentDelay *= 2
                } else throw e
            }
        }
        return block()
    }

    private suspend fun buildDailySummary(): DailySummary = withContext(Dispatchers.IO) {
        try {
            val todayStart = System.currentTimeMillis() - (24 * 60 * 60 * 1000L)
            val emotions   = emotionRepo.getEmotionsInRange(todayStart, System.currentTimeMillis())
            val journals   = journalRepo.getAllEntries().firstOrNull()?.filter { it.timestamp >= todayStart } ?: emptyList()
            val typing     = typingRepo.getRecentStats().firstOrNull()?.filter { it.timestamp >= todayStart } ?: emptyList()
            DailySummary(emotions, journals, typing)
        } catch (e: Exception) {
            DailySummary(emptyList(), emptyList(), emptyList())
        }
    }

    private fun buildSystemPrompt(summary: DailySummary): String {
        return """
            You are MoodSense AI, a warm and empathetic mood therapist.
            Context for today:
            - User had ${summary.emotions.size} emotional events.
            - User wrote ${summary.journals.size} journals.
            Your goal is to be supportive, concise, and help the user process their day.
        """.trimIndent()
    }

    private fun generateFollowUpSuggestions(lastMsg: String): List<String> {
        return listOf("Tell me more", "What should I do?", "Relaxation tip")
    }
}

private data class DailySummary(
    val emotions: List<EmotionHistoryEntity>,
    val journals: List<MoodJournalEntity>,
    val typing:   List<TypingStatsEntity>
)
