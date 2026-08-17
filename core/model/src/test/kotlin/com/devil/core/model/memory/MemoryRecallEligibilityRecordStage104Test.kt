package com.devil.core.model.memory

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class MemoryRecallEligibilityRecordStage104Test {

    @Test
    fun `recall eligibility record preserves exact Stage 103 continuity record`() {
        val representation =
            LogicalMemoryRepresentation.create(
                memoryId =
                    MemoryId.from(
                        "memory-stage-104-record",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "subject-stage-104-record",
                    ),
                memoryClass =
                    MemoryClass.SEMANTIC,
                sensitivity =
                    MemorySensitivity.PRIVATE,
                confidence =
                    MemoryConfidence.from(
                        86,
                    ),
                retention =
                    MemoryRetention.LONG_TERM,
                source =
                    MemorySource.create(
                        sourceId =
                            "source-stage-104-record",
                        sourceType =
                            "stage-104-test",
                    ),
                ownerVisibleReason =
                    OwnerVisibleMemoryReason.from(
                        "Preserve bounded recall eligibility provenance.",
                    ),
                content =
                    "Stage 104 recall eligibility test content.",
            )

        val continuity =
            MemoryContinuityRecord.create(
                representation = representation,
            )

        val record =
            MemoryRecallEligibilityRecord.create(
                continuity = continuity,
            )

        assertSame(
            continuity,
            record.continuity,
        )

        assertSame(
            representation,
            record.continuity.representation,
        )

        assertEquals(
            MemoryId.from(
                "memory-stage-104-record",
            ),
            record.continuity.representation.memoryId,
        )

        assertEquals(
            IdentityId.from(
                "subject-stage-104-record",
            ),
            record.continuity.representation.subjectIdentityId,
        )

        assertEquals(
            MemoryClass.SEMANTIC,
            record.continuity.representation.memoryClass,
        )

        assertEquals(
            MemorySensitivity.PRIVATE,
            record.continuity.representation.sensitivity,
        )

        assertEquals(
            MemoryConfidence.from(
                86,
            ),
            record.continuity.representation.confidence,
        )

        assertEquals(
            MemoryRetention.LONG_TERM,
            record.continuity.representation.retention,
        )
    }
}
