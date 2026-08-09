package com.devil.app.execution

import com.devil.app.capability.AndroidCapabilityState
import com.devil.app.permission.AndroidPermissionAssessment
import com.devil.app.permission.AndroidPermissionAssessmentStatus
import com.devil.core.model.capability.CapabilityAvailabilityState
import com.devil.core.model.capability.CapabilityHealthState
import com.devil.core.runtime.execution.ExecutionResult
import com.devil.core.runtime.execution.ExecutionStatus

/**
 * Default Stage 30 first-safe-execution adapter.
 *
 * This adapter preserves the separation between constitutional execution
 * approval and Android platform execution.
 *
 * APPROVED is necessary but is never sufficient by itself.
 *
 * The selected capability must also be AVAILABLE and healthy READY, and Android
 * permission assessment must be either NOT_REQUIRED or GRANTED.
 *
 * DENIED and UNAVAILABLE Android permission states defer execution. Android
 * permission is never translated into Devil authorization.
 *
 * The default performer currently remains conservative and performs no Android
 * action because no production capability-to-platform-action mapping has yet
 * been approved.
 */
class DefaultAndroidExecutionAdapter(
    private val performer: AndroidExecutionPerformer =
        DefaultAndroidExecutionPerformer(),
) : AndroidExecutionAdapter {

    override fun execute(
        execution: ExecutionResult,
        capabilityState: AndroidCapabilityState,
        permissionAssessment: AndroidPermissionAssessment,
    ): AndroidExecutionAttemptResult {
        return when (execution.status) {
            ExecutionStatus.DEFERRED ->
                AndroidExecutionAttemptResult.create(
                    traceId = execution.traceId,
                    status = AndroidExecutionAttemptStatus.DEFERRED,
                )

            ExecutionStatus.FAILED ->
                AndroidExecutionAttemptResult.create(
                    traceId = execution.traceId,
                    status = AndroidExecutionAttemptStatus.FAILED,
                    error = requireNotNull(execution.error),
                )

            ExecutionStatus.APPROVED -> {
                val request = requireNotNull(execution.request)
                val capability = request.capability

                require(
                    capabilityState.capability.capabilityId ==
                        capability.capabilityId,
                ) {
                    "Android capability state and execution request must refer to the same capability identity."
                }

                require(
                    permissionAssessment.capabilityId ==
                        capability.capabilityId,
                ) {
                    "Android permission assessment and execution request must refer to the same capability identity."
                }

                val capabilityEligible =
                    capabilityState.availability ==
                        CapabilityAvailabilityState.AVAILABLE &&
                        capabilityState.health ==
                        CapabilityHealthState.READY

                val permissionEligible =
                    permissionAssessment.status ==
                        AndroidPermissionAssessmentStatus.NOT_REQUIRED ||
                        permissionAssessment.status ==
                        AndroidPermissionAssessmentStatus.GRANTED

                if (!capabilityEligible || !permissionEligible) {
                    AndroidExecutionAttemptResult.create(
                        traceId = execution.traceId,
                        status = AndroidExecutionAttemptStatus.DEFERRED,
                    )
                } else {
                    val result =
                        performer.perform(
                            traceId = execution.traceId,
                            request = request,
                        )

                    require(result.traceId == execution.traceId) {
                        "Android execution request and performer result must use the same trace identity."
                    }

                    result
                }
            }
        }
    }
}
