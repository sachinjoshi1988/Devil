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

class SessionValidityEvaluationResultTest {

    @Test
    fun `valid result preserves matching request`() {
        val request = createRequest(
            "trace-session-validity-evaluation-001",
        )

        val result = SessionValidityEvaluationResult.create(
            traceId = request.context.traceId,
            status = SessionValidityEvaluationStatus.VALID,
            request = request,
        )

        assertEquals(
            SessionValidityEvaluationStatus.VALID,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `invalid result preserves matching request`() {
        val request = createRequest(
            "trace-session-validity-evaluation-002",
        )

        val result = SessionValidityEvaluationResult.create(
            traceId = request.context.traceId,
            status = SessionValidityEvaluationStatus.INVALID,
            request = request,
        )

        assertEquals(
            SessionValidityEvaluationStatus.INVALID,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `unavailable result contains neither request nor error`() {
        val traceId = TraceId.from(
            "trace-session-validity-evaluation-003",
        )

        val result = SessionValidityEvaluationResult.create(
            traceId = traceId,
            status = SessionValidityEvaluationStatus.UNAVAILABLE,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            SessionValidityEvaluationStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `failed result preserves matching error`() {
        val traceId = TraceId.from(
            "trace-session-validity-evaluation-004",
        )
        val error = createError(traceId)

        val result = SessionValidityEvaluationResult.create(
            traceId = traceId,
            status = SessionValidityEvaluationStatus.FAILED,
            error = error,
        )

        assertEquals(
            SessionValidityEvaluationStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `determined result rejects request from another trace`() {
        val request = createRequest(
            "trace-session-validity-evaluation-005",
        )

        assertFailsWith<IllegalArgumentException> {
            SessionValidityEvaluationResult.create(
                traceId = TraceId.from(
                    "trace-session-validity-evaluation-other",
                ),
                status = SessionValidityEvaluationStatus.VALID,
                request = request,
            )
        }
    }

    @Test
    fun `failed result rejects error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            SessionValidityEvaluationResult.create(
                traceId = TraceId.from(
                    "trace-session-validity-evaluation-006",
                ),
                status = SessionValidityEvaluationStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-session-validity-error-other",
                    ),
                ),
            )
        }
    }

    private fun createRequest(
        traceValue: String,
    ): SessionValidityRequest {
        val context = ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.SYSTEM,
            trustLevel = ContextTrustLevel.UNVERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_301_000L,
                ),
        )

        val session = SessionRecord.create(
            sessionId = SessionId.from(
                "session-validity-evaluation",
            ),
            subjectIdentityId = IdentityId.from(
                "subject-session-validity-evaluation",
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
        )

        return SessionValidityRequest.create(
            context = context,
            session = session,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_302_000L,
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "SESSION_VALIDITY_EVALUATION_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_303_000L,
                ),
            summary =
                "Session validity evaluation failed.",
        )
    }
}
