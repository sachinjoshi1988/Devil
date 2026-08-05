package com.devil.core.runtime.plan

import com.devil.core.model.common.TraceId
import com.devil.core.model.plan.PlanCreationRequest

/**
 * Supplies one genuine plan identity for a bounded plan-creation request when
 * an authorized identity policy is available.
 *
 * This provider must not fabricate plan identities. It does not create planning
 * strategy, create plans, bind or authorize capabilities, execute actions,
 * observe results, verify outcomes, or report final outcomes.
 */
interface PlanIdentityProvider {

    fun provide(
        traceId: TraceId,
        request: PlanCreationRequest,
    ): PlanIdentityProvisionResult
}
