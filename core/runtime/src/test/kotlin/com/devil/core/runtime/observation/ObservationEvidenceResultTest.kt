package com.devil.core.runtime.observation

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ObservationEvidenceResultTest {

    @Test
    fun `observed evidence preserves capability and normalized description`() {
        val traceId =
            TraceId.from(
                "trace-observation-evidence-result-001",
            )
        val capabilityId =
            CapabilityId.from(
                "capability-observation-evidence",
            )

        val result =
            ObservationEvidenceResult.create(
                traceId = traceId,
                status = ObservationEvidenceStatus.OBSERVED,
                capabilityId = capabilityId,
                description =
                    "  Genuine bounded observation evidence.  ",
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ObservationEvidenceStatus.OBSERVED,
            result.status,
        )
        assertEquals(capabilityId, result.capabilityId)
        assertEquals(
            "Genuine bounded observation evidence.",
            result.description,
        )
        assertNull(result.error)
    }

    @Test
    fun `deferred evidence contains no fabricated evidence or error`() {
        val result =
            ObservationEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-observation-evidence-result-002",
                    ),
                status = ObservationEvidenceStatus.DEFERRED,
            )

        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `failed evidence preserves matching operational error`() {
        val traceId =
            TraceId.from(
                "trace-observation-evidence-result-003",
            )
        val error = createError(traceId)

        val result =
            ObservationEvidenceResult.create(
                traceId = traceId,
                status = ObservationEvidenceStatus.FAILED,
                error = error,
            )

        assertEquals(
            ObservationEvidenceStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
        assertNull(result.capabilityId)
        assertNull(result.description)
    }

    @Test
    fun `observed evidence requires capability identity`() {
        assertFailsWith<IllegalArgumentException> {
            ObservationEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-observation-evidence-result-004",
                    ),
                status = ObservationEvidenceStatus.OBSERVED,
                description =
                    "Evidence without capability identity.",
            )
        }
    }

    @Test
    fun `observed evidence requires nonblank description`() {
        assertFailsWith<IllegalArgumentException> {
            ObservationEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-observation-evidence-result-005",
                    ),
                status = ObservationEvidenceStatus.OBSERVED,
                capabilityId =
                    CapabilityId.from(
                        "capability-observation-evidence",
                    ),
                description = "   ",
            )
        }
    }

    @Test
    fun `deferred evidence rejects fabricated capability evidence`() {
        assertFailsWith<IllegalArgumentException> {
            ObservationEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-observation-evidence-result-006",
                    ),
                status = ObservationEvidenceStatus.DEFERRED,
                capabilityId =
                    CapabilityId.from(
                        "capability-observation-evidence",
                    ),
                description =
                    "Deferred evidence must not contain evidence.",
            )
        }
    }

    @Test
    fun `failed evidence requires error`() {
        assertFailsWith<IllegalArgumentException> {
            ObservationEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-observation-evidence-result-007",
                    ),
                status = ObservationEvidenceStatus.FAILED,
            )
        }
    }

    @Test
    fun `failed evidence rejects error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            ObservationEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-observation-evidence-result-008",
                    ),
                status = ObservationEvidenceStatus.FAILED,
                error =
                    createError(
                        TraceId.from(
                            "trace-observation-evidence-error-other",
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
                    "OBSERVATION_EVIDENCE_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_118_500L,
                ),
            summary =
                "Bounded observation-evidence operation failed.",
        )
    }
}
