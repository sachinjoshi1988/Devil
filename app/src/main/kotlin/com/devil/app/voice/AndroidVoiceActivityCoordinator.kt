package com.devil.app.voice

/**
 * Stage 201 bounded Voice Activity & Noise Handling coordinator.
 *
 * It classifies only explicitly supplied audio-level evidence.
 *
 * Classification:
 *
 * rmsDb <= silenceThresholdDb
 * -> SILENCE
 *
 * silenceThresholdDb < rmsDb < voiceThresholdDb
 * -> NOISE
 *
 * rmsDb >= voiceThresholdDb
 * -> VOICE_ACTIVITY
 *
 * It does not:
 *
 * - capture microphone audio;
 * - modify SpeechRecognizer callbacks;
 * - recognize speech content;
 * - identify a speaker;
 * - authenticate or authorize;
 * - infer emotional tone;
 * - implement Stage 202 Emotional-Tone Awareness.
 *
 * AUDIO_LEVEL != SPEECH_CONTENT.
 * VOICE_ACTIVITY != RECOGNIZED_SPEECH.
 * NOISE != UNKNOWN_SPEAKER.
 * VOICE_ACTIVITY != AUTHENTICATION.
 */
class AndroidVoiceActivityCoordinator {

    fun classify(
        evidence: AndroidVoiceActivityEvidence,
    ): AndroidVoiceActivityResult {
        val classification =
            when {
                evidence.rmsDb <= evidence.silenceThresholdDb ->
                    AndroidVoiceActivityClassification.SILENCE

                evidence.rmsDb >= evidence.voiceThresholdDb ->
                    AndroidVoiceActivityClassification.VOICE_ACTIVITY

                else ->
                    AndroidVoiceActivityClassification.NOISE
            }

        return AndroidVoiceActivityResult.create(
            classification = classification,
            evidence = evidence,
        )
    }
}
