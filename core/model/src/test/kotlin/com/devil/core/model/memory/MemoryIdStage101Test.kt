package com.devil.core.model.memory

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MemoryIdStage101Test {

    @Test
    fun `memory id trims and preserves nonblank identity`() {
        val memoryId =
            MemoryId.from(
                "  memory-stage101-001  ",
            )

        assertEquals(
            "memory-stage101-001",
            memoryId.value,
        )
    }

    @Test
    fun `memory id rejects blank identity`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryId.from("   ")
        }
    }
}
