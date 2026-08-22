package com.devil.app.voice

/**
 * Stage 198 bounded Multilingual Speech Recognition coordinator.
 *
 * It consumes one exact Stage 197 speech-recognition result plus one explicitly
 * supplied language tag.
 *
 * It does not:
 *
 * - auto-detect language;
 * - translate recognized text;
 * - rewrite the transcript;
 * - change Android SpeechRecognizer configuration;
 * - identify or authenticate the speaker;
 * - grant authorization;
 * - implement Stage 199 Devil Voice.
 *
 * LANGUAGE_TAG != DETECTED_LANGUAGE.
 * MULTILINGUAL_RECOGNITION != TRANSLATION.
 * MULTILINGUAL_RECOGNITION != AUTHENTICATION.
 */
class AndroidMultilingualSpeechRecognitionCoordinator {

    fun integrate(
        speechRecognition: AndroidSpeechRecognitionV2Result,
        languageTag: String?,
    ): AndroidMultilingualSpeechRecognitionResult {
        if (
            speechRecognition.status !=
                AndroidSpeechRecognitionV2Status.AVAILABLE ||
            languageTag.isNullOrBlank()
        ) {
            return AndroidMultilingualSpeechRecognitionResult.create(
                status = AndroidMultilingualSpeechRecognitionStatus.DEFERRED,
                speechRecognition = speechRecognition,
            )
        }

        return AndroidMultilingualSpeechRecognitionResult.create(
            status = AndroidMultilingualSpeechRecognitionStatus.AVAILABLE,
            speechRecognition = speechRecognition,
            languageTag = languageTag,
        )
    }
}
