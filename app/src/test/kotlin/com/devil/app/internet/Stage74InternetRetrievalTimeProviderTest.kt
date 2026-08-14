package com.devil.app.internet

import com.devil.core.model.common.DevilTimestamp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Stage74InternetRetrievalTimeProviderTest {

    @Test
    fun `retrieval time provider may preserve one explicitly observed instant`() {
        val expected =
            DevilTimestamp.fromEpochMilliseconds(
                1_754_000_201_000L,
            )

        val provider =
            AndroidInternetRetrievalTimeProvider {
                expected
            }

        assertEquals(
            expected,
            provider.observedAt(),
        )
    }

    @Test
    fun `default retrieval time provider produces representable observed time`() {
        val observed =
            DefaultAndroidInternetRetrievalTimeProvider()
                .observedAt()

        assertTrue(
            observed.epochMilliseconds > 0L,
        )
    }
}
