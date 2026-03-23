package com.moodsense.ai.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.moodsense.ai.ml.RecognitionResult
import com.moodsense.ai.ml.SongRecognizer
import com.moodsense.ai.ui.components.MoodSenseTopBar
import com.moodsense.ai.viewmodel.SoundViewModel

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SoundMonitorScreen(
    onNavigateBack: () -> Unit,
    viewModel: SoundViewModel = hiltViewModel()
) {
    val micPermission     = rememberPermissionState(android.Manifest.permission.RECORD_AUDIO)
    val recognitionResult by viewModel.recognitionResult.collectAsStateWithLifecycle()
    val context           = LocalContext.current

    Scaffold(
        topBar = {
            MoodSenseTopBar(title = "Song Recognizer", onNavigateBack = onNavigateBack)
        }
    ) { padding ->
        if (!micPermission.status.isGranted) {
            SoundPermissionRequestView(
                onRequest   = { micPermission.launchPermissionRequest() }
            )
            return@Scaffold
        }

        LazyColumn(
            modifier            = Modifier.fillMaxSize().padding(padding),
            contentPadding      = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Text(
                    text  = when (recognitionResult) {
                        is RecognitionResult.Idle       -> "Tap the button to identify\na song playing around you"
                        is RecognitionResult.Recording  -> "Listening…\nHold your phone toward the music"
                        is RecognitionResult.Processing -> "Analyzing audio…"
                        is RecognitionResult.Found      -> "Song identified! 🎉"
                        is RecognitionResult.NotFound   -> "Couldn't identify the song"
                        is RecognitionResult.Error      -> "Something went wrong"
                    },
                    style     = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color     = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = if (recognitionResult is RecognitionResult.Idle) 0.6f else 1f
                    )
                )
            }

            item {
                ShazamButton(
                    state     = recognitionResult,
                    onTap     = {
                        if (recognitionResult is RecognitionResult.Found ||
                            recognitionResult is RecognitionResult.NotFound ||
                            recognitionResult is RecognitionResult.Error) {
                            viewModel.resetRecognition()
                        } else {
                            viewModel.identifySong()
                        }
                    }
                )
            }

            if (recognitionResult is RecognitionResult.Recording) {
                item {
                    RecordingProgressBar()
                }
            }

            if (recognitionResult is RecognitionResult.Processing) {
                item {
                    Row(
                        verticalAlignment    = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        Text("Matching against millions of songs…",
                            style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            if (recognitionResult is RecognitionResult.Found) {
                item {
                    SongResultCard(
                        result  = recognitionResult as RecognitionResult.Found,
                        onOpenYoutube = { vid ->
                            context.startActivity(
                                Intent(Intent.ACTION_VIEW,
                                    Uri.parse("https://www.youtube.com/watch?v=$vid"))
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                        },
                        onOpenSpotify = { id ->
                            try {
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW,
                                        Uri.parse("spotify:track:$id"))
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                )
                            } catch (e: Exception) {
                                context.startActivity(
                                    Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://open.spotify.com/track/$id"))
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                )
                            }
                        }
                    )
                }
            }

            if (recognitionResult is RecognitionResult.NotFound) {
                item {
                    NotFoundCard(
                        message = (recognitionResult as RecognitionResult.NotFound).message,
                        onRetry = { viewModel.identifySong() }
                    )
                }
            }

            if (recognitionResult is RecognitionResult.Error) {
                item {
                    ErrorInfoCard(
                        message = (recognitionResult as RecognitionResult.Error).message
                    )
                }
            }

            if (recognitionResult is RecognitionResult.Idle ||
                recognitionResult is RecognitionResult.Error) {
                item { SetupGuideCard() }
            }
        }
    }
}

@Composable
private fun ShazamButton(
    state: RecognitionResult,
    onTap: () -> Unit
) {
    val isListening = state is RecognitionResult.Recording || state is RecognitionResult.Processing
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    val ring1 by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.6f,
        animationSpec = infiniteRepeatable(
            tween(1200, easing = EaseOut), RepeatMode.Restart
        ), label = "r1"
    )
    val ring2 by infiniteTransition.animateFloat(
        initialValue = 1f, targetValue = 1.9f,
        animationSpec = infiniteRepeatable(
            tween(1200, 300, EaseOut), RepeatMode.Restart
        ), label = "r2"
    )
    val ring1Alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f, targetValue = 0f,
        animationSpec = infiniteRepeatable(
            tween(1200, easing = EaseOut), RepeatMode.Restart
        ), label = "a1"
    )
    val ring2Alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f, targetValue = 0f,
        animationSpec = infiniteRepeatable(
            tween(1200, 300, EaseOut), RepeatMode.Restart
        ), label = "a2"
    )

    val buttonColor = when (state) {
        is RecognitionResult.Found    -> Color(0xFF1DB954)
        is RecognitionResult.NotFound -> Color(0xFFFF9800)
        is RecognitionResult.Error    -> Color(0xFFE53935)
        else                          -> Color(0xFF1976D2)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier         = Modifier.size(200.dp)
    ) {
        if (isListening) {
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .scale(ring2)
                    .clip(CircleShape)
                    .background(buttonColor.copy(alpha = ring2Alpha))
            )
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .scale(ring1)
                    .clip(CircleShape)
                    .background(buttonColor.copy(alpha = ring1Alpha))
            )
        }

        Button(
            onClick = onTap,
            modifier = Modifier.size(130.dp),
            shape    = CircleShape,
            colors   = ButtonDefaults.buttonColors(containerColor = buttonColor),
            elevation = ButtonDefaults.buttonElevation(8.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = when (state) {
                        is RecognitionResult.Found    -> Icons.Default.Check
                        is RecognitionResult.NotFound -> Icons.Default.Refresh
                        is RecognitionResult.Error    -> Icons.Default.Refresh
                        else                          -> Icons.Default.GraphicEq
                    },
                    contentDescription = null,
                    modifier = Modifier.size(44.dp),
                    tint     = Color.White
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text  = when (state) {
                        is RecognitionResult.Recording  -> "Listening"
                        is RecognitionResult.Processing -> "Matching"
                        is RecognitionResult.Found      -> "Again"
                        else                            -> "Tap"
                    },
                    color      = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize   = 13.sp
                )
            }
        }
    }
}

@Composable
private fun RecordingProgressBar() {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(1f, animationSpec = tween(8000, easing = LinearEasing))
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Recording ${(progress.value * 8).toInt()}s / 8s",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(Modifier.height(6.dp))
        LinearProgressIndicator(
            progress            = progress.value,
            modifier            = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
            color               = Color(0xFF1976D2),
            trackColor          = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SongResultCard(
    result: RecognitionResult.Found,
    onOpenYoutube: (String) -> Unit,
    onOpenSpotify: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(20.dp),
        colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!result.albumArtUrl.isNullOrBlank()) {
                    AsyncImage(
                        model              = result.albumArtUrl,
                        contentDescription = "Album art",
                        modifier           = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp)),
                        contentScale       = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier          = Modifier.size(80.dp).clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(listOf(Color(0xFF1976D2), Color(0xFF42A5F5)))
                            ),
                        contentAlignment  = Alignment.Center
                    ) {
                        Text("🎵", fontSize = 36.sp)
                    }
                }

                Spacer(Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text       = result.title,
                        style      = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color      = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text  = result.artist,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(Modifier.height(14.dp))
            Divider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f))
            Spacer(Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                if (result.youtubeId != null) {
                    Button(
                        onClick = { onOpenYoutube(result.youtubeId) },
                        colors  = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF0000)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.PlayArrow, null, Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("YouTube")
                    }
                }
                if (result.spotifyTrackId != null) {
                    OutlinedButton(
                        onClick = { onOpenSpotify(result.spotifyTrackId) },
                        border  = BorderStroke(1.dp, Color(0xFF1DB954)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.MusicNote, null, Modifier.size(18.dp), tint = Color(0xFF1DB954))
                        Spacer(Modifier.width(6.dp))
                        Text("Spotify", color = Color(0xFF1DB954))
                    }
                }
            }
        }
    }
}

@Composable
private fun NotFoundCard(message: String, onRetry: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
    ) {
        Column(
            modifier              = Modifier.padding(20.dp),
            horizontalAlignment   = Alignment.CenterHorizontally
        ) {
            Text("🎵", fontSize = 40.sp)
            Spacer(Modifier.height(8.dp))
            Text(message, textAlign = TextAlign.Center, color = Color(0xFF795548))
            Spacer(Modifier.height(12.dp))
            Button(onClick = onRetry) {
                Icon(Icons.Default.Refresh, null)
                Spacer(Modifier.width(6.dp))
                Text("Try Again")
            }
        }
    }
}

@Composable
private fun ErrorInfoCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("⚠️ Error", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onErrorContainer)
            Spacer(Modifier.height(6.dp))
            Text(message, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onErrorContainer)
        }
    }
}

@Composable
private fun SetupGuideCard() {
    if (SongRecognizer.ACR_ACCESS_KEY != "YOUR_ACR_ACCESS_KEY") return
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🔑 Quick Setup Required", fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("To enable song recognition:\n1. console.acrcloud.com\n2. Add Key & Secret in SongRecognizer.kt", 
                style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun SoundPermissionRequestView(
    onRequest: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🎤", fontSize = 64.sp)
        Spacer(Modifier.height(16.dp))
        Text("Microphone Required", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("MoodSense needs mic access to listen and identify songs playing around you.", textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onRequest) { Text("Grant Permission") }
    }
}
