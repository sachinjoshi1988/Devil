package com.devil.core.runtime.security

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.security.SessionId
import com.devil.core.model.security.SessionRecord
import com.devil.core.model.security.SessionState
import com.devil.core.model.security.SessionValidityRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultSessionValidityEvaluatorTest {

    @Test
    fun `active session within validity window is valid`() {
        val traceId = TraceId.from(
            "trace-default-session-validity-001",
        )

        val result =
            DefaultSessionValidityEvaluator().evaluate(
                traceId = traceId,
                request = createRequest(
                    traceId = traceId,
                    state = SessionState.ACTIVE,
                    observedAt = 1_754_000_300_000L,
                ),
            )

        assertEquals(
            SessionValidityEvaluationStatus.VALID,
            result.status,
        )
        assertNull(result.error)
    }

    @Test
    fun `active session at exact establishment time is valid`() {
        val traceId = TraceId.from(
            "trace-default-session-validity-002",
        )

        val result =
            DefaultSessionValidityEvaluator().evaluate(
                traceId = traceId,
                request = createRequest(
                    traceId = traceId,
                    state = SessionState.ACTIVE,
                    observedAt = ESTABLISHED_AT,
                ),
            )

        assertEquals(
            SessionValidityEvaluationStatus.VALID,
            result.status,
        )
    }

    @Test
    fun `active session at exact expiration time is invalid`() {
        val traceId = TraceId.from(
            "trace-default-session-validity-003",
        )

        val result =
            DefaultSessionValidityEvaluator().evaluate(
                traceId = traceId,
                request = createRequest(
                    traceId = traceId,
                    state = SessionState.ACTIVE,
                    observedAt = EXPIRES_AT,
                ),
            )

        assertEquals(
            SessionValidityEvaluationStatus.INVALID,
            result.status,
        )
    }

    @Test
    fun `active session after expiration is invalid`() {
        val traceId = TraceId.from(
            "trace-default-session-validity-004",
        )

        val result =
            DefaultSessionValidityEvaluator().evaluate(
                traceId = traceId,
                request = createRequest(
                    traceId = traceId,
                    state = SessionState.ACTIVE,
                    observedAt = EXPIRES_AT + 1L,
                ),
            )

        assertEquals(
            SessionValidityEvaluationStatus.INVALID,
            result.status,
        )
    }

    @Test
    fun `active session before establishment is invalid`() {
        val traceId = TraceId.from(
            "trace-default-session-validity-005",
        )

        val result =
            DefaultSessionValidityEvaluator().evaluate(
                traceId = traceId,
                request = createRequest(
                    traceId = traceId,
                    state = SessionState.ACTIVE,
                    observedAt = ESTABLISHED_AT - 1L,
                ),
            )

        assertEquals(
            SessionValidityEvaluationStatus.INVALID,
            result.status,
        )
    }

    @Test
    fun `expired lifecycle state is invalid even before expiration timestamp`() {
        val traceId = TraceId.from(
            "trace-default-session-validity-006",
        )

        val result =
            DefaultSessionValidityEvaluator().evaluate(
                traceId = traceId,
                request = createRequest(
                    traceId = traceId,
                    state = SessionState.EXPIRED,
                    observedAt = 1_754_000_300_000L,
                ),
            )

        assertEquals(
            SessionValidityEvaluationStatus.INVALID,
            result.status,
        )
    }

    @Test
    fun `revoked lifecycle state is always invalid`() {
        val traceId = TraceId.from(
            "trace-default-session-validity-007",
        )

        val result =
            DefaultSessionValidityEvaluator().evaluate(
                traceId = traceId,
                request = createRequest(
                    traceId = traceId,
                    state = SessionState.REVOKED,
                    observedAt = 1_754_000_300_000L,
                ),
            )

        assertEquals(
            SessionValidityEvaluationStatus.INVALID,
            result.status,
        )
    }

    @Test
    fun `evaluation preserves request without mutating session`() {
        val traceId = TraceId.from(
            "trace-default-session-validity-008",
        )
        val request = createRequest(
            traceId = traceId,
            state = SessionState.ACTIVE,
            observedAt = EXPIRES_AT + 1L,
        )
        val originalSession = request.session

        val result =
            DefaultSessionValidityEvaluator().evaluate(
                traceId = traceId,
                request = request,
            )

        assertEquals(
            SessionValidityEvaluationStatus.INVALID,
            result.status,
        )
        assertEquals(request, result.request)
        assertEquals(originalSession, request.session)
        assertEquals(
            SessionState.ACTIVE,
            request.session.state,
        )
    }

    @Test
    fun `evaluate rejects request from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultSessionValidityEvaluator().evaluate(
                traceId = TraceId.from(
                    "trace-default-session-validity-009",
                ),
                request = createRequest(
                    traceId = TraceId.from(
                        "trace-default-session-validity-other",
                    ),
                    state = SessionState.ACTIVE,
                    observedAt = 1_754_000_300_000L,
                ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
        state: SessionState,
        observedAt: Long,
    ): SessionValidityRequest {
        return SessionValidityRequest.create(
            context =
                ContextEnvelope.create(
                    traceId = traceId,
                    schemaVersion = SchemaVersion.from(1),
                    source = ContextSource.SYSTEM,
                    trustLevel = ContextTrustLevel.UNVERIFIED,
                    securityLevel =
                        ContextSecurityLevel.RESTRICTED,
                    observedAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            observedAt,
                        ),
                ),
            session =
                SessionRecord.create(
                    sessionId = SessionId.from(
                        "session-default-validity",
                    ),
                    subjectIdentityId = IdentityId.from(
                        "subject-default-validity",
                    ),
                    state = state,
                    establishedAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            ESTABLISHED_AT,
                        ),
                    expiresAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            EXPIRES_AT,
                        ),
                ),
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    observedAt,
                ),
        )
    }

    companion object {
        private const val ESTABLISHED_AT =
            1_754_000_196_000L

        private const val EXPIRES_AT =
            1_754_003_796_000L
    }
}
