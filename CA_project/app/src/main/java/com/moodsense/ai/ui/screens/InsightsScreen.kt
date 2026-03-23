package com.moodsense.ai.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moodsense.ai.domain.model.Emotion
import com.moodsense.ai.domain.model.InsightType
import com.moodsense.ai.domain.model.MoodInsight
import com.moodsense.ai.ui.components.LoadingIndicator
import com.moodsense.ai.ui.components.MoodSenseTopBar
import com.moodsense.ai.ui.components.StatCard
import com.moodsense.ai.viewmodel.InsightsViewModel

/**
 * Insights screen showing mood patterns, emotion frequency, and typing behavior
 */
@Composable
fun InsightsScreen(
    onNavigateBack: () -> Unit,
    viewModel: InsightsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            MoodSenseTopBar(
                title = "Your Insights",
                onNavigateBack = onNavigateBack
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            LoadingIndicator(message = "Analyzing your mood patterns...")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Summary stats
                item {
                    Text(
                        text = "Overview",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            label = "Total\nEmotions",
                            value = "${uiState.emotionData.size}",
                            icon = "🎭",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "Journal\nEntries",
                            value = "${uiState.journalData.size}",
                            icon = "📔",
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = "Typing\nSessions",
                            value = "${uiState.typingData.size}",
                            icon = "⌨️",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Emotion frequency
                if (uiState.emotionData.isNotEmpty()) {
                    item {
                        Text(
                            text = "Emotion Frequency",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    item {
                        EmotionFrequencyChart(emotions = uiState.emotionData.map { it.emotion })
                    }
                }

                // Mood timeline
                if (uiState.journalData.isNotEmpty()) {
                    item {
                        Text(
                            text = "Mood Timeline",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    item {
                        MoodTimelineCard(journals = uiState.journalData)
                    }
                }

                // Typing stats
                if (uiState.typingData.isNotEmpty()) {
                    item {
                        Text(
                            text = "Typing Behavior",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    item {
                        TypingStatsCard(typingData = uiState.typingData)
                    }
                }

                // Insights
                if (uiState.insights.isNotEmpty()) {
                    item {
                        Text(
                            text = "Personalized Insights",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    items(uiState.insights) { insight ->
                        InsightCard(insight = insight)
                    }
                } else if (!uiState.isLoading) {
                    item {
                        EmptyInsightsCard()
                    }
                }
            }
        }
    }
}

@Composable
private fun EmotionFrequencyChart(emotions: List<String>) {
    val emotionCounts = emotions.groupBy { it }
        .mapValues { it.value.size }
        .toList()
        .sortedByDescending { it.second }
        .take(6)
    val maxCount = emotionCounts.maxOfOrNull { it.second } ?: 1

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            emotionCounts.forEach { (emotion, count) ->
                val fraction = count.toFloat() / maxCount.toFloat()
                val emoji = getEmotionEmoji(emotion)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = emoji, fontSize = 20.sp, modifier = Modifier.width(36.dp))
                    Text(
                        text = emotion.lowercase().replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.width(80.dp)
                    )
                    LinearProgressIndicator(
                        progress = fraction,
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp),
                        color = getEmotionColorForName(emotion)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$count",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.width(30.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun MoodTimelineCard(journals: List<com.moodsense.ai.data.local.entities.MoodJournalEntity>) {
    val recentEntries = journals.sortedByDescending { it.timestamp }.take(7)
    val avgMood = recentEntries.map { it.mood }.average()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Last 7 entries",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = "Avg: ${String.format("%.1f", avgMood)}/10",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            // Simple bar chart
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                recentEntries.reversed().forEach { entry ->
                    val heightFraction = entry.mood / 10f
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(heightFraction)
                        ) {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp),
                                color = getMoodColor(entry.mood)
                            ) {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TypingStatsCard(typingData: List<com.moodsense.ai.data.local.entities.TypingStatsEntity>) {
    val avgWpm = typingData.map { it.wordsPerMinute }.average()
    val avgBackspace = typingData.map { it.backspaceFrequency }.average()
    val avgPause = typingData.map { it.averagePauseDuration }.average()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            StatCard(
                label = "Avg WPM",
                value = "${avgWpm.toInt()}",
                icon = "⚡",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Backspace\nRate",
                value = "${(avgBackspace * 100).toInt()}%",
                icon = "⌫",
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Avg Pause",
                value = "${avgPause.toLong() / 1000}s",
                icon = "⏸",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun InsightCard(insight: MoodInsight) {
    val backgroundColor = when (insight.type) {
        InsightType.EMOTION_PATTERN -> MaterialTheme.colorScheme.primaryContainer
        InsightType.TYPING_PATTERN -> MaterialTheme.colorScheme.secondaryContainer
        InsightType.TIME_PATTERN -> MaterialTheme.colorScheme.tertiaryContainer
        InsightType.MOOD_TREND -> MaterialTheme.colorScheme.surfaceVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(text = insight.emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = insight.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = insight.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun EmptyInsightsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "🌱", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Keep Using MoodSense!",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Insights will appear after you use emotion detection, sound monitoring, and journal entries for a few days.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

private fun getEmotionEmoji(emotion: String): String = when (emotion.uppercase()) {
    "HAPPY" -> "😊"
    "SAD" -> "😢"
    "ANGRY" -> "😠"
    "FEAR" -> "😨"
    "SURPRISE" -> "😲"
    "DISGUST" -> "🤢"
    else -> "😐"
}

private fun getEmotionColorForName(emotion: String): Color = when (emotion.uppercase()) {
    "HAPPY" -> Color(0xFFFFD700)
    "SAD" -> Color(0xFF4169E1)
    "ANGRY" -> Color(0xFFFF4500)
    "FEAR" -> Color(0xFF8B008B)
    "SURPRISE" -> Color(0xFFFF8C00)
    "DISGUST" -> Color(0xFF228B22)
    else -> Color(0xFF708090)
}

private fun getMoodColor(mood: Int): Color = when {
    mood >= 8 -> Color(0xFF4CAF50)
    mood >= 5 -> Color(0xFFFF9800)
    else -> Color(0xFFF44336)
}
