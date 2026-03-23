package com.moodsense.ai.data.repository

import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface GroqApi {
    @POST("v1/chat/completions")
    suspend fun getChatCompletion(
        @Header("Authorization") apiKey: String,
        @Body request: GroqChatRequest
    ): GroqChatResponse
}

data class GroqChatRequest(
    @SerializedName("model")
    val model: String,
    @SerializedName("messages")
    val messages: List<GroqMessage>,
    @SerializedName("temperature")
    val temperature: Double? = null,
    @SerializedName("max_tokens")
    val maxTokens: Int? = null,
    @SerializedName("stream")
    val stream: Boolean? = false
)

data class GroqMessage(
    @SerializedName("role")
    val role: String,
    @SerializedName("content")
    val content: String
)

data class GroqChatResponse(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("choices")
    val choices: List<GroqChoice>
)

data class GroqChoice(
    @SerializedName("message")
    val message: GroqMessage,
    @SerializedName("finish_reason")
    val finishReason: String? = null
)
