package com.moodsense.ai.ui.screens

import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.moodsense.ai.domain.model.Emotion
import com.moodsense.ai.ui.components.EmotionBadge
import com.moodsense.ai.ui.components.MoodSenseTopBar
import com.moodsense.ai.ui.components.getEmotionColor
import com.moodsense.ai.viewmodel.EmotionViewModel
import java.util.concurrent.Executors

/**
 * Camera screen for real-time facial emotion detection
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EmotionCameraScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMusic: (Emotion) -> Unit,
    viewModel: EmotionViewModel = hiltViewModel()
) {
    val cameraPermission = rememberPermissionState(android.Manifest.permission.CAMERA)
    val emotionResult by viewModel.emotionResult.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            MoodSenseTopBar(
                title = "Emotion Detection",
                onNavigateBack = onNavigateBack
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (cameraPermission.status.isGranted) {
                // Camera Preview
                CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    onFrameAnalyzed = { imageProxy -> viewModel.analyzeFrame(imageProxy) }
                )

                // Overlay UI
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Face detection frame indicator
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(220.dp)
                                .border(
                                    width = 2.dp,
                                    color = if (emotionResult?.emotion != Emotion.UNKNOWN && emotionResult != null)
                                        getEmotionColor(emotionResult!!.emotion)
                                    else Color.White.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(20.dp)
                                )
                        ) {
                            // Corner indicators
                            CornerIndicators(
                                color = emotionResult?.let { getEmotionColor(it.emotion) }
                                    ?: Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }

                    // Emotion result at bottom
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (emotionResult != null && emotionResult!!.emotion != Emotion.UNKNOWN) {
                            EmotionBadge(
                                emotion = emotionResult!!.emotion,
                                confidence = emotionResult!!.confidence
                            )

                            Button(
                                onClick = { onNavigateToMusic(emotionResult!!.emotion) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Icon(Icons.Default.MusicNote, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Get Music for ${emotionResult!!.emotion.displayName} Mood")
                            }
                        } else {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.Black.copy(alpha = 0.5f)
                                )
                            ) {
                                Text(
                                    text = "Position your face in the frame",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }
            } else {
                // Permission not granted
                PermissionRequestView(
                    title = "Camera Permission Required",
                    description = "MoodSense needs camera access to detect your facial expressions and emotions.",
                    icon = "📸",
                    onRequest = { cameraPermission.launchPermissionRequest() }
                )
            }
        }
    }
}

@Composable
private fun CornerIndicators(color: Color) {
    val cornerSize = 20.dp
    val strokeWidth = 3.dp

    Box(modifier = Modifier.fillMaxSize()) {
        // Top-left
        Box(
            modifier = Modifier
                .size(cornerSize)
                .align(Alignment.TopStart)
                .padding(4.dp)
                .background(Color.Transparent)
        )
    }
}

/**
 * Camera preview composable using CameraX
 */
@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onFrameAnalyzed: (ImageProxy) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val previewView = remember { PreviewView(context) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        onFrameAnalyzed(imageProxy)
                    }
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_FRONT_CAMERA,
                    preview,
                    imageAnalyzer
                )
            } catch (e: Exception) {
                Log.e("CameraPreview", "Camera binding failed: ${e.message}")
            }
        }, ContextCompat.getMainExecutor(context))
    }

    AndroidView(
        factory = { previewView },
        modifier = modifier
    )
}

/**
 * Permission request placeholder screen
 */
@Composable
fun PermissionRequestView(
    title: String,
    description: String,
    icon: String,
    onRequest: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = icon, fontSize = 72.sp)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onRequest,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Grant Permission", style = MaterialTheme.typography.labelLarge)
        }
    }
}
