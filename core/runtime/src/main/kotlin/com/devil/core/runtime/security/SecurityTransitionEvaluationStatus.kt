package com.devil.core.runtime.security

/**
 * Describes the bounded result of constitutional security-transition evaluation.
 *
 * APPROVED means genuine constitutional security policy established that one
 * supplied SecurityTransitionRequest may proceed to later controlled security
 * state handling.
 *
 * UNAVAILABLE means no justified security-transition determination can currently
 * be established.
 *
 * FAILED represents an operational security-evaluation failure.
 *
 * This status does not authenticate a subject, prove owner identity, establish
 * trust, grant authorization, create or validate a session, enter Owner Mode,
 * approve high-security confirmation, grant Android permission, or permit
 * execution.
 */
enum class SecurityTransitionEvaluationStatus {
    APPROVED,
    UNAVAILABLE,
    FAILED,
}
