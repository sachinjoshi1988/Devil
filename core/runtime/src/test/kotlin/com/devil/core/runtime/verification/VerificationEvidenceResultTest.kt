package com.devil.core.runtime.verification

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class VerificationEvidenceResultTest {

    @Test
    fun `verified evidence preserves capability and normalized description`() {
        val traceId =
            TraceId.from(
                "trace-verification-evidence-result-001",
            )

        val capabilityId =
            CapabilityId.from(
                "capability-verification-evidence",
            )

        val result =
            VerificationEvidenceResult.create(
                traceId = traceId,
                status = VerificationEvidenceStatus.VERIFIED,
                capabilityId = capabilityId,
                description =
                    "  Genuine bounded verification evidence.  ",
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            VerificationEvidenceStatus.VERIFIED,
            result.status,
        )
        assertEquals(capabilityId, result.capabilityId)
        assertEquals(
            "Genuine bounded verification evidence.",
            result.description,
        )
        assertNull(result.error)
    }

    @Test
    fun `deferred evidence contains no fabricated evidence or error`() {
        val result =
            VerificationEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-verification-evidence-result-002",
                    ),
                status = VerificationEvidenceStatus.DEFERRED,
            )

        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `failed evidence preserves matching operational error`() {
        val traceId =
            TraceId.from(
                "trace-verification-evidence-result-003",
            )

        val error = createError(traceId)

        val result =
            VerificationEvidenceResult.create(
                traceId = traceId,
                status = VerificationEvidenceStatus.FAILED,
                error = error,
            )

        assertEquals(
            VerificationEvidenceStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
        assertNull(result.capabilityId)
        assertNull(result.description)
    }

    @Test
    fun `verified evidence requires capability identity`() {
        assertFailsWith<IllegalArgumentException> {
            VerificationEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-verification-evidence-result-004",
                    ),
                status = VerificationEvidenceStatus.VERIFIED,
                description =
                    "Evidence without capability identity.",
            )
        }
    }

    @Test
    fun `verified evidence requires nonblank description`() {
        assertFailsWith<IllegalArgumentException> {
            VerificationEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-verification-evidence-result-005",
                    ),
                status = VerificationEvidenceStatus.VERIFIED,
                capabilityId =
                    CapabilityId.from(
                        "capability-verification-evidence",
                    ),
                description = "   ",
            )
        }
    }

    @Test
    fun `deferred evidence rejects fabricated capability evidence`() {
        assertFailsWith<IllegalArgumentException> {
            VerificationEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-verification-evidence-result-006",
                    ),
                status = VerificationEvidenceStatus.DEFERRED,
                capabilityId =
                    CapabilityId.from(
                        "capability-verification-evidence",
                    ),
                description =
                    "Deferred verification evidence must not contain evidence.",
            )
        }
    }

    @Test
    fun `failed evidence requires error`() {
        assertFailsWith<IllegalArgumentException> {
            VerificationEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-verification-evidence-result-007",
                    ),
                status = VerificationEvidenceStatus.FAILED,
            )
        }
    }

    @Test
    fun `failed evidence rejects error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            VerificationEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-verification-evidence-result-008",
                    ),
                status = VerificationEvidenceStatus.FAILED,
                error =
                    createError(
                        TraceId.from(
                            "trace-verification-evidence-error-other",
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
                    "VERIFICATION_EVIDENCE_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_128_500L,
                ),
            summary =
                "Bounded verification-evidence operation failed.",
        )
    }
}
