package com.devil.core.runtime.authorization

/**
 * Describes the result of constitutional authorization evaluation.
 *
 * This status permits or prevents continuation beyond the authorization
 * boundary. It does not select decisions, authorize a specific capability,
 * perform execution, or claim an outcome.
 */
enum class AuthorizationStatus {
    AUTHORIZED,
    DENIED,
    DEFERRED,
    FAILED,
}
