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

class CapabilitySelectionResultTest {

    @Test
    fun `create preserves selected result with capability`() {
        val traceId = TraceId.from("trace-capability-selection-001")
        val capability = createCapability()

        val result = CapabilitySelectionResult.create(
            traceId = traceId,
            status = CapabilitySelectionStatus.SELECTED,
            capability = capability,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(CapabilitySelectionStatus.SELECTED, result.status)
        assertEquals(capability, result.capability)
        assertNull(result.error)
    }

    @Test
    fun `create preserves deferred result without capability or error`() {
        val traceId = TraceId.from("trace-capability-selection-002")

        val result = CapabilitySelectionResult.create(
            traceId = traceId,
            status = CapabilitySelectionStatus.DEFERRED,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(CapabilitySelectionStatus.DEFERRED, result.status)
        assertNull(result.capability)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from("trace-capability-selection-003")
        val error = createError(traceId)

        val result = CapabilitySelectionResult.create(
            traceId = traceId,
            status = CapabilitySelectionStatus.FAILED,
            error = error,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(CapabilitySelectionStatus.FAILED, result.status)
        assertNull(result.capability)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects selected result without capability`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilitySelectionResult.create(
                traceId = TraceId.from("trace-capability-selection-004"),
                status = CapabilitySelectionStatus.SELECTED,
            )
        }
    }

    @Test
    fun `create rejects deferred result with capability`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilitySelectionResult.create(
                traceId = TraceId.from("trace-capability-selection-005"),
                status = CapabilitySelectionStatus.DEFERRED,
                capability = createCapability(),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilitySelectionResult.create(
                traceId = TraceId.from("trace-capability-selection-006"),
                status = CapabilitySelectionStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects non-failed result with error`() {
        val traceId = TraceId.from("trace-capability-selection-007")

        assertFailsWith<IllegalArgumentException> {
            CapabilitySelectionResult.create(
                traceId = traceId,
                status = CapabilitySelectionStatus.SELECTED,
                capability = createCapability(),
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilitySelectionResult.create(
                traceId = TraceId.from("trace-capability-selection-008"),
                status = CapabilitySelectionStatus.FAILED,
                error = createError(
                    TraceId.from("trace-capability-selection-error-other"),
                ),
            )
        }
    }

    private fun createCapability(): CapabilityContract {
        return CapabilityContract.create(
            capabilityId = CapabilityId.from("capability-camera"),
            category = CapabilityCategory.ACTION,
            name = "Camera",
            description = "Opens the device camera.",
        )
    }

    private fun createError(traceId: TraceId): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from("CAPABILITY_SELECTION_FAILED"),
            traceId = traceId,
            occurredAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_022_000L,
            ),
            summary = "Capability selection failed.",
        )
    }
}
