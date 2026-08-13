package com.devil.core.runtime.learning

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class LearningEvidenceResultTest {

    @Test
    fun `established evidence preserves capability and normalized description`() {
        val traceId =
            TraceId.from(
                "trace-learning-evidence-result-001",
            )

        val capabilityId =
            CapabilityId.from(
                "capability-learning-evidence",
            )

        val result =
            LearningEvidenceResult.create(
                traceId = traceId,
                status = LearningEvidenceStatus.ESTABLISHED,
                capabilityId = capabilityId,
                description =
                    "  Genuine bounded Learning evidence.  ",
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            LearningEvidenceStatus.ESTABLISHED,
            result.status,
        )
        assertEquals(capabilityId, result.capabilityId)
        assertEquals(
            "Genuine bounded Learning evidence.",
            result.description,
        )
        assertNull(result.error)
    }

    @Test
    fun `deferred evidence contains no fabricated evidence or error`() {
        val result =
            LearningEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-learning-evidence-result-002",
                    ),
                status = LearningEvidenceStatus.DEFERRED,
            )

        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `failed evidence preserves matching operational error`() {
        val traceId =
            TraceId.from(
                "trace-learning-evidence-result-003",
            )
        val error = createError(traceId)

        val result =
            LearningEvidenceResult.create(
                traceId = traceId,
                status = LearningEvidenceStatus.FAILED,
                error = error,
            )

        assertEquals(
            LearningEvidenceStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
        assertNull(result.capabilityId)
        assertNull(result.description)
    }

    @Test
    fun `established evidence requires capability identity`() {
        assertFailsWith<IllegalArgumentException> {
            LearningEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-learning-evidence-result-004",
                    ),
                status = LearningEvidenceStatus.ESTABLISHED,
                description = "Evidence without capability identity.",
            )
        }
    }

    @Test
    fun `established evidence requires nonblank description`() {
        assertFailsWith<IllegalArgumentException> {
            LearningEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-learning-evidence-result-005",
                    ),
                status = LearningEvidenceStatus.ESTABLISHED,
                capabilityId =
                    CapabilityId.from(
                        "capability-learning-evidence",
                    ),
                description = "   ",
            )
        }
    }

    @Test
    fun `deferred evidence rejects fabricated capability evidence`() {
        assertFailsWith<IllegalArgumentException> {
            LearningEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-learning-evidence-result-006",
                    ),
                status = LearningEvidenceStatus.DEFERRED,
                capabilityId =
                    CapabilityId.from(
                        "capability-learning-evidence",
                    ),
                description =
                    "Deferred Learning evidence must not contain evidence.",
            )
        }
    }

    @Test
    fun `failed evidence requires error`() {
        assertFailsWith<IllegalArgumentException> {
            LearningEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-learning-evidence-result-007",
                    ),
                status = LearningEvidenceStatus.FAILED,
            )
        }
    }

    @Test
    fun `failed evidence rejects error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            LearningEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-learning-evidence-result-008",
                    ),
                status = LearningEvidenceStatus.FAILED,
                error =
                    createError(
                        TraceId.from(
                            "trace-learning-evidence-error-other",
                        ),
                    ),
            )
        }
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "LEARNING_EVIDENCE_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_690_500L,
                ),
            summary =
                "Bounded Learning-evidence operation failed.",
        )
    }
}
