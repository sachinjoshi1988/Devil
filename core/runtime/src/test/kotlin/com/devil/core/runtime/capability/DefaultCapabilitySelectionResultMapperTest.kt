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

class DefaultCapabilitySelectionResultMapperTest {

    @Test
    fun `map preserves resolved capability as operationally selected`() {
        val traceId = TraceId.from(
            "trace-capability-result-mapper-001",
        )
        val capability = createCapability()
        val mapper: CapabilitySelectionResultMapper =
            DefaultCapabilitySelectionResultMapper()

        val result = mapper.map(
            traceId = traceId,
            resolution = CapabilitySelectionResolutionResult.create(
                traceId = traceId,
                status =
                    CapabilitySelectionResolutionStatus.RESOLVED,
                capability = capability,
            ),
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            CapabilitySelectionStatus.SELECTED,
            result.status,
        )
        assertEquals(capability, result.capability)
        assertNull(result.error)
    }

    @Test
    fun `map translates unavailable resolution into operational deferral`() {
        val traceId = TraceId.from(
            "trace-capability-result-mapper-002",
        )

        val result =
            DefaultCapabilitySelectionResultMapper().map(
                traceId = traceId,
                resolution =
                    CapabilitySelectionResolutionResult.create(
                        traceId = traceId,
                        status =
                            CapabilitySelectionResolutionStatus.UNAVAILABLE,
                    ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            CapabilitySelectionStatus.DEFERRED,
            result.status,
        )
        assertNull(result.capability)
        assertNull(result.error)
    }

    @Test
    fun `map preserves failed resolution error`() {
        val traceId = TraceId.from(
            "trace-capability-result-mapper-003",
        )
        val error = createError(traceId)

        val result =
            DefaultCapabilitySelectionResultMapper().map(
                traceId = traceId,
                resolution =
                    CapabilitySelectionResolutionResult.create(
                        traceId = traceId,
                        status =
                            CapabilitySelectionResolutionStatus.FAILED,
                        error = error,
                    ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            CapabilitySelectionStatus.FAILED,
            result.status,
        )
        assertNull(result.capability)
        assertEquals(error, result.error)
    }

    @Test
    fun `map does not alter selected capability contract`() {
        val traceId = TraceId.from(
            "trace-capability-result-mapper-004",
        )
        val capability = CapabilityContract.create(
            capabilityId = CapabilityId.from(
                "capability-settings",
            ),
            category = CapabilityCategory.ACTION,
            name = "Settings",
            description =
                "Opens the registered device settings capability.",
        )

        val result =
            DefaultCapabilitySelectionResultMapper().map(
                traceId = traceId,
                resolution =
                    CapabilitySelectionResolutionResult.create(
                        traceId = traceId,
                        status =
                            CapabilitySelectionResolutionStatus.RESOLVED,
                        capability = capability,
                    ),
            )

        assertEquals(capability, result.capability)
        assertEquals(
            "capability-settings",
            result.capability?.capabilityId?.value,
        )
        assertEquals(
            CapabilityCategory.ACTION,
            result.capability?.category,
        )
        assertEquals(
            "Settings",
            result.capability?.name,
        )
    }

    @Test
    fun `map rejects resolution result from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultCapabilitySelectionResultMapper().map(
                traceId = TraceId.from(
                    "trace-capability-result-mapper-005",
                ),
                resolution =
                    CapabilitySelectionResolutionResult.create(
                        traceId = TraceId.from(
                            "trace-capability-resolution-other",
                        ),
                        status =
                            CapabilitySelectionResolutionStatus.UNAVAILABLE,
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
                    1_754_000_092_500L,
                ),
            summary =
                "Capability selection resolution failed.",
        )
    }
}
