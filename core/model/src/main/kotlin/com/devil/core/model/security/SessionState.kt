package com.devil.core.model.security

/**
 * Describes the bounded lifecycle state of one established constitutional
 * security session.
 *
 * ACTIVE means only that the session has not been represented as expired or
 * revoked by the owning session mechanism.
 *
 * EXPIRED means the session's approved validity period has ended.
 *
 * REVOKED means the session has been explicitly invalidated by its owning
 * security mechanism.
 *
 * These states do not authenticate a subject, prove owner identity, establish
 * trust, grant authorization, enter Owner Mode, approve high-security
 * confirmation, grant Android permission, or permit capability execution.
 *
 * Session lifecycle is distinct from SecurityStage. An ACTIVE session does not
 * itself prove that the constitutional security position is SESSION or any
 * later stage.
 */
enum class SessionState {
    ACTIVE,
    EXPIRED,
    REVOKED,
}
