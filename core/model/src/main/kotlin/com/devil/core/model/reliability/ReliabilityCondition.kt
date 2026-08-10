package com.devil.core.model.reliability

/**
 * Stage 45 bounded classification of one observed reliability condition.
 *
 * This describes reliability evidence only.
 *
 * HEALTHY means no recovery condition is represented by the supplied evidence.
 *
 * DEGRADED means useful operation may remain possible, but the supplied evidence
 * shows reduced reliability.
 *
 * UNAVAILABLE means the affected subject must not presently be treated as
 * operationally available.
 *
 * FAILED means one explicit failure has been established.
 *
 * Reliability condition
 * != capability availability
 * != capability health mutation
 * != authorization
 * != Executive readiness
 * != Android permission
 * != retry permission
 * != execution approval
 * != verified Outcome.
 */
enum class ReliabilityCondition {
    HEALTHY,
    DEGRADED,
    UNAVAILABLE,
    FAILED,
}
