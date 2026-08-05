package com.devil.core.runtime.plan

import com.devil.core.model.common.TraceId
import com.devil.core.model.plan.PlanRecord

/**
 * Translates one bounded constitutional PlanRecord into the stable operational
 * Plan Authority result contract.
 *
 * Plan lifecycle state remains represented by PlanState inside the record.
 * This mapper does not create plans, generate identity, create strategy, bind
 * capabilities, execute actions, observe results, verify outcomes, or report
 * final outcomes.
 */
interface PlanCreationResultMapper {

    fun map(
        traceId: TraceId,
        plan: PlanRecord,
    ): PlanAuthorityResult
}
