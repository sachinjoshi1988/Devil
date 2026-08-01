package com.devil.core.model.plan

/**
 * Describes the constitutional lifecycle state of a plan.
 *
 * This state reflects planning only. It does not indicate capability
 * execution, platform execution, or verified outcome.
 */
enum class PlanState {
    CREATED,
    WAITING,
    READY,
    ACTIVE,
    COMPLETED,
    CANCELLED,
    FAILED,
}
