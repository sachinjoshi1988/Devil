package com.devil.core.model.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class MemoryRecallRequestStage105Test {

    @Test
    fun `recall request preserves exact Stage 104 eligibility record`() {
        val traceId =
            TraceId.from(
                "trace-stage-105-request",
            )

        val representation =
            LogicalMemoryRepresentation.create(
                memoryId =
                    MemoryId.from(
                        "memory-stage-105-request",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "subject-stage-105-request",
                    ),
                memoryClass =
                    MemoryClass.SEMANTIC,
                sensitivity =
                    MemorySensitivity.PRIVATE,
                confidence =
                    MemoryConfidence.from(
                        90,
                    ),
                retention =
                    MemoryRetention.LONG_TERM,
                source =
                    MemorySource.create(
                        sourceId =
                            "source-stage-105-request",
                        sourceType =
                            "stage-105-test",
                    ),
                ownerVisibleReason =
                    OwnerVisibleMemoryReason.from(
                        "Stage 105 bounded request test.",
                    ),
                content =
                    "Stage 105 recall request content.",
            )

        val continuity =
            MemoryContinuityRecord.create(
                representation = representation,
            )

        val eligibility =
            MemoryRecallEligibilityRecord.create(
                continuity = continuity,
            )

        val request =
            MemoryRecallRequest.create(
                traceId = traceId,
                eligibility = eligibility,
            )

        assertEquals(
            traceId,
            request.traceId,
        )

        assertSame(
            eligibility,
            request.eligibility,
        )

        assertSame(
            continuity,
            request.eligibility.continuity,
        )

        assertSame(
            representation,
            request
                .eligibility
                .continuity
                .representation,
        )
    }
}
