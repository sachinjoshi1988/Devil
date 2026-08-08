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

class SessionValidityResultTest {

    @Test
    fun `valid result preserves matching request`() {
        val request = createRequest(
            "trace-session-validity-result-001",
        )

        val result = SessionValidityResult.create(
            traceId = request.context.traceId,
            status = SessionValidityStatus.VALID,
            request = request,
        )

        assertEquals(SessionValidityStatus.VALID, result.status)
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `invalid result preserves matching request`() {
        val request = createRequest(
            "trace-session-validity-result-002",
        )

        val result = SessionValidityResult.create(
            traceId = request.context.traceId,
            status = SessionValidityStatus.INVALID,
            request = request,
        )

        assertEquals(SessionValidityStatus.INVALID, result.status)
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `deferred result contains neither request nor error`() {
        val traceId = TraceId.from(
            "trace-session-validity-result-003",
        )

        val result = SessionValidityResult.create(
            traceId = traceId,
            status = SessionValidityStatus.DEFERRED,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(SessionValidityStatus.DEFERRED, result.status)
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `failed result preserves matching error`() {
        val traceId = TraceId.from(
            "trace-session-validity-result-004",
        )
        val error = createError(traceId)

        val result = SessionValidityResult.create(
            traceId = traceId,
            status = SessionValidityStatus.FAILED,
            error = error,
        )

        assertEquals(SessionValidityStatus.FAILED, result.status)
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `determined result rejects request from another trace`() {
        val request = createRequest(
            "trace-session-validity-result-005",
        )

        assertFailsWith<IllegalArgumentException> {
            SessionValidityResult.create(
                traceId = TraceId.from(
                    "trace-session-validity-result-other",
                ),
                status = SessionValidityStatus.VALID,
                request = request,
            )
        }
    }

    @Test
    fun `failed result rejects error from another trace`() {
        assertFailsWith<IllegalArgumentException> {
            SessionValidityResult.create(
                traceId = TraceId.from(
                    "trace-session-validity-result-006",
                ),
                status = SessionValidityStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-session-validity-result-error-other",
                    ),
                ),
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
                            1_754_000_304_000L,
                        ),
                ),
            session =
                SessionRecord.create(
                    sessionId = SessionId.from(
                        "session-validity-result",
                    ),
                    subjectIdentityId = IdentityId.from(
                        "subject-session-validity-result",
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
                    1_754_000_304_000L,
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
                    1_754_000_304_100L,
                ),
            summary = "Session validity failed.",
        )
    }
}
