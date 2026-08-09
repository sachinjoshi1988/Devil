package com.devil.app.voice

import com.devil.app.conversation.ConversationUiState

/**
 * Preserves one bounded Stage 35 UI result after Android voice-input handling.
 *
 * state is the resulting conversation presentation state.
 *
 * message is optional voice-input presentation information only. It is not a
 * RuntimeResult, constitutional decision, execution result, verification result,
 * final Outcome, or completion claim.
 */
data class VoiceConversationHandlingResult(
    val state: ConversationUiState,
    val message: String?,
)
