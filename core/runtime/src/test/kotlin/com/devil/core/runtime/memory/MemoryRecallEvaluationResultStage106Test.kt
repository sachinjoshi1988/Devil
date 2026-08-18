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

class MemoryRecallEvaluationResultStage106Test {

    @Test
    fun `recallable result preserves exact Stage 105 recall request`() {
        val traceId =
            TraceId.from(
                "trace-stage-106-result-recallable",
            )

        val request =
            createRequest(
                traceId = traceId,
            )

        val result =
            MemoryRecallEvaluationResult.create(
                traceId = traceId,
                status =
                    MemoryRecallEvaluationStatus.RECALLABLE,
                request = request,
            )

        assertEquals(
            MemoryRecallEvaluationStatus.RECALLABLE,
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
        val result =
            MemoryRecallEvaluationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage-106-result-unavailable",
                    ),
                status =
                    MemoryRecallEvaluationStatus.UNAVAILABLE,
            )

        assertEquals(
            MemoryRecallEvaluationStatus.UNAVAILABLE,
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
    fun `recallable result requires recall request`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryRecallEvaluationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage-106-result-missing-request",
                    ),
                status =
                    MemoryRecallEvaluationStatus.RECALLABLE,
            )
        }
    }

    @Test
    fun `recallable result rejects request from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryRecallEvaluationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage-106-result-expected",
                    ),
                status =
                    MemoryRecallEvaluationStatus.RECALLABLE,
                request =
                    createRequest(
                        traceId =
                            TraceId.from(
                                "trace-stage-106-result-other",
                            ),
                    ),
            )
        }
    }

    @Test
    fun `failed result preserves matching error`() {
        val traceId =
            TraceId.from(
                "trace-stage-106-result-failed",
            )

        val error =
            createError(
                traceId = traceId,
            )

        val result =
            MemoryRecallEvaluationResult.create(
                traceId = traceId,
                status =
                    MemoryRecallEvaluationStatus.FAILED,
                error = error,
            )

        assertEquals(
            MemoryRecallEvaluationStatus.FAILED,
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
            MemoryRecallEvaluationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage-106-error-expected",
                    ),
                status =
                    MemoryRecallEvaluationStatus.FAILED,
                error =
                    createError(
                        traceId =
                            TraceId.from(
                                "trace-stage-106-error-other",
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
                        "memory-stage-106-result",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "subject-stage-106-result",
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
                            "source-stage-106-result",
                        sourceType =
                            "stage-106-test",
                    ),
                ownerVisibleReason =
                    OwnerVisibleMemoryReason.from(
                        "Stage 106 bounded result test.",
                    ),
                content =
                    "Stage 106 recall evaluation result content.",
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
                    "STAGE_106_TEST_FAILURE",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_106_001_000L,
                ),
            summary =
                "Stage 106 synthetic recall-evaluation failure.",
        )
    }
}
