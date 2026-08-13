package com.devil.app.execution

import com.devil.app.capability.AndroidCapabilityStateProvider
import com.devil.app.permission.AndroidPermissionAuthorityAdapter
import com.devil.core.runtime.execution.ExecutionAttemptPort
import com.devil.core.runtime.execution.ExecutionAttemptResult
import com.devil.core.runtime.execution.ExecutionAttemptStatus
import com.devil.core.runtime.execution.ExecutionResult
import com.devil.core.runtime.execution.ExecutionStatus

/**
 * Android implementation of the neutral constitutional ExecutionAttemptPort.
 *
 * This boundary is reached only from the single Unified Devil Runtime after the
 * existing Execution Authority has produced one genuine ExecutionResult.
 *
 * Constitutional execution approval remains necessary but insufficient for an
 * Android platform attempt.
 *
 * For one APPROVED execution request this port independently obtains:
 *
 * - the current Android capability availability and health state;
 * - the current Android operating-system permission assessment;
 * - and the result of the existing bounded AndroidExecutionAdapter.
 *
 * Android permission never becomes Devil constitutional authorization.
 *
 * Capability health READY never becomes Executive readiness.
 *
 * Execution APPROVED != ATTEMPTED.
 * ATTEMPTED != OBSERVED.
 * OBSERVED != VERIFIED.
 * VERIFIED != OUTCOME.
 *
 * ATTEMPTED is returned to core only when the existing Android execution
 * boundary genuinely reports AndroidExecutionAttemptStatus.ATTEMPTED for the
 * exact selected capability.
 *
 * This port does not perform Observation, Verification, Outcome establishment,
 * World Model update, Learning, or Memory operations and creates no second
 * runtime, Brain, Executive, Planner, or Security Authority.
 */
class DefaultAndroidExecutionAttemptPort(
    private val capabilityStateProvider: AndroidCapabilityStateProvider,
    private val permissionAuthorityAdapter: AndroidPermissionAuthorityAdapter,
    private val executionAdapter: AndroidExecutionAdapter,
) : ExecutionAttemptPort {

    override fun attempt(
        execution: ExecutionResult,
    ): ExecutionAttemptResult {
        return when (execution.status) {
            ExecutionStatus.DEFERRED ->
                ExecutionAttemptResult.create(
                    traceId = execution.traceId,
                    status = ExecutionAttemptStatus.DEFERRED,
                )

            ExecutionStatus.FAILED ->
                ExecutionAttemptResult.create(
                    traceId = execution.traceId,
                    status = ExecutionAttemptStatus.FAILED,
                    error = requireNotNull(execution.error),
                )

            ExecutionStatus.APPROVED -> {
                val request = requireNotNull(execution.request)
                val capability = request.capability

                val capabilityState =
                    capabilityStateProvider.stateOf(
                        capability = capability,
                    )

                require(
                    capabilityState.capability.capabilityId ==
                        capability.capabilityId,
                ) {
                    "Android execution-attempt port capability state and constitutional execution request must use the same capability identity."
                }

                val permissionAssessment =
                    permissionAuthorityAdapter.assess(
                        capability = capability,
                    )

                require(
                    permissionAssessment.capabilityId ==
                        capability.capabilityId,
                ) {
                    "Android execution-attempt port permission assessment and constitutional execution request must use the same capability identity."
                }

                val androidAttempt =
                    executionAdapter.execute(
                        execution = execution,
                        capabilityState = capabilityState,
                        permissionAssessment = permissionAssessment,
                    )

                require(androidAttempt.traceId == execution.traceId) {
                    "Android execution-attempt port and Android execution result must use the same trace identity."
                }

                when (androidAttempt.status) {
                    AndroidExecutionAttemptStatus.ATTEMPTED -> {
                        require(
                            androidAttempt.capabilityId ==
                                capability.capabilityId,
                        ) {
                            "Android attempted capability and constitutional execution request must use the same capability identity."
                        }

                        ExecutionAttemptResult.create(
                            traceId = execution.traceId,
                            status = ExecutionAttemptStatus.ATTEMPTED,
                            request = request,
                        )
                    }

                    AndroidExecutionAttemptStatus.DEFERRED ->
                        ExecutionAttemptResult.create(
                            traceId = execution.traceId,
                            status = ExecutionAttemptStatus.DEFERRED,
                        )

                    AndroidExecutionAttemptStatus.FAILED ->
                        ExecutionAttemptResult.create(
                            traceId = execution.traceId,
                            status = ExecutionAttemptStatus.FAILED,
                            error = requireNotNull(androidAttempt.error),
                        )
                }
            }
        }
    }
}
