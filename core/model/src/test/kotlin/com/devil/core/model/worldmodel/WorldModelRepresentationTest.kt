package com.devil.core.model.worldmodel

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class WorldModelRepresentationTest {

    @Test
    fun `create preserves evidence-backed representation provenance`() {
        val traceId = TraceId.from(
            "trace-world-model-representation-001",
        )
        val capabilityId = CapabilityId.from(
            "capability-camera",
        )

        val representation = WorldModelRepresentation.create(
            traceId = traceId,
            capabilityId = capabilityId,
            description =
                "The foreground application was genuinely established as Camera.",
        )

        assertEquals(traceId, representation.traceId)
        assertEquals(capabilityId, representation.capabilityId)
        assertEquals(
            "The foreground application was genuinely established as Camera.",
            representation.description,
        )
    }

    @Test
    fun `create normalizes evidence-backed description`() {
        val representation = WorldModelRepresentation.create(
            traceId = TraceId.from(
                "trace-world-model-representation-002",
            ),
            capabilityId = CapabilityId.from(
                "capability-camera",
            ),
            description =
                "  Camera application state was genuinely established.  ",
        )

        assertEquals(
            "Camera application state was genuinely established.",
            representation.description,
        )
    }

    @Test
    fun `create rejects blank representation description`() {
        assertFailsWith<IllegalArgumentException> {
            WorldModelRepresentation.create(
                traceId = TraceId.from(
                    "trace-world-model-representation-003",
                ),
                capabilityId = CapabilityId.from(
                    "capability-camera",
                ),
                description = "   ",
            )
        }
    }
}
