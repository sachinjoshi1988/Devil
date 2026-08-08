package com.devil.core.runtime.security

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.security.SessionId
import com.devil.core.model.security.SessionRecord
import com.devil.core.model.security.SessionState
import com.devil.core.model.security.SessionValidityRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultSessionValidityResultMapperTest {

    @Test
    fun `map converts valid evaluation into valid operational result`() {
        val request = createRequest(
            "trace-session-validity-mapper-001",
        )

        val evaluation =
            SessionValidityEvaluationResult.create(
                traceId = request.context.traceId,
                status = SessionValidityEvaluationStatus.VALID,
                request = request,
            )

        val result =
            DefaultSessionValidityResultMapper().map(
                traceId = request.context.traceId,
                evaluation = evaluation,
            )

        assertEquals(SessionValidityStatus.VALID, result.status)
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `map converts invalid evaluation into invalid operational result`() {
        val request = createRequest(
            "trace-session-validity-mapper-002",
        )

        val evaluation =
            SessionValidityEvaluationResult.create(
                traceId = request.context.traceId,
                status = SessionValidityEvaluationStatus.INVALID,
                request = request,
            )

        val result =
            DefaultSessionValidityResultMapper().map(
                traceId = request.context.traceId,
                evaluation = evaluation,
            )

        assertEquals(SessionValidityStatus.INVALID, result.status)
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `map converts unavailable evaluation into deferred result`() {
        val traceId = TraceId.from(
            "trace-session-validity-mapper-003",
        )

        val evaluation =
            SessionValidityEvaluationResult.create(
                traceId = traceId,
                status = SessionValidityEvaluationStatus.UNAVAILABLE,
            )

        val result =
            DefaultSessionValidityResultMapper().map(
                traceId = traceId,
                evaluation = evaluation,
            )

        assertEquals(SessionValidityStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `map preserves failed evaluation error`() {
        val traceId = TraceId.from(
            "trace-session-validity-mapper-004",
        )
        val error = createError(traceId)

        val evaluation =
            SessionValidityEvaluationResult.create(
                traceId = traceId,
                status = SessionValidityEvaluationStatus.FAILED,
                error = error,
            )

        val result =
            DefaultSessionValidityResultMapper().map(
                traceId = traceId,
                evaluation = evaluation,
            )

        assertEquals(SessionValidityStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `map rejects evaluation from another trace`() {
        val evaluation =
            SessionValidityEvaluationResult.create(
                traceId = TraceId.from(
                    "trace-session-validity-mapper-005",
                ),
                status = SessionValidityEvaluationStatus.UNAVAILABLE,
            )

        assertFailsWith<IllegalArgumentException> {
            DefaultSessionValidityResultMapper().map(
                traceId = TraceId.from(
                    "trace-session-validity-mapper-other",
                ),
                evaluation = evaluation,
            )
        }
    }

    private fun createRequest(
        traceValue: String,
    ): SessionValidityRequest {
        return SessionValidityRequest.create(
            context =
                ContextEnvelope.create(
                    traceId = TraceId.from(traceValue),
                    schemaVersion = SchemaVersion.from(1),
                    source = ContextSource.SYSTEM,
                    trustLevel = ContextTrustLevel.UNVERIFIED,
                    securityLevel =
                        ContextSecurityLevel.RESTRICTED,
                    observedAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            1_754_000_305_000L,
                        ),
                ),
            session =
                SessionRecord.create(
                    sessionId = SessionId.from(
                        "session-validity-mapper",
                    ),
                    subjectIdentityId = IdentityId.from(
                        "subject-session-validity-mapper",
                    ),
                    state = SessionState.ACTIVE,
                    establishedAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            1_754_000_196_000L,
                        ),
                    expiresAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            1_754_003_796_000L,
                        ),
                ),
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_305_000L,
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "SESSION_VALIDITY_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_305_100L,
                ),
            summary = "Session validity failed.",
        )
    }
}
