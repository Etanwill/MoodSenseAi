package com.moodsense.ai.util

import com.moodsense.ai.data.local.entities.EmotionHistoryEntity
import com.moodsense.ai.data.local.entities.MoodJournalEntity
import com.moodsense.ai.data.local.entities.TypingStatsEntity
import com.moodsense.ai.domain.model.InsightType
import com.moodsense.ai.domain.model.MoodInsight
import java.util.Calendar

/**
 * Analyzes stored mood data to generate insights and patterns.
 * All processing is done on-device.
 */
object InsightsAnalyzer {

    /**
     * Generate insights from all available data
     */
    fun generateInsights(
        emotions: List<EmotionHistoryEntity>,
        journals: List<MoodJournalEntity>,
        typingStats: List<TypingStatsEntity>
    ): List<MoodInsight> {
        val insights = mutableListOf<MoodInsight>()

        insights.addAll(analyzeEmotionPatterns(emotions))
        insights.addAll(analyzeTypingPatterns(typingStats))
        insights.addAll(analyzeTimePatterns(emotions, journals))
        insights.addAll(analyzeMoodTrends(journals))

        return insights.take(8) // Return top 8 insights
    }

    /**
     * Analyze which emotions appear most frequently
     */
    private fun analyzeEmotionPatterns(emotions: List<EmotionHistoryEntity>): List<MoodInsight> {
        if (emotions.size < 5) return emptyList()

        val insights = mutableListOf<MoodInsight>()
        val emotionCounts = emotions.groupBy { it.emotion }
            .mapValues { it.value.size }
            .toList()
            .sortedByDescending { it.second }

        val total = emotions.size.toFloat()
        val topEmotion = emotionCounts.firstOrNull()

        if (topEmotion != null && topEmotion.second / total > 0.4f) {
            val percentage = (topEmotion.second / total * 100).toInt()
            insights.add(
                MoodInsight(
                    title = "Dominant Emotion",
                    description = "You appear ${topEmotion.first.lowercase()} in ${percentage}% of detections",
                    emoji = getEmotionEmoji(topEmotion.first),
                    type = InsightType.EMOTION_PATTERN
                )
            )
        }

        return insights
    }

    /**
     * Analyze typing behavior patterns
     */
    private fun analyzeTypingPatterns(stats: List<TypingStatsEntity>): List<MoodInsight> {
        if (stats.size < 3) return emptyList()

        val insights = mutableListOf<MoodInsight>()
        val avgWpm = stats.map { it.wordsPerMinute }.average()
        val avgBackspace = stats.map { it.backspaceFrequency }.average()

        // Detect high backspace rate (could indicate uncertainty/stress)
        if (avgBackspace > 0.25) {
            insights.add(
                MoodInsight(
                    title = "Frequent Corrections",
                    description = "You delete about ${(avgBackspace * 100).toInt()}% of your keystrokes — you may feel uncertain or stressed when writing",
                    emoji = "⌫",
                    type = InsightType.TYPING_PATTERN
                )
            )
        }

        // Detect slow typing (could indicate tiredness)
        if (avgWpm < 25 && stats.size >= 5) {
            insights.add(
                MoodInsight(
                    title = "Slow Typing Pattern",
                    description = "Your average typing speed is ${avgWpm.toInt()} WPM — this may indicate fatigue or low energy",
                    emoji = "🐢",
                    type = InsightType.TYPING_PATTERN
                )
            )
        } else if (avgWpm > 60) {
            insights.add(
                MoodInsight(
                    title = "Fast Typist",
                    description = "You type at ${avgWpm.toInt()} WPM on average — this suggests high energy and focus",
                    emoji = "⚡",
                    type = InsightType.TYPING_PATTERN
                )
            )
        }

        return insights
    }

    /**
     * Analyze time-based patterns in emotions and journals
     */
    private fun analyzeTimePatterns(
        emotions: List<EmotionHistoryEntity>,
        journals: List<MoodJournalEntity>
    ): List<MoodInsight> {
        if (emotions.size < 10) return emptyList()

        val insights = mutableListOf<MoodInsight>()

        // Group emotions by hour of day
        val byHour = emotions.groupBy { entity ->
            val cal = Calendar.getInstance()
            cal.timeInMillis = entity.timestamp
            cal.get(Calendar.HOUR_OF_DAY)
        }

        // Find hours with most negative emotions
        val negativeEmotions = setOf("ANGRY", "SAD", "FEAR", "DISGUST")
        val negativeByHour = byHour.mapValues { (_, entities) ->
            entities.count { it.emotion in negativeEmotions }.toFloat() / entities.size
        }

        val peakNegativeHour = negativeByHour.maxByOrNull { it.value }
        if (peakNegativeHour != null && peakNegativeHour.value > 0.6f) {
            val timeLabel = formatHour(peakNegativeHour.key)
            insights.add(
                MoodInsight(
                    title = "Challenging Time",
                    description = "You tend to feel more negative emotions around $timeLabel",
                    emoji = "⏰",
                    type = InsightType.TIME_PATTERN
                )
            )
        }

        // Find day of week patterns
        val byDayOfWeek = emotions.groupBy { entity ->
            val cal = Calendar.getInstance()
            cal.timeInMillis = entity.timestamp
            cal.get(Calendar.DAY_OF_WEEK)
        }

        val happyEmotions = setOf("HAPPY", "SURPRISE")
        val happyByDay = byDayOfWeek.mapValues { (_, entities) ->
            entities.count { it.emotion in happyEmotions }.toFloat() / entities.size
        }

        val bestDay = happyByDay.maxByOrNull { it.value }
        if (bestDay != null && bestDay.value > 0.5f) {
            val dayName = getDayName(bestDay.key)
            insights.add(
                MoodInsight(
                    title = "Your Happy Day",
                    description = "You tend to feel happiest on $dayName",
                    emoji = "🌟",
                    type = InsightType.TIME_PATTERN
                )
            )
        }

        return insights
    }

    /**
     * Analyze mood trends over time from journal entries
     */
    private fun analyzeMoodTrends(journals: List<MoodJournalEntity>): List<MoodInsight> {
        if (journals.size < 5) return emptyList()

        val insights = mutableListOf<MoodInsight>()
        val sortedJournals = journals.sortedBy { it.timestamp }
        val recentMoods = sortedJournals.takeLast(7).map { it.mood }

        if (recentMoods.size >= 3) {
            val firstHalf = recentMoods.take(recentMoods.size / 2).average()
            val secondHalf = recentMoods.drop(recentMoods.size / 2).average()

            when {
                secondHalf - firstHalf > 1.5 -> {
                    insights.add(
                        MoodInsight(
                            title = "Improving Mood! 📈",
                            description = "Your mood has been trending upward recently",
                            emoji = "📈",
                            type = InsightType.MOOD_TREND
                        )
                    )
                }
                firstHalf - secondHalf > 1.5 -> {
                    insights.add(
                        MoodInsight(
                            title = "Mood Dip Detected",
                            description = "Your recent journal entries show a declining mood trend. Consider self-care.",
                            emoji = "📉",
                            type = InsightType.MOOD_TREND
                        )
                    )
                }
            }
        }

        // Check for common tags
        val allTags = journals.flatMap { journal ->
            try {
                com.google.gson.Gson().fromJson(
                    journal.tags,
                    Array<String>::class.java
                )?.toList() ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }

        val tagCounts = allTags.groupBy { it }.mapValues { it.value.size }
        val topTag = tagCounts.maxByOrNull { it.value }
        if (topTag != null && topTag.value >= 3) {
            insights.add(
                MoodInsight(
                    title = "Common Feeling",
                    description = "You've tagged yourself as '${topTag.key.lowercase()}' ${topTag.value} times recently",
                    emoji = "🏷️",
                    type = InsightType.MOOD_TREND
                )
            )
        }

        return insights
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

    private fun formatHour(hour: Int): String = when {
        hour == 0 -> "midnight"
        hour < 12 -> "${hour}AM"
        hour == 12 -> "noon"
        else -> "${hour - 12}PM"
    }

    private fun getDayName(dayOfWeek: Int): String = when (dayOfWeek) {
        Calendar.SUNDAY -> "Sunday"
        Calendar.MONDAY -> "Monday"
        Calendar.TUESDAY -> "Tuesday"
        Calendar.WEDNESDAY -> "Wednesday"
        Calendar.THURSDAY -> "Thursday"
        Calendar.FRIDAY -> "Friday"
        Calendar.SATURDAY -> "Saturday"
        else -> "Unknown"
    }
}
