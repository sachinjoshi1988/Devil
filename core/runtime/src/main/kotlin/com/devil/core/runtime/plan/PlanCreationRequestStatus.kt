package com.devil.core.runtime.plan

/**
 * Describes whether a bounded constitutional plan-creation request is available.
 *
 * This status reports request availability only. It does not create planning
 * strategy, generate plan identity, create plans, bind capabilities, execute
 * actions, observe results, verify outcomes, or report final outcomes.
 */
enum class PlanCreationRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
