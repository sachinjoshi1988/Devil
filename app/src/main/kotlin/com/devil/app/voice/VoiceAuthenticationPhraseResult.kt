package com.devil.app.voice

/**
 * Preserves recognition of the Stage 37 verbal authentication-request phrase.
 *
 * CODE_RED_RECOGNIZED is a request to enter the real authentication boundary
 * when such a production boundary exists.
 *
 * It is never proof of identity by itself.
 */
@ConsistentCopyVisibility
data class VoiceAuthenticationPhraseResult private constructor(
    val status: VoiceAuthenticationPhraseStatus,
) {
    companion object {

        fun codeRedRecognized(): VoiceAuthenticationPhraseResult {
            return VoiceAuthenticationPhraseResult(
                status =
                    VoiceAuthenticationPhraseStatus.CODE_RED_RECOGNIZED,
            )
        }

        fun notRecognized(): VoiceAuthenticationPhraseResult {
            return VoiceAuthenticationPhraseResult(
                status =
                    VoiceAuthenticationPhraseStatus.NOT_RECOGNIZED,
            )
        }
    }
}
