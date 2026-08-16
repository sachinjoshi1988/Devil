package com.devil.core.model.memory

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MemorySourceStage101Test {

    @Test
    fun `memory source normalizes explicit provenance fields`() {
        val source =
            MemorySource.create(
                sourceId = "  source-stage101-owner-input  ",
                sourceType = "  owner-supplied  ",
            )

        assertEquals(
            "source-stage101-owner-input",
            source.sourceId,
        )

        assertEquals(
            "owner-supplied",
            source.sourceType,
        )
    }

    @Test
    fun `memory source rejects blank source identity`() {
        assertFailsWith<IllegalArgumentException> {
            MemorySource.create(
                sourceId = "   ",
                sourceType = "owner-supplied",
            )
        }
    }

    @Test
    fun `memory source rejects blank source type`() {
        assertFailsWith<IllegalArgumentException> {
            MemorySource.create(
                sourceId = "source-stage101",
                sourceType = "   ",
            )
        }
    }
}
