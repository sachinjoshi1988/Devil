package com.devil.core.runtime.learning

/**
 * Stage 93 bounded Failure Learning preparation status.
 *
 * PREPARED means one structurally valid FailureLearningRecord was produced from:
 *
 * - one existing Stage 92 evidence-backed learning record;
 * - explicit OutcomeState.VERIFIED_FAILURE;
 * - and one bounded supplied lesson.
 *
 * PREPARED does not mean:
 *
 * - constitutional Learning occurred;
 * - the lesson became truth;
 * - new evidence was established;
 * - World Model state changed;
 * - Memory was proposed;
 * - Memory Authority approved anything;
 * - Memory was committed or persisted;
 * - a Decision changed;
 * - a Task changed;
 * - a Plan changed;
 * - Planner strategy changed;
 * - authorization changed;
 * - execution occurred;
 * - or Controlled Autonomy was granted.
 *
 * DEFERRED means no truthful bounded Failure Learning record was produced.
 *
 * PREPARED != LEARNED.
 * PREPARED != MEMORY_PROPOSED.
 * PREPARED != STRATEGY_ADAPTED.
 * PREPARED != AUTONOMY_GRANTED.
 */
enum class FailureLearningPreparationStatus {
    PREPARED,
    DEFERRED,
}
