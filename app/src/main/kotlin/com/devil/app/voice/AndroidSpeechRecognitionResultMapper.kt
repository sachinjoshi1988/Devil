package com.devil.app.voice

import android.speech.SpeechRecognizer

/**
 * Maps bounded Android SpeechRecognizer terminal information into the stable
 * Devil AndroidVoiceInputResult contract.
 *
 * This mapper does not perform speech recognition, request microphone
 * permission, authenticate a speaker, infer meaning, invoke the Unified Devil
 * Runtime, or grant any authority.
 *
 * Recognition candidates are treated only as Android-produced textual input.
 */
object AndroidSpeechRecognitionResultMapper {

    fun fromCandidates(
        candidates: List<String>?,
    ): AndroidVoiceInputResult {
        val transcript =
            candidates
                .orEmpty()
                .asSequence()
                .map { it.trim() }
                .firstOrNull { it.isNotEmpty() }

        return if (transcript == null) {
            AndroidVoiceInputResult.noMatch()
        } else {
            AndroidVoiceInputResult.recognized(
                transcript = transcript,
            )
        }
    }

    fun fromError(
        errorCode: Int,
        cancellationRequested: Boolean,
    ): AndroidVoiceInputResult {
        if (cancellationRequested) {
            return AndroidVoiceInputResult.cancelled()
        }

        return when (errorCode) {
            SpeechRecognizer.ERROR_NO_MATCH,
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT,
            -> AndroidVoiceInputResult.noMatch()

            else ->
                AndroidVoiceInputResult.failed(
                    errorCode =
                        "ANDROID_SPEECH_RECOGNIZER_ERROR_$errorCode",
                )
        }
    }
}
