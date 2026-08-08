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

class DefaultSessionValidityAuthorityTest {

    @Test
    fun `default authority returns valid for active session within validity window`() {
        val request = createRequest(
            traceId = TraceId.from(
                "trace-session-validity-authority-001",
            ),
            state = SessionState.ACTIVE,
            observedAt = VALID_OBSERVATION,
        )

        val authority: SessionValidityAuthority =
            DefaultSessionValidityAuthority()

        val result = authority.evaluateValidity(request)

        assertEquals(
            request.context.traceId,
            result.traceId,
        )
        assertEquals(
            SessionValidityStatus.VALID,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `default authority returns invalid for expired active session`() {
        val request = createRequest(
            traceId = TraceId.from(
                "trace-session-validity-authority-002",
            ),
            state = SessionState.ACTIVE,
            observedAt = EXPIRES_AT,
        )

        val result =
            DefaultSessionValidityAuthority()
                .evaluateValidity(request)

        assertEquals(
            SessionValidityStatus.INVALID,
            result.status,
        )
        assertEquals(request, result.request)
        assertEquals(
            SessionState.ACTIVE,
            request.session.state,
        )
    }

    @Test
    fun `default authority returns invalid for revoked session without mutating it`() {
        val request = createRequest(
            traceId = TraceId.from(
                "trace-session-validity-authority-003",
            ),
            state = SessionState.REVOKED,
            observedAt = VALID_OBSERVATION,
        )

        val originalSession = request.session

        val result =
            DefaultSessionValidityAuthority()
                .evaluateValidity(request)

        assertEquals(
            SessionValidityStatus.INVALID,
            result.status,
        )
        assertEquals(originalSession, request.session)
        assertEquals(
            SessionState.REVOKED,
            request.session.state,
        )
    }

    @Test
    fun `authority preserves evaluator unavailable as deferred result`() {
        val request = createRequest(
            traceId = TraceId.from(
                "trace-session-validity-authority-004",
            ),
            state = SessionState.ACTIVE,
            observedAt = VALID_OBSERVATION,
        )

        val evaluator =
            object : SessionValidityEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: SessionValidityRequest,
                ): SessionValidityEvaluationResult {
                    return SessionValidityEvaluationResult.create(
                        traceId = traceId,
                        status =
                            SessionValidityEvaluationStatus.UNAVAILABLE,
                    )
                }
            }

        val authority =
            DefaultSessionValidityAuthority(
                evaluator = evaluator,
            )

        val result = authority.evaluateValidity(request)

        assertEquals(
            SessionValidityStatus.DEFERRED,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `authority rejects evaluator result from another trace`() {
        val request = createRequest(
            traceId = TraceId.from(
                "trace-session-validity-authority-005",
            ),
            state = SessionState.ACTIVE,
            observedAt = VALID_OBSERVATION,
        )

        val evaluator =
            object : SessionValidityEvaluator {
                override fun evaluate(
                    traceId: TraceId,
                    request: SessionValidityRequest,
                ): SessionValidityEvaluationResult {
                    return SessionValidityEvaluationResult.create(
                        traceId =
                            TraceId.from(
                                "trace-session-validity-authority-evaluation-other",
                            ),
                        status =
                            SessionValidityEvaluationStatus.UNAVAILABLE,
                    )
                }
            }

        val authority =
            DefaultSessionValidityAuthority(
                evaluator = evaluator,
            )

        assertFailsWith<IllegalArgumentException> {
            authority.evaluateValidity(request)
        }
    }

    @Test
    fun `authority rejects mapped result from another trace`() {
        val request = createRequest(
            traceId = TraceId.from(
                "trace-session-validity-authority-006",
            ),
            state = SessionState.ACTIVE,
            observedAt = VALID_OBSERVATION,
        )

        val mapper =
            object : SessionValidityResultMapper {
                override fun map(
                    traceId: TraceId,
                    evaluation: SessionValidityEvaluationResult,
                ): SessionValidityResult {
                    return SessionValidityResult.create(
                        traceId =
                            TraceId.from(
                                "trace-session-validity-authority-result-other",
                            ),
                        status = SessionValidityStatus.DEFERRED,
                    )
                }
            }

        val authority =
            DefaultSessionValidityAuthority(
                resultMapper = mapper,
            )

        assertFailsWith<IllegalArgumentException> {
            authority.evaluateValidity(request)
        }
    }

    @Test
    fun `authority does not advance constitutional security stage`() {
        val request = createRequest(
            traceId = TraceId.from(
                "trace-session-validity-authority-007",
            ),
            state = SessionState.ACTIVE,
            observedAt = VALID_OBSERVATION,
        )

        val result =
            DefaultSessionValidityAuthority()
                .evaluateValidity(request)

        assertEquals(
            SessionValidityStatus.VALID,
            result.status,
        )

        assertEquals(
            SessionState.ACTIVE,
            request.session.state,
        )
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
                    trustLevel =
                        ContextTrustLevel.UNVERIFIED,
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
                        "session-validity-authority",
                    ),
                    subjectIdentityId = IdentityId.from(
                        "subject-session-validity-authority",
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

        private const val VALID_OBSERVATION =
            1_754_000_300_000L

        private const val EXPIRES_AT =
            1_754_003_796_000L
    }
}
