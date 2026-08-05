package com.devil.core.runtime.decision

import com.devil.core.model.common.TraceId
import com.devil.core.model.decision.DecisionRecord

/**
 * Translates one bounded constitutional DecisionRecord into the stable
 * operational Decision Authority result contract.
 *
 * Decision quality remains represented by DecisionState inside the record.
 * This mapper does not reevaluate or select decisions, reinterpret
 * understanding, create memory, create tasks, plan work, authorize
 * capabilities, execute actions, observe results, or verify outcomes.
 */
interface DecisionEvaluationResultMapper {

    fun map(
        traceId: TraceId,
        decision: DecisionRecord,
    ): DecisionAuthorityResult
}
