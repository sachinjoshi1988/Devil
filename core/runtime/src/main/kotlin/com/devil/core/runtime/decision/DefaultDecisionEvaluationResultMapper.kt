package com.devil.core.runtime.decision

import com.devil.core.model.common.TraceId
import com.devil.core.model.decision.DecisionRecord

/**
 * Default Stage 7 mapping from bounded DecisionRecord values into the stable
 * DecisionAuthorityResult contract.
 *
 * Every valid DecisionRecord is mapped as PRODUCED. Its SELECTED, DEFERRED,
 * REQUIRES_CLARIFICATION, or REJECTED state remains unchanged inside the
 * record and is not converted into operational deferral or failure.
 *
 * This mapper performs no decision evaluation, decision selection,
 * understanding interpretation, memory creation, task creation, planning,
 * capability authorization, execution, observation, or verification.
 */
class DefaultDecisionEvaluationResultMapper :
    DecisionEvaluationResultMapper {

    override fun map(
        traceId: TraceId,
        decision: DecisionRecord,
    ): DecisionAuthorityResult {
        return DecisionAuthorityResult.create(
            traceId = traceId,
            status = DecisionAuthorityStatus.PRODUCED,
            decision = decision,
        )
    }
}
