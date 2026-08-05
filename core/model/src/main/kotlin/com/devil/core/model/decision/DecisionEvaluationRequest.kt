package com.devil.core.model.decision

import com.devil.core.model.understanding.UnderstandingRecord

/**
 * Represents one structured request for bounded constitutional decision
 * evaluation.
 *
 * The request preserves one completed UnderstandingRecord without reinterpreting
 * its summary or changing its understanding-quality state.
 *
 * This request does not create a decision, create memory, create tasks, plan
 * work, authorize capabilities, execute actions, observe results, or verify
 * outcomes.
 */
@ConsistentCopyVisibility
data class DecisionEvaluationRequest private constructor(
    val understanding: UnderstandingRecord,
) {
    companion object {
        fun create(
            understanding: UnderstandingRecord,
        ): DecisionEvaluationRequest {
            return DecisionEvaluationRequest(
                understanding = understanding,
            )
        }
    }
}
