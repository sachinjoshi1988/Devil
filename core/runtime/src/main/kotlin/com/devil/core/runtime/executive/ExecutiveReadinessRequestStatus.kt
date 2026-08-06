package com.devil.core.runtime.executive

/**
 * Describes whether one bounded constitutional Executive-readiness request is
 * available.
 *
 * This status reports request availability only. It does not establish
 * readiness, authorize execution, check capability availability or health,
 * evaluate operating-system permission, execute actions, observe results,
 * verify outcomes, or report final outcomes.
 */
enum class ExecutiveReadinessRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
