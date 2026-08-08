package com.devil.core.runtime.security

/**
 * Describes the stable operational result of constitutional session-validity
 * evaluation.
 *
 * VALID means approved session-validity evaluation established that one
 * bounded session remains valid at the authoritative observation time.
 *
 * INVALID means approved session-validity evaluation established that one
 * bounded session is not valid at the authoritative observation time.
 *
 * DEFERRED means no justified session-validity determination is currently
 * available.
 *
 * FAILED represents an operational failure with a matching error.
 *
 * This status does not authenticate a subject, prove owner identity, grant
 * authorization, advance SecurityStage, enter Owner Mode, approve
 * high-security confirmation, grant Android permission, or permit execution.
 */
enum class SessionValidityStatus {
    VALID,
    INVALID,
    DEFERRED,
    FAILED,
}
