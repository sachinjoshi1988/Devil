package com.devil.app.conversation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

/**
 * Compose conversation presentation surface for the unified Devil conversation
 * pipeline.
 *
 * Stage 24 established typed conversation presentation.
 *
 * Stage 35 adds one bounded voice-input affordance. The screen itself still does
 * not perform speech recognition, request Android permission, create
 * constitutional context, invoke UnifiedDevilRuntime, authenticate a speaker,
 * execute capabilities, or fabricate conversational success.
 */
@Composable
fun ConversationScreen(
    state: ConversationUiState,
    onDraftChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    onVoiceInput: () -> Unit = {},
    isVoiceListening: Boolean = false,
    voiceInputEnabled: Boolean = true,
    voiceInputMessage: String? = null,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Devil",
                style = MaterialTheme.typography.headlineMedium,
            )

            Text(
                text = "Conversation",
                style = MaterialTheme.typography.titleMedium,
            )

            HorizontalDivider()

            ConversationTimeline(
                entries = state.entries,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
            )

            OutlinedTextField(
                value = state.draft,
                onValueChange = onDraftChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSubmitting && !isVoiceListening,
                label = {
                    Text("Message")
                },
                placeholder = {
                    Text("Type a message")
                },
                minLines = 1,
                maxLines = 4,
            )

            Button(
                onClick = onSubmit,
                modifier = Modifier.fillMaxWidth(),
                enabled =
                    !state.isSubmitting &&
                        !isVoiceListening &&
                        state.draft.isNotBlank(),
            ) {
                Text(
                    text =
                        if (state.isSubmitting) {
                            "Submitting"
                        } else {
                            "Send"
                        },
                )
            }

            Button(
                onClick = onVoiceInput,
                modifier = Modifier.fillMaxWidth(),
                enabled =
                    voiceInputEnabled &&
                        !state.isSubmitting &&
                        !isVoiceListening,
            ) {
                Text(
                    text =
                        if (isVoiceListening) {
                            "Listening"
                        } else {
                            "Speak"
                        },
                )
            }

            voiceInputMessage?.let { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                )
            }

            state.submissionNotice?.let { notice ->
                Text(
                    text = notice.message,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

/**
 * Renders the current immutable conversation presentation timeline.
 */
@Composable
private fun ConversationTimeline(
    entries: List<ConversationTimelineEntry>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
            modifier.semantics {
                contentDescription = "Conversation timeline"
            },
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (entries.isEmpty()) {
            item {
                Text(
                    text = "No conversation entries yet.",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        } else {
            items(
                items = entries,
                key = { entry ->
                    entry.id.value
                },
            ) { entry ->
                ConversationTimelineRow(
                    entry = entry,
                )
            }
        }
    }
}

/**
 * Renders one presentation entry without reinterpreting its meaning.
 */
@Composable
private fun ConversationTimelineRow(
    entry: ConversationTimelineEntry,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text =
                    when (entry.role) {
                        ConversationEntryRole.USER -> "You"
                        ConversationEntryRole.RUNTIME -> "Runtime"
                    },
                style = MaterialTheme.typography.labelMedium,
            )
        }

        Text(
            text = entry.content,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
