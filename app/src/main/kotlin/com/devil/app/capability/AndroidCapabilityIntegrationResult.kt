package com.devil.app.capability

import com.devil.core.model.common.TraceId
import com.devil.core.runtime.capability.CapabilityGovernanceV2Result

/**
 * Stage 175 bounded Android Capability Integration result.
 *
 * This result preserves the core Capability Governance V2 result produced from
 * explicitly supplied Android embodiment availability and health state.
 *
 * It does not:
 *
 * - register or select a capability;
 * - fabricate availability or health;
 * - inspect or grant Android permission;
 * - grant Devil authorization;
 * - establish Executive readiness;
 * - create an ExecutionRequest;
 * - execute an Android action;
 * - observe or verify an effect;
 * - establish Outcome;
 * - or implement Stage 176 Android Permission Intelligence.
 *
 * ANDROID_CAPABILITY_INTEGRATION != ANDROID_PERMISSION.
 * ANDROID_PERMISSION != DEVIL_AUTHORIZATION.
 * CAPABILITY_GOVERNANCE_SATISFIED != EXECUTION_APPROVAL.
 */
@ConsistentCopyVisibility
data class AndroidCapabilityIntegrationResult private constructor(
    val traceId: TraceId,
    val governance: CapabilityGovernanceV2Result,
) {
    companion object {

        fun create(
            traceId: TraceId,
            governance: CapabilityGovernanceV2Result,
        ): AndroidCapabilityIntegrationResult {
            require(governance.traceId == traceId) {
                "Android Capability Integration and Capability Governance V2 result must use the same trace identity."
            }

            return AndroidCapabilityIntegrationResult(
                traceId = traceId,
                governance = governance,
            )
        }
    }
}
