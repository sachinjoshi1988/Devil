package com.devil.core.model.security

/**
 * Describes the current bounded constitutional security stage.
 *
 * This stage represents security position only.
 *
 * It does not authenticate a subject, prove owner identity, establish trust,
 * grant authorization, create a session, enter Owner Mode, approve a
 * high-security action, grant Android permission, or permit execution.
 *
 * Wake establishes attention only. It does not prove authentication.
 */
enum class SecurityStage {
    LOCKED,
    WAKE,
    AUTHENTICATION,
    SESSION,
    OWNER_MODE,
    HIGH_SECURITY_CONFIRMATION,
}
