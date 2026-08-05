package com.devil.core.runtime.plan

import com.devil.core.model.common.TraceId
import com.devil.core.model.plan.PlanRecord

/**
 * Default Stage 9 mapping from bounded PlanRecord values into the stable
 * PlanAuthorityResult contract.
 *
 * Every valid PlanRecord is mapped as CREATED. Its lifecycle state remains
 * unchanged inside the record and is not converted into operational deferral
 * or failure.
 *
 * This mapper performs no plan creation, identity generation, strategy
 * creation, capability binding, execution, observation, verification, or
 * outcome reporting.
 */
class DefaultPlanCreationResultMapper :
    PlanCreationResultMapper {

    override fun map(
        traceId: TraceId,
        plan: PlanRecord,
    ): PlanAuthorityResult {
        return PlanAuthorityResult.create(
            traceId = traceId,
            status = PlanAuthorityStatus.CREATED,
            plan = plan,
        )
    }
}
