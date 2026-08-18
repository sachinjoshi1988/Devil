package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
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

class Stage106MemoryRecallEvaluationGovernanceTest {

    private val evaluator =
        DefaultMemoryRecallEvaluator()

    @Test
    fun `valid Stage 105 request fails closed when no approved recall source exists`() {
        val traceId =
            TraceId.from(
                "trace-stage-106-unavailable",
            )

        val request =
            createRequest(
                traceId = traceId,
            )

        val result =
            evaluator.evaluate(
                traceId = traceId,
                request = request,
            )

        assertEquals(
            traceId,
            result.traceId,
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
    fun `evaluator rejects Stage 105 request from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            evaluator.evaluate(
                traceId =
                    TraceId.from(
                        "trace-stage-106-expected",
                    ),
                request =
                    createRequest(
                        traceId =
                            TraceId.from(
                                "trace-stage-106-other",
                            ),
                    ),
            )
        }
    }

    @Test
    fun `Stage 106 input preserves exact Stage 105 eligibility and logical memory representation`() {
        val traceId =
            TraceId.from(
                "trace-stage-106-preservation",
            )

        val representation =
            createRepresentation()

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

        val result =
            evaluator.evaluate(
                traceId = traceId,
                request = request,
            )

        assertEquals(
            MemoryRecallEvaluationStatus.UNAVAILABLE,
            result.status,
        )
    }

    @Test
    fun `unavailable Stage 106 evaluation does not become actual recall`() {
        val traceId =
            TraceId.from(
                "trace-stage-106-no-recall",
            )

        val result =
            evaluator.evaluate(
                traceId = traceId,
                request =
                    createRequest(
                        traceId = traceId,
                    ),
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

    private fun createRequest(
        traceId: TraceId,
    ): MemoryRecallRequest {
        return MemoryRecallRequest.create(
            traceId = traceId,
            eligibility =
                MemoryRecallEligibilityRecord.create(
                    continuity =
                        MemoryContinuityRecord.create(
                            representation =
                                createRepresentation(),
                        ),
                ),
        )
    }

    private fun createRepresentation(): LogicalMemoryRepresentation {
        return LogicalMemoryRepresentation.create(
            memoryId =
                MemoryId.from(
                    "memory-stage-106-governance",
                ),
            subjectIdentityId =
                IdentityId.from(
                    "subject-stage-106-governance",
                ),
            memoryClass =
                MemoryClass.SEMANTIC,
            sensitivity =
                MemorySensitivity.PRIVATE,
            confidence =
                MemoryConfidence.from(
                    92,
                ),
            retention =
                MemoryRetention.LONG_TERM,
            source =
                MemorySource.create(
                    sourceId =
                        "source-stage-106-governance",
                    sourceType =
                        "stage-106-test",
                ),
            ownerVisibleReason =
                OwnerVisibleMemoryReason.from(
                    "Stage 106 bounded governance test.",
                ),
            content =
                "Stage 106 recall evaluation governance content.",
        )
    }
}
