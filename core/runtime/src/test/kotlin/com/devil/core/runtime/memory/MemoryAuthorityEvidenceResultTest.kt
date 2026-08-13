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

class MemoryAuthorityEvidenceResultTest {

    @Test
    fun `create preserves established Memory Authority evidence`() {
        val traceId =
            TraceId.from(
                "trace-memory-authority-evidence-result-001",
            )
        val capabilityId =
            CapabilityId.from(
                "capability-camera",
            )

        val result =
            MemoryAuthorityEvidenceResult.create(
                traceId = traceId,
                status =
                    MemoryAuthorityEvidenceStatus.ESTABLISHED,
                capabilityId = capabilityId,
                description =
                    "  Independent Memory Authority evidence was established.  ",
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryAuthorityEvidenceStatus.ESTABLISHED,
            result.status,
        )
        assertEquals(capabilityId, result.capabilityId)
        assertEquals(
            "Independent Memory Authority evidence was established.",
            result.description,
        )
        assertNull(result.error)
    }

    @Test
    fun `create preserves deferred result without evidence or error`() {
        val traceId =
            TraceId.from(
                "trace-memory-authority-evidence-result-002",
            )

        val result =
            MemoryAuthorityEvidenceResult.create(
                traceId = traceId,
                status =
                    MemoryAuthorityEvidenceStatus.DEFERRED,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            MemoryAuthorityEvidenceStatus.DEFERRED,
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
                "trace-memory-authority-evidence-result-003",
            )
        val error = createError(traceId)

        val result =
            MemoryAuthorityEvidenceResult.create(
                traceId = traceId,
                status =
                    MemoryAuthorityEvidenceStatus.FAILED,
                error = error,
            )

        assertEquals(
            MemoryAuthorityEvidenceStatus.FAILED,
            result.status,
        )
        assertNull(result.capabilityId)
        assertNull(result.description)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects established evidence without capability identity`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryAuthorityEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-memory-authority-evidence-result-004",
                    ),
                status =
                    MemoryAuthorityEvidenceStatus.ESTABLISHED,
                description =
                    "Independent Memory Authority evidence.",
            )
        }
    }

    @Test
    fun `create rejects established evidence without description`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryAuthorityEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-memory-authority-evidence-result-005",
                    ),
                status =
                    MemoryAuthorityEvidenceStatus.ESTABLISHED,
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
            MemoryAuthorityEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-memory-authority-evidence-result-006",
                    ),
                status =
                    MemoryAuthorityEvidenceStatus.ESTABLISHED,
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
            MemoryAuthorityEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-memory-authority-evidence-result-007",
                    ),
                status =
                    MemoryAuthorityEvidenceStatus.DEFERRED,
                capabilityId =
                    CapabilityId.from(
                        "capability-camera",
                    ),
                description =
                    "Evidence must not accompany deferred status.",
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryAuthorityEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-memory-authority-evidence-result-008",
                    ),
                status =
                    MemoryAuthorityEvidenceStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            MemoryAuthorityEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-memory-authority-evidence-result-009",
                    ),
                status =
                    MemoryAuthorityEvidenceStatus.FAILED,
                error =
                    createError(
                        TraceId.from(
                            "trace-memory-authority-evidence-error-other",
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
                    "MEMORY_AUTHORITY_EVIDENCE_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_271_000L,
                ),
            summary =
                "Bounded Memory Authority evidence establishment failed.",
        )
    }
}
