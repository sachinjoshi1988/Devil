package com.devil.app.voice

import kotlin.test.Test
import kotlin.test.assertEquals

class VoiceAuthenticationPhrasePolicyTest {

    private val policy =
        VoiceAuthenticationPhrasePolicy()

    @Test
    fun `code red is recognized as authentication request phrase`() {
        val result =
            policy.evaluate(
                "Code Red",
            )

        assertEquals(
            VoiceAuthenticationPhraseStatus.CODE_RED_RECOGNIZED,
            result.status,
        )
    }

    @Test
    fun `code red normalization tolerates punctuation and spacing`() {
        val result =
            policy.evaluate(
                "  CODE,   RED!  ",
            )

        assertEquals(
            VoiceAuthenticationPhraseStatus.CODE_RED_RECOGNIZED,
            result.status,
        )
    }

    @Test
    fun `similar phrase is not accepted as code red`() {
        val result =
            policy.evaluate(
                "Code Ready",
            )

        assertEquals(
            VoiceAuthenticationPhraseStatus.NOT_RECOGNIZED,
            result.status,
        )
    }

    @Test
    fun `wake phrase is not treated as code red`() {
        val result =
            policy.evaluate(
                "Hey Devil",
            )

        assertEquals(
            VoiceAuthenticationPhraseStatus.NOT_RECOGNIZED,
            result.status,
        )
    }
}
