package com.devil.app.voice

/**
 * Describes one bounded Android voice-input result.
 *
 * RECOGNIZED means Android speech recognition produced one non-blank textual
 * transcript.
 *
 * NO_MATCH means the recognition attempt completed without a usable transcript.
 *
 * CANCELLED means the bounded recognition attempt was cancelled.
 *
 * FAILED means the Android voice-input mechanism failed operationally.
 *
 * None of these states authenticate the speaker, establish subject identity or
 * trust, grant Devil authorization, establish a session, permit execution, or
 * prove that recognized speech was semantically correct.
 */
enum class AndroidVoiceInputStatus {
    RECOGNIZED,
    NO_MATCH,
    CANCELLED,
    FAILED,
}
