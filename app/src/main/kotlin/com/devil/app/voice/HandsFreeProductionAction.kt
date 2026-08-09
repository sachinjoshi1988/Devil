package com.devil.app.voice

/**
 * Stage 37 Android orchestration action resulting from one recognized
 * hands-free transcript.
 *
 * NONE
 *     No Android follow-up is required.
 *
 * LISTEN
 *     Continue bounded wake listening.
 *
 * SPEAK_AND_LISTEN
 *     Speak the supplied presentation message, then resume bounded listening.
 *
 * AUTHENTICATION_HANDOFF
 *     A Code Red request reached the genuine authentication handoff boundary.
 *
 * SUBMIT_CONVERSATION
 *     Reserved for text from a genuinely established ACTIVE_SESSION.
 *
 * None of these actions grant constitutional authority.
 */
enum class HandsFreeProductionAction {
    NONE,
    LISTEN,
    SPEAK_AND_LISTEN,
    AUTHENTICATION_HANDOFF,
    SUBMIT_CONVERSATION,
}
