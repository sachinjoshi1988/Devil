package com.devil.app.voice

/**
 * Stage 197 bounded Speech Recognition V2 coordinator.
 *
 * It integrates only an already-established AndroidVoiceInputResult.
 *
 * A genuine RECOGNIZED result becomes AVAILABLE with exact transcript
 * provenance.
 *
 * NO_MATCH, CANCELLED, and FAILED remain DEFERRED.
 *
 * It does not identify the speaker, authenticate the owner, evaluate wake
 * phrases, infer intent, submit to runtime, execute capabilities, or implement
 * Stage 198 Multilingual Speech Recognition.
 *
 * SPEECH_RECOGNIZED != SPEAKER_IDENTIFIED.
 * SPEECH_RECOGNIZED != AUTHENTICATED.
 * TRANSCRIPT != INTENT.
 */
class AndroidSpeechRecognitionV2Coordinator {

    fun integrate(
        voiceInputResult: AndroidVoiceInputResult,
    ): AndroidSpeechRecognitionV2Result {
        return when (voiceInputResult.status) {
            AndroidVoiceInputStatus.RECOGNIZED ->
                AndroidSpeechRecognitionV2Result.create(
                    status = AndroidSpeechRecognitionV2Status.AVAILABLE,
                    voiceInputResult = voiceInputResult,
                    transcript = voiceInputResult.transcript,
                )

            AndroidVoiceInputStatus.NO_MATCH,
            AndroidVoiceInputStatus.CANCELLED,
            AndroidVoiceInputStatus.FAILED,
            ->
                AndroidSpeechRecognitionV2Result.create(
                    status = AndroidSpeechRecognitionV2Status.DEFERRED,
                    voiceInputResult = voiceInputResult,
                )
        }
    }
}
