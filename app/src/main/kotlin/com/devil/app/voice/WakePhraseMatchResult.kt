package com.devil.app.voice

/**
 * Preserves the bounded result of Stage 37 wake-phrase evaluation.
 *
 * A matched phrase preserves the normalized recognized text that established
 * attention.
 *
 * This result is not authentication evidence.
 */
@ConsistentCopyVisibility
data class WakePhraseMatchResult private constructor(
    val status: WakePhraseMatchStatus,
    val normalizedTranscript: String?,
) {
    companion object {

        fun matched(
            normalizedTranscript: String,
        ): WakePhraseMatchResult {
            val preservedTranscript =
                normalizedTranscript.trim()

            require(preservedTranscript.isNotEmpty()) {
                "Matched wake transcript must not be blank."
            }

            return WakePhraseMatchResult(
                status = WakePhraseMatchStatus.MATCHED,
                normalizedTranscript = preservedTranscript,
            )
        }

        fun notMatched(): WakePhraseMatchResult {
            return WakePhraseMatchResult(
                status = WakePhraseMatchStatus.NOT_MATCHED,
                normalizedTranscript = null,
            )
        }
    }
}
