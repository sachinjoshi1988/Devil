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
 * Compose presentation surface for the unified Devil conversation pipeline.
 *
 * Stage 24 established typed conversation presentation.
 *
 * Stage 35 added bounded one-shot voice input.
 *
 * Stage 36 added bounded voice-output presentation.
 *
 * Stage 37 adds an explicit hands-free presentation control.
 *
 * The screen does not perform SpeechRecognizer work, TextToSpeech work,
 * authentication, session establishment, runtime submission, authorization,
 * capability execution, verification, or outcome establishment.
 *
 * Hands-free UI state is presentation/control state only.
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
    isVoiceSpeaking: Boolean = false,
    voiceOutputMessage: String? = null,
    onHandsFreeToggle: () -> Unit = {},
    handsFreeEnabled: Boolean = false,
    handsFreeMessage: String? = null,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            verticalArrangement =
                Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Devil",
                style =
                    MaterialTheme.typography.headlineMedium,
            )

            Text(
                text = "Conversation",
                style =
                    MaterialTheme.typography.titleMedium,
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
                modifier =
                    Modifier.fillMaxWidth(),
                enabled =
                    !state.isSubmitting &&
                        !isVoiceListening &&
                        !isVoiceSpeaking &&
                        !handsFreeEnabled,
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
                modifier =
                    Modifier.fillMaxWidth(),
                enabled =
                    !state.isSubmitting &&
                        !isVoiceListening &&
                        !isVoiceSpeaking &&
                        !handsFreeEnabled &&
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
                modifier =
                    Modifier.fillMaxWidth(),
                enabled =
                    voiceInputEnabled &&
                        !state.isSubmitting &&
                        !isVoiceListening &&
                        !isVoiceSpeaking &&
                        !handsFreeEnabled,
            ) {
                Text(
                    text =
                        if (
                            isVoiceListening &&
                            !handsFreeEnabled
                        ) {
                            "Listening"
                        } else {
                            "Speak"
                        },
                )
            }

            Button(
                onClick = onHandsFreeToggle,
                modifier =
                    Modifier.fillMaxWidth(),
                enabled =
                    !state.isSubmitting &&
                        !isVoiceSpeaking &&
                        (
                            !isVoiceListening ||
                                handsFreeEnabled
                        ),
            ) {
                Text(
                    text =
                        if (handsFreeEnabled) {
                            "Stop Hands-Free"
                        } else {
                            "Hands-Free"
                        },
                )
            }

            if (handsFreeEnabled) {
                Text(
                    text = "Hands-Free active",
                    style =
                        MaterialTheme.typography.bodySmall,
                )
            }

            handsFreeMessage?.let { message ->
                Text(
                    text = message,
                    style =
                        MaterialTheme.typography.bodySmall,
                )
            }

            if (isVoiceSpeaking) {
                Text(
                    text = "Speaking",
                    style =
                        MaterialTheme.typography.bodySmall,
                )
            }

            voiceOutputMessage?.let { message ->
                Text(
                    text = message,
                    style =
                        MaterialTheme.typography.bodySmall,
                )
            }

            voiceInputMessage?.let { message ->
                Text(
                    text = message,
                    style =
                        MaterialTheme.typography.bodySmall,
                )
            }

            state.submissionNotice?.let { notice ->
                Text(
                    text = notice.message,
                    style =
                        MaterialTheme.typography.bodySmall,
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
                contentDescription =
                    "Conversation timeline"
            },
        verticalArrangement =
            Arrangement.spacedBy(12.dp),
    ) {
        if (entries.isEmpty()) {
            item {
                Text(
                    text =
                        "No conversation entries yet.",
                    style =
                        MaterialTheme.typography.bodyMedium,
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
        modifier =
            Modifier.fillMaxWidth(),
        verticalArrangement =
            Arrangement.spacedBy(4.dp),
    ) {
        Row(
            modifier =
                Modifier.fillMaxWidth(),
        ) {
            Text(
                text =
                    when (entry.role) {
                        ConversationEntryRole.USER ->
                            "You"

                        ConversationEntryRole.RUNTIME ->
                            "Runtime"
                    },
                style =
                    MaterialTheme.typography.labelMedium,
            )
        }

        Text(
            text = entry.content,
            style =
                MaterialTheme.typography.bodyLarge,
        )
    }
}
