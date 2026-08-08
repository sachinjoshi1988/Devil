package com.devil.core.model.security

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.context.ContextEnvelope

/**
 * Represents one bounded request to evaluate the current validity of an existing
 * constitutional security session.
 *
 * The request binds authoritative constitutional context, one existing
 * SessionRecord, and the authoritative observation time against which session
 * validity may later be evaluated.
 *
 * Creating this request does not establish that the session is valid, expired,
 * revoked, authenticated, owner-controlled, authorized, or permitted to advance
 * SecurityStage.
 *
 * It does not mutate SessionRecord, extend a session, create a session, enter
 * Owner Mode, approve high-security confirmation, grant Android permission, or
 * permit capability execution.
 *
 * Session-validity evaluation belongs to the Security Authority and its approved
 * session policy.
 */
@ConsistentCopyVisibility
data class SessionValidityRequest private constructor(
    val context: ContextEnvelope,
    val session: SessionRecord,
    val observedAt: DevilTimestamp,
) {
    companion object {
        fun create(
            context: ContextEnvelope,
            session: SessionRecord,
            observedAt: DevilTimestamp,
        ): SessionValidityRequest {
            return SessionValidityRequest(
                context = context,
                session = session,
                observedAt = observedAt,
            )
        }
    }
}
