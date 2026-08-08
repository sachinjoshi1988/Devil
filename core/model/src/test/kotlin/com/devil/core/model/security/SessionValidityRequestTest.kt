package com.devil.core.model.security

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals

class SessionValidityRequestTest {

    @Test
    fun `create preserves context session and authoritative observation time`() {
        val context = createContext(
            "trace-session-validity-request-001",
        )
        val session = createSession(
            state = SessionState.ACTIVE,
        )
        val observedAt =
            DevilTimestamp.fromEpochMilliseconds(
                1_754_000_300_000L,
            )

        val request = SessionValidityRequest.create(
            context = context,
            session = session,
            observedAt = observedAt,
        )

        assertEquals(context, request.context)
        assertEquals(session, request.session)
        assertEquals(observedAt, request.observedAt)
    }

    @Test
    fun `request preserves active session without declaring it valid`() {
        val session = createSession(
            state = SessionState.ACTIVE,
        )

        val request = SessionValidityRequest.create(
            context = createContext(
                "trace-session-validity-request-002",
            ),
            session = session,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_300_000L,
                ),
        )

        assertEquals(
            SessionState.ACTIVE,
            request.session.state,
        )
    }

    @Test
    fun `request preserves expired session without changing lifecycle state`() {
        val session = createSession(
            state = SessionState.EXPIRED,
        )

        val request = SessionValidityRequest.create(
            context = createContext(
                "trace-session-validity-request-003",
            ),
            session = session,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_300_000L,
                ),
        )

        assertEquals(
            SessionState.EXPIRED,
            request.session.state,
        )
    }

    @Test
    fun `request preserves revoked session without changing lifecycle state`() {
        val session = createSession(
            state = SessionState.REVOKED,
        )

        val request = SessionValidityRequest.create(
            context = createContext(
                "trace-session-validity-request-004",
            ),
            session = session,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_300_000L,
                ),
        )

        assertEquals(
            SessionState.REVOKED,
            request.session.state,
        )
    }

    @Test
    fun `request observation time does not alter session validity window`() {
        val session = createSession(
            state = SessionState.ACTIVE,
        )
        val establishedAt = session.establishedAt
        val expiresAt = session.expiresAt

        SessionValidityRequest.create(
            context = createContext(
                "trace-session-validity-request-005",
            ),
            session = session,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_100_000_000L,
                ),
        )

        assertEquals(
            establishedAt,
            session.establishedAt,
        )
        assertEquals(
            expiresAt,
            session.expiresAt,
        )
        assertEquals(
            SessionState.ACTIVE,
            session.state,
        )
    }

    private fun createContext(
        traceValue: String,
    ): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.SYSTEM,
            trustLevel = ContextTrustLevel.UNVERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_299_000L,
                ),
        )
    }

    private fun createSession(
        state: SessionState,
    ): SessionRecord {
        return SessionRecord.create(
            sessionId = SessionId.from(
                "session-validity-request",
            ),
            subjectIdentityId = IdentityId.from(
                "subject-session-validity",
            ),
            state = state,
            establishedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_196_000L,
                ),
            expiresAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_003_796_000L,
                ),
        )
    }
}
