package com.devil.core.runtime.decision

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingActionability
import com.devil.core.model.understanding.UnderstandingIntent
import com.devil.core.model.understanding.UnderstandingSemantics
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.authorization.AuthorizationStatus
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.identity.IdentityStatus
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.trust.TrustStatus
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultDecisionAuthorityTest {

    @Test
    fun `decide coordinates produced understanding through provider resolver and mapper`() {
        val context = createContext(
            "trace-decision-default-001",
        )

        val result = DefaultDecisionAuthority().decide(
            context = context,
            identity = createIdentity(context.traceId),
            trust = createTrust(context.traceId),
            authorization =
                createAuthorization(context.traceId),
            understanding =
                createProducedUnderstanding(context),
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(
            DecisionAuthorityStatus.PRODUCED,
            result.status,
        )

        val decision = requireNotNull(result.decision)

        assertEquals(
            DecisionState.SELECTED,
            decision.state,
        )
        assertEquals(
            "Proceed with the understood request to open target: camera.",
            decision.summary,
        )
        assertNull(result.error)
    }
    @Test
    fun `decide defers when evaluation request is unavailable`() {
        val context = createContext(
            "trace-decision-default-002",
        )

        val result = DefaultDecisionAuthority().decide(
            context = context,
            identity = createIdentity(context.traceId),
            trust = createTrust(context.traceId),
            authorization =
                createAuthorization(context.traceId),
            understanding =
                UnderstandingAuthorityResult.create(
                    traceId = context.traceId,
                    status =
                        UnderstandingAuthorityStatus.DEFERRED,
                ),
        )

        assertEquals(
            DecisionAuthorityStatus.DEFERRED,
            result.status,
        )
        assertNull(result.decision)
        assertNull(result.error)
    }

    @Test
    fun `decide preserves failed evaluation request error`() {
        val context = createContext(
            "trace-decision-default-003",
        )
        val error = createError(context.traceId)

        val authority = DefaultDecisionAuthority(
            requestProvider = object :
                DecisionEvaluationRequestProvider {
                override fun provide(
                    understanding:
                        UnderstandingAuthorityResult,
                ): DecisionEvaluationRequestResult {
                    return DecisionEvaluationRequestResult.create(
                        traceId = context.traceId,
                        status =
                            DecisionEvaluationRequestStatus.FAILED,
                        error = error,
                    )
                }
            },
        )

        val result = authority.decide(
            context = context,
            identity = createIdentity(context.traceId),
            trust = createTrust(context.traceId),
            authorization =
                createAuthorization(context.traceId),
            understanding =
                UnderstandingAuthorityResult.create(
                    traceId = context.traceId,
                    status =
                        UnderstandingAuthorityStatus.DEFERRED,
                ),
        )

        assertEquals(
            DecisionAuthorityStatus.FAILED,
            result.status,
        )
        assertNull(result.decision)
        assertEquals(error, result.error)
    }

    @Test
    fun `decide rejects identity result from a different trace`() {
        val context = createContext(
            "trace-decision-default-004",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultDecisionAuthority().decide(
                context = context,
                identity = createIdentity(
                    TraceId.from(
                        "trace-decision-identity-other",
                    ),
                ),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                understanding =
                    UnderstandingAuthorityResult.create(
                        traceId = context.traceId,
                        status =
                            UnderstandingAuthorityStatus.DEFERRED,
                    ),
            )
        }
    }

    @Test
    fun `decide rejects trust result from a different trace`() {
        val context = createContext(
            "trace-decision-default-005",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultDecisionAuthority().decide(
                context = context,
                identity =
                    createIdentity(context.traceId),
                trust = createTrust(
                    TraceId.from(
                        "trace-decision-trust-other",
                    ),
                ),
                authorization =
                    createAuthorization(context.traceId),
                understanding =
                    UnderstandingAuthorityResult.create(
                        traceId = context.traceId,
                        status =
                            UnderstandingAuthorityStatus.DEFERRED,
                    ),
            )
        }
    }

    @Test
    fun `decide rejects authorization result from a different trace`() {
        val context = createContext(
            "trace-decision-default-006",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultDecisionAuthority().decide(
                context = context,
                identity =
                    createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(
                    TraceId.from(
                        "trace-decision-authorization-other",
                    ),
                ),
                understanding =
                    UnderstandingAuthorityResult.create(
                        traceId = context.traceId,
                        status =
                            UnderstandingAuthorityStatus.DEFERRED,
                    ),
            )
        }
    }

    @Test
    fun `decide rejects understanding result from a different trace`() {
        val context = createContext(
            "trace-decision-default-007",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultDecisionAuthority().decide(
                context = context,
                identity =
                    createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                understanding =
                    UnderstandingAuthorityResult.create(
                        traceId = TraceId.from(
                            "trace-decision-understanding-other",
                        ),
                        status =
                            UnderstandingAuthorityStatus.DEFERRED,
                    ),
            )
        }
    }

    @Test
    fun `decide rejects request result from a different trace`() {
        val context = createContext(
            "trace-decision-default-008",
        )

        val authority = DefaultDecisionAuthority(
            requestProvider = object :
                DecisionEvaluationRequestProvider {
                override fun provide(
                    understanding:
                        UnderstandingAuthorityResult,
                ): DecisionEvaluationRequestResult {
                    return DecisionEvaluationRequestResult.create(
                        traceId = TraceId.from(
                            "trace-decision-request-other",
                        ),
                        status =
                            DecisionEvaluationRequestStatus.UNAVAILABLE,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            authority.decide(
                context = context,
                identity =
                    createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                understanding =
                    UnderstandingAuthorityResult.create(
                        traceId = context.traceId,
                        status =
                            UnderstandingAuthorityStatus.DEFERRED,
                    ),
            )
        }
    }

    @Test
    fun `decide rejects mapped result from a different trace`() {
        val context = createContext(
            "trace-decision-default-009",
        )

        val authority = DefaultDecisionAuthority(
            resultMapper = object :
                DecisionEvaluationResultMapper {
                override fun map(
                    traceId: TraceId,
                    decision: DecisionRecord,
                ): DecisionAuthorityResult {
                    return DecisionAuthorityResult.create(
                        traceId = TraceId.from(
                            "trace-decision-mapper-other",
                        ),
                        status =
                            DecisionAuthorityStatus.DEFERRED,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            authority.decide(
                context = context,
                identity =
                    createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                understanding =
                    createProducedUnderstanding(context),
            )
        }
    }

    private fun createProducedUnderstanding(
        context: ContextEnvelope,
    ): UnderstandingAuthorityResult {
        return UnderstandingAuthorityResult.create(
            traceId = context.traceId,
            status = UnderstandingAuthorityStatus.PRODUCED,
            understanding = UnderstandingRecord.create(
                context = context,
                state = UnderstandingState.COMPLETE,
                summary =
                    "User requested opening the target: camera.",
                semantics = UnderstandingSemantics.create(
                    intent = UnderstandingIntent.OPEN_TARGET,
                    actionability =
                        UnderstandingActionability.ACTIONABLE,
                    meaning = "open target",
                    target = "camera",
                ),
            ),
        )
    }


    private fun createIdentity(
        traceId: TraceId,
    ): IdentityResult {
        return IdentityResult.create(
            traceId = traceId,
            status = IdentityStatus.UNRESOLVED,
        )
    }

    private fun createTrust(
        traceId: TraceId,
    ): TrustResult {
        return TrustResult.create(
            traceId = traceId,
            status = TrustStatus.DEFERRED,
        )
    }

    private fun createAuthorization(
        traceId: TraceId,
    ): AuthorizationResult {
        return AuthorizationResult.create(
            traceId = traceId,
            status = AuthorizationStatus.DEFERRED,
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "DECISION_EVALUATION_REQUEST_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_076_500L,
                ),
            summary =
                "Decision evaluation request failed.",
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
            securityLevel =
                ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_076_000L,
                ),
        )
    }
}
