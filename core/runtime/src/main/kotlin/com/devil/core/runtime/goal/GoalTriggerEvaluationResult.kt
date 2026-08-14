package com.devil.core.runtime.goal

import com.devil.core.model.common.TraceId
import com.devil.core.model.goal.GoalTriggerRecord

/**
 * Stable Stage 79 result for one bounded goal-trigger evaluation.
 *
 * The traceId belongs to the current trigger-evaluation cycle. It deliberately
 * need not equal the originating trace preserved by the long-running goal,
 * because long-running goals span separate constitutional reasoning cycles.
 *
 * An eligible result preserves the evaluated trigger. A deferred result
 * contains no trigger.
 *
 * Preserving an eligible trigger grants no execution or authorization
 * semantics.
 */
@ConsistentCopyVisibility
data class GoalTriggerEvaluationResult private constructor(
    val traceId: TraceId,
    val status: GoalTriggerEvaluationStatus,
    val trigger: GoalTriggerRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: GoalTriggerEvaluationStatus,
            trigger: GoalTriggerRecord? = null,
        ): GoalTriggerEvaluationResult {
            when (status) {
                GoalTriggerEvaluationStatus.ELIGIBLE_FOR_RECONSIDERATION -> {
                    require(trigger != null) {
                        "Eligible goal-trigger evaluation requires one evaluated trigger."
                    }
                }

                GoalTriggerEvaluationStatus.DEFERRED -> {
                    require(trigger == null) {
                        "Deferred goal-trigger evaluation must not contain a trigger."
                    }
                }
            }

            return GoalTriggerEvaluationResult(
                traceId = traceId,
                status = status,
                trigger = trigger,
            )
        }
    }
}
