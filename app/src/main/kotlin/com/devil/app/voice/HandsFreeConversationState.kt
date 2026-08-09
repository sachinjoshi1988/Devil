package com.devil.app.voice

/**
 * Stage 37 presentation/control state for one bounded hands-free interaction.
 *
 * IDLE
 *     No wake interaction is active.
 *
 * AWAITING_AUTHENTICATION_PHRASE
 *     A wake phrase established attention and Devil may request "Code Red".
 *
 * AUTHENTICATION_REQUESTED
 *     "Code Red" was recognized and a genuine authentication boundary is now
 *     required. This state is explicitly not authenticated.
 *
 * ACTIVE_SESSION
 *     Reserved for future use only after genuine authentication and session
 *     establishment evidence exists.
 *
 * No Stage 37 component may enter ACTIVE_SESSION merely because voice text was
 * recognized.
 */
enum class HandsFreeConversationState {
    IDLE,
    AWAITING_AUTHENTICATION_PHRASE,
    AUTHENTICATION_REQUESTED,
    ACTIVE_SESSION,
}
