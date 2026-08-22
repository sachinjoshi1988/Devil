package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class Stage196WakePhraseV2Test {

    private val policy =
        AndroidWakePhraseV2Policy()

    @Test
    fun `all approved Stage 196 wake phrases establish attention`() {
        val phrases =
            listOf(
                "Devil",
                "Hello Devil",
                "Devil, are you there?",
                "Devil, are you listening?",
            )

        phrases.forEach { phrase ->
            val result =
                policy.evaluate(phrase)

            assertEquals(
                AndroidWakePhraseV2Status.MATCHED,
                result.status,
            )
        }
    }

    @Test
    fun `wake phrase normalization tolerates punctuation case and spacing`() {
        val result =
            policy.evaluate(
                "  HELLO,   DEVIL!!!  ",
            )

        assertEquals(
            AndroidWakePhraseV2Status.MATCHED,
            result.status,
        )
        assertEquals(
            "hello devil",
            result.normalizedTranscript,
        )
    }

    @Test
    fun `code red alone is not a wake phrase`() {
        val result =
            policy.evaluate("Code Red")

        assertEquals(
            AndroidWakePhraseV2Status.NOT_MATCHED,
            result.status,
        )
        assertNull(result.normalizedTranscript)
    }

    @Test
    fun `devil code red is not a Stage 196 wake phrase`() {
        val result =
            policy.evaluate("Devil, Code Red")

        assertEquals(
            AndroidWakePhraseV2Status.NOT_MATCHED,
            result.status,
        )
    }

    @Test
    fun `phrase merely containing devil is not accepted`() {
        val result =
            policy.evaluate(
                "Devil open my messages",
            )

        assertEquals(
            AndroidWakePhraseV2Status.NOT_MATCHED,
            result.status,
        )
    }

    @Test
    fun `matched result requires normalized transcript`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidWakePhraseV2Result.create(
                status = AndroidWakePhraseV2Status.MATCHED,
            )
        }
    }

    @Test
    fun `not matched result rejects transcript`() {
        assertFailsWith<IllegalArgumentException> {
            AndroidWakePhraseV2Result.create(
                status = AndroidWakePhraseV2Status.NOT_MATCHED,
                normalizedTranscript = "devil",
            )
        }
    }
}
