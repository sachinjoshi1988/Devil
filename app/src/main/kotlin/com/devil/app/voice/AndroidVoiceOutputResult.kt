package com.devil.app.voice

/**
 * Stable bounded result for one Android voice-output attempt.
 *
 * SPOKEN preserves the normalized text supplied to the platform speech layer.
 *
 * UNAVAILABLE and CANCELLED contain neither spoken text nor error code.
 *
 * FAILED contains one non-blank operational error code and no spoken text.
 *
 * Spoken text must already exist as truthful presentation content before this
 * result is created. Voice output does not generate conversational meaning.
 */
@ConsistentCopyVisibility
data class AndroidVoiceOutputResult private constructor(
    val status: AndroidVoiceOutputStatus,
    val spokenText: String?,
    val errorCode: String?,
) {
    companion object {

        fun spoken(
            text: String,
        ): AndroidVoiceOutputResult {
            val normalizedText = text.trim()

            require(normalizedText.isNotEmpty()) {
                "Android voice-output text must not be blank."
            }

            return AndroidVoiceOutputResult(
                status = AndroidVoiceOutputStatus.SPOKEN,
                spokenText = normalizedText,
                errorCode = null,
            )
        }

        fun unavailable(): AndroidVoiceOutputResult {
            return AndroidVoiceOutputResult(
                status = AndroidVoiceOutputStatus.UNAVAILABLE,
                spokenText = null,
                errorCode = null,
            )
        }

        fun cancelled(): AndroidVoiceOutputResult {
            return AndroidVoiceOutputResult(
                status = AndroidVoiceOutputStatus.CANCELLED,
                spokenText = null,
                errorCode = null,
            )
        }

        fun failed(
            errorCode: String,
        ): AndroidVoiceOutputResult {
            val normalizedErrorCode = errorCode.trim()

            require(normalizedErrorCode.isNotEmpty()) {
                "Android voice-output error code must not be blank."
            }

            return AndroidVoiceOutputResult(
                status = AndroidVoiceOutputStatus.FAILED,
                spokenText = null,
                errorCode = normalizedErrorCode,
            )
        }
    }
}
