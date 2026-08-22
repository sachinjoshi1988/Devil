package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage197SpeechRecognitionV2Test {

    @Test
    fun `recognized Android voice input becomes available with exact provenance`() {
        val input =
            AndroidVoiceInputResult.recognized(
                "  Hello Devil  ",
            )

        val result =
            AndroidSpeechRecognitionV2Coordinator()
                .integrate(input)

        assertEquals(
            AndroidSpeechRecognitionV2Status.AVAILABLE,
            result.status,
        )
        assertSame(input, result.voiceInputResult)
        assertEquals(
            "Hello Devil",
            result.transcript,
        )
    }

    @Test
    fun `no match remains deferred`() {
        val input =
            AndroidVoiceInputResult.noMatch()

        val result =
            AndroidSpeechRecognitionV2Coordinator()
                .integrate(input)

        assertEquals(
            AndroidSpeechRecognitionV2Status.DEFERRED,
            result.status,
        )
        assertSame(input, result.voiceInputResult)
        assertNull(result.transcript)
    }

    @Test
    fun `cancelled recognition remains deferred`() {
        val result =
            AndroidSpeechRecognitionV2Coordinator()
                .integrate(
                    AndroidVoiceInputResult.cancelled(),
                )

        assertEquals(
            AndroidSpeechRecognitionV2Status.DEFERRED,
            result.status,
        )
        assertNull(result.transcript)
    }

    @Test
    fun `failed recognition remains deferred`() {
        val result =
            AndroidSpeechRecognitionV2Coordinator()
                .integrate(
                    AndroidVoiceInputResult.failed(
                        "ANDROID_TEST_FAILURE",
                    ),
                )

        assertEquals(
            AndroidSpeechRecognitionV2Status.DEFERRED,
            result.status,
        )
        assertNull(result.transcript)
    }

    @Test
    fun `available result rejects altered transcript`() {
        val input =
            AndroidVoiceInputResult.recognized(
                "Hello Devil",
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidSpeechRecognitionV2Result.create(
                status = AndroidSpeechRecognitionV2Status.AVAILABLE,
                voiceInputResult = input,
                transcript = "Different transcript",
            )
        }
    }

    @Test
    fun `deferred result rejects recognized input`() {
        val input =
            AndroidVoiceInputResult.recognized(
                "Hello Devil",
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidSpeechRecognitionV2Result.create(
                status = AndroidSpeechRecognitionV2Status.DEFERRED,
                voiceInputResult = input,
            )
        }
    }
}
