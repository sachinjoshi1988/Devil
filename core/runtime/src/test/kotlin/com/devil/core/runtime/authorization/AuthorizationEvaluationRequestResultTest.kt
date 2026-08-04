package com.devil.core.runtime.authorization

import com.devil.core.model.authorization.AuthorizationEvaluationRequest
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
import com.devil.core.model.trust.SubjectTrustLevel
import com.devil.core.model.trust.TrustAssessment
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class AuthorizationEvaluationRequestResultTest {

    @Test
    fun `create preserves available result with matching request`() {
        val context = createContext(
            "trace-authorization-request-result-001",
        )
        val request = createRequest(context)

        val result = AuthorizationEvaluationRequestResult.create(
            traceId = context.traceId,
            status = AuthorizationEvaluationRequestStatus.AVAILABLE,
            request = request,
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(
            AuthorizationEvaluationRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result without request or error`() {
        val traceId = TraceId.from(
            "trace-authorization-request-result-002",
        )

        val result = AuthorizationEvaluationRequestResult.create(
            traceId = traceId,
            status = AuthorizationEvaluationRequestStatus.UNAVAILABLE,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            AuthorizationEvaluationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-authorization-request-result-003",
        )
        val error = createError(traceId)

        val result = AuthorizationEvaluationRequestResult.create(
            traceId = traceId,
            status = AuthorizationEvaluationRequestStatus.FAILED,
            error = error,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            AuthorizationEvaluationRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without request`() {
        assertFailsWith<IllegalArgumentException> {
            AuthorizationEvaluationRequestResult.create(
                traceId = TraceId.from(
                    "trace-authorization-request-result-004",
                ),
                status = AuthorizationEvaluationRequestStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects available request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            AuthorizationEvaluationRequestResult.create(
                traceId = TraceId.from(
                    "trace-authorization-request-result-005",
                ),
                status = AuthorizationEvaluationRequestStatus.AVAILABLE,
                request = createRequest(
                    createContext(
                        "trace-authorization-request-result-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with request`() {
        val context = createContext(
            "trace-authorization-request-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            AuthorizationEvaluationRequestResult.create(
                traceId = context.traceId,
                status = AuthorizationEvaluationRequestStatus.UNAVAILABLE,
                request = createRequest(context),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            AuthorizationEvaluationRequestResult.create(
                traceId = TraceId.from(
                    "trace-authorization-request-result-007",
                ),
                status = AuthorizationEvaluationRequestStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            AuthorizationEvaluationRequestResult.create(
                traceId = TraceId.from(
                    "trace-authorization-request-result-008",
                ),
                status = AuthorizationEvaluationRequestStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-authorization-request-error-other",
                    ),
                ),
            )
        }
    }

    private fun createRequest(
        context: ContextEnvelope,
    ): AuthorizationEvaluationRequest {
        val identityId = IdentityId.from(
            "subject-authorization-request-result",
        )

        return AuthorizationEvaluationRequest.create(
            context = context,
            subjectIdentityId = identityId,
            trustAssessment = TrustAssessment.create(
                subjectIdentityId = identityId,
                level = SubjectTrustLevel.UNESTABLISHED,
                rationale = "No subject trust conclusion is available.",
            ),
        )
    }

    private fun createContext(
        traceValue: String,
    ): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_054_000L,
            ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "AUTHORIZATION_EVALUATION_REQUEST_FAILED",
            ),
            traceId = traceId,
            occurredAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_054_500L,
            ),
            summary = "Authorization evaluation request construction failed.",
        )
    }
}
