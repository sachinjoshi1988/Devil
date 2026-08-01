package com.devil.core.runtime.plan

/**
 * Describes the operational result of the Plan Authority.
 *
 * This status reports whether a PlanRecord was created. The planning lifecycle
 * state belongs to PlanState inside that record.
 */
enum class PlanAuthorityStatus {
    CREATED,
    DEFERRED,
    FAILED,
}
