package com.devil.app.voice

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale
import java.util.UUID

/**
 * Default Stage 36 Android TextToSpeech-backed voice-output source.
 *
 * This source performs presentation only.
 *
 * It speaks exactly the normalized supplied text and does not:
 *
 * - generate conversational content;
 * - invoke UnifiedDevilRuntime;
 * - infer intent;
 * - authenticate a subject;
 * - grant authorization;
 * - execute capabilities;
 * - establish verification;
 * - establish final Outcome;
 * - report task completion.
 *
 * Android TextToSpeech lifecycle entry points are restricted to the Android
 * main thread.
 *
 * TextToSpeech terminal callbacks are returned to Devil through the main thread
 * before presentation state is changed.
 */
class DefaultAndroidVoiceOutputSource(
    context: Context,
    private val voiceProfile: DevilVoiceProfile,
) : AndroidVoiceOutputSource {

    private val applicationContext =
        context.applicationContext

    private val mainHandler =
        Handler(
            Looper.getMainLooper(),
        )

    private var textToSpeech: TextToSpeech? =
        null

    private var initialized: Boolean =
        false

    private var initializationFailed: Boolean =
        false

    private var released: Boolean =
        false

    private var pendingListener:
        AndroidVoiceOutputListener? =
        null

    private var pendingText: String? =
        null

    private var activeUtteranceId: String? =
        null

    override fun speak(
        text: String,
        listener: AndroidVoiceOutputListener,
    ) {
        requireMainThread()

        check(!released) {
            "Released Android voice-output source cannot speak."
        }

        check(pendingListener == null) {
            "Android voice-output source already has an active speech attempt."
        }

        val normalizedText =
            text.trim()

        require(normalizedText.isNotEmpty()) {
            "Android voice-output text must not be blank."
        }

        pendingListener =
            listener

        pendingText =
            normalizedText

        if (initializationFailed) {
            finish(
                AndroidVoiceOutputResult.unavailable(),
            )

            return
        }

        val existingEngine =
            textToSpeech

        if (existingEngine == null) {
            createTextToSpeech()
            return
        }

        if (initialized) {
            speakPrepared(
                engine = existingEngine,
                text = normalizedText,
            )
        }
    }

    override fun stop() {
        requireMainThread()

        if (released) {
            return
        }

        textToSpeech?.stop()

        if (
            pendingListener != null ||
            activeUtteranceId != null
        ) {
            finish(
                AndroidVoiceOutputResult.cancelled(),
            )
        }
    }

    override fun release() {
        requireMainThread()

        if (released) {
            return
        }

        textToSpeech?.stop()
        textToSpeech?.shutdown()

        textToSpeech = null
        initialized = false
        initializationFailed = false
        released = true

        if (
            pendingListener != null ||
            activeUtteranceId != null
        ) {
            finish(
                AndroidVoiceOutputResult.cancelled(),
            )
        }
    }

    private fun createTextToSpeech() {
        textToSpeech =
            TextToSpeech(
                applicationContext,
            ) { status ->
                dispatchToMain {
                    handleInitialization(
                        status = status,
                    )
                }
            }
    }

    private fun handleInitialization(
        status: Int,
    ) {
        if (released) {
            return
        }

        if (status != TextToSpeech.SUCCESS) {
            initializationFailed = true

            finish(
                AndroidVoiceOutputResult.unavailable(),
            )

            return
        }

        val engine =
            textToSpeech
                ?: run {
                    initializationFailed = true

                    finish(
                        AndroidVoiceOutputResult.unavailable(),
                    )

                    return
                }

        val languageResult =
            engine.setLanguage(
                Locale.forLanguageTag(voiceProfile.languageTag),
            )

        if (
            languageResult ==
                TextToSpeech.LANG_MISSING_DATA ||
            languageResult ==
                TextToSpeech.LANG_NOT_SUPPORTED
        ) {
            initializationFailed = true

            finish(
                AndroidVoiceOutputResult.unavailable(),
            )

            return
        }

        val speechRateResult =
            engine.setSpeechRate(
                voiceProfile.speechRate,
            )

        val pitchResult =
            engine.setPitch(
                voiceProfile.pitch,
            )

        if (
            speechRateResult == TextToSpeech.ERROR ||
            pitchResult == TextToSpeech.ERROR
        ) {
            initializationFailed = true

            finish(
                AndroidVoiceOutputResult.unavailable(),
            )

            return
        }

        engine.setOnUtteranceProgressListener(
            utteranceProgressListener,
        )

        initialized = true

        val text =
            pendingText

        if (text != null) {
            speakPrepared(
                engine = engine,
                text = text,
            )
        }
    }

    private fun speakPrepared(
        engine: TextToSpeech,
        text: String,
    ) {
        val utteranceId =
            "devil-stage-36-${UUID.randomUUID()}"

        activeUtteranceId =
            utteranceId

        val result =
            engine.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                utteranceId,
            )

        if (result == TextToSpeech.ERROR) {
            finish(
                AndroidVoiceOutputResult.failed(
                    errorCode =
                        "ANDROID_TEXT_TO_SPEECH_SPEAK_FAILED",
                ),
            )
        }
    }

    private val utteranceProgressListener =
        object : UtteranceProgressListener() {

            override fun onStart(
                utteranceId: String?,
            ) = Unit

            override fun onDone(
                utteranceId: String?,
            ) {
                dispatchToMain {
                    if (
                        utteranceId ==
                            activeUtteranceId
                    ) {
                        val text =
                            pendingText
                                ?: return@dispatchToMain

                        finish(
                            AndroidVoiceOutputResult.spoken(
                                text = text,
                            ),
                        )
                    }
                }
            }

            @Deprecated(
                "Deprecated in Android API",
            )
            override fun onError(
                utteranceId: String?,
            ) {
                onError(
                    utteranceId,
                    TextToSpeech.ERROR,
                )
            }

            override fun onError(
                utteranceId: String?,
                errorCode: Int,
            ) {
                dispatchToMain {
                    if (
                        utteranceId !=
                            activeUtteranceId
                    ) {
                        return@dispatchToMain
                    }

                    finish(
                        AndroidVoiceOutputResult.failed(
                            errorCode =
                                "ANDROID_TEXT_TO_SPEECH_ERROR_$errorCode",
                        ),
                    )
                }
            }
        }

    private fun finish(
        result: AndroidVoiceOutputResult,
    ) {
        requireMainThread()

        val listener =
            pendingListener
                ?: return

        pendingListener = null
        pendingText = null
        activeUtteranceId = null

        listener.onResult(
            result,
        )
    }

    private fun dispatchToMain(
        block: () -> Unit,
    ) {
        if (
            Looper.myLooper() ==
                Looper.getMainLooper()
        ) {
            block()
        } else {
            mainHandler.post(
                block,
            )
        }
    }

    private fun requireMainThread() {
        check(
            Looper.myLooper() ==
                Looper.getMainLooper(),
        ) {
            "Android voice-output lifecycle must run on the Android main thread."
        }
    }
}
