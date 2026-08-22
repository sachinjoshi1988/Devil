package com.devil.app.voice

/**
 * Stage 198 bounded Multilingual Speech Recognition result.
 *
 * AVAILABLE preserves the exact Stage 197 speech-recognition result and one
 * explicitly supplied normalized language tag.
 *
 * DEFERRED preserves the exact Stage 197 result and no language tag.
 *
 * LANGUAGE_TAG != DETECTED_LANGUAGE.
 * MULTILINGUAL_RECOGNITION != TRANSLATION.
 * MULTILINGUAL_RECOGNITION != AUTHENTICATION.
 */
@ConsistentCopyVisibility
data class AndroidMultilingualSpeechRecognitionResult private constructor(
    val status: AndroidMultilingualSpeechRecognitionStatus,
    val speechRecognition: AndroidSpeechRecognitionV2Result,
    val languageTag: String?,
) {
    companion object {
        fun create(
            status: AndroidMultilingualSpeechRecognitionStatus,
            speechRecognition: AndroidSpeechRecognitionV2Result,
            languageTag: String? = null,
        ): AndroidMultilingualSpeechRecognitionResult {
            when (status) {
                AndroidMultilingualSpeechRecognitionStatus.AVAILABLE -> {
                    require(
                        speechRecognition.status ==
                            AndroidSpeechRecognitionV2Status.AVAILABLE,
                    ) {
                        "Available Stage 198 multilingual speech recognition requires available Stage 197 speech recognition."
                    }

                    val normalizedLanguageTag =
                        requireNotNull(languageTag) {
                            "Available Stage 198 multilingual speech recognition requires one language tag."
                        }.trim()

                    require(normalizedLanguageTag.isNotEmpty()) {
                        "Stage 198 language tag must not be blank."
                    }

                    return AndroidMultilingualSpeechRecognitionResult(
                        status = status,
                        speechRecognition = speechRecognition,
                        languageTag = normalizedLanguageTag,
                    )
                }

                AndroidMultilingualSpeechRecognitionStatus.DEFERRED -> {
                    require(languageTag == null) {
                        "Deferred Stage 198 multilingual speech recognition must not contain a language tag."
                    }

                    return AndroidMultilingualSpeechRecognitionResult(
                        status = status,
                        speechRecognition = speechRecognition,
                        languageTag = null,
                    )
                }
            }
        }
    }
}
