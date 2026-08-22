package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage198MultilingualSpeechRecognitionTest {

    @Test
    fun `available Stage 197 recognition plus explicit language becomes available`() {
        val speechRecognition =
            AndroidSpeechRecognitionV2Coordinator()
                .integrate(
                    AndroidVoiceInputResult.recognized(
                        "Bonjour Devil",
                    ),
                )

        val result =
            AndroidMultilingualSpeechRecognitionCoordinator()
                .integrate(
                    speechRecognition = speechRecognition,
                    languageTag = "  fr-FR  ",
                )

        assertEquals(
            AndroidMultilingualSpeechRecognitionStatus.AVAILABLE,
            result.status,
        )
        assertSame(
            speechRecognition,
            result.speechRecognition,
        )
        assertEquals(
            "fr-FR",
            result.languageTag,
        )
        assertEquals(
            "Bonjour Devil",
            result.speechRecognition.transcript,
        )
    }

    @Test
    fun `missing language tag remains deferred`() {
        val speechRecognition =
            AndroidSpeechRecognitionV2Coordinator()
                .integrate(
                    AndroidVoiceInputResult.recognized(
                        "Hello Devil",
                    ),
                )

        val result =
            AndroidMultilingualSpeechRecognitionCoordinator()
                .integrate(
                    speechRecognition = speechRecognition,
                    languageTag = null,
                )

        assertEquals(
            AndroidMultilingualSpeechRecognitionStatus.DEFERRED,
            result.status,
        )
        assertSame(
            speechRecognition,
            result.speechRecognition,
        )
        assertNull(result.languageTag)
    }

    @Test
    fun `blank language tag remains deferred`() {
        val speechRecognition =
            AndroidSpeechRecognitionV2Coordinator()
                .integrate(
                    AndroidVoiceInputResult.recognized(
                        "Hello Devil",
                    ),
                )

        val result =
            AndroidMultilingualSpeechRecognitionCoordinator()
                .integrate(
                    speechRecognition = speechRecognition,
                    languageTag = "   ",
                )

        assertEquals(
            AndroidMultilingualSpeechRecognitionStatus.DEFERRED,
            result.status,
        )
        assertNull(result.languageTag)
    }

    @Test
    fun `deferred Stage 197 recognition cannot become multilingual available`() {
        val speechRecognition =
            AndroidSpeechRecognitionV2Coordinator()
                .integrate(
                    AndroidVoiceInputResult.noMatch(),
                )

        val result =
            AndroidMultilingualSpeechRecognitionCoordinator()
                .integrate(
                    speechRecognition = speechRecognition,
                    languageTag = "en-IN",
                )

        assertEquals(
            AndroidMultilingualSpeechRecognitionStatus.DEFERRED,
            result.status,
        )
        assertNull(result.languageTag)
    }

    @Test
    fun `available result rejects blank language tag`() {
        val speechRecognition =
            AndroidSpeechRecognitionV2Coordinator()
                .integrate(
                    AndroidVoiceInputResult.recognized(
                        "Hola Devil",
                    ),
                )

        assertFailsWith<IllegalArgumentException> {
            AndroidMultilingualSpeechRecognitionResult.create(
                status = AndroidMultilingualSpeechRecognitionStatus.AVAILABLE,
                speechRecognition = speechRecognition,
                languageTag = "   ",
            )
        }
    }

    @Test
    fun `deferred result rejects language tag`() {
        val speechRecognition =
            AndroidSpeechRecognitionV2Coordinator()
                .integrate(
                    AndroidVoiceInputResult.noMatch(),
                )

        assertFailsWith<IllegalArgumentException> {
            AndroidMultilingualSpeechRecognitionResult.create(
                status = AndroidMultilingualSpeechRecognitionStatus.DEFERRED,
                speechRecognition = speechRecognition,
                languageTag = "en-US",
            )
        }
    }
}
