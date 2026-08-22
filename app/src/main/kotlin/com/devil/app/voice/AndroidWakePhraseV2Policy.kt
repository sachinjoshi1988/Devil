package com.devil.app.voice

import java.util.Locale

/**
 * Stage 196 bounded Wake Phrase V2 policy.
 *
 * Canonical wake phrases:
 *
 * - Devil
 * - Hello Devil
 * - Devil, are you there?
 * - Devil, are you listening?
 *
 * A match establishes attention only.
 *
 * It does not:
 *
 * - recognize Code Red as a wake phrase;
 * - authenticate a speaker;
 * - verify owner voice;
 * - create ACTIVE_SESSION;
 * - grant authorization;
 * - execute commands;
 * - implement Stage 197 Speech Recognition.
 *
 * WAKE_MATCHED != AUTHENTICATED.
 * CODE_RED_RECOGNIZED != ACCESS_GRANTED.
 * ATTENTION_ESTABLISHED != ACTIVE_SESSION.
 */
class AndroidWakePhraseV2Policy {

    fun evaluate(
        transcript: String,
    ): AndroidWakePhraseV2Result {
        val normalized =
            normalize(transcript)

        return if (normalized in APPROVED_WAKE_PHRASES) {
            AndroidWakePhraseV2Result.create(
                status = AndroidWakePhraseV2Status.MATCHED,
                normalizedTranscript = normalized,
            )
        } else {
            AndroidWakePhraseV2Result.create(
                status = AndroidWakePhraseV2Status.NOT_MATCHED,
            )
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

    private companion object {
        val APPROVED_WAKE_PHRASES =
            setOf(
                "devil",
                "hello devil",
                "devil are you there",
                "devil are you listening",
            )
    }
}
