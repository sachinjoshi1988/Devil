package com.devil.core.runtime.worldmodel

import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class WorldModelUpdateEvidenceResultTest {

    @Test
    fun `established evidence preserves capability and normalized description`() {
        val traceId =
            TraceId.from(
                "trace-world-model-update-evidence-result-001",
            )

        val capabilityId =
            CapabilityId.from(
                "capability-world-model-update-evidence",
            )

        val result =
            WorldModelUpdateEvidenceResult.create(
                traceId = traceId,
                status =
                    WorldModelUpdateEvidenceStatus.ESTABLISHED,
                capabilityId = capabilityId,
                description =
                    "  Genuine bounded World Model update evidence.  ",
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            WorldModelUpdateEvidenceStatus.ESTABLISHED,
            result.status,
        )
        assertEquals(capabilityId, result.capabilityId)
        assertEquals(
            "Genuine bounded World Model update evidence.",
            result.description,
        )
        assertNull(result.error)
    }

    @Test
    fun `deferred evidence contains no fabricated evidence or error`() {
        val result =
            WorldModelUpdateEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-world-model-update-evidence-result-002",
                    ),
                status =
                    WorldModelUpdateEvidenceStatus.DEFERRED,
            )

        assertNull(result.capabilityId)
        assertNull(result.description)
        assertNull(result.error)
    }

    @Test
    fun `failed evidence preserves matching operational error`() {
        val traceId =
            TraceId.from(
                "trace-world-model-update-evidence-result-003",
            )

        val error =
            createError(traceId)

        val result =
            WorldModelUpdateEvidenceResult.create(
                traceId = traceId,
                status =
                    WorldModelUpdateEvidenceStatus.FAILED,
                error = error,
            )

        assertEquals(
            WorldModelUpdateEvidenceStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
        assertNull(result.capabilityId)
        assertNull(result.description)
    }

    @Test
    fun `established evidence requires capability identity`() {
        assertFailsWith<IllegalArgumentException> {
            WorldModelUpdateEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-world-model-update-evidence-result-004",
                    ),
                status =
                    WorldModelUpdateEvidenceStatus.ESTABLISHED,
                description =
                    "Evidence without capability identity.",
            )
        }
    }

    @Test
    fun `established evidence requires nonblank description`() {
        assertFailsWith<IllegalArgumentException> {
            WorldModelUpdateEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-world-model-update-evidence-result-005",
                    ),
                status =
                    WorldModelUpdateEvidenceStatus.ESTABLISHED,
                capabilityId =
                    CapabilityId.from(
                        "capability-world-model-update-evidence",
                    ),
                description = "   ",
            )
        }
    }

    @Test
    fun `deferred evidence rejects fabricated capability evidence`() {
        assertFailsWith<IllegalArgumentException> {
            WorldModelUpdateEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-world-model-update-evidence-result-006",
                    ),
                status =
                    WorldModelUpdateEvidenceStatus.DEFERRED,
                capabilityId =
                    CapabilityId.from(
                        "capability-world-model-update-evidence",
                    ),
                description =
                    "Deferred World Model update evidence must not contain evidence.",
            )
        }
    }

    @Test
    fun `failed evidence requires error`() {
        assertFailsWith<IllegalArgumentException> {
            WorldModelUpdateEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-world-model-update-evidence-result-007",
                    ),
                status =
                    WorldModelUpdateEvidenceStatus.FAILED,
            )
        }
    }

    @Test
    fun `failed evidence rejects error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            WorldModelUpdateEvidenceResult.create(
                traceId =
                    TraceId.from(
                        "trace-world-model-update-evidence-result-008",
                    ),
                status =
                    WorldModelUpdateEvidenceStatus.FAILED,
                error =
                    createError(
                        TraceId.from(
                            "trace-world-model-update-evidence-error-other",
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
                    "WORLD_MODEL_UPDATE_EVIDENCE_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_680_500L,
                ),
            summary =
                "Bounded World Model update-evidence operation failed.",
        )
    }
}
