package com.devil.app.capability

import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityCategory
import com.devil.core.model.capability.CapabilityContract
import com.devil.core.model.capability.CapabilityHealthState
import com.devil.core.model.capability.CapabilityId
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.runtime.capability.CapabilityGovernanceV2Status
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.capability.CapabilitySelectionStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage175AndroidCapabilityIntegrationTest {

    @Test
    fun `selected available ready Android capability satisfies bounded governance`() {
        val traceId = TraceId.from("trace-stage-175-ready")
        val capability = capability("capability-stage-175-ready")

        val coordinator =
            AndroidCapabilityIntegrationCoordinator(
                capabilityStateProvider =
                    AndroidCapabilityStateProvider { selected ->
                        AndroidCapabilityState.create(
                            capability = selected,
                            availability =
                                CapabilityAvailabilityState.AVAILABLE,
                            health = CapabilityHealthState.READY,
                        )
                    },
            )

        val result =
            coordinator.integrate(
                traceId = traceId,
                capabilitySelection =
                    CapabilitySelectionResult.create(
                        traceId = traceId,
                        status = CapabilitySelectionStatus.SELECTED,
                        capability = capability,
                    ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            CapabilityGovernanceV2Status.SATISFIED,
            result.governance.status,
        )

        val record = requireNotNull(result.governance.record)

        assertSame(capability, record.capability)
        assertEquals(
            CapabilityAvailabilityState.AVAILABLE,
            record.availability,
        )
        assertEquals(
            CapabilityHealthState.READY,
            record.health,
        )
    }

    @Test
    fun `selected unavailable Android capability defers governance`() {
        val traceId = TraceId.from("trace-stage-175-unavailable")
        val capability = capability("capability-stage-175-unavailable")

        val coordinator =
            AndroidCapabilityIntegrationCoordinator(
                capabilityStateProvider =
                    AndroidCapabilityStateProvider { selected ->
                        AndroidCapabilityState.create(
                            capability = selected,
                            availability =
                                CapabilityAvailabilityState.UNAVAILABLE,
                            health = CapabilityHealthState.READY,
                        )
                    },
            )

        val result =
            coordinator.integrate(
                traceId = traceId,
                capabilitySelection =
                    CapabilitySelectionResult.create(
                        traceId = traceId,
                        status = CapabilitySelectionStatus.SELECTED,
                        capability = capability,
                    ),
            )

        assertEquals(
            CapabilityGovernanceV2Status.DEFERRED,
            result.governance.status,
        )
        assertEquals(null, result.governance.record)
        assertEquals(null, result.governance.error)
    }

    @Test
    fun `selected degraded Android capability defers governance`() {
        val traceId = TraceId.from("trace-stage-175-degraded")
        val capability = capability("capability-stage-175-degraded")

        val coordinator =
            AndroidCapabilityIntegrationCoordinator(
                capabilityStateProvider =
                    AndroidCapabilityStateProvider { selected ->
                        AndroidCapabilityState.create(
                            capability = selected,
                            availability =
                                CapabilityAvailabilityState.AVAILABLE,
                            health = CapabilityHealthState.DEGRADED,
                        )
                    },
            )

        val result =
            coordinator.integrate(
                traceId = traceId,
                capabilitySelection =
                    CapabilitySelectionResult.create(
                        traceId = traceId,
                        status = CapabilitySelectionStatus.SELECTED,
                        capability = capability,
                    ),
            )

        assertEquals(
            CapabilityGovernanceV2Status.DEFERRED,
            result.governance.status,
        )
    }

    @Test
    fun `deferred capability selection remains deferred without requesting Android state`() {
        val traceId = TraceId.from("trace-stage-175-deferred")
        var stateRequested = false

        val coordinator =
            AndroidCapabilityIntegrationCoordinator(
                capabilityStateProvider =
                    AndroidCapabilityStateProvider {
                        stateRequested = true
                        error("Android state must not be requested.")
                    },
            )

        val result =
            coordinator.integrate(
                traceId = traceId,
                capabilitySelection =
                    CapabilitySelectionResult.create(
                        traceId = traceId,
                        status = CapabilitySelectionStatus.DEFERRED,
                    ),
            )

        assertEquals(false, stateRequested)
        assertEquals(
            CapabilityGovernanceV2Status.DEFERRED,
            result.governance.status,
        )
    }

    @Test
    fun `Android state must preserve exact selected capability`() {
        val traceId = TraceId.from("trace-stage-175-provenance")
        val selected = capability("capability-stage-175-selected")
        val substituted = capability("capability-stage-175-substituted")

        val coordinator =
            AndroidCapabilityIntegrationCoordinator(
                capabilityStateProvider =
                    AndroidCapabilityStateProvider {
                        AndroidCapabilityState.create(
                            capability = substituted,
                            availability =
                                CapabilityAvailabilityState.AVAILABLE,
                            health = CapabilityHealthState.READY,
                        )
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator.integrate(
                traceId = traceId,
                capabilitySelection =
                    CapabilitySelectionResult.create(
                        traceId = traceId,
                        status = CapabilitySelectionStatus.SELECTED,
                        capability = selected,
                    ),
            )
        }
    }

    @Test
    fun `integration result rejects mismatched trace provenance`() {
        val integrationTrace =
            TraceId.from("trace-stage-175-integration")
        val governanceTrace =
            TraceId.from("trace-stage-175-governance")

        val governance =
            com.devil.core.runtime.capability.CapabilityGovernanceV2Result.create(
                traceId = governanceTrace,
                status = CapabilityGovernanceV2Status.DEFERRED,
            )

        assertFailsWith<IllegalArgumentException> {
            AndroidCapabilityIntegrationResult.create(
                traceId = integrationTrace,
                governance = governance,
            )
        }
    }

    private fun capability(
        id: String,
    ): CapabilityContract {
        return CapabilityContract.create(
            capabilityId = CapabilityId.from(id),
            category = CapabilityCategory.ACTION,
            name = "Stage 175 Android Capability",
            description =
                "Represents one bounded Android capability integration test contract.",
        )
    }
}
