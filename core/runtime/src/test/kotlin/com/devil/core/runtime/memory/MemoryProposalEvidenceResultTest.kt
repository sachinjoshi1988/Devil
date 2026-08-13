package com.devil.core.runtime.memory

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class MemoryProposalEvidenceResultTest {

    @Test
    fun `create preserves established evidence`() {
        val traceId =
            TraceId.from(
                "trace-memory-proposal-evidence-result-001",
            )
        val capabilityId =
            CapabilityId.from(
                "capability-camera",
            )

        val result =
            MemoryProposalEvidenceResult.create(
                traceId = traceId,
                status =
                    MemoryProposalEvidenceStatus.ESTABLISHED,
                capabilityId = capabilityId,
                description =
                    "  Genuine bounded Memory Proposal evidence.  ",
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryProposalEvidenceStatus.ESTABLISHED,
            result.status,
        )
        assertEquals(capabilityId, result.capabilityId)
        assertEquals(
            "Genuine bounded Memory Proposal evidence.",
            result.description,
        )
        assertNull(result.error)
    }

    @Test
    fun `create preserves deferred result without evidence or error`() {
        val traceId =
            TraceId.from(
                "trace-memory-proposal-evidence-result-002",
            )

        val result =
            MemoryProposalEvidenceResult.create(
                traceId = traceId,
                status =
                    MemoryProposalEvidenceStatus.DEFERRED,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryProposalEvidenceStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId =
            TraceId.from(
                "trace-memory-proposal-evidence-result-003",
            )
        val error = createError(traceId)

        val result =
            MemoryProposalEvidenceResult.create(
                traceId = traceId,
                status =
                    MemoryProposalEvidenceStatus.FAILED,
                error = error,
            )

        assertEquals(
            MemoryProposalEvidenceStatus.FAILED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects established evidence without capability identity`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryProposalEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-memory-proposal-evidence-result-004",
                    ),
                status =
                    MemoryProposalEvidenceStatus.ESTABLISHED,
                description =
                    "Genuine bounded Memory Proposal evidence.",
            )
        }
    }

    @Test
    fun `create rejects established evidence without description`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryProposalEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-memory-proposal-evidence-result-005",
                    ),
                status =
                    MemoryProposalEvidenceStatus.ESTABLISHED,
                capabilityId =
                    CapabilityId.from(
                        "capability-camera",
                    ),
            )
        }
    }

    @Test
    fun `create rejects established evidence with blank description`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryProposalEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-memory-proposal-evidence-result-006",
                    ),
                status =
                    MemoryProposalEvidenceStatus.ESTABLISHED,
                capabilityId =
                    CapabilityId.from(
                        "capability-camera",
                    ),
                description = "   ",
            )
        }
    }

    @Test
    fun `create rejects deferred result containing evidence`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryProposalEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-memory-proposal-evidence-result-007",
                    ),
                status =
                    MemoryProposalEvidenceStatus.DEFERRED,
                capabilityId =
                    CapabilityId.from(
                        "capability-camera",
                    ),
                description =
                    "Evidence must not accompany deferred state.",
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryProposalEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-memory-proposal-evidence-result-008",
                    ),
                status =
                    MemoryProposalEvidenceStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryProposalEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-memory-proposal-evidence-result-009",
                    ),
                status =
                    MemoryProposalEvidenceStatus.FAILED,
                error =
                    createError(
                        TraceId.from(
                            "trace-memory-proposal-evidence-error-other",
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
                    "MEMORY_PROPOSAL_EVIDENCE_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_180_500L,
                ),
            summary =
                "Bounded constitutional Memory Proposal evidence failed.",
        )
    }
}
