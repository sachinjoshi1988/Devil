package com.devil.core.runtime.decision

import com.devil.core.runtime.understanding.UnderstandingAuthorityResult

/**
 * Supplies a structured constitutional decision-evaluation request when one
 * bounded UnderstandingRecord has been produced.
 *
 * This provider does not evaluate or select a decision, reinterpret
 * understanding, create memory, create tasks, plan work, authorize
 * capabilities, execute actions, observe results, or verify outcomes.
 */
interface DecisionEvaluationRequestProvider {

    fun provide(
        understanding: UnderstandingAuthorityResult,
    ): DecisionEvaluationRequestResult
}
