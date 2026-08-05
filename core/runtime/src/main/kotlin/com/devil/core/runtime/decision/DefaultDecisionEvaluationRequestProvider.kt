package com.devil.core.runtime.decision

import com.devil.core.model.decision.DecisionEvaluationRequest
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityStatus

/**
 * Default Stage 7 constitutional decision-evaluation request provider.
 *
 * A request is available only when the Understanding Authority produced one
 * bounded UnderstandingRecord. Deferred understanding remains unavailable.
 * Understanding failure propagates its matching error.
 *
 * This implementation does not reinterpret understanding, evaluate or select a
 * decision, create memory, create tasks, plan work, authorize capabilities,
 * execute actions, observe results, or verify outcomes.
 */
class DefaultDecisionEvaluationRequestProvider :
    DecisionEvaluationRequestProvider {

    override fun provide(
        understanding: UnderstandingAuthorityResult,
    ): DecisionEvaluationRequestResult {
        return when (understanding.status) {
            UnderstandingAuthorityStatus.PRODUCED ->
                DecisionEvaluationRequestResult.create(
                    traceId = understanding.traceId,
                    status =
                        DecisionEvaluationRequestStatus.AVAILABLE,
                    request = DecisionEvaluationRequest.create(
                        understanding =
                            requireNotNull(
                                understanding.understanding,
                            ),
                    ),
                )

            UnderstandingAuthorityStatus.DEFERRED ->
                DecisionEvaluationRequestResult.create(
                    traceId = understanding.traceId,
                    status =
                        DecisionEvaluationRequestStatus.UNAVAILABLE,
                )

            UnderstandingAuthorityStatus.FAILED ->
                DecisionEvaluationRequestResult.create(
                    traceId = understanding.traceId,
                    status =
                        DecisionEvaluationRequestStatus.FAILED,
                    error = requireNotNull(understanding.error),
                )
        }
    }
}
