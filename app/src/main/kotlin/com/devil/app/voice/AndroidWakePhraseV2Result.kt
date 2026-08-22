package com.devil.app.voice

/**
 * Stage 196 bounded Wake Phrase V2 result.
 *
 * MATCHED preserves exactly one normalized canonical wake transcript.
 * NOT_MATCHED preserves no transcript.
 *
 * MATCHED establishes attention only.
 *
 * WAKE_MATCHED != AUTHENTICATED.
 * ATTENTION_ESTABLISHED != ACTIVE_SESSION.
 */
@ConsistentCopyVisibility
data class AndroidWakePhraseV2Result private constructor(
    val status: AndroidWakePhraseV2Status,
    val normalizedTranscript: String?,
) {
    companion object {
        fun create(
            status: AndroidWakePhraseV2Status,
            normalizedTranscript: String? = null,
        ): AndroidWakePhraseV2Result {
            when (status) {
                AndroidWakePhraseV2Status.MATCHED -> {
                    val transcript =
                        requireNotNull(normalizedTranscript) {
                            "Matched Stage 196 wake result requires one normalized transcript."
                        }.trim()

                    require(transcript.isNotEmpty()) {
                        "Matched Stage 196 wake transcript must not be blank."
                    }

                    return AndroidWakePhraseV2Result(
                        status = status,
                        normalizedTranscript = transcript,
                    )
                }

                AndroidWakePhraseV2Status.NOT_MATCHED -> {
                    require(normalizedTranscript == null) {
                        "Not-matched Stage 196 wake result must not contain a transcript."
                    }

                    return AndroidWakePhraseV2Result(
                        status = status,
                        normalizedTranscript = null,
                    )
                }
            }
        }
    }
}
