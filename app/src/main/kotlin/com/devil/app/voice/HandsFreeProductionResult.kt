package com.devil.app.voice

/**
 * Preserves one Stage 37 production hands-free orchestration result.
 *
 * runtimeTranscript may exist only when a genuine ACTIVE_SESSION has already
 * allowed ordinary recognized conversation input.
 *
 * authenticationResult preserves the real authentication handoff result and
 * must never be inferred from the phrase "Code Red".
 */
data class HandsFreeProductionResult(
    val state: HandsFreeConversationState,
    val action: HandsFreeProductionAction,
    val message: String?,
    val runtimeTranscript: String?,
    val authenticationResult: HandsFreeAuthenticationHandoffResult?,
)
