package com.devil.core.model.authorization

/**
 * Describes the established outcome of one constitutional authorization
 * evaluation.
 *
 * This state applies only to continuation beyond the authorization boundary.
 * It does not authorize a specific capability, grant operating-system
 * permission, enter Owner Mode, permit execution, or verify an outcome.
 */
enum class AuthorizationEvaluationState {
    AUTHORIZED,
    DENIED,
    DEFERRED,
}
