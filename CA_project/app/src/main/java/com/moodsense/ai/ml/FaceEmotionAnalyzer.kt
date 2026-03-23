package com.moodsense.ai.ml

import android.graphics.PointF
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*
import com.moodsense.ai.domain.model.Emotion
import com.moodsense.ai.domain.model.EmotionResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs

/**
 * On-device face emotion analyzer using Google ML Kit.
 * Analyzes facial landmarks and expressions to detect emotions.
 * Runs completely offline.
 */
@Singleton
class FaceEmotionAnalyzer @Inject constructor() {

    private val faceDetector: FaceDetector by lazy {
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE) // Switched to ACCURATE
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setMinFaceSize(0.15f)
            .build()
        FaceDetection.getClient(options)
    }

    /**
     * Analyze an image and return detected emotion with confidence
     */
    suspend fun analyzeImage(image: InputImage): EmotionResult {
        return try {
            val faces = faceDetector.process(image).await()

            if (faces.isEmpty()) {
                return EmotionResult(Emotion.UNKNOWN, 0f)
            }

            // Use the largest face detected
            val face = faces.maxByOrNull { it.boundingBox.width() * it.boundingBox.height() }
                ?: return EmotionResult(Emotion.UNKNOWN, 0f)

            classifyEmotion(face)
        } catch (e: Exception) {
            Log.e("FaceEmotionAnalyzer", "Error analyzing face: ${e.message}")
            EmotionResult(Emotion.UNKNOWN, 0f)
        }
    }

    /**
     * Classify emotion from ML Kit face detection results.
     * Uses smile probability, eye open probability, and facial contours.
     */
    private fun classifyEmotion(face: Face): EmotionResult {
        val smileProbability = face.smilingProbability ?: 0f
        val leftEyeOpen = face.leftEyeOpenProbability ?: 0.5f
        val rightEyeOpen = face.rightEyeOpenProbability ?: 0.5f
        val avgEyeOpen = (leftEyeOpen + rightEyeOpen) / 2f

        // Analyze face contours for deeper emotion detection
        val browContour = analyzeBrowPosition(face)
        val mouthContour = analyzeMouthShape(face)

        return when {
            // HAPPY: High smile + open eyes
            smileProbability > 0.65f && avgEyeOpen > 0.55f -> {
                EmotionResult(Emotion.HAPPY, smileProbability)
            }

            // SURPRISE: Eyes wide open + raised brows + open mouth
            avgEyeOpen > 0.85f && mouthContour == MouthShape.OPEN && browContour == BrowPosition.RAISED -> {
                EmotionResult(Emotion.SURPRISE, avgEyeOpen * 0.8f)
            }

            // FEAR: Wide eyes + raised brows + no smile
            avgEyeOpen > 0.8f && smileProbability < 0.25f && browContour == BrowPosition.RAISED -> {
                EmotionResult(Emotion.FEAR, avgEyeOpen * 0.7f)
            }

            // ANGRY: Furrowed brows + low smile + narrowed eyes
            smileProbability < 0.2f && (browContour == BrowPosition.FURROWED || avgEyeOpen < 0.45f) -> {
                EmotionResult(Emotion.ANGRY, 0.75f)
            }

            // SAD: Downturned mouth + partially closed eyes + low smile
            smileProbability < 0.15f && (mouthContour == MouthShape.DOWNTURNED || browContour == BrowPosition.FURROWED) -> {
                EmotionResult(Emotion.SAD, 0.7f)
            }

            // DISGUST: Low smile + partially closed eyes + furrowed brow
            smileProbability < 0.15f && avgEyeOpen < 0.55f && browContour == BrowPosition.FURROWED -> {
                EmotionResult(Emotion.DISGUST, 0.7f)
            }

            // NEUTRAL: Mid-range values
            smileProbability in 0.15f..0.6f -> {
                EmotionResult(Emotion.NEUTRAL, 0.8f)
            }

            // Default NEUTRAL
            else -> {
                EmotionResult(Emotion.NEUTRAL, 0.6f)
            }
        }
    }

    /**
     * Analyze eyebrow position from face contours
     */
    private fun analyzeBrowPosition(face: Face): BrowPosition {
        val leftBrowTop = face.getContour(FaceContour.LEFT_EYEBROW_TOP)?.points
        val rightBrowTop = face.getContour(FaceContour.RIGHT_EYEBROW_TOP)?.points

        if (leftBrowTop == null || rightBrowTop == null) return BrowPosition.NEUTRAL

        val leftBrowCenter = leftBrowTop.getOrNull(leftBrowTop.size / 2)
        val rightBrowCenter = rightBrowTop.getOrNull(rightBrowTop.size / 2)
        val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)?.position
        val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)?.position

        if (leftBrowCenter == null || rightBrowCenter == null || leftEye == null || rightEye == null) {
            return BrowPosition.NEUTRAL
        }

        // Calculate distance from brow to eye - smaller means furrowed/lowered
        val leftDist = leftEye.y - leftBrowCenter.y
        val rightDist = rightEye.y - rightBrowCenter.y
        val avgDist = (leftDist + rightDist) / 2f

        return when {
            avgDist < 25f -> BrowPosition.FURROWED
            avgDist > 38f -> BrowPosition.RAISED
            else -> BrowPosition.NEUTRAL
        }
    }

    /**
     * Analyze mouth shape from face contours
     */
    private fun analyzeMouthShape(face: Face): MouthShape {
        val upperLip = face.getContour(FaceContour.UPPER_LIP_TOP)?.points
        val lowerLip = face.getContour(FaceContour.LOWER_LIP_BOTTOM)?.points

        if (upperLip == null || lowerLip == null) return MouthShape.NEUTRAL

        val upperCenter = upperLip.getOrNull(upperLip.size / 2)
        val lowerCenter = lowerLip.getOrNull(lowerLip.size / 2)

        if (upperCenter == null || lowerCenter == null) return MouthShape.NEUTRAL

        val mouthOpenness = lowerCenter.y - upperCenter.y

        // Check corners for upturned/downturned
        val mouthLeft = face.getLandmark(FaceLandmark.MOUTH_LEFT)?.position
        val mouthRight = face.getLandmark(FaceLandmark.MOUTH_RIGHT)?.position
        val mouthBottom = face.getLandmark(FaceLandmark.MOUTH_BOTTOM)?.position

        return when {
            mouthOpenness > 20f -> MouthShape.OPEN
            mouthBottom != null && mouthLeft != null && mouthRight != null -> {
                val cornerAvg = (mouthLeft.y + mouthRight.y) / 2f
                if (mouthBottom.y < cornerAvg) MouthShape.UPTURNED
                else MouthShape.DOWNTURNED
            }
            else -> MouthShape.NEUTRAL
        }
    }

    private enum class BrowPosition { RAISED, FURROWED, NEUTRAL }
    private enum class MouthShape { OPEN, UPTURNED, DOWNTURNED, NEUTRAL }

    fun close() {
        faceDetector.close()
    }
}
