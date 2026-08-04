package com.devil.core.runtime.understanding

import com.devil.core.model.understanding.UnderstandingEvaluationRequest
import com.devil.core.model.understanding.UnderstandingRecord

/**
 * Produces one bounded understanding record from a structured evaluation
 * request.
 *
 * This resolver does not perform conversation intake, create memory, select a
 * decision, create tasks, plan work, authorize capabilities, execute actions,
 * observe results, or verify outcomes.
 */
interface UnderstandingEvaluationResolver {

    fun evaluate(
        request: UnderstandingEvaluationRequest,
    ): UnderstandingRecord
}
