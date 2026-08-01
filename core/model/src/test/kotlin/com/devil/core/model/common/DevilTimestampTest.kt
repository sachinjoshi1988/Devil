package com.devil.core.model.common

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DevilTimestampTest {

    @Test
    fun `from epoch milliseconds preserves a valid timestamp`() {
        val timestamp = DevilTimestamp.fromEpochMilliseconds(1_754_000_000_000L)

        assertEquals(1_754_000_000_000L, timestamp.epochMilliseconds)
    }

    @Test
    fun `from epoch milliseconds accepts the Unix epoch`() {
        val timestamp = DevilTimestamp.fromEpochMilliseconds(0L)

        assertEquals(0L, timestamp.epochMilliseconds)
    }

    @Test
    fun `from epoch milliseconds rejects a negative timestamp`() {
        assertFailsWith<IllegalArgumentException> {
            DevilTimestamp.fromEpochMilliseconds(-1L)
        }
    }
}
