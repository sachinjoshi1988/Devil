package com.devil.core.model.proactive

import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.goal.GoalTriggerRecord

/**
 * Immutable Stage 80 representation of one bounded proactive-assistance candidate.
 *
 * A proactive-assistance record may exist only after:
 *
 * - an existing long-running-goal trigger became eligible for reconsideration;
 * - a fresh constitutional reasoning cycle produced one selected Decision;
 * - relevance was explicitly established;
 * - interruption was explicitly justified;
 * - and one truthful nonblank presentation message was supplied.
 *
 * This record does not:
 *
 * - create or select a Decision;
 * - establish identity, trust, authentication, or authorization;
 * - create a Task or Plan;
 * - select or activate a capability;
 * - invoke UnifiedDevilRuntime;
 * - create ConversationInput;
 * - post an Android notification;
 * - speak through TextToSpeech;
 * - execute an action;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform Learning;
 * - create or commit Memory;
 * - or guarantee that the user will actually be interrupted.
 *
 * RELEVANT
 * != INTERRUPTIBLE
 * != PRESENTED
 * != SPOKEN
 * != AUTHORIZED
 * != EXECUTED.
 */
@ConsistentCopyVisibility
data class ProactiveAssistanceRecord private constructor(
    val trigger: GoalTriggerRecord,
    val decision: DecisionRecord,
    val message: String,
) {
    companion object {

        fun create(
            trigger: GoalTriggerRecord,
            decision: DecisionRecord,
            message: String,
        ): ProactiveAssistanceRecord {
            val normalizedMessage = message.trim()

            require(
                decision.state == DecisionState.SELECTED,
            ) {
                "Proactive assistance requires one fresh selected constitutional Decision."
            }

            require(normalizedMessage.isNotEmpty()) {
                "Proactive-assistance presentation message must not be blank."
            }

            return ProactiveAssistanceRecord(
                trigger = trigger,
                decision = decision,
                message = normalizedMessage,
            )
        }
    }
}
