package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class WakePhrasePolicyTest {

    private val policy = WakePhrasePolicy()

    @Test
    fun `devil matches approved wake phrase`() {
        val result =
            policy.evaluate("Devil")

        assertEquals(
            WakePhraseMatchStatus.MATCHED,
            result.status,
        )
        assertEquals(
            "devil",
            result.normalizedTranscript,
        )
    }

    @Test
    fun `hey devil matches approved wake phrase`() {
        val result =
            policy.evaluate("  Hey Devil!  ")

        assertEquals(
            WakePhraseMatchStatus.MATCHED,
            result.status,
        )
        assertEquals(
            "hey devil",
            result.normalizedTranscript,
        )
    }

    @Test
    fun `devil are you there matches approved wake phrase`() {
        val result =
            policy.evaluate(
                "Devil, are you there?",
            )

        assertEquals(
            WakePhraseMatchStatus.MATCHED,
            result.status,
        )
        assertEquals(
            "devil are you there",
            result.normalizedTranscript,
        )
    }

    @Test
    fun `ordinary conversation text does not wake devil`() {
        val result =
            policy.evaluate(
                "What is the weather today?",
            )

        assertEquals(
            WakePhraseMatchStatus.NOT_MATCHED,
            result.status,
        )
        assertNull(result.normalizedTranscript)
    }

    @Test
    fun `phrase merely containing devil does not become wake phrase`() {
        val result =
            policy.evaluate(
                "Tell Devil to open something",
            )

        assertEquals(
            WakePhraseMatchStatus.NOT_MATCHED,
            result.status,
        )
        assertNull(result.normalizedTranscript)
    }
}
