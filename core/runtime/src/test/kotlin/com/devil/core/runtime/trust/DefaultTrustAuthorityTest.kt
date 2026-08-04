package com.devil.core.runtime.trust

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
import com.devil.core.model.trust.TrustEvaluationRequest
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.identity.IdentityStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultTrustAuthorityTest {

    @Test
    fun `evaluate coordinates resolved identity through trust chain`() {
        val context = createContext(
            "trace-trust-default-001",
            ContextTrustLevel.TRUSTED,
        )
        val identity = IdentityResult.create(
            traceId = context.traceId,
            status = IdentityStatus.RESOLVED,
            identityId = IdentityId.from(
                "subject-trust-default-001",
            ),
        )
        val authority: TrustAuthority =
            DefaultTrustAuthority()

        val result = authority.evaluate(
            context = context,
            identity = identity,
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(TrustStatus.DEFERRED, result.status)
        assertNull(result.trustLevel)
        assertNull(result.error)
    }

    @Test
    fun `evaluate defers when trust request is unavailable`() {
        val context = createContext(
            "trace-trust-default-002",
        )
        val authority: TrustAuthority =
            DefaultTrustAuthority()

        val result = authority.evaluate(
            context = context,
            identity = IdentityResult.create(
                traceId = context.traceId,
                status = IdentityStatus.UNRESOLVED,
            ),
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(TrustStatus.DEFERRED, result.status)
        assertNull(result.trustLevel)
        assertNull(result.error)
    }

    @Test
    fun `evaluate preserves failed trust request result`() {
        val context = createContext(
            "trace-trust-default-003",
        )
        val error = createError(context.traceId)

        val authority: TrustAuthority =
            DefaultTrustAuthority(
                requestProvider = object :
                    TrustEvaluationRequestProvider {
                    override fun provide(
                        context: ContextEnvelope,
                        identity: IdentityResult,
                    ): TrustEvaluationRequestResult {
                        return TrustEvaluationRequestResult.create(
                            traceId = context.traceId,
                            status = TrustEvaluationRequestStatus.FAILED,
                            error = error,
                        )
                    }
                },
            )

        val result = authority.evaluate(
            context = context,
            identity = IdentityResult.create(
                traceId = context.traceId,
                status = IdentityStatus.UNRESOLVED,
            ),
        )

        assertEquals(TrustStatus.FAILED, result.status)
        assertNull(result.trustLevel)
        assertEquals(error, result.error)
    }

    @Test
    fun `evaluate rejects request result from a different trace`() {
        val context = createContext(
            "trace-trust-default-004",
        )

        val authority: TrustAuthority =
            DefaultTrustAuthority(
                requestProvider = object :
                    TrustEvaluationRequestProvider {
                    override fun provide(
                        context: ContextEnvelope,
                        identity: IdentityResult,
                    ): TrustEvaluationRequestResult {
                        return TrustEvaluationRequestResult.create(
                            traceId = TraceId.from(
                                "trace-trust-request-other",
                            ),
                            status = TrustEvaluationRequestStatus.UNAVAILABLE,
                        )
                    }
                },
            )

        assertFailsWith<IllegalArgumentException> {
            authority.evaluate(
                context = context,
                identity = IdentityResult.create(
                    traceId = context.traceId,
                    status = IdentityStatus.UNRESOLVED,
                ),
            )
        }
    }

    @Test
    fun `evaluate rejects mapped result from a different trace`() {
        val context = createContext(
            "trace-trust-default-005",
        )
        val identityId = IdentityId.from(
            "subject-trust-default-005",
        )
        val request = TrustEvaluationRequest.create(
            context = context,
            subjectIdentityId = identityId,
        )

        val authority: TrustAuthority =
            DefaultTrustAuthority(
                requestProvider = availableProvider(request),
                resolver = object : TrustEvaluationResolver {
                    override fun evaluate(
                        request: TrustEvaluationRequest,
                    ): TrustAssessment {
                        return TrustAssessment.create(
                            subjectIdentityId =
                                request.subjectIdentityId,
                            level =
                                SubjectTrustLevel.UNESTABLISHED,
                            rationale =
                                "No trust conclusion is available.",
                        )
                    }
                },
                resultMapper = object :
                    TrustEvaluationResultMapper {
                    override fun map(
                        traceId: TraceId,
                        assessment: TrustAssessment,
                    ): TrustResult {
                        return TrustResult.create(
                            traceId = TraceId.from(
                                "trace-trust-mapper-other",
                            ),
                            status = TrustStatus.DEFERRED,
                        )
                    }
                },
            )

        assertFailsWith<IllegalArgumentException> {
            authority.evaluate(
                context = context,
                identity = IdentityResult.create(
                    traceId = context.traceId,
                    status = IdentityStatus.RESOLVED,
                    identityId = identityId,
                ),
            )
        }
    }

    private fun availableProvider(
        request: TrustEvaluationRequest,
    ): TrustEvaluationRequestProvider {
        return object : TrustEvaluationRequestProvider {
            override fun provide(
                context: ContextEnvelope,
                identity: IdentityResult,
            ): TrustEvaluationRequestResult {
                return TrustEvaluationRequestResult.create(
                    traceId = context.traceId,
                    status = TrustEvaluationRequestStatus.AVAILABLE,
                    request = request,
                )
            }
        }
    }

    private fun createContext(
        traceValue: String,
        trustLevel: ContextTrustLevel =
            ContextTrustLevel.VERIFIED,
    ): ContextEnvelope {
        return ContextEnvelope.create(
            traceId = TraceId.from(traceValue),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = trustLevel,
            securityLevel = ContextSecurityLevel.RESTRICTED,
            observedAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_052_000L,
            ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "TRUST_EVALUATION_REQUEST_FAILED",
            ),
            traceId = traceId,
            occurredAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_052_500L,
            ),
            summary = "Trust evaluation request failed.",
        )
    }
}
