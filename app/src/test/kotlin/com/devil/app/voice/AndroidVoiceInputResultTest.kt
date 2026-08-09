package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class AndroidVoiceInputResultTest {

    @Test
    fun `recognized result preserves normalized transcript`() {
        val result =
            AndroidVoiceInputResult.recognized(
                "  Hello Devil  ",
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
    fun `recognized result rejects blank transcript`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidVoiceInputResult.recognized("   ")
        }
    }

    @Test
    fun `no match result contains no transcript or error`() {
        val result =
            AndroidVoiceInputResult.noMatch()

        assertEquals(
            AndroidVoiceInputStatus.NO_MATCH,
            result.status,
        )
        assertNull(result.transcript)
        assertNull(result.errorCode)
    }

    @Test
    fun `cancelled result contains no transcript or error`() {
        val result =
            AndroidVoiceInputResult.cancelled()

        assertEquals(
            AndroidVoiceInputStatus.CANCELLED,
            result.status,
        )
        assertNull(result.transcript)
        assertNull(result.errorCode)
    }

    @Test
    fun `failed result preserves normalized operational error code`() {
        val result =
            AndroidVoiceInputResult.failed(
                "  SPEECH_RECOGNIZER_ERROR  ",
            )

        assertEquals(
            AndroidVoiceInputStatus.FAILED,
            result.status,
        )
        assertNull(result.transcript)
        assertEquals(
            "SPEECH_RECOGNIZER_ERROR",
            result.errorCode,
        )
    }

    @Test
    fun `failed result rejects blank error code`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidVoiceInputResult.failed("   ")
        }
    }
}
