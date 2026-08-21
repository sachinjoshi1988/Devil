package com.devil.app.capability

import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityHealthState
import com.devil.core.model.common.TraceId
import com.devil.core.runtime.capability.CapabilityGovernanceV2Coordinator
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.capability.CapabilitySelectionStatus

/**
 * Stage 175 bounded Android Capability Integration coordinator.
 *
 * This coordinator connects one already-produced constitutional capability
 * selection to the existing Android embodiment capability-state seam and then
 * delegates bounded capability governance to Stage 98.
 *
 * A genuinely SELECTED capability obtains Android embodiment availability and
 * health state.
 *
 * DEFERRED and FAILED capability selection remain upstream constitutional states
 * and are delegated to Stage 98 without fabricating Android capability state.
 *
 * It does not:
 *
 * - register or select capabilities;
 * - fabricate Android capability state;
 * - inspect or grant Android permission;
 * - grant constitutional authorization;
 * - establish Executive readiness;
 * - create an ExecutionRequest;
 * - execute Android behavior;
 * - observe or verify effects;
 * - establish Outcome;
 * - or implement Stage 176 Android Permission Intelligence.
 *
 * Registered != Available != Authorized != Ready != Executed.
 *
 * Android permission != Devil authorization.
 */
class AndroidCapabilityIntegrationCoordinator(
    private val capabilityStateProvider: AndroidCapabilityStateProvider,
    private val governanceCoordinator: CapabilityGovernanceV2Coordinator =
        CapabilityGovernanceV2Coordinator(),
) {

    fun integrate(
        traceId: TraceId,
        capabilitySelection: CapabilitySelectionResult,
    ): AndroidCapabilityIntegrationResult {
        require(capabilitySelection.traceId == traceId) {
            "Android Capability Integration trace and capability selection result must use the same trace identity."
        }

        val governance =
            when (capabilitySelection.status) {
                CapabilitySelectionStatus.SELECTED -> {
                    val capability =
                        requireNotNull(capabilitySelection.capability) {
                            "Selected capability results require a capability."
                        }

                    val state =
                        capabilityStateProvider.stateOf(
                            capability = capability,
                        )

                    require(state.capability == capability) {
                        "Android capability state must preserve the selected capability."
                    }

                    governanceCoordinator.assess(
                        traceId = traceId,
                        capabilitySelection = capabilitySelection,
                        availability = state.availability,
                        health = state.health,
                    )
                }

                CapabilitySelectionStatus.DEFERRED,
                CapabilitySelectionStatus.FAILED,
                -> {
                    governanceCoordinator.assess(
                        traceId = traceId,
                        capabilitySelection = capabilitySelection,
                        availability = CapabilityAvailabilityState.UNAVAILABLE,
                        health = CapabilityHealthState.UNAVAILABLE,
                    )
                }
            }

        return AndroidCapabilityIntegrationResult.create(
            traceId = traceId,
            governance = governance,
        )
    }
}
