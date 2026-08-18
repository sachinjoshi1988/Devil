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
import com.devil.core.model.memory.MemoryRetention
import com.devil.core.model.memory.MemorySensitivity
import com.devil.core.model.memory.MemorySource
import com.devil.core.model.memory.OwnerVisibleMemoryReason
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage105MemoryRecallRequestGovernanceTest {

    private val provider =
        DefaultMemoryRecallRequestProvider()

    @Test
    fun `eligible Stage 104 result produces bounded recall request`() {
        val traceId =
            TraceId.from(
                "trace-stage-105-eligible",
            )

        val eligibility =
            eligible(
                traceId = traceId,
            )

        val eligibilityRecord =
            requireNotNull(
                eligibility.record,
            )

        val result =
            provider.provide(
                eligibility = eligibility,
            )

        assertEquals(
            MemoryRecallRequestStatus.AVAILABLE,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val request =
            requireNotNull(
                result.request,
            )

        assertEquals(
            traceId,
            request.traceId,
        )

        assertSame(
            eligibilityRecord,
            request.eligibility,
        )

        assertSame(
            eligibilityRecord.continuity,
            request.eligibility.continuity,
        )

        assertSame(
            eligibilityRecord
                .continuity
                .representation,
            request
                .eligibility
                .continuity
                .representation,
        )

        assertNull(
            result.error,
        )
    }

    @Test
    fun `deferred Stage 104 result produces unavailable recall request`() {
        val traceId =
            TraceId.from(
                "trace-stage-105-deferred",
            )

        val result =
            provider.provide(
                eligibility =
                    MemoryRecallEligibilityResult.create(
                        traceId = traceId,
                        status =
                            MemoryRecallEligibilityStatus.DEFERRED,
                    ),
            )

        assertEquals(
            MemoryRecallRequestStatus.UNAVAILABLE,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        assertNull(
            result.request,
        )

        assertNull(
            result.error,
        )
    }

    @Test
    fun `failed Stage 104 result propagates matching error`() {
        val traceId =
            TraceId.from(
                "trace-stage-105-failed",
            )

        val error =
            createError(
                traceId = traceId,
            )

        val result =
            provider.provide(
                eligibility =
                    MemoryRecallEligibilityResult.create(
                        traceId = traceId,
                        status =
                            MemoryRecallEligibilityStatus.FAILED,
                        error = error,
                    ),
            )

        assertEquals(
            MemoryRecallRequestStatus.FAILED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
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
    fun `available request preserves existing memory identity without independent redirect`() {
        val traceId =
            TraceId.from(
                "trace-stage-105-identity-preservation",
            )

        val eligibility =
            eligible(
                traceId = traceId,
            )

        val originalRepresentation =
            requireNotNull(
                eligibility.record,
            )
                .continuity
                .representation

        val result =
            provider.provide(
                eligibility = eligibility,
            )

        val request =
            requireNotNull(
                result.request,
            )

        val preservedRepresentation =
            request
                .eligibility
                .continuity
                .representation

        assertSame(
            originalRepresentation,
            preservedRepresentation,
        )

        assertEquals(
            originalRepresentation.memoryId,
            preservedRepresentation.memoryId,
        )

        assertEquals(
            originalRepresentation.subjectIdentityId,
            preservedRepresentation.subjectIdentityId,
        )
    }

    private fun eligible(
        traceId: TraceId,
    ): MemoryRecallEligibilityResult {
        val representation =
            LogicalMemoryRepresentation.create(
                memoryId =
                    MemoryId.from(
                        "memory-stage-105-governance",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "subject-stage-105-governance",
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
                            "source-stage-105-governance",
                        sourceType =
                            "stage-105-test",
                    ),
                ownerVisibleReason =
                    OwnerVisibleMemoryReason.from(
                        "Stage 105 bounded governance test.",
                    ),
                content =
                    "Stage 105 recall request governance content.",
            )

        return MemoryRecallEligibilityResult.create(
            traceId = traceId,
            status =
                MemoryRecallEligibilityStatus.ELIGIBLE,
            record =
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
                    "STAGE_105_GOVERNANCE_FAILURE",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_105_002_000L,
                ),
            summary =
                "Stage 105 synthetic governance failure.",
        )
    }
}
