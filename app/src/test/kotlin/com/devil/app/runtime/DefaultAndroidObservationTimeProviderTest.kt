package com.devil.app.runtime

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DefaultAndroidObservationTimeProviderTest {

    @Test
    fun `observe preserves current epoch milliseconds`() {
        val provider: AndroidObservationTimeProvider =
            DefaultAndroidObservationTimeProvider(
                currentTimeMilliseconds = {
                    1_754_000_189_000L
                },
            )

        val timestamp = provider.observe()

        assertEquals(
            1_754_000_189_000L,
            timestamp.epochMilliseconds,
        )
    }

    @Test
    fun `observe obtains a fresh clock value for each observation`() {
        val observations = ArrayDeque(
            listOf(
                1_754_000_189_000L,
                1_754_000_189_500L,
            ),
        )

        val provider =
            DefaultAndroidObservationTimeProvider(
                currentTimeMilliseconds = {
                    observations.removeFirst()
                },
            )

        val first = provider.observe()
        val second = provider.observe()

        assertEquals(
            1_754_000_189_000L,
            first.epochMilliseconds,
        )
        assertEquals(
            1_754_000_189_500L,
            second.epochMilliseconds,
        )
    }

    @Test
    fun `observe preserves Devil timestamp validation`() {
        val provider =
            DefaultAndroidObservationTimeProvider(
                currentTimeMilliseconds = {
                    -1L
                },
            )

        assertFailsWith<IllegalArgumentException> {
            provider.observe()
        }
    }
}
