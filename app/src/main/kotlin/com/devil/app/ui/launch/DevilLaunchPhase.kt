package com.devil.app.ui.launch

/**
 * Stage 51 presentation-only launch phases.
 *
 * These phases describe branded UI timing only.
 *
 * They do not represent:
 *
 * - authentication;
 * - security stage;
 * - runtime readiness;
 * - authorization;
 * - execution;
 * - observation;
 * - verification;
 * - Outcome;
 * - memory state.
 */
enum class DevilLaunchPhase {
    VOID,
    CORE_IGNITION,
    CORE_PULSE,
    IDENTITY_REVEAL,
    WORDMARK,
    COMPLETE,
}
