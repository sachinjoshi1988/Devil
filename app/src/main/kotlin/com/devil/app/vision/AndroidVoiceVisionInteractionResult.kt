package com.devil.app.vision

import com.devil.app.voice.AndroidMultilingualSpeechRecognitionResult
import com.devil.app.voice.AndroidMultilingualSpeechRecognitionStatus

/**
 * Stage 210 bounded Voice + Vision Interaction result.
 *
 * AVAILABLE preserves the exact Stage 198 Multilingual Speech Recognition
 * result and the exact Stage 209 Screen Vision result.
 *
 * DEFERRED preserves both exact upstream results without claiming integrated
 * voice + vision availability.
 *
 * VOICE_VISION_INTERACTION != INTENT.
 * TRANSCRIPT != VISUAL_COMMAND.
 * VOICE_CONTEXT != TARGET_RESOLUTION.
 * SCREEN_VISION != EXECUTION.
 * LANGUAGE_TAG != DETECTED_LANGUAGE.
 * VOICE_PLUS_VISION != AUTHENTICATION.
 * VOICE_PLUS_VISION != AUTHORIZATION.
 * VOICE_PLUS_VISION != CONSTITUTIONAL_VERIFICATION.
 */
@ConsistentCopyVisibility
data class AndroidVoiceVisionInteractionResult private constructor(
    val status: AndroidVoiceVisionInteractionStatus,
    val speechRecognition: AndroidMultilingualSpeechRecognitionResult,
    val screenVision: AndroidScreenVisionResult,
) {
    companion object {
        fun create(
            status: AndroidVoiceVisionInteractionStatus,
            speechRecognition: AndroidMultilingualSpeechRecognitionResult,
            screenVision: AndroidScreenVisionResult,
        ): AndroidVoiceVisionInteractionResult {
            when (status) {
                AndroidVoiceVisionInteractionStatus.AVAILABLE -> {
                    require(
                        speechRecognition.status ==
                            AndroidMultilingualSpeechRecognitionStatus.AVAILABLE,
                    ) {
                        "Available Stage 210 voice + vision interaction requires available Stage 198 multilingual speech recognition."
                    }

                    require(
                        screenVision.status ==
                            AndroidScreenVisionStatus.AVAILABLE,
                    ) {
                        "Available Stage 210 voice + vision interaction requires available Stage 209 screen vision."
                    }
                }

                AndroidVoiceVisionInteractionStatus.DEFERRED -> Unit
            }

            return AndroidVoiceVisionInteractionResult(
                status = status,
                speechRecognition = speechRecognition,
                screenVision = screenVision,
            )
        }
    }
}
