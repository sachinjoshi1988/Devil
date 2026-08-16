package com.devil.core.model.memory

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MemoryConfidenceStage101Test {

    @Test
    fun `memory confidence accepts lower bound`() {
        assertEquals(
            0,
            MemoryConfidence.from(0).value,
        )
    }

    @Test
    fun `memory confidence accepts upper bound`() {
        assertEquals(
            100,
            MemoryConfidence.from(100).value,
        )
    }

    @Test
    fun `memory confidence rejects value below lower bound`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryConfidence.from(-1)
        }
    }

    @Test
    fun `memory confidence rejects value above upper bound`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryConfidence.from(101)
        }
    }
}
