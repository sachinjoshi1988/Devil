package com.devil.core.runtime.plan

/**
 * Describes whether one genuine plan identity is available for plan creation.
 *
 * This status does not generate identities, create planning strategy, create
 * plans, bind capabilities, execute actions, observe results, verify outcomes,
 * or report final outcomes.
 */
enum class PlanIdentityProvisionStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
