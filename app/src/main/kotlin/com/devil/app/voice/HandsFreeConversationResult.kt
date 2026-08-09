package com.devil.app.voice

/**
 * Preserves one bounded Stage 37 hands-free state transition.
 *
 * message is presentation text only.
 *
 * runtimeTranscript is populated only when an already-authenticated future
 * ACTIVE_SESSION permits ordinary recognized conversation text to proceed.
 *
 * Stage 37 does not itself establish ACTIVE_SESSION.
 */
data class HandsFreeConversationResult(
    val state: HandsFreeConversationState,
    val status: HandsFreeConversationResultStatus,
    val message: String?,
    val runtimeTranscript: String?,
)
