package com.devil.app.permission

import com.devil.app.capability.AndroidCapabilityIntegrationResult
import com.devil.core.model.common.TraceId

/**
 * Stage 176 bounded Android Permission Intelligence result.
 *
 * This result preserves one exact Stage 175 Android Capability Integration result
 * together with one optional Android operating-system permission assessment.
 *
 * It does not:
 *
 * - request or grant Android permission;
 * - grant Devil constitutional authorization;
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
@ConsistentCopyVisibility
data class AndroidPermissionIntelligenceResult private constructor(
    val traceId: TraceId,
    val capabilityIntegration: AndroidCapabilityIntegrationResult,
    val permissionAssessment: AndroidPermissionAssessment?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            capabilityIntegration: AndroidCapabilityIntegrationResult,
            permissionAssessment: AndroidPermissionAssessment? = null,
        ): AndroidPermissionIntelligenceResult {
            require(capabilityIntegration.traceId == traceId) {
                "Android Permission Intelligence and Stage 175 integration must use the same trace identity."
            }

            return AndroidPermissionIntelligenceResult(
                traceId = traceId,
                capabilityIntegration = capabilityIntegration,
                permissionAssessment = permissionAssessment,
            )
        }
    }
}
