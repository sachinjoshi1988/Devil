package com.devil.core.runtime.goal

/**
 * Stage 79 result of evaluating one bounded long-running-goal trigger condition.
 *
 * ELIGIBLE_FOR_RECONSIDERATION means only that the represented trigger
 * condition matched sufficiently for the long-running goal to approach a
 * fresh constitutional reasoning cycle.
 *
 * It does not mean:
 *
 * - authenticated;
 * - trusted;
 * - authorized;
 * - Decision selected;
 * - Task created;
 * - Plan created;
 * - capability selected;
 * - execution approved;
 * - execution attempted;
 * - effect observed;
 * - effect verified;
 * - or Outcome established.
 *
 * DEFERRED means the trigger condition does not currently justify such
 * reconsideration.
 */
enum class GoalTriggerEvaluationStatus {
    ELIGIBLE_FOR_RECONSIDERATION,
    DEFERRED,
}
