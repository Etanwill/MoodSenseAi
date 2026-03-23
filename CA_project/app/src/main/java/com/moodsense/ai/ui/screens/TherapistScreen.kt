package com.moodsense.ai.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moodsense.ai.viewmodel.ChatMessage
import com.moodsense.ai.viewmodel.TherapistUiState
import com.moodsense.ai.viewmodel.TherapistViewModel

@Composable
fun TherapistScreen(
    onNavigateBack: () -> Unit,
    viewModel: TherapistViewModel = hiltViewModel()
) {
    val uiState  by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    var inputText by remember { mutableStateOf("") }

    // Auto-scroll to bottom when new message arrives
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    // Start session automatically on open
    LaunchedEffect(Unit) {
        if (uiState.messages.isEmpty()) viewModel.startSession()
    }

    Scaffold(
        topBar = {
            TherapistTopBar(onNavigateBack = onNavigateBack, uiState = uiState)
        },
        bottomBar = {
            ChatInputBar(
                text          = inputText,
                onTextChange  = { inputText = it },
                onSend        = {
                    if (inputText.isNotBlank()) {
                        viewModel.sendMessage(inputText.trim())
                        inputText = ""
                    }
                },
                isLoading     = uiState.isTyping,
                enabled       = !uiState.isTyping && uiState.messages.isNotEmpty()
            )
        }
    ) { padding ->
        LazyColumn(
            state           = listState,
            modifier        = Modifier.fillMaxSize().padding(padding),
            contentPadding  = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Welcome header (only before first message)
            if (uiState.messages.isEmpty() && !uiState.isTyping && uiState.error == null) {
                item { WelcomeHeader() }
            }

            // Chat messages
            items(uiState.messages, key = { it.id }) { message ->
                AnimatedVisibility(
                    visible = true,
                    enter   = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
                ) {
                    MessageBubble(message = message)
                }
            }

            // Typing indicator
            if (uiState.isTyping) {
                item { TypingIndicator() }
            }

            // Error
            if (uiState.error != null) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors   = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text     = "⚠️ ${uiState.error}",
                                style    = MaterialTheme.typography.bodySmall,
                                color    = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(Modifier.height(8.dp))
                            TextButton(
                                onClick = {
                                    if (uiState.messages.isEmpty()) {
                                        viewModel.startSession()
                                    } else {
                                        // If we have messages, we probably failed during a sendMessage
                                        // For simplicity, we can just clear error and let user try again
                                        // or we could retry the last message if we stored it.
                                        // Here we just allow them to try sending again.
                                        viewModel.startSession() // Re-init if it was start session that failed
                                    }
                                },
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }

            // Quick reply suggestions
            if (!uiState.isTyping && uiState.suggestions.isNotEmpty()) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        uiState.suggestions.forEach { suggestion ->
                            SuggestionChip(
                                onClick = {
                                    viewModel.sendMessage(suggestion)
                                },
                                label   = { Text(suggestion, style = MaterialTheme.typography.bodySmall) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Top bar
// ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TherapistTopBar(
    onNavigateBack: () -> Unit,
    uiState: TherapistUiState
) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, "Back")
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier         = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(Color(0xFF6650A4), Color(0xFF9C27B0)))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🧠", fontSize = 18.sp)
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        "AI Mood Therapist",
                        style      = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        if (uiState.isTyping) "Thinking…" else "Powered by Gemini AI",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(Icons.Default.FavoriteBorder, "Wellness", tint = Color(0xFF9C27B0))
            }
        }
    )
}

// ─────────────────────────────────────────────────────────────
// Welcome header
// ─────────────────────────────────────────────────────────────

@Composable
private fun WelcomeHeader() {
    Column(
        modifier            = Modifier.fillMaxWidth().padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier         = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(Color(0xFF6650A4), Color(0xFF9C27B0)))
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("🧠", fontSize = 40.sp)
        }
        Spacer(Modifier.height(16.dp))
        Text(
            "Your Personal Mood Therapist",
            style      = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign  = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "I've analyzed your emotions, journal entries,\nand patterns from today. Let's talk.",
            style     = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color     = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        Spacer(Modifier.height(16.dp))
        CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
    }
}

// ─────────────────────────────────────────────────────────────
// Message bubble
// ─────────────────────────────────────────────────────────────

@Composable
private fun MessageBubble(message: ChatMessage) {
    val isAI = !message.isUser

    Row(
        modifier             = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isAI) Arrangement.Start else Arrangement.End,
        verticalAlignment    = Alignment.Bottom
    ) {
        if (isAI) {
            Box(
                modifier         = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(listOf(Color(0xFF6650A4), Color(0xFF9C27B0)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("🧠", fontSize = 14.sp)
            }
            Spacer(Modifier.width(8.dp))
        }

        Card(
            shape  = RoundedCornerShape(
                topStart    = if (isAI) 4.dp else 20.dp,
                topEnd      = 20.dp,
                bottomStart = 20.dp,
                bottomEnd   = if (isAI) 20.dp else 4.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (isAI)
                    MaterialTheme.colorScheme.surfaceVariant
                else
                    Color(0xFF6650A4)
            ),
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text     = message.text,
                modifier = Modifier.padding(12.dp, 10.dp),
                style    = MaterialTheme.typography.bodyMedium,
                color    = if (isAI)
                    MaterialTheme.colorScheme.onSurfaceVariant
                else
                    Color.White
            )
        }

        if (!isAI) {
            Spacer(Modifier.width(8.dp))
            Box(
                modifier         = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text("😊", fontSize = 14.sp)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Typing indicator
// ─────────────────────────────────────────────────────────────

@Composable
private fun TypingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")
    val dot1 by infiniteTransition.animateFloat(
        0.3f, 1f,
        infiniteRepeatable(tween(600), RepeatMode.Reverse), label = "d1"
    )
    val dot2 by infiniteTransition.animateFloat(
        0.3f, 1f,
        infiniteRepeatable(tween(600, 150), RepeatMode.Reverse), label = "d2"
    )
    val dot3 by infiniteTransition.animateFloat(
        0.3f, 1f,
        infiniteRepeatable(tween(600, 300), RepeatMode.Reverse), label = "d3"
    )

    Row(verticalAlignment = Alignment.Bottom) {
        Box(
            modifier         = Modifier.size(32.dp).clip(CircleShape)
                .background(Brush.linearGradient(listOf(Color(0xFF6650A4), Color(0xFF9C27B0)))),
            contentAlignment = Alignment.Center
        ) { Text("🧠", fontSize = 14.sp) }
        Spacer(Modifier.width(8.dp))
        Card(
            shape  = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp, 14.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(dot1, dot2, dot3).forEach { alpha ->
                    Box(
                        Modifier.size(8.dp).clip(CircleShape)
                            .background(
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha)
                            )
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────
// Chat input bar
// ─────────────────────────────────────────────────────────────

@Composable
private fun ChatInputBar(
    text:         String,
    onTextChange: (String) -> Unit,
    onSend:       () -> Unit,
    isLoading:    Boolean,
    enabled:      Boolean
) {
    Surface(
        tonalElevation = 3.dp,
        modifier       = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier          = Modifier.padding(12.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value         = text,
                onValueChange = onTextChange,
                placeholder   = { Text("Share how you're feeling…") },
                modifier      = Modifier.weight(1f),
                shape         = RoundedCornerShape(24.dp),
                maxLines      = 3,
                enabled       = enabled,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { onSend() })
            )
            Spacer(Modifier.width(8.dp))
            FilledIconButton(
                onClick  = onSend,
                enabled  = enabled && text.isNotBlank(),
                modifier = Modifier.size(48.dp),
                colors   = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color(0xFF6650A4)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier   = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color      = Color.White
                    )
                } else {
                    Icon(Icons.Default.Send, "Send", tint = Color.White)
                }
            }
        }
    }
}