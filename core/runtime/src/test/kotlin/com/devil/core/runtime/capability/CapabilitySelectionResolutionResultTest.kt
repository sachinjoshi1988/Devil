package com.devil.core.runtime.capability

import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class CapabilitySelectionResolutionResultTest {

    @Test
    fun `create preserves resolved result with capability`() {
        val traceId = TraceId.from(
            "trace-capability-resolution-result-001",
        )
        val capability = createCapability()

        val result = CapabilitySelectionResolutionResult.create(
            traceId = traceId,
            status = CapabilitySelectionResolutionStatus.RESOLVED,
            capability = capability,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            CapabilitySelectionResolutionStatus.RESOLVED,
            result.status,
        )
        assertEquals(capability, result.capability)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result without capability or error`() {
        val traceId = TraceId.from(
            "trace-capability-resolution-result-002",
        )

        val result = CapabilitySelectionResolutionResult.create(
            traceId = traceId,
            status = CapabilitySelectionResolutionStatus.UNAVAILABLE,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            CapabilitySelectionResolutionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.capability)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-capability-resolution-result-003",
        )
        val error = createError(traceId)

        val result = CapabilitySelectionResolutionResult.create(
            traceId = traceId,
            status = CapabilitySelectionResolutionStatus.FAILED,
            error = error,
        )

        assertEquals(
            CapabilitySelectionResolutionStatus.FAILED,
            result.status,
        )
        assertNull(result.capability)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects resolved result without capability`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilitySelectionResolutionResult.create(
                traceId = TraceId.from(
                    "trace-capability-resolution-result-004",
                ),
                status = CapabilitySelectionResolutionStatus.RESOLVED,
            )
        }
    }

    @Test
    fun `create rejects resolved result with error`() {
        val traceId = TraceId.from(
            "trace-capability-resolution-result-005",
        )

        assertFailsWith<IllegalArgumentException> {
            CapabilitySelectionResolutionResult.create(
                traceId = traceId,
                status = CapabilitySelectionResolutionStatus.RESOLVED,
                capability = createCapability(),
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with capability`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilitySelectionResolutionResult.create(
                traceId = TraceId.from(
                    "trace-capability-resolution-result-006",
                ),
                status =
                    CapabilitySelectionResolutionStatus.UNAVAILABLE,
                capability = createCapability(),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilitySelectionResolutionResult.create(
                traceId = TraceId.from(
                    "trace-capability-resolution-result-007",
                ),
                status = CapabilitySelectionResolutionStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilitySelectionResolutionResult.create(
                traceId = TraceId.from(
                    "trace-capability-resolution-result-008",
                ),
                status = CapabilitySelectionResolutionStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-capability-resolution-error-other",
                    ),
                ),
            )
        }
    }

    private fun createCapability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId = CapabilityId.from(
                "capability-camera",
            ),
            category = CapabilityCategory.ACTION,
            name = "Camera",
            description =
                "Performs one bounded registered camera action.",
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "CAPABILITY_SELECTION_RESOLUTION_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_091_500L,
                ),
            summary =
                "Capability selection resolution failed.",
        )
    }
}
