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

class CapabilityRegistryResultTest {

    @Test
    fun `create preserves available registered capabilities`() {
        val traceId = TraceId.from(
            "trace-capability-registry-result-001",
        )
        val capabilities = listOf(
            createCapability(
                id = "capability-camera",
                name = "Camera",
            ),
            createCapability(
                id = "capability-settings",
                name = "Settings",
            ),
        )

        val result = CapabilityRegistryResult.create(
            traceId = traceId,
            status = CapabilityRegistryStatus.AVAILABLE,
            capabilities = capabilities,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            CapabilityRegistryStatus.AVAILABLE,
            result.status,
        )
        assertEquals(capabilities, result.capabilities)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result without capabilities or error`() {
        val traceId = TraceId.from(
            "trace-capability-registry-result-002",
        )

        val result = CapabilityRegistryResult.create(
            traceId = traceId,
            status = CapabilityRegistryStatus.UNAVAILABLE,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            CapabilityRegistryStatus.UNAVAILABLE,
            result.status,
        )
        assertEquals(emptyList(), result.capabilities)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-capability-registry-result-003",
        )
        val error = createError(traceId)

        val result = CapabilityRegistryResult.create(
            traceId = traceId,
            status = CapabilityRegistryStatus.FAILED,
            error = error,
        )

        assertEquals(
            CapabilityRegistryStatus.FAILED,
            result.status,
        )
        assertEquals(emptyList(), result.capabilities)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without capabilities`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilityRegistryResult.create(
                traceId = TraceId.from(
                    "trace-capability-registry-result-004",
                ),
                status = CapabilityRegistryStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects duplicate capability identities`() {
        val capability = createCapability(
            id = "capability-camera",
            name = "Camera",
        )

        assertFailsWith<IllegalArgumentException> {
            CapabilityRegistryResult.create(
                traceId = TraceId.from(
                    "trace-capability-registry-result-005",
                ),
                status = CapabilityRegistryStatus.AVAILABLE,
                capabilities = listOf(
                    capability,
                    capability,
                ),
            )
        }
    }

    @Test
    fun `create rejects available result with error`() {
        val traceId = TraceId.from(
            "trace-capability-registry-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            CapabilityRegistryResult.create(
                traceId = traceId,
                status = CapabilityRegistryStatus.AVAILABLE,
                capabilities = listOf(
                    createCapability(
                        id = "capability-camera",
                        name = "Camera",
                    ),
                ),
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with capabilities`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilityRegistryResult.create(
                traceId = TraceId.from(
                    "trace-capability-registry-result-007",
                ),
                status = CapabilityRegistryStatus.UNAVAILABLE,
                capabilities = listOf(
                    createCapability(
                        id = "capability-camera",
                        name = "Camera",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilityRegistryResult.create(
                traceId = TraceId.from(
                    "trace-capability-registry-result-008",
                ),
                status = CapabilityRegistryStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            CapabilityRegistryResult.create(
                traceId = TraceId.from(
                    "trace-capability-registry-result-009",
                ),
                status = CapabilityRegistryStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-capability-registry-error-other",
                    ),
                ),
            )
        }
    }

    private fun createCapability(
        id: String,
        name: String,
    ): CapabilityContract {
        return CapabilityContract.create(
            capabilityId = CapabilityId.from(id),
            category = CapabilityCategory.ACTION,
            name = name,
            description =
                "Performs one bounded registered action.",
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "CAPABILITY_REGISTRY_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_090_500L,
                ),
            summary = "Capability registry access failed.",
        )
    }
}
