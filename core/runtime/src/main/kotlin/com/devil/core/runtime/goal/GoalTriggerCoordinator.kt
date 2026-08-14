package com.devil.core.runtime.goal

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.goal.GoalTriggerKind
import com.devil.core.model.goal.GoalTriggerRecord

/**
 * Stage 79 bounded scheduled/event trigger coordinator.
 *
 * This coordinator evaluates an already-created GoalTriggerRecord against
 * explicitly supplied observation data.
 *
 * It performs no clock access and subscribes to no event source.
 *
 * It does not:
 *
 * - create another Brain;
 * - restore authorization;
 * - preserve a Security session;
 * - create a Decision;
 * - create a Task or Plan;
 * - select or activate a capability;
 * - invoke UnifiedDevilRuntime;
 * - create ConversationInput;
 * - schedule Android work;
 * - execute an action;
 * - establish Observation or Verification;
 * - establish Outcome;
 * - mutate World Model state;
 * - perform Learning;
 * - or commit Memory.
 *
 * Matching a trigger establishes only eligibility for fresh constitutional
 * reconsideration.
 */
class GoalTriggerCoordinator {

    fun evaluate(
        traceId: TraceId,
        trigger: GoalTriggerRecord,
        observedAt: DevilTimestamp,
        observedEventKey: String? = null,
    ): GoalTriggerEvaluationResult {
        val eligible =
            when (trigger.kind) {
                GoalTriggerKind.SCHEDULED_TIME -> {
                    val scheduledAt =
                        requireNotNull(trigger.scheduledAt)

                    observedAt.epochMilliseconds >=
                        scheduledAt.epochMilliseconds
                }

                GoalTriggerKind.EXTERNAL_EVENT -> {
                    val expectedEvent =
                        requireNotNull(trigger.eventKey)

                    val observedEvent =
                        observedEventKey
                            ?.trim()
                            ?.takeIf { it.isNotEmpty() }

                    observedEvent == expectedEvent
                }
            }

        return if (eligible) {
            GoalTriggerEvaluationResult.create(
                traceId = traceId,
                status =
                    GoalTriggerEvaluationStatus
                        .ELIGIBLE_FOR_RECONSIDERATION,
                trigger = trigger,
            )
        } else {
            GoalTriggerEvaluationResult.create(
                traceId = traceId,
                status = GoalTriggerEvaluationStatus.DEFERRED,
            )
        }
    }
}
