package com.devil.app.conversation

import com.devil.app.ui.accessibility.devilInclusiveHeading
import com.devil.app.ui.accessibility.devilInclusiveInteractiveTarget

import com.devil.app.ui.adaptive.DevilAdaptiveContainer
import com.devil.app.ui.adaptive.LocalDevilAdaptivePresentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.devil.app.R
import com.devil.app.ui.voice.DevilVoiceInterface

/**
 * Stage 253 Main Conversation Experience.
 *
 * This screen is a futuristic Devil presentation surface around the already
 * established unified conversation pipeline.
 *
 * Stage 253 changes presentation only.
 *
 * It preserves:
 *
 * - existing ConversationUiState;
 * - exact ConversationTimelineEntry content and role;
 * - typed submission callbacks;
 * - voice-input callbacks and state;
 * - voice-output presentation state;
 * - hands-free callbacks and state;
 * - truthful submission notices;
 * - accessibility semantics and live-region announcements.
 *
 * It does not:
 *
 * - generate conversation meaning;
 * - fabricate timestamps, delivery state, authentication, trust, Owner Mode,
 *   authorization, execution, observation, verification, or Outcome;
 * - modify runtime submission;
 * - create Memory;
 * - implement Stage 254 Voice Interface.
 *
 * CONVERSATION_PRESENTATION != RUNTIME_TRUTH.
 * CONVERSATION_PRESENTATION != AUTHENTICATION.
 * CONVERSATION_PRESENTATION != AUTHORIZATION.
 * CONVERSATION_PRESENTATION != EXECUTION.
 * CONVERSATION_PRESENTATION != VERIFICATION.
 * CONVERSATION_PRESENTATION != MEMORY.
 */
@Composable
fun ConversationScreen(
    state: ConversationUiState,
    onDraftChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    onMemoryOpen: () -> Unit = {},
    onTaskOpen: () -> Unit = {},
    onEducationOpen: () -> Unit = {},
    onResearchOpen: () -> Unit = {},
    onFinanceOpen: () -> Unit = {},
    onSecurityOpen: () -> Unit = {},
    onSettingsOpen: () -> Unit = {},
    onVoiceInput: () -> Unit = {},
    isVoiceListening: Boolean = false,
    voiceInputEnabled: Boolean = true,
    voiceInputMessage: String? = null,
    isVoiceSpeaking: Boolean = false,
    voiceOutputMessage: String? = null,
    onHandsFreeToggle: () -> Unit = {},
    handsFreeEnabled: Boolean = false,
    handsFreeMessage: String? = null,
    accessibilityDiagnosticMessage: String? = null,
) {
    val background =
        MaterialTheme.colorScheme.background

    val devilRed =
        MaterialTheme.colorScheme.primary

    DevilAdaptiveContainer {
        Surface(
            modifier =
                modifier.fillMaxSize(),
            color =
                background,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(background),
            ) {
                DevilConversationHeader(
                    devilRed = devilRed,
                    onMemoryOpen = onMemoryOpen,
                    onTaskOpen = onTaskOpen,
                    onEducationOpen = onEducationOpen,
                    onResearchOpen = onResearchOpen,
                    onFinanceOpen = onFinanceOpen,
                    onSecurityOpen = onSecurityOpen,
                    onSettingsOpen = onSettingsOpen,
                    taskNavigationEnabled =
                        state.isSubmitting.not() &&
                            isVoiceListening.not() &&
                            isVoiceSpeaking.not() &&
                            handsFreeEnabled.not(),
                    memoryNavigationEnabled =
                        state.isSubmitting.not() &&
                            isVoiceListening.not() &&
                            isVoiceSpeaking.not() &&
                            handsFreeEnabled.not(),
                    educationNavigationEnabled =
                        state.isSubmitting.not() &&
                            isVoiceListening.not() &&
                            isVoiceSpeaking.not() &&
                            handsFreeEnabled.not(),
                    settingsNavigationEnabled =
                        state.isSubmitting.not() &&
                            isVoiceListening.not() &&
                            isVoiceSpeaking.not() &&
                            handsFreeEnabled.not(),
                    securityNavigationEnabled =
                        state.isSubmitting.not() &&
                            isVoiceListening.not() &&
                            isVoiceSpeaking.not() &&
                            handsFreeEnabled.not(),
                    financeNavigationEnabled =
                        state.isSubmitting.not() &&
                            isVoiceListening.not() &&
                            isVoiceSpeaking.not() &&
                            handsFreeEnabled.not(),
                    researchNavigationEnabled =
                        state.isSubmitting.not() &&
                            isVoiceListening.not() &&
                            isVoiceSpeaking.not() &&
                            handsFreeEnabled.not(),
                )

                ConversationStatusDeck(
                    handsFreeEnabled = handsFreeEnabled,
                    handsFreeMessage = handsFreeMessage,
                    isVoiceSpeaking = isVoiceSpeaking,
                    voiceOutputMessage = voiceOutputMessage,
                    voiceInputMessage = voiceInputMessage,
                    accessibilityDiagnosticMessage =
                        accessibilityDiagnosticMessage,
                    submissionNotice =
                        state.submissionNotice?.message,
                )

                ConversationTimeline(
                    entries = state.entries,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .weight(1f),
                )

                if (
                    isVoiceListening ||
                    isVoiceSpeaking ||
                    handsFreeEnabled
                ) {
                    DevilVoiceInterface(
                        isVoiceListening = isVoiceListening,
                        isSubmitting = state.isSubmitting,
                        isVoiceSpeaking = isVoiceSpeaking,
                        voiceInputEnabled = voiceInputEnabled,
                        handsFreeEnabled = handsFreeEnabled,
                        onVoiceInput = onVoiceInput,
                        onHandsFreeToggle = onHandsFreeToggle,
                        voiceInputMessage = voiceInputMessage,
                        voiceOutputMessage = voiceOutputMessage,
                        handsFreeMessage = handsFreeMessage,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 12.dp,
                                    vertical = 4.dp,
                                ),
                    )
                }
                CompactConversationVoiceControls(
                    isVoiceListening = isVoiceListening,
                    isSubmitting = state.isSubmitting,
                    isVoiceSpeaking = isVoiceSpeaking,
                    voiceInputEnabled = voiceInputEnabled,
                    handsFreeEnabled = handsFreeEnabled,
                    onVoiceInput = onVoiceInput,
                    onHandsFreeToggle = onHandsFreeToggle,
                    devilRed = devilRed,
                )

                DevilConversationComposer(
                    draft = state.draft,
                    onDraftChange = onDraftChange,
                    onSubmit = onSubmit,
                    isSubmitting = state.isSubmitting,
                    isVoiceListening = isVoiceListening,
                    isVoiceSpeaking = isVoiceSpeaking,
                    handsFreeEnabled = handsFreeEnabled,
                    devilRed = devilRed,
                )
            }
        }
    }
}

/**
 * Compact Devil identity treatment for the main conversation surface.
 *
 * Stage 255 adds presentation-only navigation to the Memory Interface.
 * Stage 256 adds presentation-only navigation to the Task & Automation Interface.
 * Stage 257 adds presentation-only navigation to the Education Interface.
 * Stage 259 adds presentation-only navigation to the Research Interface.
 * Stage 260 adds presentation-only navigation to the Finance Interface.
 * Stage 261 adds presentation-only navigation to the Security Interface.
 * Stage 262 adds presentation-only navigation to the Settings / Privacy / Permissions Interface.
 *
 * MEMORY_NAVIGATION != MEMORY_RECALL.
 * MEMORY_NAVIGATION != MEMORY_DISCLOSURE.
 * MEMORY_NAVIGATION != MEMORY_AUTHORITY.
 *
 * TASK_NAVIGATION != TASK_CREATION.
 * TASK_NAVIGATION != EXECUTION.
 *
 * EDUCATION_NAVIGATION != EDUCATION_SESSION_CREATION.
 * EDUCATION_NAVIGATION != EDUCATION_DELIVERY.
 * EDUCATION_NAVIGATION != AUTHORIZATION.
 * EDUCATION_NAVIGATION != CONSTITUTIONAL_LEARNING.
 *
 * RESEARCH_NAVIGATION != RESEARCH_EXECUTION.
 * RESEARCH_NAVIGATION != RESEARCH_VERIFICATION.
 * RESEARCH_NAVIGATION != WORLD_MODEL_UPDATE.
 * RESEARCH_NAVIGATION != MEMORY.
 *
 * FINANCE_NAVIGATION != FINANCIAL_AUTHORITY.
 * FINANCE_NAVIGATION != ACCOUNT_ACCESS.
 * FINANCE_NAVIGATION != TRANSACTION.
 * FINANCE_NAVIGATION != EXECUTION.
 * FINANCE_NAVIGATION != FINANCIAL_VERIFICATION.
 *
 * SECURITY_NAVIGATION != SECURITY_AUTHORITY.
 * SECURITY_NAVIGATION != AUTHENTICATION.
 * SECURITY_NAVIGATION != AUTHORIZATION.
 * SECURITY_NAVIGATION != SECURITY_RESPONSE_EXECUTION.
 * SECURITY_NAVIGATION != CONSTITUTIONAL_VERIFICATION.
 *
 * SETTINGS_NAVIGATION != SETTINGS_CHANGE.
 * SETTINGS_NAVIGATION != ANDROID_PERMISSION_REQUEST.
 * SETTINGS_NAVIGATION != ANDROID_PERMISSION_GRANT.
 * SETTINGS_NAVIGATION != DEVIL_AUTHORIZATION.
 * SETTINGS_NAVIGATION != PRIVACY_DISCLOSURE.
 * SETTINGS_NAVIGATION != EXECUTION.
 */
@Composable
private fun DevilConversationHeader(
    devilRed: Color,
    onMemoryOpen: () -> Unit,
    onTaskOpen: () -> Unit,
    onEducationOpen: () -> Unit,
    onResearchOpen: () -> Unit,
    onFinanceOpen: () -> Unit,
    onSecurityOpen: () -> Unit,
    onSettingsOpen: () -> Unit,
    taskNavigationEnabled: Boolean,
    memoryNavigationEnabled: Boolean,
    educationNavigationEnabled: Boolean,
    settingsNavigationEnabled: Boolean,
    securityNavigationEnabled: Boolean,
    financeNavigationEnabled: Boolean,
    researchNavigationEnabled: Boolean,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surface,
                )
                .border(
                    width = 1.dp,
                    color = devilRed.copy(alpha = 0.35f),
                )
                .padding(
                    horizontal = 18.dp,
                    vertical = 12.dp,
                ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter =
                    painterResource(
                        id = R.drawable.devil_primary_logo,
                    ),
                contentDescription =
                    "Devil",
                modifier =
                    Modifier.size(42.dp),
            )

            Spacer(
                modifier =
                    Modifier.width(12.dp),
            )

            Column(
                modifier =
                    Modifier.weight(1f),
                verticalArrangement =
                    Arrangement.spacedBy(1.dp),
            ) {
                Text(
                    text = "DEVIL",
                    modifier =
                        Modifier.devilInclusiveHeading().semantics {
                            heading()
                        },
                    style =
                        MaterialTheme.typography.titleLarge,
                    color =
                        MaterialTheme.colorScheme.onBackground,
                    fontWeight =
                        FontWeight.Black,
                )

                Text(
                    text = "MAIN CONVERSATION",
                    style =
                        MaterialTheme.typography.labelMedium,
                    color =
                        devilRed,
                    maxLines = 1,
                )
            }
        }

        FlowRow(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
            horizontalArrangement =
                Arrangement.spacedBy(6.dp),
            verticalArrangement =
                Arrangement.spacedBy(6.dp),
        ) {
            Button(
                modifier = Modifier.devilInclusiveInteractiveTarget(),
                onClick = onMemoryOpen,
                enabled = memoryNavigationEnabled,
                shape =
                    RoundedCornerShape(16.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            devilRed.copy(alpha = 0.14f),
                        contentColor =
                            devilRed,
                    ),
                contentPadding =
                    PaddingValues(
                        horizontal = 12.dp,
                        vertical = 9.dp,
                    ),
            ) {
                Text(
                    text = "MEMORY",
                    style =
                        MaterialTheme.typography.labelMedium,
                    fontWeight =
                        FontWeight.Bold,
                    maxLines = 1,
                )
            }

            Button(
                modifier = Modifier.devilInclusiveInteractiveTarget(),
                onClick = onTaskOpen,
                enabled = taskNavigationEnabled,
                shape =
                    RoundedCornerShape(16.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            devilRed.copy(alpha = 0.14f),
                        contentColor =
                            devilRed,
                    ),
                contentPadding =
                    PaddingValues(
                        horizontal = 12.dp,
                        vertical = 9.dp,
                    ),
            ) {
                Text(
                    text = "TASKS",
                    style =
                        MaterialTheme.typography.labelMedium,
                    fontWeight =
                        FontWeight.Bold,
                    maxLines = 1,
                )
            }

            Button(
                modifier = Modifier.devilInclusiveInteractiveTarget(),
                onClick = onEducationOpen,
                enabled = educationNavigationEnabled,
                shape =
                    RoundedCornerShape(16.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            devilRed.copy(alpha = 0.14f),
                        contentColor =
                            devilRed,
                    ),
                contentPadding =
                    PaddingValues(
                        horizontal = 12.dp,
                        vertical = 9.dp,
                    ),
            ) {
                Text(
                    text = "LEARN",
                    style =
                        MaterialTheme.typography.labelMedium,
                    fontWeight =
                        FontWeight.Bold,
                    maxLines = 1,
                )
            }

            Button(
                modifier = Modifier.devilInclusiveInteractiveTarget(),
                onClick = onResearchOpen,
                enabled = researchNavigationEnabled,
                shape =
                    RoundedCornerShape(16.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            devilRed.copy(alpha = 0.14f),
                        contentColor =
                            devilRed,
                    ),
                contentPadding =
                    PaddingValues(
                        horizontal = 12.dp,
                        vertical = 9.dp,
                    ),
            ) {
                Text(
                    text = "RESEARCH",
                    style =
                        MaterialTheme.typography.labelMedium,
                    fontWeight =
                        FontWeight.Bold,
                    maxLines = 1,
                )
            }

            Button(
                modifier = Modifier.devilInclusiveInteractiveTarget(),
                onClick = onFinanceOpen,
                enabled = financeNavigationEnabled,
                shape =
                    RoundedCornerShape(16.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            devilRed.copy(alpha = 0.14f),
                        contentColor =
                            devilRed,
                    ),
                contentPadding =
                    PaddingValues(
                        horizontal = 12.dp,
                        vertical = 9.dp,
                    ),
            ) {
                Text(
                    text = "FINANCE",
                    style =
                        MaterialTheme.typography.labelMedium,
                    fontWeight =
                        FontWeight.Bold,
                    maxLines = 1,
                )
            }

            Button(
                modifier = Modifier.devilInclusiveInteractiveTarget(),
                onClick = onSecurityOpen,
                enabled = securityNavigationEnabled,
                shape =
                    RoundedCornerShape(16.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            devilRed.copy(alpha = 0.14f),
                        contentColor =
                            devilRed,
                    ),
                contentPadding =
                    PaddingValues(
                        horizontal = 12.dp,
                        vertical = 9.dp,
                    ),
            ) {
                Text(
                    text = "SECURITY",
                    style =
                        MaterialTheme.typography.labelMedium,
                    fontWeight =
                        FontWeight.Bold,
                    maxLines = 1,
                )
            }

            Button(
                modifier = Modifier.devilInclusiveInteractiveTarget(),
                onClick = onSettingsOpen,
                enabled = settingsNavigationEnabled,
                shape =
                    RoundedCornerShape(16.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            devilRed.copy(alpha = 0.14f),
                        contentColor =
                            devilRed,
                    ),
                contentPadding =
                    PaddingValues(
                        horizontal = 12.dp,
                        vertical = 9.dp,
                    ),
            ) {
                Text(
                    text = "SETTINGS",
                    style =
                        MaterialTheme.typography.labelMedium,
                    fontWeight =
                        FontWeight.Bold,
                    maxLines = 1,
                )
            }
        }
    }
}
/**
 * Truthful conversation timeline presented as asymmetric Devil / owner cards.
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
        contentPadding =
            PaddingValues(
                horizontal = 16.dp,
                vertical = 18.dp,
            ),
        verticalArrangement =
            Arrangement.spacedBy(14.dp),
    ) {
        if (entries.isEmpty()) {
            item {
                EmptyConversationState()
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

@Composable
private fun EmptyConversationState() {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 34.dp),
        contentAlignment =
            Alignment.Center,
    ) {
        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.spacedBy(8.dp),
        ) {
            Image(
                painter =
                    painterResource(
                        id = R.drawable.devil_primary_logo,
                    ),
                contentDescription = null,
                modifier =
                    Modifier.size(58.dp),
            )

            Text(
                text = "DEVIL IS LISTENING",
                style =
                    MaterialTheme.typography.titleMedium,
                color =
                    MaterialTheme.colorScheme.onBackground,
            )

            Text(
                text = "Start a conversation.",
                style =
                    MaterialTheme.typography.bodyMedium,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/**
 * Renders existing timeline truth without adding invented metadata.
 */
@Composable
private fun ConversationTimelineRow(
    entry: ConversationTimelineEntry,
) {
    when (entry.role) {
        ConversationEntryRole.USER ->
            OwnerConversationCard(
                entry = entry,
            )

        ConversationEntryRole.RUNTIME ->
            DevilConversationCard(
                entry = entry,
            )

        ConversationEntryRole.ASSISTANT ->
            DevilConversationCard(
                entry = entry,
            )

        ConversationEntryRole.OUTCOME ->
            DevilConversationCard(
                entry = entry,
            )
    }
}

@Composable
private fun OwnerConversationCard(
    entry: ConversationTimelineEntry,
) {
    Row(
        modifier =
            Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.End,
    ) {
        Column(
            modifier =
                Modifier
                    .widthIn(
                        max = LocalDevilAdaptivePresentation.current.conversationCardMaxWidth,
                    )
                    .clip(
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 5.dp,
                            bottomStart = 18.dp,
                            bottomEnd = 18.dp,
                        ),
                    )
                    .background(
                        MaterialTheme.colorScheme.primary,
                    )
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp,
                    ),
            verticalArrangement =
                Arrangement.spacedBy(5.dp),
        ) {
            Text(
                text = "YOU",
                style =
                    MaterialTheme.typography.labelMedium,
                color =
                    MaterialTheme.colorScheme.onPrimary.copy(
                        alpha = 0.76f,
                    ),
            )

            Text(
                text = entry.content,
                style =
                    MaterialTheme.typography.bodyLarge,
                color =
                    MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

@Composable
private fun DevilConversationCard(
    entry: ConversationTimelineEntry,
) {
    val devilRed =
        MaterialTheme.colorScheme.primary

    Row(
        modifier =
            Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.Start,
        verticalAlignment =
            Alignment.Top,
    ) {
        Box(
            modifier =
                Modifier
                    .size(34.dp)
                    .clip(
                        RoundedCornerShape(10.dp),
                    )
                    .background(
                        devilRed.copy(alpha = 0.10f),
                    )
                    .border(
                        width = 1.dp,
                        color = devilRed.copy(alpha = 0.48f),
                        shape = RoundedCornerShape(10.dp),
                    ),
            contentAlignment =
                Alignment.Center,
        ) {
            Image(
                painter =
                    painterResource(
                        id = R.drawable.devil_primary_logo,
                    ),
                contentDescription = null,
                modifier =
                    Modifier.size(26.dp),
            )
        }

        Spacer(
            modifier =
                Modifier.width(9.dp),
        )

        Column(
            modifier =
                Modifier
                    .widthIn(
                        max = LocalDevilAdaptivePresentation.current.conversationCardMaxWidth,
                    )
                    .clip(
                        RoundedCornerShape(
                            topStart = 5.dp,
                            topEnd = 18.dp,
                            bottomStart = 18.dp,
                            bottomEnd = 18.dp,
                        ),
                    )
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                    )
                    .border(
                        width = 1.dp,
                        color = devilRed.copy(alpha = 0.22f),
                        shape =
                            RoundedCornerShape(
                                topStart = 5.dp,
                                topEnd = 18.dp,
                                bottomStart = 18.dp,
                                bottomEnd = 18.dp,
                            ),
                    )
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp,
                    ),
            verticalArrangement =
                Arrangement.spacedBy(5.dp),
        ) {
            Text(
                text = "DEVIL",
                style =
                    MaterialTheme.typography.labelMedium,
                color =
                    devilRed,
            )

            Text(
                text = entry.content,
                style =
                    MaterialTheme.typography.bodyLarge,
                color =
                    MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

/**
 * Compact truthful presentation deck for existing changing UI state.
 */
@Composable
private fun ConversationStatusDeck(
    handsFreeEnabled: Boolean,
    handsFreeMessage: String?,
    isVoiceSpeaking: Boolean,
    voiceOutputMessage: String?,
    voiceInputMessage: String?,
    accessibilityDiagnosticMessage: String?,
    submissionNotice: String?,
) {
    val messages =
        buildList {
            if (handsFreeEnabled) {
                add("Hands-Free active")
            }

            handsFreeMessage?.let(::add)

            if (isVoiceSpeaking) {
                add("Speaking")
            }

            voiceOutputMessage?.let(::add)
            voiceInputMessage?.let(::add)
            accessibilityDiagnosticMessage?.let(::add)
            submissionNotice?.let(::add)
        }

    if (messages.isEmpty()) {
        return
    }

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 4.dp,
                )
                .clip(
                    RoundedCornerShape(14.dp),
                )
                .background(
                    MaterialTheme.colorScheme.surface,
                )
                .border(
                    width = 1.dp,
                    color =
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.22f,
                        ),
                    shape =
                        RoundedCornerShape(14.dp),
                )
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp,
                ),
        verticalArrangement =
            Arrangement.spacedBy(3.dp),
    ) {
        messages.forEach { message ->
            Text(
                text = message,
                modifier =
                    Modifier.politeAccessibilityStatus(),
                style =
                    MaterialTheme.typography.bodySmall,
                color =
                    MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/**
 * Compact daily-use voice controls for the main conversation surface.
 *
 * These controls reuse the existing voice callbacks and state.
 *
 * VOICE_CONTROL != AUTHENTICATION.
 * VOICE_CONTROL != AUTHORIZATION.
 * VOICE_CONTROL != EXECUTION.
 * VOICE_CONTROL != VERIFICATION.
 */
@Composable
private fun CompactConversationVoiceControls(
    isVoiceListening: Boolean,
    isSubmitting: Boolean,
    isVoiceSpeaking: Boolean,
    voiceInputEnabled: Boolean,
    handsFreeEnabled: Boolean,
    onVoiceInput: () -> Unit,
    onHandsFreeToggle: () -> Unit,
    devilRed: Color,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 4.dp,
                ),
        horizontalArrangement =
            Arrangement.spacedBy(8.dp),
    ) {
        Button(
            onClick = onVoiceInput,
            enabled =
                voiceInputEnabled &&
                    !isSubmitting &&
                    !isVoiceListening &&
                    !isVoiceSpeaking &&
                    !handsFreeEnabled,
            modifier =
                Modifier
                    .weight(1f)
                    .devilInclusiveInteractiveTarget(),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        devilRed.copy(alpha = 0.16f),
                    contentColor =
                        devilRed,
                ),
            shape =
                RoundedCornerShape(16.dp),
        ) {
            Text(
                text =
                    if (isVoiceListening && !handsFreeEnabled) {
                        "LISTENING"
                    } else {
                        "VOICE"
                    },
                fontWeight =
                    FontWeight.Bold,
            )
        }

        Button(
            onClick = onHandsFreeToggle,
            enabled =
                !isSubmitting &&
                    !isVoiceSpeaking &&
                    (!isVoiceListening || handsFreeEnabled),
            modifier =
                Modifier
                    .weight(1f)
                    .devilInclusiveInteractiveTarget(),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        devilRed.copy(alpha = 0.16f),
                    contentColor =
                        devilRed,
                ),
            shape =
                RoundedCornerShape(16.dp),
        ) {
            Text(
                text =
                    if (handsFreeEnabled) {
                        "STOP HANDS-FREE"
                    } else {
                        "HANDS-FREE"
                    },
                fontWeight =
                    FontWeight.Bold,
            )
        }
    }
}

/**
 * Stage 253 unified bottom composer.
 *
 * Existing callback semantics and enable/disable rules remain authoritative.
 */
@Composable
private fun DevilConversationComposer(
    draft: String,
    onDraftChange: (String) -> Unit,
    onSubmit: () -> Unit,
    isSubmitting: Boolean,
    isVoiceListening: Boolean,
    isVoiceSpeaking: Boolean,
    handsFreeEnabled: Boolean,
    devilRed: Color,
) {
    val canSubmit =
        !isSubmitting &&
            !isVoiceListening &&
            !isVoiceSpeaking &&
            !handsFreeEnabled &&
            draft.isNotBlank()

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surface,
                )
                .border(
                    width = 1.dp,
                    color = devilRed.copy(alpha = 0.30f),
                )
                .padding(12.dp),
        horizontalArrangement =
            Arrangement.spacedBy(9.dp),
        verticalAlignment =
            Alignment.Bottom,
    ) {
        OutlinedTextField(
            value = draft,
            onValueChange = onDraftChange,
            modifier =
                Modifier.weight(1f),
            enabled =
                !isSubmitting &&
                    !isVoiceListening &&
                    !isVoiceSpeaking &&
                    !handsFreeEnabled,
            placeholder = {
                Text(
                    text = "Message Devil…",
                )
            },
            minLines = 1,
            maxLines = 4,
            keyboardOptions =
                KeyboardOptions(
                    imeAction = ImeAction.Send,
                ),
            keyboardActions =
                KeyboardActions(
                    onSend = {
                        if (canSubmit) {
                            onSubmit()
                        }
                    },
                ),
            shape =
                RoundedCornerShape(18.dp),
            colors =
                OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = devilRed,
                    unfocusedBorderColor =
                        devilRed.copy(alpha = 0.36f),
                    focusedTextColor =
                        MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor =
                        MaterialTheme.colorScheme.onSurface,
                    cursorColor = devilRed,
                    focusedPlaceholderColor =
                        MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedPlaceholderColor =
                        MaterialTheme.colorScheme.onSurfaceVariant,
                ),
        )

        Button(
            modifier =
                Modifier.devilInclusiveInteractiveTarget(),
            onClick = onSubmit,
            enabled = canSubmit,
            shape =
                RoundedCornerShape(18.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = devilRed,
                    contentColor =
                        MaterialTheme.colorScheme.onPrimary,
                ),
            contentPadding =
                PaddingValues(
                    horizontal = 18.dp,
                    vertical = 16.dp,
                ),
        ) {
            Text(
                text =
                    if (isSubmitting) {
                        "..."
                    } else {
                        "SEND"
                    },
                fontWeight =
                    FontWeight.Bold,
            )
        }
    }
}


/**
 * Marks truthful changing presentation status as a polite accessibility live
 * region.
 *
 * Accessibility presentation does not alter Devil state or authority.
 */
private fun Modifier.politeAccessibilityStatus(): Modifier {
    return semantics {
        liveRegion =
            LiveRegionMode.Polite
    }
}
