package com.devil.app.voice

/**
 * Stage 201 bounded Voice Activity & Noise result.
 *
 * The exact supplied Stage 201 evidence is preserved.
 *
 * Classification does not establish recognized speech, speaker identity,
 * authentication, intent, emotional tone, or verified conversational meaning.
 */
@ConsistentCopyVisibility
data class AndroidVoiceActivityResult private constructor(
    val classification: AndroidVoiceActivityClassification,
    val evidence: AndroidVoiceActivityEvidence,
) {
    companion object {
        fun create(
            classification: AndroidVoiceActivityClassification,
            evidence: AndroidVoiceActivityEvidence,
        ): AndroidVoiceActivityResult {
            return AndroidVoiceActivityResult(
                classification = classification,
                evidence = evidence,
            )
        }
    }
}
