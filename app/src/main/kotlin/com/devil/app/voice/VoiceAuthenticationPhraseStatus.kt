package com.devil.app.voice

/**
 * Describes Stage 37 recognition of the bounded verbal authentication-request
 * phrase.
 *
 * CODE_RED_RECOGNIZED means only that the words "Code Red" were recognized.
 *
 * It does not mean authentication succeeded.
 */
enum class VoiceAuthenticationPhraseStatus {
    CODE_RED_RECOGNIZED,
    NOT_RECOGNIZED,
}
