package com.devil.app.voice

/**
 * Preserves the bounded result of one Android voice-input attempt.
 *
 * RECOGNIZED requires one normalized non-blank transcript and no error code.
 *
 * NO_MATCH and CANCELLED contain neither transcript nor error code.
 *
 * FAILED contains one normalized non-blank operational error code and no
 * transcript.
 *
 * A recognized transcript is input provenance only. It is not proof of speaker
 * identity, authentication, semantic understanding, decision, execution,
 * verification, outcome, or task completion.
 */
@ConsistentCopyVisibility
data class AndroidVoiceInputResult private constructor(
    val status: AndroidVoiceInputStatus,
    val transcript: String?,
    val errorCode: String?,
) {
    companion object {

        fun recognized(
            transcript: String,
        ): AndroidVoiceInputResult {
            val normalizedTranscript = transcript.trim()

            require(normalizedTranscript.isNotEmpty()) {
                "Recognized Android voice transcript must not be blank."
            }

            return AndroidVoiceInputResult(
                status = AndroidVoiceInputStatus.RECOGNIZED,
                transcript = normalizedTranscript,
                errorCode = null,
            )
        }

        fun noMatch(): AndroidVoiceInputResult {
            return AndroidVoiceInputResult(
                status = AndroidVoiceInputStatus.NO_MATCH,
                transcript = null,
                errorCode = null,
            )
        }

        fun cancelled(): AndroidVoiceInputResult {
            return AndroidVoiceInputResult(
                status = AndroidVoiceInputStatus.CANCELLED,
                transcript = null,
                errorCode = null,
            )
        }

        fun failed(
            errorCode: String,
        ): AndroidVoiceInputResult {
            val normalizedErrorCode = errorCode.trim()

            require(normalizedErrorCode.isNotEmpty()) {
                "Android voice-input error code must not be blank."
            }

            return AndroidVoiceInputResult(
                status = AndroidVoiceInputStatus.FAILED,
                transcript = null,
                errorCode = normalizedErrorCode,
            )
        }
    }
}
