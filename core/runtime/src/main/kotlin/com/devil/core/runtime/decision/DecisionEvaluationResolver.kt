package com.devil.core.runtime.decision

import com.devil.core.model.decision.DecisionEvaluationRequest
import com.devil.core.model.decision.DecisionRecord

/**
 * Produces one bounded constitutional decision record from a structured
 * decision-evaluation request.
 *
 * This resolver does not reinterpret understanding, create memory, create
 * tasks, plan work, authorize capabilities, execute actions, observe results,
 * or verify outcomes.
 */
interface DecisionEvaluationResolver {

    fun evaluate(
        request: DecisionEvaluationRequest,
    ): DecisionRecord
}
