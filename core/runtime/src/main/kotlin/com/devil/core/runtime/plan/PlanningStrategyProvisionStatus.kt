package com.devil.core.runtime.plan

/**
 * Describes whether one bounded constitutional planning strategy is available.
 *
 * This status does not create strategy, generate plan identity, create plans,
 * bind capabilities, execute actions, observe results, verify outcomes, or
 * report final outcomes.
 */
enum class PlanningStrategyProvisionStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
