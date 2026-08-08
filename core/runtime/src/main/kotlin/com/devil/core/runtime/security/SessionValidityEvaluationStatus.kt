package com.devil.core.runtime.security

/**
 * Describes the bounded result of constitutional session-validity evaluation.
 *
 * VALID means genuine approved session policy established that the supplied
 * session remains currently valid at the authoritative observation time.
 *
 * INVALID means genuine approved session policy established that the supplied
 * session is not currently valid.
 *
 * UNAVAILABLE means no justified session-validity determination can currently
 * be established.
 *
 * FAILED represents an operational session-validity evaluation failure.
 *
 * This status does not authenticate a subject, prove owner identity, grant
 * authorization, advance SecurityStage, enter Owner Mode, approve
 * high-security confirmation, grant Android permission, or permit execution.
 */
enum class SessionValidityEvaluationStatus {
    VALID,
    INVALID,
    UNAVAILABLE,
    FAILED,
}
