package com.devil.core.runtime.outcome

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class OutcomeEvidenceResultTest {

    @Test
    fun `established evidence preserves capability and normalized description`() {
        val traceId =
            TraceId.from(
                "trace-outcome-evidence-result-001",
            )

        val capabilityId =
            CapabilityId.from(
                "capability-outcome-evidence",
            )

        val result =
            OutcomeEvidenceResult.create(
                traceId = traceId,
                status = OutcomeEvidenceStatus.ESTABLISHED,
                capabilityId = capabilityId,
                description =
                    "  Genuine bounded outcome evidence.  ",
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            OutcomeEvidenceStatus.ESTABLISHED,
            result.status,
        )
        assertEquals(capabilityId, result.capabilityId)
        assertEquals(
            "Genuine bounded outcome evidence.",
            result.description,
        )
        assertNull(result.error)
    }

    @Test
    fun `deferred evidence contains no fabricated evidence or error`() {
        val result =
            OutcomeEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-outcome-evidence-result-002",
                    ),
                status = OutcomeEvidenceStatus.DEFERRED,
            )

        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `failed evidence preserves matching operational error`() {
        val traceId =
            TraceId.from(
                "trace-outcome-evidence-result-003",
            )

        val error = createError(traceId)

        val result =
            OutcomeEvidenceResult.create(
                traceId = traceId,
                status = OutcomeEvidenceStatus.FAILED,
                error = error,
            )

        assertEquals(
            OutcomeEvidenceStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
        assertNull(result.capabilityId)
        assertNull(result.description)
    }

    @Test
    fun `established evidence requires capability identity`() {
        assertFailsWith<IllegalArgumentException> {
            OutcomeEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-outcome-evidence-result-004",
                    ),
                status = OutcomeEvidenceStatus.ESTABLISHED,
                description =
                    "Evidence without capability identity.",
            )
        }
    }

    @Test
    fun `established evidence requires nonblank description`() {
        assertFailsWith<IllegalArgumentException> {
            OutcomeEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-outcome-evidence-result-005",
                    ),
                status = OutcomeEvidenceStatus.ESTABLISHED,
                capabilityId =
                    CapabilityId.from(
                        "capability-outcome-evidence",
                    ),
                description = "   ",
            )
        }
    }

    @Test
    fun `deferred evidence rejects fabricated capability evidence`() {
        assertFailsWith<IllegalArgumentException> {
            OutcomeEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-outcome-evidence-result-006",
                    ),
                status = OutcomeEvidenceStatus.DEFERRED,
                capabilityId =
                    CapabilityId.from(
                        "capability-outcome-evidence",
                    ),
                description =
                    "Deferred outcome evidence must not contain evidence.",
            )
        }
    }

    @Test
    fun `failed evidence requires error`() {
        assertFailsWith<IllegalArgumentException> {
            OutcomeEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-outcome-evidence-result-007",
                    ),
                status = OutcomeEvidenceStatus.FAILED,
            )
        }
    }

    @Test
    fun `failed evidence rejects error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            OutcomeEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-outcome-evidence-result-008",
                    ),
                status = OutcomeEvidenceStatus.FAILED,
                error =
                    createError(
                        TraceId.from(
                            "trace-outcome-evidence-error-other",
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
                    "OUTCOME_EVIDENCE_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_136_500L,
                ),
            summary =
                "Bounded outcome-evidence operation failed.",
        )
    }
}
