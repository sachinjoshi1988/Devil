package com.devil.app.voice

/**
 * Stable Stage 37 orchestration result for one recognized voice transcript.
 *
 * state preserves the resulting hands-free control state.
 *
 * action describes only the next bounded Android-side lifecycle action.
 *
 * spokenMessage contains presentation text that may be spoken exactly as
 * supplied. It is never a fabricated constitutional result.
 *
 * conversationTranscript is present only when an already authenticated
 * ACTIVE_SESSION permits ordinary recognized text to enter the existing
 * conversation submission path.
 *
 * AuthenticationRequested does not mean Authenticated.
 */
data class HandsFreeInteractionResult(
    val state: HandsFreeConversationState,
    val action: HandsFreeInteractionAction,
    val spokenMessage: String?,
    val conversationTranscript: String?,
)
