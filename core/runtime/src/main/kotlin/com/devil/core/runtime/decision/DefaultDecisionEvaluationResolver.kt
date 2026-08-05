package com.devil.core.runtime.decision

import com.devil.core.model.decision.DecisionEvaluationRequest
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState

/**
 * Default Stage 7 constitutional decision-evaluation resolver.
 *
 * No constitutional decision policy or reasoning engine is available yet.
 * This resolver therefore preserves the supplied UnderstandingRecord and
 * returns DEFERRED rather than selecting, rejecting, or requesting
 * clarification without justified policy evidence.
 *
 * It performs no understanding evaluation, memory creation, task creation,
 * planning, capability authorization, execution, observation, or
 * verification.
 */
class DefaultDecisionEvaluationResolver :
    DecisionEvaluationResolver {

    override fun evaluate(
        request: DecisionEvaluationRequest,
    ): DecisionRecord {
        return DecisionRecord.create(
            understanding = request.understanding,
            state = DecisionState.DEFERRED,
            summary =
                "No constitutional decision policy is available.",
        )
    }
}
