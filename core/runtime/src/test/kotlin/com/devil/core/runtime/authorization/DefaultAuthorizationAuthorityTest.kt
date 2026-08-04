package com.devil.core.runtime.authorization

import com.devil.core.model.authorization.AuthorizationAssessment
import com.devil.core.model.authorization.AuthorizationEvaluationRequest
import com.devil.core.model.authorization.AuthorizationEvaluationState
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
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.identity.IdentityStatus
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.trust.TrustStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultAuthorizationAuthorityTest {

    @Test
    fun `authorize defers when authorization request is unavailable`() {
        val context = createContext(
            "trace-authorization-default-001",
        )
        val authority: AuthorizationAuthority =
            DefaultAuthorizationAuthority()

        val result = authority.authorize(
            context = context,
            identity = IdentityResult.create(
                traceId = context.traceId,
                status = IdentityStatus.UNRESOLVED,
            ),
            trust = TrustResult.create(
                traceId = context.traceId,
                status = TrustStatus.DEFERRED,
            ),
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(
            AuthorizationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.error)
    }

    @Test
    fun `authorize coordinates available request through resolver and mapper`() {
        val context = createContext(
            "trace-authorization-default-002",
        )
        val identityId = IdentityId.from(
            "subject-authorization-default-002",
        )
        val request = createRequest(
            context = context,
            identityId = identityId,
        )
        val authority: AuthorizationAuthority =
            DefaultAuthorizationAuthority(
                requestProvider = availableProvider(request),
                resolver = object :
                    AuthorizationEvaluationResolver {
                    override fun evaluate(
                        request: AuthorizationEvaluationRequest,
                    ): AuthorizationAssessment {
                        return AuthorizationAssessment.create(
                            subjectIdentityId =
                                request.subjectIdentityId,
                            state =
                                AuthorizationEvaluationState.AUTHORIZED,
                            rationale =
                                "Test policy permits constitutional continuation.",
                        )
                    }
                },
                resultMapper =
                    DefaultAuthorizationEvaluationResultMapper(),
            )

        val result = authority.authorize(
            context = context,
            identity = IdentityResult.create(
                traceId = context.traceId,
                status = IdentityStatus.RESOLVED,
                identityId = identityId,
            ),
            trust = TrustResult.create(
                traceId = context.traceId,
                status = TrustStatus.DEFERRED,
            ),
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(
            AuthorizationStatus.AUTHORIZED,
            result.status,
        )
        assertNull(result.error)
    }

    @Test
    fun `authorize preserves failed request result`() {
        val context = createContext(
            "trace-authorization-default-003",
        )
        val error = createError(context.traceId)
        val authority: AuthorizationAuthority =
            DefaultAuthorizationAuthority(
                requestProvider = object :
                    AuthorizationEvaluationRequestProvider {
                    override fun provide(
                        context: ContextEnvelope,
                        identity: IdentityResult,
                        trust: TrustResult,
                    ): AuthorizationEvaluationRequestResult {
                        return AuthorizationEvaluationRequestResult.create(
                            traceId = context.traceId,
                            status =
                                AuthorizationEvaluationRequestStatus.FAILED,
                            error = error,
                        )
                    }
                },
            )

        val result = authority.authorize(
            context = context,
            identity = IdentityResult.create(
                traceId = context.traceId,
                status = IdentityStatus.UNRESOLVED,
            ),
            trust = TrustResult.create(
                traceId = context.traceId,
                status = TrustStatus.DEFERRED,
            ),
        )

        assertEquals(
            AuthorizationStatus.FAILED,
            result.status,
        )
        assertEquals(error, result.error)
    }

    @Test
    fun `authorize rejects request result from a different trace`() {
        val context = createContext(
            "trace-authorization-default-004",
        )
        val authority: AuthorizationAuthority =
            DefaultAuthorizationAuthority(
                requestProvider = object :
                    AuthorizationEvaluationRequestProvider {
                    override fun provide(
                        context: ContextEnvelope,
                        identity: IdentityResult,
                        trust: TrustResult,
                    ): AuthorizationEvaluationRequestResult {
                        return AuthorizationEvaluationRequestResult.create(
                            traceId = TraceId.from(
                                "trace-authorization-request-other",
                            ),
                            status =
                                AuthorizationEvaluationRequestStatus.UNAVAILABLE,
                        )
                    }
                },
            )

        assertFailsWith<IllegalArgumentException> {
            authority.authorize(
                context = context,
                identity = IdentityResult.create(
                    traceId = context.traceId,
                    status = IdentityStatus.UNRESOLVED,
                ),
                trust = TrustResult.create(
                    traceId = context.traceId,
                    status = TrustStatus.DEFERRED,
                ),
            )
        }
    }

    @Test
    fun `authorize rejects mapped result from a different trace`() {
        val context = createContext(
            "trace-authorization-default-005",
        )
        val identityId = IdentityId.from(
            "subject-authorization-default-005",
        )
        val request = createRequest(
            context = context,
            identityId = identityId,
        )
        val authority: AuthorizationAuthority =
            DefaultAuthorizationAuthority(
                requestProvider = availableProvider(request),
                resolver = DefaultAuthorizationEvaluationResolver(),
                resultMapper = object :
                    AuthorizationEvaluationResultMapper {
                    override fun map(
                        traceId: TraceId,
                        assessment: AuthorizationAssessment,
                    ): AuthorizationResult {
                        return AuthorizationResult.create(
                            traceId = TraceId.from(
                                "trace-authorization-mapper-other",
                            ),
                            status = AuthorizationStatus.DEFERRED,
                        )
                    }
                },
            )

        assertFailsWith<IllegalArgumentException> {
            authority.authorize(
                context = context,
                identity = IdentityResult.create(
                    traceId = context.traceId,
                    status = IdentityStatus.RESOLVED,
                    identityId = identityId,
                ),
                trust = TrustResult.create(
                    traceId = context.traceId,
                    status = TrustStatus.DEFERRED,
                ),
            )
        }
    }

    private fun availableProvider(
        request: AuthorizationEvaluationRequest,
    ): AuthorizationEvaluationRequestProvider {
        return object : AuthorizationEvaluationRequestProvider {
            override fun provide(
                context: ContextEnvelope,
                identity: IdentityResult,
                trust: TrustResult,
            ): AuthorizationEvaluationRequestResult {
                return AuthorizationEvaluationRequestResult.create(
                    traceId = context.traceId,
                    status =
                        AuthorizationEvaluationRequestStatus.AVAILABLE,
                    request = request,
                )
            }
        }
    }

    private fun createRequest(
        context: ContextEnvelope,
        identityId: IdentityId,
    ): AuthorizationEvaluationRequest {
        return AuthorizationEvaluationRequest.create(
            context = context,
            subjectIdentityId = identityId,
            trustAssessment = TrustAssessment.create(
                subjectIdentityId = identityId,
                level = SubjectTrustLevel.UNESTABLISHED,
                rationale =
                    "No subject trust conclusion is available.",
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
                1_754_000_057_000L,
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
                1_754_000_057_500L,
            ),
            summary = "Authorization evaluation request failed.",
        )
    }
}
