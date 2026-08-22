package com.devil.app.vision

import com.devil.app.voice.AndroidMultilingualSpeechRecognitionResult
import com.devil.app.voice.AndroidMultilingualSpeechRecognitionStatus

/**
 * Stage 210 bounded Voice + Vision Interaction coordinator.
 *
 * It integrates one exact Stage 198 Multilingual Speech Recognition result
 * with one exact Stage 209 Screen Vision result.
 *
 * It does not:
 *
 * - rewrite or interpret the recognized transcript;
 * - infer user intent;
 * - infer that speech refers to a visible screen element;
 * - resolve "this", "that", or another visual reference;
 * - resolve an actionable accessibility target;
 * - perform OCR;
 * - capture new speech or visual input;
 * - execute accessibility or other capabilities;
 * - identify or authenticate the speaker;
 * - grant authorization;
 * - create Memory;
 * - establish constitutional Observation, Verification, or Outcome;
 * - implement Stage 211 Educational Vision.
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
class AndroidVoiceVisionInteractionCoordinator {

    fun integrate(
        speechRecognition: AndroidMultilingualSpeechRecognitionResult,
        screenVision: AndroidScreenVisionResult,
    ): AndroidVoiceVisionInteractionResult {
        val status =
            if (
                speechRecognition.status ==
                    AndroidMultilingualSpeechRecognitionStatus.AVAILABLE &&
                screenVision.status ==
                    AndroidScreenVisionStatus.AVAILABLE
            ) {
                AndroidVoiceVisionInteractionStatus.AVAILABLE
            } else {
                AndroidVoiceVisionInteractionStatus.DEFERRED
            }

        return AndroidVoiceVisionInteractionResult.create(
            status = status,
            speechRecognition = speechRecognition,
            screenVision = screenVision,
        )
    }
}
