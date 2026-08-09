package com.devil.app.voice

/**
 * Describes the next bounded Android presentation/lifecycle action produced by
 * Stage 37 hands-free orchestration.
 *
 * NONE
 *     No platform action should occur.
 *
 * SPEAK_AND_LISTEN
 *     Speak already-established presentation text and then begin another
 *     bounded recognition attempt.
 *
 * REQUEST_AUTHENTICATION
 *     Hand control to a genuine authentication boundary.
 *
 * SUBMIT_CONVERSATION
 *     Submit already-recognized ordinary conversation text through the existing
 *     unified conversation pipeline.
 *
 * These actions do not themselves authenticate a subject, create a session,
 * grant authorization, execute a capability, or establish success.
 */
enum class HandsFreeInteractionAction {
    NONE,
    SPEAK_AND_LISTEN,
    REQUEST_AUTHENTICATION,
    SUBMIT_CONVERSATION,
}
