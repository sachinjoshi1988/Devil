package com.devil.core.runtime.executive

import com.devil.core.model.executive.ExecutiveReadinessRequest
import com.devil.core.runtime.capability.CapabilitySelectionResult
import com.devil.core.runtime.capability.CapabilitySelectionStatus
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.plan.PlanAuthorityStatus

/**
 * Default Stage 11 constitutional Executive-readiness request provider.
 *
 * A request is available only when the Plan Authority created one bounded
 * PlanRecord and Capability Selection selected one registered capability.
 * Deferred dependencies remain unavailable. Dependency failures preserve their
 * matching error.
 *
 * This implementation does not establish readiness, authorize execution,
 * evaluate capability availability or health, check operating-system
 * permission, execute actions, observe results, verify outcomes, or report
 * final outcomes.
 */
class DefaultExecutiveReadinessRequestProvider :
    ExecutiveReadinessRequestProvider {

    override fun provide(
        plan: PlanAuthorityResult,
        capability: CapabilitySelectionResult,
    ): ExecutiveReadinessRequestResult {
        require(capability.traceId == plan.traceId) {
            "Plan and capability selection results must use the same trace identity."
        }

        return when (plan.status) {
            PlanAuthorityStatus.CREATED -> {
                when (capability.status) {
                    CapabilitySelectionStatus.SELECTED ->
                        ExecutiveReadinessRequestResult.create(
                            traceId = plan.traceId,
                            status =
                                ExecutiveReadinessRequestStatus.AVAILABLE,
                            request = ExecutiveReadinessRequest.create(
                                plan = requireNotNull(plan.plan),
                                capability =
                                    requireNotNull(capability.capability),
                            ),
                        )

                    CapabilitySelectionStatus.DEFERRED ->
                        ExecutiveReadinessRequestResult.create(
                            traceId = plan.traceId,
                            status =
                                ExecutiveReadinessRequestStatus.UNAVAILABLE,
                        )

                    CapabilitySelectionStatus.FAILED ->
                        ExecutiveReadinessRequestResult.create(
                            traceId = plan.traceId,
                            status = ExecutiveReadinessRequestStatus.FAILED,
                            error = requireNotNull(capability.error),
                        )
                }
            }

            PlanAuthorityStatus.DEFERRED ->
                ExecutiveReadinessRequestResult.create(
                    traceId = plan.traceId,
                    status = ExecutiveReadinessRequestStatus.UNAVAILABLE,
                )

            PlanAuthorityStatus.FAILED ->
                ExecutiveReadinessRequestResult.create(
                    traceId = plan.traceId,
                    status = ExecutiveReadinessRequestStatus.FAILED,
                    error = requireNotNull(plan.error),
                )
        }
    }
}
