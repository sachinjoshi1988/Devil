package com.devil.core.model.goal

import com.devil.core.model.common.DevilTimestamp

/**
 * Immutable Stage 79 representation of one bounded trigger attached to an
 * existing long-running goal.
 *
 * A scheduled trigger preserves only:
 *
 * - the goal whose future reconsideration is relevant;
 * - one trigger identity;
 * - one trigger kind;
 * - and the explicit condition supplied for that kind.
 *
 * For SCHEDULED_TIME, scheduledAt must exist and eventKey must not exist.
 *
 * For EXTERNAL_EVENT, eventKey must exist and scheduledAt must not exist.
 *
 * This record does not access a clock, observe an event, schedule platform
 * work, authenticate an event source, create a Decision, create a Task or
 * Plan, grant authorization, select a capability, execute anything, or
 * establish an Outcome.
 *
 * SCHEDULED
 * != DUE
 * != TRIGGERED
 * != AUTHORIZED
 * != EXECUTED.
 */
@ConsistentCopyVisibility
data class GoalTriggerRecord private constructor(
    val triggerId: GoalTriggerId,
    val goal: LongRunningGoalRecord,
    val kind: GoalTriggerKind,
    val scheduledAt: DevilTimestamp?,
    val eventKey: String?,
) {
    companion object {

        fun create(
            triggerId: GoalTriggerId,
            goal: LongRunningGoalRecord,
            kind: GoalTriggerKind,
            scheduledAt: DevilTimestamp? = null,
            eventKey: String? = null,
        ): GoalTriggerRecord {
            require(
                goal.state == LongRunningGoalState.ACTIVE ||
                    goal.state == LongRunningGoalState.WAITING,
            ) {
                "Goal triggers require an active or waiting long-running goal."
            }

            val normalizedEventKey =
                eventKey
                    ?.trim()
                    ?.takeIf { it.isNotEmpty() }

            when (kind) {
                GoalTriggerKind.SCHEDULED_TIME -> {
                    require(scheduledAt != null) {
                        "Scheduled-time goal triggers require a timestamp."
                    }

                    require(normalizedEventKey == null) {
                        "Scheduled-time goal triggers must not contain an event key."
                    }
                }

                GoalTriggerKind.EXTERNAL_EVENT -> {
                    require(scheduledAt == null) {
                        "External-event goal triggers must not contain a scheduled timestamp."
                    }

                    require(normalizedEventKey != null) {
                        "External-event goal triggers require a nonblank event key."
                    }
                }
            }

            return GoalTriggerRecord(
                triggerId = triggerId,
                goal = goal,
                kind = kind,
                scheduledAt = scheduledAt,
                eventKey = normalizedEventKey,
            )
        }
    }
}
