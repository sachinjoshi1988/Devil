package com.devil.core.runtime.decision

import com.devil.core.model.decision.DecisionEvaluationRequest
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.understanding.UnderstandingActionability
import com.devil.core.model.understanding.UnderstandingIntent
import com.devil.core.model.understanding.UnderstandingState

/**
 * Default bounded constitutional decision-evaluation resolver.
 *
 * Stage 57 introduces a deliberately small deterministic Decision policy over
 * semantic understanding established by the preceding Understanding Authority.
 *
 * Decision remains downstream from Understanding and upstream from Task.
 *
 * A SELECTED decision does not:
 * - create a task;
 * - create a plan;
 * - select or authorize a capability;
 * - establish Android permission;
 * - execute an action;
 * - observe an execution attempt;
 * - verify an effect;
 * - establish an Outcome.
 *
 * Unsupported or incomplete understanding remains deferred rather than being
 * converted into fabricated intent or unjustified action.
 */
class DefaultDecisionEvaluationResolver :
    DecisionEvaluationResolver {

    override fun evaluate(
        request: DecisionEvaluationRequest,
    ): DecisionRecord {
        val understanding = request.understanding

        return when (understanding.state) {
            UnderstandingState.COMPLETE ->
                evaluateCompleteUnderstanding(request)

            UnderstandingState.AMBIGUOUS ->
                DecisionRecord.create(
                    understanding = understanding,
                    state =
                        DecisionState.REQUIRES_CLARIFICATION,
                    summary =
                        "Clarification is required before a constitutional decision can be selected.",
                )

            UnderstandingState.INCOMPLETE ->
                DecisionRecord.create(
                    understanding = understanding,
                    state = DecisionState.DEFERRED,
                    summary =
                        "Decision deferred because understanding is incomplete.",
                )

            UnderstandingState.UNSUPPORTED ->
                DecisionRecord.create(
                    understanding = understanding,
                    state = DecisionState.DEFERRED,
                    summary =
                        "Decision deferred because the supplied meaning is unsupported.",
                )
        }
    }

    private fun evaluateCompleteUnderstanding(
        request: DecisionEvaluationRequest,
    ): DecisionRecord {
        val understanding = request.understanding
        val semantics =
            understanding.semantics
                ?: return DecisionRecord.create(
                    understanding = understanding,
                    state = DecisionState.DEFERRED,
                    summary =
                        "Decision deferred because complete understanding lacks structured semantics.",
                )

        return when (semantics.intent) {
            UnderstandingIntent.GREETING -> {
                if (
                    semantics.actionability !=
                    UnderstandingActionability.NON_ACTIONABLE
                ) {
                    deferredSemanticMismatch(request)
                } else {
                    DecisionRecord.create(
                        understanding = understanding,
                        state = DecisionState.SELECTED,
                        summary =
                            "Acknowledge the user's greeting.",
                    )
                }
            }

            UnderstandingIntent.OPEN_TARGET -> {
                if (
                    semantics.actionability !=
                    UnderstandingActionability.ACTIONABLE ||
                    semantics.target == null
                ) {
                    deferredSemanticMismatch(request)
                } else {
                    DecisionRecord.create(
                        understanding = understanding,
                        state = DecisionState.SELECTED,
                        summary =
                            "Proceed with the understood request to open target: ${semantics.target}.",
                    )
                }
            }

            UnderstandingIntent.INFORMATIONAL -> {
                if (
                    semantics.actionability !=
                    UnderstandingActionability.NON_ACTIONABLE
                ) {
                    deferredSemanticMismatch(request)
                } else {
                    DecisionRecord.create(
                        understanding = understanding,
                        state = DecisionState.SELECTED,
                        summary =
                            "Accept the supplied informational statement for bounded conversational handling.",
                    )
                }
            }
        }
    }

    private fun deferredSemanticMismatch(
        request: DecisionEvaluationRequest,
    ): DecisionRecord {
        return DecisionRecord.create(
            understanding = request.understanding,
            state = DecisionState.DEFERRED,
            summary =
                "Decision deferred because structured semantics are inconsistent with the bounded decision policy.",
        )
    }
}
