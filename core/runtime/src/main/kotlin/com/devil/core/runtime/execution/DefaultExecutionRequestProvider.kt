package com.devil.core.runtime.execution

import com.devil.core.model.execution.ExecutionRequest
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.capability.CapabilitySelectionStatus
import com.devil.core.runtime.executive.ExecutiveReadinessResult
import com.devil.core.runtime.executive.ExecutiveReadinessStatus
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.plan.PlanAuthorityStatus

/**
 * Default Stage 12 constitutional execution-request provider.
 *
 * A request is available only when the Plan Authority produced one PlanRecord,
 * Capability Selection selected one registered capability, and Executive
 * Readiness produced affirmative readiness.
 *
 * Deferred dependencies remain unavailable. Dependency failures preserve their
 * matching errors.
 *
 * This implementation does not establish capability health, check operating-
 * system permission, activate capabilities, execute actions, observe execution,
 * verify outcomes, or report final success.
 */
class DefaultExecutionRequestProvider : ExecutionRequestProvider {

    override fun provide(
        plan: PlanAuthorityResult,
        capability: CapabilitySelectionResult,
        readiness: ExecutiveReadinessResult,
    ): ExecutionRequestResult {
        require(capability.traceId == plan.traceId) {
            "Plan and capability selection results must use the same trace identity."
        }

        require(readiness.traceId == plan.traceId) {
            "Plan and Executive readiness results must use the same trace identity."
        }

        return when (plan.status) {
            PlanAuthorityStatus.CREATED -> {
                when (capability.status) {
                    CapabilitySelectionStatus.SELECTED -> {
                        when (readiness.status) {
                            ExecutiveReadinessStatus.READY ->
                                ExecutionRequestResult.create(
                                    traceId = plan.traceId,
                                    status = ExecutionRequestStatus.AVAILABLE,
                                    request = ExecutionRequest.create(
                                        plan = requireNotNull(plan.plan),
                                        capability =
                                            requireNotNull(capability.capability),
                                    ),
                                )

                            ExecutiveReadinessStatus.DEFERRED ->
                                ExecutionRequestResult.create(
                                    traceId = plan.traceId,
                                    status = ExecutionRequestStatus.UNAVAILABLE,
                                )

                            ExecutiveReadinessStatus.FAILED ->
                                ExecutionRequestResult.create(
                                    traceId = plan.traceId,
                                    status = ExecutionRequestStatus.FAILED,
                                    error = requireNotNull(readiness.error),
                                )
                        }
                    }

                    CapabilitySelectionStatus.DEFERRED ->
                        ExecutionRequestResult.create(
                            traceId = plan.traceId,
                            status = ExecutionRequestStatus.UNAVAILABLE,
                        )

                    CapabilitySelectionStatus.FAILED ->
                        ExecutionRequestResult.create(
                            traceId = plan.traceId,
                            status = ExecutionRequestStatus.FAILED,
                            error = requireNotNull(capability.error),
                        )
                }
            }

            PlanAuthorityStatus.DEFERRED ->
                ExecutionRequestResult.create(
                    traceId = plan.traceId,
                    status = ExecutionRequestStatus.UNAVAILABLE,
                )

            PlanAuthorityStatus.FAILED ->
                ExecutionRequestResult.create(
                    traceId = plan.traceId,
                    status = ExecutionRequestStatus.FAILED,
                    error = requireNotNull(plan.error),
                )
        }
    }
}
