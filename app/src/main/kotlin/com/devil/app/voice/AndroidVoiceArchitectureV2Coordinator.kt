package com.devil.app.voice

/**
 * Stage 195 bounded Voice Architecture V2 coordinator.
 *
 * It integrates only existing Android voice input/output contracts.
 *
 * Both sources must be explicitly supplied for AVAILABLE.
 *
 * It does not:
 *
 * - start SpeechRecognizer listening;
 * - invoke TextToSpeech;
 * - evaluate a wake phrase;
 * - authenticate a speaker;
 * - establish ACTIVE_SESSION;
 * - submit conversation input;
 * - grant Devil authorization;
 * - establish execution, Verification, or Outcome;
 * - implement Stage 196 Wake Phrase.
 *
 * WAKE_PHRASE != AUTHENTICATION.
 */
class AndroidVoiceArchitectureV2Coordinator {
    fun integrate(
        inputSource: AndroidVoiceInputSource?,
        outputSource: AndroidVoiceOutputSource?,
    ): AndroidVoiceArchitectureV2Result {
        if (
            inputSource == null ||
            outputSource == null
        ) {
            return AndroidVoiceArchitectureV2Result.create(
                status = AndroidVoiceArchitectureV2Status.DEFERRED,
            )
        }

        return AndroidVoiceArchitectureV2Result.create(
            status = AndroidVoiceArchitectureV2Status.AVAILABLE,
            inputSource = inputSource,
            outputSource = outputSource,
        )
    }
}
