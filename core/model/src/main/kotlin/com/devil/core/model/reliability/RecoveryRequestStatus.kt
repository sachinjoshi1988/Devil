package com.devil.core.model.reliability

/**
 * Stage 45 bounded recovery-request policy result.
 *
 * AVAILABLE means one structurally valid recovery request may be produced for
 * later constitutional consideration.
 *
 * UNAVAILABLE means current evidence does not permit a safe recovery request.
 *
 * EXHAUSTED means the supplied finite recovery-attempt budget has no remaining
 * attempts.
 *
 * AVAILABLE
 * != authorized
 * != Executive ready
 * != execution approved
 * != recovery started.
 */
enum class RecoveryRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    EXHAUSTED,
}
