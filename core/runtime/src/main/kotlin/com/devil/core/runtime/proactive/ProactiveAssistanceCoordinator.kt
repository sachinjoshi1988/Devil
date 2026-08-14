package com.devil.core.runtime.proactive

import com.devil.core.model.common.TraceId
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.proactive.ProactiveAssistanceRecord
import com.devil.core.runtime.goal.GoalTriggerEvaluationResult
import com.devil.core.runtime.goal.GoalTriggerEvaluationStatus

/**
 * Stage 80 bounded proactive-assistance coordinator.
 *
 * This coordinator consumes:
 *
 * - one Stage 79 trigger evaluation;
 * - one fresh constitutional Decision from the current reasoning cycle;
 * - an explicitly supplied relevance determination;
 * - an explicitly supplied interruption justification;
 * - and truthful bounded presentation content.
 *
 * It does not infer relevance from raw notification content.
 *
 * It does not treat notification arrival or trigger matching as permission
 * to interrupt the user.
 *
 * It does not:
 *
 * - create another Brain;
 * - select the Decision;
 * - invoke UnifiedDevilRuntime;
 * - create ConversationInput;
 * - create Tasks or Plans;
 * - grant authorization;
 * - select capabilities;
 * - post Android notifications;
 * - invoke TextToSpeech;
 * - execute actions;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform Learning;
 * - commit Memory;
 * - or persist proactive state.
 *
 * Trigger eligibility permits reconsideration only.
 * Fresh constitutional Decision remains mandatory.
 */
class ProactiveAssistanceCoordinator {

    fun evaluate(
        traceId: TraceId,
        triggerEvaluation: GoalTriggerEvaluationResult,
        decision: DecisionRecord,
        relevanceEstablished: Boolean,
        interruptionJustified: Boolean,
        message: String,
    ): ProactiveAssistanceEvaluationResult {
        require(
            decision.understanding.context.traceId == traceId,
        ) {
            "Proactive-assistance evaluation and fresh Decision must use the same trace identity."
        }

        if (
            triggerEvaluation.status !=
            GoalTriggerEvaluationStatus.ELIGIBLE_FOR_RECONSIDERATION
        ) {
            return deferred(traceId)
        }

        if (triggerEvaluation.trigger == null) {
            return deferred(traceId)
        }

        if (decision.state != DecisionState.SELECTED) {
            return deferred(traceId)
        }

        if (!relevanceEstablished) {
            return deferred(traceId)
        }

        if (!interruptionJustified) {
            return deferred(traceId)
        }

        if (message.isBlank()) {
            return deferred(traceId)
        }

        val record =
            ProactiveAssistanceRecord.create(
                trigger = triggerEvaluation.trigger,
                decision = decision,
                message = message,
            )

        return ProactiveAssistanceEvaluationResult.create(
            traceId = traceId,
            status =
                ProactiveAssistanceEvaluationStatus
                    .ELIGIBLE_FOR_PRESENTATION,
            record = record,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): ProactiveAssistanceEvaluationResult {
        return ProactiveAssistanceEvaluationResult.create(
            traceId = traceId,
            status = ProactiveAssistanceEvaluationStatus.DEFERRED,
        )
    }
}
