package com.devil.core.model.security

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.identity.IdentityId

/**
 * Records one bounded constitutional security session.
 *
 * A session record preserves an existing SessionId, the subject identity to which
 * that session belongs, its lifecycle state, and its approved validity window.
 *
 * The record does not create a session, authenticate a subject, prove owner
 * identity, establish trust, grant authorization, advance SecurityStage, enter
 * Owner Mode, approve high-security confirmation, grant Android permission, or
 * permit capability execution.
 *
 * Session establishment and lifecycle mutation belong to the Security Authority
 * and its approved session mechanism.
 *
 * The validity window represents bounded session timing only. Creating this
 * record does not establish that the session is currently valid; current validity
 * requires evaluation by the proper Security Authority against an authoritative
 * observation time and approved session policy.
 */
@ConsistentCopyVisibility
data class SessionRecord private constructor(
    val sessionId: SessionId,
    val subjectIdentityId: IdentityId,
    val state: SessionState,
    val establishedAt: DevilTimestamp,
    val expiresAt: DevilTimestamp,
) {
    companion object {
        fun create(
            sessionId: SessionId,
            subjectIdentityId: IdentityId,
            state: SessionState,
            establishedAt: DevilTimestamp,
            expiresAt: DevilTimestamp,
        ): SessionRecord {
            require(
                expiresAt.epochMilliseconds >
                    establishedAt.epochMilliseconds,
            ) {
                "Session expiration must be later than session establishment."
            }

            return SessionRecord(
                sessionId = sessionId,
                subjectIdentityId = subjectIdentityId,
                state = state,
                establishedAt = establishedAt,
                expiresAt = expiresAt,
            )
        }
    }
}
