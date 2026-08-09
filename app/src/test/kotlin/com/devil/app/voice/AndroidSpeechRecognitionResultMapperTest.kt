package com.devil.app.voice

import android.speech.SpeechRecognizer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AndroidSpeechRecognitionResultMapperTest {

    @Test
    fun `first non blank recognition candidate becomes normalized transcript`() {
        val result =
            AndroidSpeechRecognitionResultMapper.fromCandidates(
                candidates =
                    listOf(
                        "   ",
                        "  Hello Devil  ",
                        "Alternative result",
                    ),
            )

        assertEquals(
            AndroidVoiceInputStatus.RECOGNIZED,
            result.status,
        )
        assertEquals(
            "Hello Devil",
            result.transcript,
        )
        assertNull(result.errorCode)
    }

    @Test
    fun `missing candidates produce no match`() {
        val result =
            AndroidSpeechRecognitionResultMapper.fromCandidates(
                candidates = null,
            )

        assertEquals(
            AndroidVoiceInputStatus.NO_MATCH,
            result.status,
        )
        assertNull(result.transcript)
        assertNull(result.errorCode)
    }

    @Test
    fun `blank candidates produce no match`() {
        val result =
            AndroidSpeechRecognitionResultMapper.fromCandidates(
                candidates =
                    listOf(
                        " ",
                        "   ",
                    ),
            )

        assertEquals(
            AndroidVoiceInputStatus.NO_MATCH,
            result.status,
        )
        assertNull(result.transcript)
        assertNull(result.errorCode)
    }

    @Test
    fun `android no match error remains no match`() {
        val result =
            AndroidSpeechRecognitionResultMapper.fromError(
                errorCode = SpeechRecognizer.ERROR_NO_MATCH,
                cancellationRequested = false,
            )

        assertEquals(
            AndroidVoiceInputStatus.NO_MATCH,
            result.status,
        )
        assertNull(result.transcript)
        assertNull(result.errorCode)
    }

    @Test
    fun `android speech timeout remains no match`() {
        val result =
            AndroidSpeechRecognitionResultMapper.fromError(
                errorCode = SpeechRecognizer.ERROR_SPEECH_TIMEOUT,
                cancellationRequested = false,
            )

        assertEquals(
            AndroidVoiceInputStatus.NO_MATCH,
            result.status,
        )
        assertNull(result.transcript)
        assertNull(result.errorCode)
    }

    @Test
    fun `requested cancellation remains cancellation regardless of android error`() {
        val result =
            AndroidSpeechRecognitionResultMapper.fromError(
                errorCode = SpeechRecognizer.ERROR_CLIENT,
                cancellationRequested = true,
            )

        assertEquals(
            AndroidVoiceInputStatus.CANCELLED,
            result.status,
        )
        assertNull(result.transcript)
        assertNull(result.errorCode)
    }

    @Test
    fun `operational recognizer error becomes bounded failure`() {
        val result =
            AndroidSpeechRecognitionResultMapper.fromError(
                errorCode = SpeechRecognizer.ERROR_NETWORK,
                cancellationRequested = false,
            )

        assertEquals(
            AndroidVoiceInputStatus.FAILED,
            result.status,
        )
        assertNull(result.transcript)
        assertEquals(
            "ANDROID_SPEECH_RECOGNIZER_ERROR_${SpeechRecognizer.ERROR_NETWORK}",
            result.errorCode,
        )
    }
}
