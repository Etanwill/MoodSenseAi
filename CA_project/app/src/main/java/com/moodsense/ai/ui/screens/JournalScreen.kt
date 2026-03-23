package com.moodsense.ai.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moodsense.ai.data.local.entities.MoodJournalEntity
import com.moodsense.ai.domain.model.MoodTag
import com.moodsense.ai.ui.components.ErrorCard
import com.moodsense.ai.ui.components.MoodSenseTopBar
import com.moodsense.ai.viewmodel.JournalViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Mood journal screen for writing entries and viewing history
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    onNavigateBack: () -> Unit,
    viewModel: JournalViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val entries by viewModel.entries.collectAsStateWithLifecycle()

    var noteText by remember { mutableStateOf("") }
    var selectedTags by remember { mutableStateOf(setOf<MoodTag>()) }
    var moodScore by remember { mutableStateOf(5) }
    var showEntryForm by remember { mutableStateOf(true) }

    // Handle save success
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            noteText = ""
            selectedTags = emptySet()
            moodScore = 5
            showEntryForm = false
            viewModel.resetSavedState()
        }
    }

    Scaffold(
        topBar = {
            MoodSenseTopBar(
                title = "Mood Journal",
                onNavigateBack = onNavigateBack,
                actions = {
                    IconButton(onClick = { showEntryForm = !showEntryForm }) {
                        Icon(
                            if (showEntryForm) Icons.Default.List else Icons.Default.Add,
                            contentDescription = "Toggle view"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (!showEntryForm) {
                FloatingActionButton(
                    onClick = { showEntryForm = true }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "New Entry")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Error
            if (uiState.error != null) {
                item {
                    ErrorCard(
                        message = uiState.error!!,
                        onDismiss = { viewModel.clearError() }
                    )
                }
            }

            // New Entry Form
            if (showEntryForm) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "✍️ New Journal Entry",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            // Text Input with typing tracking
                            OutlinedTextField(
                                value = noteText,
                                onValueChange = { newText ->
                                    val isBackspace = newText.length < noteText.length
                                    viewModel.onKeyPress(isBackspace)
                                    noteText = newText
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .onFocusChanged { focusState ->
                                        if (focusState.isFocused) viewModel.onTextFieldFocused()
                                    },
                                placeholder = { Text("How are you feeling today? What happened?") },
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Sentences
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )

                            // Mood Slider
                            Text(
                                text = "Mood Score: ${getMoodEmoji(moodScore)} $moodScore/10",
                                style = MaterialTheme.typography.labelLarge
                            )
                            Slider(
                                value = moodScore.toFloat(),
                                onValueChange = { moodScore = it.toInt() },
                                valueRange = 1f..10f,
                                steps = 8,
                                modifier = Modifier.fillMaxWidth()
                            )

                            // Tag chips
                            Text(
                                text = "How are you feeling?",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold
                            )

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(MoodTag.values()) { tag ->
                                    val isSelected = tag in selectedTags
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = {
                                            selectedTags = if (isSelected) {
                                                selectedTags - tag
                                            } else {
                                                selectedTags + tag
                                            }
                                        },
                                        label = { Text(tag.label) },
                                        leadingIcon = if (isSelected) {
                                            { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp)) }
                                        } else null
                                    )
                                }
                            }

                            // Save button
                            Button(
                                onClick = {
                                    viewModel.saveEntry(
                                        note = noteText,
                                        selectedTags = selectedTags.toList(),
                                        detectedEmotion = null,
                                        mood = moodScore
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                enabled = !uiState.isLoading && noteText.isNotBlank()
                            ) {
                                if (uiState.isLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                } else {
                                    Icon(Icons.Default.Save, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Save Entry", style = MaterialTheme.typography.labelLarge)
                                }
                            }
                        }
                    }
                }
            }

            // Past entries section
            if (entries.isNotEmpty()) {
                item {
                    Text(
                        text = "Past Entries",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(entries) { entry ->
                    JournalEntryCard(
                        entry = entry,
                        onDelete = { viewModel.deleteEntry(it) }
                    )
                }
            } else if (!showEntryForm) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "📔", fontSize = 64.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No journal entries yet",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "Tap + to write your first entry",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun JournalEntryCard(
    entry: MoodJournalEntity,
    onDelete: (MoodJournalEntity) -> Unit
) {
    val dateFormat = SimpleDateFormat("EEE, MMM d • h:mm a", Locale.getDefault())
    val dateStr = dateFormat.format(Date(entry.timestamp))

    var showDeleteDialog by remember { mutableStateOf(false) }

    // Parse tags from JSON
    val tags = try {
        com.google.gson.Gson().fromJson(entry.tags, Array<String>::class.java)?.toList() ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Entry?") },
            text = { Text("This journal entry will be permanently deleted.") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete(entry)
                    showDeleteDialog = false
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = getMoodEmoji(entry.mood), fontSize = 24.sp)
                    Column {
                        Text(
                            text = dateStr,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "Mood: ${entry.mood}/10",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                IconButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Text(
                text = entry.note,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3
            )

            if (tags.isNotEmpty()) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(tags) { tag ->
                        SuggestionChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = tag.lowercase().replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun getMoodEmoji(mood: Int): String = when {
    mood >= 9 -> "😁"
    mood >= 7 -> "😊"
    mood >= 5 -> "😐"
    mood >= 3 -> "😕"
    else -> "😢"
}
