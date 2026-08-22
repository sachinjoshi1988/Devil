package com.devil.app.voice

/**
 * Stage 197 bounded Speech Recognition V2 result.
 *
 * AVAILABLE preserves the exact upstream AndroidVoiceInputResult together with
 * its recognized transcript.
 *
 * DEFERRED preserves the exact upstream AndroidVoiceInputResult and no
 * transcript.
 *
 * SPEECH_RECOGNIZED != SPEAKER_IDENTIFIED.
 * SPEECH_RECOGNIZED != AUTHENTICATED.
 * TRANSCRIPT != INTENT.
 */
@ConsistentCopyVisibility
data class AndroidSpeechRecognitionV2Result private constructor(
    val status: AndroidSpeechRecognitionV2Status,
    val voiceInputResult: AndroidVoiceInputResult,
    val transcript: String?,
) {
    companion object {
        fun create(
            status: AndroidSpeechRecognitionV2Status,
            voiceInputResult: AndroidVoiceInputResult,
            transcript: String? = null,
        ): AndroidSpeechRecognitionV2Result {
            when (status) {
                AndroidSpeechRecognitionV2Status.AVAILABLE -> {
                    require(
                        voiceInputResult.status ==
                            AndroidVoiceInputStatus.RECOGNIZED,
                    ) {
                        "Available Stage 197 speech recognition requires a recognized Android voice-input result."
                    }

                    val recognizedTranscript =
                        requireNotNull(voiceInputResult.transcript) {
                            "Recognized Android voice input requires one transcript."
                        }

                    require(transcript == recognizedTranscript) {
                        "Stage 197 transcript must preserve the exact upstream recognized transcript."
                    }
                }

                AndroidSpeechRecognitionV2Status.DEFERRED -> {
                    require(
                        voiceInputResult.status !=
                            AndroidVoiceInputStatus.RECOGNIZED,
                    ) {
                        "Deferred Stage 197 speech recognition must not contain a recognized Android voice-input result."
                    }

                    require(transcript == null) {
                        "Deferred Stage 197 speech recognition must not contain a transcript."
                    }
                }
            }

            return AndroidSpeechRecognitionV2Result(
                status = status,
                voiceInputResult = voiceInputResult,
                transcript = transcript,
            )
        }
    }
}
