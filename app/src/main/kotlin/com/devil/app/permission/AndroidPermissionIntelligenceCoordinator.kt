package com.devil.app.permission

import com.devil.app.capability.AndroidCapabilityIntegrationResult
import com.devil.core.model.common.TraceId
import com.devil.core.runtime.capability.CapabilityGovernanceV2Status
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.capability.CapabilitySelectionStatus

/**
 * Stage 176 bounded Android Permission Intelligence coordinator.
 *
 * This coordinator consumes one exact Stage 175 Android Capability Integration
 * result and one matching capability selection.
 *
 * Android permission assessment occurs only when Stage 175 capability governance
 * is genuinely SATISFIED.
 *
 * It does not:
 *
 * - register or select capabilities;
 * - fabricate Android permission requirements or grant state;
 * - request or grant Android permission;
 * - grant constitutional authorization;
 * - establish Executive readiness;
 * - create an ExecutionRequest;
 * - execute Android behavior;
 * - observe or verify effects;
 * - establish Outcome;
 * - or implement Stage 177 Application Intelligence.
 *
 * ANDROID_PERMISSION_GRANTED != DEVIL_AUTHORIZATION.
 * PERMISSION_ASSESSED != EXECUTION_APPROVAL.
 */
class AndroidPermissionIntelligenceCoordinator(
    private val permissionAuthorityAdapter: AndroidPermissionAuthorityAdapter,
) {

    fun assess(
        traceId: TraceId,
        capabilityIntegration: AndroidCapabilityIntegrationResult,
        capabilitySelection: CapabilitySelectionResult,
    ): AndroidPermissionIntelligenceResult {
        require(capabilityIntegration.traceId == traceId) {
            "Android Permission Intelligence and Stage 175 integration must use the same trace identity."
        }

        require(capabilitySelection.traceId == traceId) {
            "Android Permission Intelligence and capability selection must use the same trace identity."
        }

        val governance = capabilityIntegration.governance

        val permissionAssessment =
            if (
                governance.status == CapabilityGovernanceV2Status.SATISFIED &&
                capabilitySelection.status == CapabilitySelectionStatus.SELECTED
            ) {
                val capability =
                    requireNotNull(capabilitySelection.capability) {
                        "Selected capability results require a capability."
                    }

                val governedCapability =
                    requireNotNull(governance.record).capability

                require(governedCapability == capability) {
                    "Android Permission Intelligence requires Stage 175 governance and capability selection to preserve the same capability."
                }

                permissionAuthorityAdapter.assess(capability)
            } else {
                null
            }

        return AndroidPermissionIntelligenceResult.create(
            traceId = traceId,
            capabilityIntegration = capabilityIntegration,
            permissionAssessment = permissionAssessment,
        )
    }
}
