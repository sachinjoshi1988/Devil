package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class Stage202EmotionalToneAwarenessTest {

    private val coordinator =
        AndroidEmotionalToneAwarenessCoordinator()

    @Test
    fun `calm supplied label maps to calm vocal presentation`() {
        val result =
            coordinator.classify(
                "  calm  ",
            )

        assertEquals(
            AndroidVocalTone.CALM,
            result.vocalTone,
        )
        assertEquals(
            "calm",
            result.sourceLabel,
        )
    }

    @Test
    fun `neutral supplied label maps to neutral vocal presentation`() {
        val result =
            coordinator.classify(
                "neutral",
            )

        assertEquals(
            AndroidVocalTone.NEUTRAL,
            result.vocalTone,
        )
    }

    @Test
    fun `energetic supplied label maps to energetic vocal presentation`() {
        val result =
            coordinator.classify(
                "ENERGETIC",
            )

        assertEquals(
            AndroidVocalTone.ENERGETIC,
            result.vocalTone,
        )
        assertEquals(
            "ENERGETIC",
            result.sourceLabel,
        )
    }

    @Test
    fun `tense supplied label maps to tense vocal presentation`() {
        val result =
            coordinator.classify(
                "tense",
            )

        assertEquals(
            AndroidVocalTone.TENSE,
            result.vocalTone,
        )
    }

    @Test
    fun `unrecognized supplied label remains unknown without emotional inference`() {
        val result =
            coordinator.classify(
                "excited",
            )

        assertEquals(
            AndroidVocalTone.UNKNOWN,
            result.vocalTone,
        )
        assertEquals(
            "excited",
            result.sourceLabel,
        )
    }

    @Test
    fun `blank source label is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            coordinator.classify(
                "   ",
            )
        }
    }

    @Test
    fun `result rejects blank source label`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidEmotionalToneAwarenessResult.create(
                vocalTone = AndroidVocalTone.UNKNOWN,
                sourceLabel = "   ",
            )
        }
    }
}
