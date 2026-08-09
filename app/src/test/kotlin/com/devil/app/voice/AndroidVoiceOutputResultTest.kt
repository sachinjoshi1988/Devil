package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class AndroidVoiceOutputResultTest {

    @Test
    fun `spoken result preserves normalized text`() {
        val result =
            AndroidVoiceOutputResult.spoken(
                "  Deferred by the Devil runtime.  ",
            )

        assertEquals(
            AndroidVoiceOutputStatus.SPOKEN,
            result.status,
        )
        assertEquals(
            "Deferred by the Devil runtime.",
            result.spokenText,
        )
        assertNull(result.errorCode)
    }

    @Test
    fun `spoken result rejects blank text`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidVoiceOutputResult.spoken("   ")
        }
    }

    @Test
    fun `unavailable result contains no text or error`() {
        val result =
            AndroidVoiceOutputResult.unavailable()

        assertEquals(
            AndroidVoiceOutputStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.spokenText)
        assertNull(result.errorCode)
    }

    @Test
    fun `cancelled result contains no text or error`() {
        val result =
            AndroidVoiceOutputResult.cancelled()

        assertEquals(
            AndroidVoiceOutputStatus.CANCELLED,
            result.status,
        )
        assertNull(result.spokenText)
        assertNull(result.errorCode)
    }

    @Test
    fun `failed result preserves normalized error code`() {
        val result =
            AndroidVoiceOutputResult.failed(
                "  TTS_FAILED  ",
            )

        assertEquals(
            AndroidVoiceOutputStatus.FAILED,
            result.status,
        )
        assertNull(result.spokenText)
        assertEquals(
            "TTS_FAILED",
            result.errorCode,
        )
    }

    @Test
    fun `failed result rejects blank error code`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidVoiceOutputResult.failed("   ")
        }
    }
}
