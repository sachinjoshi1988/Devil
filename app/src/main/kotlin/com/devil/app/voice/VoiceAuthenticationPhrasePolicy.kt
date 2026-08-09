package com.devil.app.voice

import java.util.Locale

/**
 * Stage 37 policy for recognizing the verbal authentication-request phrase
 * "Code Red".
 *
 * Recognition of Code Red does not authenticate the speaker.
 *
 * Code Red is therefore modeled only as a request signal that may later be
 * handed to a genuine authentication mechanism.
 *
 * Code Red != owner authentication.
 * Code Red != session establishment.
 * Code Red != Owner Mode.
 */
class VoiceAuthenticationPhrasePolicy {

    fun evaluate(
        transcript: String,
    ): VoiceAuthenticationPhraseResult {
        val normalized =
            transcript
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

        return if (normalized == "code red") {
            VoiceAuthenticationPhraseResult
                .codeRedRecognized()
        } else {
            VoiceAuthenticationPhraseResult
                .notRecognized()
        }
    }
}
