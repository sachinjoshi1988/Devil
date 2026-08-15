package com.devil.core.runtime.autonomy

/**
 * Stage 95 bounded Controlled Autonomy preparation status.
 *
 * PREPARED means exactly one structurally valid ControlledAutonomyRecord was
 * created from:
 *
 * - one existing Stage 94 StrategyAdaptationRecord; and
 * - one explicitly supplied bounded autonomy scope.
 *
 * PREPARED means only that constitutional information is available for possible
 * later reconsideration.
 *
 * PREPARED does not mean:
 *
 * - autonomy was granted;
 * - owner authorization exists;
 * - a Brain Decision exists;
 * - Security approved anything;
 * - a session is valid;
 * - Owner Mode is active;
 * - high-security confirmation exists;
 * - a Task or Plan exists;
 * - Planner strategy changed;
 * - a capability was selected;
 * - capability availability was established;
 * - Executive readiness exists;
 * - execution was requested;
 * - execution occurred;
 * - proactive work was initiated;
 * - scheduled work was initiated;
 * - a trigger fired;
 * - Memory was proposed or approved;
 * - or anything continued without constitutional reconsideration.
 *
 * DEFERRED means no truthful bounded Controlled Autonomy record was produced.
 *
 * PREPARED != AUTONOMY_GRANTED.
 * PREPARED != AUTHORIZED.
 * PREPARED != READY.
 * PREPARED != EXECUTED.
 */
enum class ControlledAutonomyPreparationStatus {
    PREPARED,
    DEFERRED,
}
