package com.moodsense.ai.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.moodsense.ai.domain.model.Emotion
import com.moodsense.ai.domain.model.MusicTrack
import com.moodsense.ai.ui.components.LoadingIndicator
import com.moodsense.ai.ui.components.MoodSenseTopBar
import com.moodsense.ai.viewmodel.MusicViewModel

/**
 * Music recommendation screen powered by iTunes Search API.
 * Free, no API key required — works out of the box.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicScreen(
    initialEmotion: Emotion = Emotion.NEUTRAL,
    onNavigateBack: () -> Unit,
    viewModel: MusicViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(initialEmotion) {
        viewModel.loadRecommendations(initialEmotion)
    }

    Scaffold(
        topBar = {
            MoodSenseTopBar(
                title = "Music for Your Mood",
                onNavigateBack = onNavigateBack
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Error card
            if (uiState.error != null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "⚠️ ${uiState.error}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            TextButton(onClick = { viewModel.clearError() }) {
                                Text("Dismiss")
                            }
                        }
                    }
                }
            }

            // Mood header card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = uiState.currentMood.emoji, fontSize = 40.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Music for ${uiState.currentMood.displayName} mood",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = if (uiState.isLoading) "Searching…"
                                else "${uiState.tracks.size} tracks found via iTunes",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                        IconButton(onClick = { viewModel.loadRecommendations(uiState.currentMood) }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                        }
                    }
                }
            }

            // Mood chips
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(Emotion.values().filter { it != Emotion.UNKNOWN }) { emotion ->
                        FilterChip(
                            selected = emotion == uiState.currentMood,
                            onClick  = { viewModel.loadRecommendations(emotion) },
                            label    = { Text("${emotion.emoji} ${emotion.displayName}") }
                        )
                    }
                }
            }

            // Loading
            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator(message = "Finding music for your mood…")
                    }
                }
            }

            // Track list
            items(uiState.tracks) { track ->
                TrackCard(track = track) { url ->
                    try {
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                    } catch (e: Exception) {
                        // ignore if no browser
                    }
                }
            }

            // Empty state
            if (!uiState.isLoading && uiState.tracks.isEmpty() && uiState.error == null) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎵", fontSize = 64.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No tracks found",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadRecommendations(uiState.currentMood) }) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Try Again")
                        }
                    }
                }
            }

            // Attribution
            if (uiState.tracks.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Powered by iTunes Search API",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrackCard(track: MusicTrack, onOpen: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        onClick  = { onOpen(track.trackUrl) }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Album art
            if (!track.albumArtUrl.isNullOrBlank()) {
                AsyncImage(
                    model              = track.albumArtUrl,
                    contentDescription = "Album art",
                    modifier           = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale       = ContentScale.Crop
                )
            } else {
                Surface(
                    modifier = Modifier.size(64.dp).clip(RoundedCornerShape(10.dp)),
                    color    = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("🎵", fontSize = 28.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text     = track.name,
                    style    = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text     = track.artistName,
                    style    = MaterialTheme.typography.bodySmall,
                    color    = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (track.albumName.isNotBlank()) {
                    Text(
                        text     = track.albumName,
                        style    = MaterialTheme.typography.labelSmall,
                        color    = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Open button — uses preview if available, else track page
            IconButton(onClick = {
                onOpen(track.previewUrl?.takeIf { it.isNotBlank() } ?: track.trackUrl)
            }) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Play preview",
                    tint = Color(0xFFFC3C44) // Apple Music red
                )
            }
        }
    }
}
