package com.devil.app.voice

import java.util.Locale

/**
 * Stage 37 approved wake-phrase policy.
 *
 * Approved wake phrases are:
 *
 * - Devil
 * - Hey Devil
 * - Devil, are you there?
 *
 * Matching is deliberately narrow. Wake phrases establish attention only.
 *
 * Wake != authentication.
 * Wake != owner identity.
 * Wake != authorization.
 * Wake != execution authority.
 */
class WakePhrasePolicy {

    fun evaluate(
        transcript: String,
    ): WakePhraseMatchResult {
        val normalized =
            normalize(transcript)

        if (normalized.isEmpty()) {
            return WakePhraseMatchResult.notMatched()
        }

        return if (
            normalized == "devil" ||
            normalized == "hey devil" ||
            normalized == "devil are you there"
        ) {
            WakePhraseMatchResult.matched(
                normalizedTranscript = normalized,
            )
        } else {
            WakePhraseMatchResult.notMatched()
        }
    }

    private fun normalize(
        transcript: String,
    ): String {
        return transcript
            .trim()
            .lowercase(Locale.ROOT)
            .replace(
                Regex("[^a-z0-9]+"),
                " ",
            )
            .trim()
            .replace(
                Regex("\\s+"),
                " ",
            )
    }
}
