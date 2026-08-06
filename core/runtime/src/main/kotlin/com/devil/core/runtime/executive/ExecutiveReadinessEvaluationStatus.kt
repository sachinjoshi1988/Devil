package com.devil.core.runtime.executive

/**
 * Describes the bounded result of constitutional Executive readiness
 * evaluation.
 *
 * READY means that a readiness policy produced affirmative readiness evidence.
 * UNAVAILABLE means no justified readiness determination was produced. FAILED
 * represents an operational evaluation failure.
 *
 * This status does not authorize execution, execute actions, observe results,
 * verify outcomes, or report final outcomes.
 */
enum class ExecutiveReadinessEvaluationStatus {
    READY,
    UNAVAILABLE,
    FAILED,
}
