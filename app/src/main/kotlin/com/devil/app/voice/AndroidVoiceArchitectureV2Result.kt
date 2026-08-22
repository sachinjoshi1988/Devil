package com.devil.app.voice

/**
 * Stage 195 bounded Voice Architecture V2 result.
 *
 * AVAILABLE preserves the exact existing bounded Android voice input/output
 * contracts supplied to Stage 195.
 *
 * DEFERRED preserves no voice sources.
 *
 * VOICE_ARCHITECTURE_AVAILABLE != LISTENING.
 * VOICE_ARCHITECTURE_AVAILABLE != SPEAKING.
 * VOICE_AVAILABLE != AUTHENTICATED_SESSION.
 */
@ConsistentCopyVisibility
data class AndroidVoiceArchitectureV2Result private constructor(
    val status: AndroidVoiceArchitectureV2Status,
    val inputSource: AndroidVoiceInputSource?,
    val outputSource: AndroidVoiceOutputSource?,
) {
    companion object {
        fun create(
            status: AndroidVoiceArchitectureV2Status,
            inputSource: AndroidVoiceInputSource? = null,
            outputSource: AndroidVoiceOutputSource? = null,
        ): AndroidVoiceArchitectureV2Result {
            when (status) {
                AndroidVoiceArchitectureV2Status.AVAILABLE -> {
                    require(inputSource != null) {
                        "Available Android Voice Architecture V2 requires one voice input source."
                    }

                    require(outputSource != null) {
                        "Available Android Voice Architecture V2 requires one voice output source."
                    }
                }

                AndroidVoiceArchitectureV2Status.DEFERRED -> {
                    require(inputSource == null) {
                        "Deferred Android Voice Architecture V2 must not contain a voice input source."
                    }

                    require(outputSource == null) {
                        "Deferred Android Voice Architecture V2 must not contain a voice output source."
                    }
                }
            }

            return AndroidVoiceArchitectureV2Result(
                status = status,
                inputSource = inputSource,
                outputSource = outputSource,
            )
        }
    }
}
