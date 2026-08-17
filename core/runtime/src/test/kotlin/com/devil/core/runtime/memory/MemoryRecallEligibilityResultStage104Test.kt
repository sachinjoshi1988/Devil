package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.memory.LogicalMemoryRepresentation
import com.devil.core.model.memory.MemoryClass
import com.devil.core.model.memory.MemoryConfidence
import com.devil.core.model.memory.MemoryContinuityRecord
import com.devil.core.model.memory.MemoryId
import com.devil.core.model.memory.MemoryRecallEligibilityRecord
import com.devil.core.model.memory.MemoryRetention
import com.devil.core.model.memory.MemorySensitivity
import com.devil.core.model.memory.MemorySource
import com.devil.core.model.memory.OwnerVisibleMemoryReason
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class MemoryRecallEligibilityResultStage104Test {

    @Test
    fun `eligible result preserves exact recall eligibility record`() {
        val traceId =
            TraceId.from(
                "trace-stage-104-result",
            )

        val record =
            createEligibilityRecord(
                subjectIdentityId =
                    IdentityId.from(
                        "subject-stage-104-result",
                    ),
            )

        val result =
            MemoryRecallEligibilityResult.create(
                traceId = traceId,
                status =
                    MemoryRecallEligibilityStatus.ELIGIBLE,
                record = record,
            )

        assertEquals(
            traceId,
            result.traceId,
        )

        assertEquals(
            MemoryRecallEligibilityStatus.ELIGIBLE,
            result.status,
        )

        assertSame(
            record,
            result.record,
        )

        assertNull(
            result.error,
        )
    }

    @Test
    fun `deferred result contains no recall eligibility state`() {
        val result =
            MemoryRecallEligibilityResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage-104-deferred",
                    ),
                status =
                    MemoryRecallEligibilityStatus.DEFERRED,
            )

        assertEquals(
            MemoryRecallEligibilityStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.record,
        )

        assertNull(
            result.error,
        )
    }

    @Test
    fun `eligible result requires recall eligibility record`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryRecallEligibilityResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage-104-missing-record",
                    ),
                status =
                    MemoryRecallEligibilityStatus.ELIGIBLE,
            )
        }
    }

    @Test
    fun `failed result requires matching upstream error`() {
        val traceId =
            TraceId.from(
                "trace-stage-104-failed",
            )

        val error =
            createError(
                traceId = traceId,
            )

        val result =
            MemoryRecallEligibilityResult.create(
                traceId = traceId,
                status =
                    MemoryRecallEligibilityStatus.FAILED,
                error = error,
            )

        assertEquals(
            MemoryRecallEligibilityStatus.FAILED,
            result.status,
        )

        assertNull(
            result.record,
        )

        assertSame(
            error,
            result.error,
        )
    }

    @Test
    fun `failed result rejects error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryRecallEligibilityResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage-104-result-a",
                    ),
                status =
                    MemoryRecallEligibilityStatus.FAILED,
                error =
                    createError(
                        traceId =
                            TraceId.from(
                                "trace-stage-104-result-b",
                            ),
                    ),
            )
        }
    }

    private fun createEligibilityRecord(
        subjectIdentityId: IdentityId,
    ): MemoryRecallEligibilityRecord {
        val representation =
            LogicalMemoryRepresentation.create(
                memoryId =
                    MemoryId.from(
                        "memory-stage-104-result",
                    ),
                subjectIdentityId = subjectIdentityId,
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
                            "source-stage-104-result",
                        sourceType =
                            "stage-104-test",
                    ),
                ownerVisibleReason =
                    OwnerVisibleMemoryReason.from(
                        "Stage 104 bounded result test.",
                    ),
                content =
                    "Stage 104 recall eligibility result content.",
            )

        return MemoryRecallEligibilityRecord.create(
            continuity =
                MemoryContinuityRecord.create(
                    representation = representation,
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = com.devil.core.model.error.ErrorCode.from("STAGE_104_TEST_FAILURE"),
            traceId = traceId,
            occurredAt = com.devil.core.model.common.DevilTimestamp.fromEpochMilliseconds(1_754_104_001_000L),
            summary =
                "Stage 104 synthetic upstream failure.",
        )
    }
}
