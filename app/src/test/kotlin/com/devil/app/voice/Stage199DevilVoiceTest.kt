package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class Stage199DevilVoiceTest {

    @Test
    fun `explicit language prepares canonical Devil voice profile`() {
        val result =
            DevilVoiceCoordinator()
                .prepare(
                    "  en-IN  ",
                )

        requireNotNull(result)

        assertEquals(
            DevilVoicePresentation.DEEP_MASCULINE,
            result.presentation,
        )
        assertEquals(
            "en-IN",
            result.languageTag,
        )
        assertEquals(
            0.9f,
            result.speechRate,
        )
        assertEquals(
            0.8f,
            result.pitch,
        )
    }

    @Test
    fun `missing language defers Devil voice preparation`() {
        assertNull(
            DevilVoiceCoordinator()
                .prepare(null),
        )
    }

    @Test
    fun `blank language defers Devil voice preparation`() {
        assertNull(
            DevilVoiceCoordinator()
                .prepare("   "),
        )
    }

    @Test
    fun `profile rejects blank language`() {
        assertFailsWith<IllegalArgumentException> {
            DevilVoiceProfile.create(
                presentation =
                    DevilVoicePresentation.DEEP_MASCULINE,
                languageTag = "   ",
                speechRate = 1.0f,
                pitch = 1.0f,
            )
        }
    }

    @Test
    fun `profile rejects non positive speech rate`() {
        assertFailsWith<IllegalArgumentException> {
            DevilVoiceProfile.create(
                presentation =
                    DevilVoicePresentation.DEEP_MASCULINE,
                languageTag = "en-US",
                speechRate = 0.0f,
                pitch = 1.0f,
            )
        }
    }

    @Test
    fun `profile rejects non positive pitch`() {
        assertFailsWith<IllegalArgumentException> {
            DevilVoiceProfile.create(
                presentation =
                    DevilVoicePresentation.DEEP_MASCULINE,
                languageTag = "en-US",
                speechRate = 1.0f,
                pitch = 0.0f,
            )
        }
    }
}
