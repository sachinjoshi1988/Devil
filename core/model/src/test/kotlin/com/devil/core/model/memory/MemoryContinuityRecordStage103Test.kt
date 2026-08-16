package com.devil.core.model.memory

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class MemoryContinuityRecordStage103Test {

    @Test
    fun `continuity record preserves exact logical memory representation`() {
        val representation =
            LogicalMemoryRepresentation.create(
                memoryId =
                    MemoryId.from(
                        "memory-stage-103-record",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "subject-stage-103-record",
                    ),
                memoryClass =
                    MemoryClass.PERSONAL,
                sensitivity =
                    MemorySensitivity.PRIVATE,
                confidence =
                    MemoryConfidence.from(86),
                retention =
                    MemoryRetention.LONG_TERM,
                source =
                    MemorySource.create(
                        sourceId =
                            "source-stage-103-record",
                        sourceType =
                            "explicit-stage-103-test-source",
                    ),
                ownerVisibleReason =
                    OwnerVisibleMemoryReason.from(
                        "Stage 103 continuity test reason.",
                    ),
                content =
                    "Stage 103 logical-memory continuity content.",
            )

        val record =
            MemoryContinuityRecord.create(
                representation = representation,
            )

        assertSame(
            representation,
            record.representation,
        )

        assertEquals(
            MemoryId.from(
                "memory-stage-103-record",
            ),
            record.representation.memoryId,
        )

        assertEquals(
            IdentityId.from(
                "subject-stage-103-record",
            ),
            record.representation.subjectIdentityId,
        )

        assertEquals(
            MemoryClass.PERSONAL,
            record.representation.memoryClass,
        )

        assertEquals(
            MemorySensitivity.PRIVATE,
            record.representation.sensitivity,
        )

        assertEquals(
            MemoryConfidence.from(86),
            record.representation.confidence,
        )

        assertEquals(
            MemoryRetention.LONG_TERM,
            record.representation.retention,
        )
    }
}
