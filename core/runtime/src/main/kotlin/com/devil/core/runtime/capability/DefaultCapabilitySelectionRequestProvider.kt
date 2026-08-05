package com.devil.core.runtime.capability

import com.devil.core.model.capability.CapabilitySelectionRequest
import com.devil.core.model.plan.PlanState
import com.devil.core.runtime.plan.PlanAuthorityResult
import com.devil.core.runtime.plan.PlanAuthorityStatus

/**
 * Default Stage 10 constitutional capability-selection request provider.
 *
 * A request is available only when the Plan Authority created one bounded
 * PlanRecord whose PlanState is CREATED. Other planning lifecycle states remain
 * unavailable. Plan failure propagates its matching error.
 *
 * This implementation does not select capabilities, establish availability or
 * health, grant authorization, check operating-system permission, execute
 * actions, observe results, verify outcomes, or report final outcomes.
 */
class DefaultCapabilitySelectionRequestProvider :
    CapabilitySelectionRequestProvider {

    override fun provide(
        plan: PlanAuthorityResult,
    ): CapabilitySelectionRequestResult {
        return when (plan.status) {
            PlanAuthorityStatus.CREATED -> {
                val record = requireNotNull(plan.plan)

                if (record.state == PlanState.CREATED) {
                    CapabilitySelectionRequestResult.create(
                        traceId = plan.traceId,
                        status =
                            CapabilitySelectionRequestStatus.AVAILABLE,
                        request = CapabilitySelectionRequest.create(
                            plan = record,
                        ),
                    )
                } else {
                    CapabilitySelectionRequestResult.create(
                        traceId = plan.traceId,
                        status =
                            CapabilitySelectionRequestStatus.UNAVAILABLE,
                    )
                }
            }

            PlanAuthorityStatus.DEFERRED ->
                CapabilitySelectionRequestResult.create(
                    traceId = plan.traceId,
                    status =
                        CapabilitySelectionRequestStatus.UNAVAILABLE,
                )

            PlanAuthorityStatus.FAILED ->
                CapabilitySelectionRequestResult.create(
                    traceId = plan.traceId,
                    status = CapabilitySelectionRequestStatus.FAILED,
                    error = requireNotNull(plan.error),
                )
        }
    }
}
