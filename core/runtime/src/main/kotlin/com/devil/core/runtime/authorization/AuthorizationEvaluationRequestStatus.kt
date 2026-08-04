package com.devil.core.runtime.authorization

/**
 * Describes whether a structured authorization-evaluation request is available.
 *
 * This status does not grant authorization, authorize a capability, grant
 * operating-system permission, enter Owner Mode, permit execution, or verify
 * an outcome.
 */
enum class AuthorizationEvaluationRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
