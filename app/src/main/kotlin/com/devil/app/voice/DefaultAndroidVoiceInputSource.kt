package com.devil.app.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

/**
 * Default Stage 35 Android SpeechRecognizer-backed voice-input source.
 *
 * This source owns one bounded SpeechRecognizer instance and performs one
 * recognition attempt at a time.
 *
 * The caller must establish Android RECORD_AUDIO permission before invoking
 * startListening.
 *
 * Android SpeechRecognizer lifecycle operations are restricted to the Android
 * main thread. Violating that requirement is rejected rather than hidden by a
 * second execution mechanism.
 *
 * This source performs speech-to-text only.
 *
 * An explicitly supplied recognition language tag configures Android speech
 * recognition only. It does not establish detected or verified conversation
 * language.
 *
 * RECOGNITION_LOCALE != UNDERSTANDING_LANGUAGE_TRUTH.
 *
 * It does not:
 *
 * - request or grant Android microphone permission;
 * - authenticate or identify the speaker;
 * - establish subject trust;
 * - establish a Devil security stage or session;
 * - grant Devil authorization;
 * - invoke UnifiedDevilRuntime;
 * - infer intent or semantic understanding;
 * - execute capabilities;
 * - claim verification, outcome, or completion.
 *
 * Recognized speech is untrusted input provenance until later constitutional
 * authorities evaluate it through the one Unified Devil Runtime.
 */
class DefaultAndroidVoiceInputSource(
    context: Context,
    private val recognitionLanguageTagProvider: () -> String? = { null },
) : AndroidVoiceInputSource {

    private val applicationContext =
        context.applicationContext

    private var speechRecognizer: SpeechRecognizer? = null

    private var activeListener: AndroidVoiceInputListener? = null

    private var cancellationRequested: Boolean = false

    private var terminalResultDelivered: Boolean = false

    private var released: Boolean = false

    override fun startListening(
        listener: AndroidVoiceInputListener,
    ) {
        requireMainThread()

        check(!released) {
            "Released Android voice-input source cannot start listening."
        }

        check(activeListener == null) {
            "Android voice-input source already has an active recognition attempt."
        }

        val recognizer =
            speechRecognizer
                ?: createSpeechRecognizer().also {
                    speechRecognizer = it
                }

        activeListener = listener
        cancellationRequested = false
        terminalResultDelivered = false

        recognizer.setRecognitionListener(
            recognitionListener,
        )

        try {
            recognizer.startListening(
                createRecognitionIntent(),
            )
        } catch (throwable: RuntimeException) {
            finish(
                AndroidVoiceInputResult.failed(
                    errorCode =
                        "ANDROID_SPEECH_RECOGNIZER_START_FAILED",
                ),
            )
        }
    }

    override fun cancel() {
        requireMainThread()

        if (released) {
            return
        }

        val listener = activeListener ?: return

        cancellationRequested = true

        try {
            speechRecognizer?.cancel()
        } finally {
            if (!terminalResultDelivered) {
                terminalResultDelivered = true
                activeListener = null

                listener.onResult(
                    AndroidVoiceInputResult.cancelled(),
                )
            }
        }
    }

    override fun release() {
        requireMainThread()

        if (released) {
            return
        }

        if (activeListener != null && !terminalResultDelivered) {
            val listener = activeListener

            terminalResultDelivered = true
            activeListener = null
            cancellationRequested = true

            listener?.onResult(
                AndroidVoiceInputResult.cancelled(),
            )
        }

        speechRecognizer?.destroy()
        speechRecognizer = null

        released = true
    }

    private fun createSpeechRecognizer(): SpeechRecognizer {
        check(
            SpeechRecognizer.isRecognitionAvailable(
                applicationContext,
            ),
        ) {
            "Android speech recognition is not available on this device."
        }

        return SpeechRecognizer.createSpeechRecognizer(
            applicationContext,
        )
    }

    private fun createRecognitionIntent(): Intent {
        return Intent(
            RecognizerIntent.ACTION_RECOGNIZE_SPEECH,
        ).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
            )

            putExtra(
                RecognizerIntent.EXTRA_PARTIAL_RESULTS,
                false,
            )

            putExtra(
                RecognizerIntent.EXTRA_MAX_RESULTS,
                5,
            )

            recognitionLanguageTagProvider()
                ?.trim()
                ?.takeIf { it.isNotEmpty() }
                ?.let { languageTag ->
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE,
                        languageTag,
                    )
                }
        }
    }

    private val recognitionListener =
        object : RecognitionListener {

            override fun onReadyForSpeech(
                params: Bundle?,
            ) {
                if (
                    activeListener != null &&
                    !terminalResultDelivered
                ) {
                    activeListener?.onReady()
                }
            }

            override fun onBeginningOfSpeech() = Unit

            override fun onRmsChanged(
                rmsdB: Float,
            ) = Unit

            override fun onBufferReceived(
                buffer: ByteArray?,
            ) = Unit

            override fun onEndOfSpeech() = Unit

            override fun onError(
                error: Int,
            ) {
                finish(
                    AndroidSpeechRecognitionResultMapper.fromError(
                        errorCode = error,
                        cancellationRequested = cancellationRequested,
                    ),
                )
            }

            override fun onResults(
                results: Bundle?,
            ) {
                val candidates =
                    results?.getStringArrayList(
                        SpeechRecognizer.RESULTS_RECOGNITION,
                    )

                finish(
                    AndroidSpeechRecognitionResultMapper.fromCandidates(
                        candidates = candidates,
                    ),
                )
            }

            override fun onPartialResults(
                partialResults: Bundle?,
            ) = Unit

            override fun onEvent(
                eventType: Int,
                params: Bundle?,
            ) = Unit
        }

    private fun finish(
        result: AndroidVoiceInputResult,
    ) {
        if (terminalResultDelivered) {
            return
        }

        val listener = activeListener ?: return

        terminalResultDelivered = true
        activeListener = null
        cancellationRequested = false

        listener.onResult(result)
    }

    private fun requireMainThread() {
        check(
            Looper.myLooper() == Looper.getMainLooper(),
        ) {
            "Android voice-input lifecycle must run on the Android main thread."
        }
    }
}
