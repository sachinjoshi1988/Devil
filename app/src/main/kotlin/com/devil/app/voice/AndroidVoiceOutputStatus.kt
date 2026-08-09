package com.devil.app.voice

/**
 * Describes one bounded Android voice-output result.
 *
 * SPOKEN means Android TextToSpeech accepted one non-blank supplied text
 * utterance for speech output.
 *
 * UNAVAILABLE means no usable Android TextToSpeech engine is currently
 * available or initialized.
 *
 * FAILED means the bounded Android voice-output mechanism failed operationally.
 *
 * CANCELLED means active speech output was deliberately stopped.
 *
 * None of these states establish semantic correctness, execution success,
 * verification, final Outcome, task completion, identity, trust, authorization,
 * or any new Devil response.
 */
enum class AndroidVoiceOutputStatus {
    SPOKEN,
    UNAVAILABLE,
    FAILED,
    CANCELLED,
}
