package com.devil.core.runtime.memory

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.memory.LogicalMemoryRepresentation
import com.devil.core.model.memory.MemoryClass
import com.devil.core.model.memory.MemoryConfidence
import com.devil.core.model.memory.MemoryContinuityRecord
import com.devil.core.model.memory.MemoryId
import com.devil.core.model.memory.MemoryRecallEligibilityRecord
import com.devil.core.model.memory.MemoryRecallRequest
import com.devil.core.model.memory.MemoryRetention
import com.devil.core.model.memory.MemorySensitivity
import com.devil.core.model.memory.MemorySource
import com.devil.core.model.memory.OwnerVisibleMemoryReason
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class MemoryRecallRequestResultStage105Test {

    @Test
    fun `available result preserves exact recall request`() {
        val traceId =
            TraceId.from(
                "trace-stage-105-result-available",
            )

        val request =
            createRequest(
                traceId = traceId,
            )

        val result =
            MemoryRecallRequestResult.create(
                traceId = traceId,
                status =
                    MemoryRecallRequestStatus.AVAILABLE,
                request = request,
            )

        assertEquals(
            MemoryRecallRequestStatus.AVAILABLE,
            result.status,
        )

        assertSame(
            request,
            result.request,
        )

        assertNull(
            result.error,
        )
    }

    @Test
    fun `unavailable result contains no request or error`() {
        val traceId =
            TraceId.from(
                "trace-stage-105-result-unavailable",
            )

        val result =
            MemoryRecallRequestResult.create(
                traceId = traceId,
                status =
                    MemoryRecallRequestStatus.UNAVAILABLE,
            )

        assertEquals(
            MemoryRecallRequestStatus.UNAVAILABLE,
            result.status,
        )

        assertNull(
            result.request,
        )

        assertNull(
            result.error,
        )
    }

    @Test
    fun `available result requires recall request`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryRecallRequestResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage-105-result-missing",
                    ),
                status =
                    MemoryRecallRequestStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `available result rejects request from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryRecallRequestResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage-105-result-expected",
                    ),
                status =
                    MemoryRecallRequestStatus.AVAILABLE,
                request =
                    createRequest(
                        traceId =
                            TraceId.from(
                                "trace-stage-105-result-other",
                            ),
                    ),
            )
        }
    }

    @Test
    fun `failed result requires matching upstream error`() {
        val traceId =
            TraceId.from(
                "trace-stage-105-result-failed",
            )

        val error =
            createError(
                traceId = traceId,
            )

        val result =
            MemoryRecallRequestResult.create(
                traceId = traceId,
                status =
                    MemoryRecallRequestStatus.FAILED,
                error = error,
            )

        assertEquals(
            MemoryRecallRequestStatus.FAILED,
            result.status,
        )

        assertSame(
            error,
            result.error,
        )

        assertNull(
            result.request,
        )
    }

    @Test
    fun `failed result rejects error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryRecallRequestResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage-105-error-expected",
                    ),
                status =
                    MemoryRecallRequestStatus.FAILED,
                error =
                    createError(
                        traceId =
                            TraceId.from(
                                "trace-stage-105-error-other",
                            ),
                    ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): MemoryRecallRequest {
        val representation =
            LogicalMemoryRepresentation.create(
                memoryId =
                    MemoryId.from(
                        "memory-stage-105-result",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "subject-stage-105-result",
                    ),
                memoryClass =
                    MemoryClass.SEMANTIC,
                sensitivity =
                    MemorySensitivity.PRIVATE,
                confidence =
                    MemoryConfidence.from(
                        91,
                    ),
                retention =
                    MemoryRetention.LONG_TERM,
                source =
                    MemorySource.create(
                        sourceId =
                            "source-stage-105-result",
                        sourceType =
                            "stage-105-test",
                    ),
                ownerVisibleReason =
                    OwnerVisibleMemoryReason.from(
                        "Stage 105 bounded result test.",
                    ),
                content =
                    "Stage 105 recall request result content.",
            )

        return MemoryRecallRequest.create(
            traceId = traceId,
            eligibility =
                MemoryRecallEligibilityRecord.create(
                    continuity =
                        MemoryContinuityRecord.create(
                            representation = representation,
                        ),
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "STAGE_105_TEST_FAILURE",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_105_001_000L,
                ),
            summary =
                "Stage 105 synthetic upstream failure.",
        )
    }
}
