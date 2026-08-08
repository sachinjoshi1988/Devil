package com.devil.core.runtime.security

/**
 * Describes the stable operational result of constitutional security-transition
 * evaluation.
 *
 * APPROVED means genuine constitutional security evaluation established that one
 * bounded SecurityTransitionRequest may proceed to later controlled security-state
 * handling.
 *
 * DEFERRED means no justified security-transition approval is currently available.
 *
 * FAILED represents an operational failure with a matching error.
 *
 * This status does not authenticate a subject, prove owner identity, establish
 * trust, grant authorization, create or validate a session, enter Owner Mode,
 * approve high-security confirmation, grant Android permission, or permit
 * execution.
 */
enum class SecurityTransitionStatus {
    APPROVED,
    DEFERRED,
    FAILED,
}
