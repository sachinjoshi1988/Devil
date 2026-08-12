package com.devil.core.runtime.understanding

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.conversation.ConversationInput
import com.devil.core.model.conversation.ConversationIntakeRecord
import com.devil.core.model.conversation.ConversationIntakeResult
import com.devil.core.model.conversation.ConversationIntakeState
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.understanding.UnderstandingActionability
import com.devil.core.model.understanding.UnderstandingIntent
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.authorization.AuthorizationStatus
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityResult
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityStatus
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.identity.IdentityStatus
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.trust.TrustStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultUnderstandingAuthorityTest {

    @Test
    fun `understand coordinates accepted intake through provider resolver and mapper`() {
        val context = createContext(
            "trace-understanding-default-001",
        )
        val authority: UnderstandingAuthority =
            DefaultUnderstandingAuthority()

        val result = authority.understand(
            context = context,
            identity = createIdentity(context.traceId),
            trust = createTrust(context.traceId),
            authorization =
                createAuthorization(context.traceId),
            conversationIntake =
                createProducedIntake(
                    context = context,
                    state =
                        ConversationIntakeState.ACCEPTED,
                ),
        )

        assertEquals(context.traceId, result.traceId)
        assertEquals(
            UnderstandingAuthorityStatus.PRODUCED,
            result.status,
        )

        val understanding =
            requireNotNull(result.understanding)

        val semantics =
            requireNotNull(understanding.semantics)

        assertEquals(
            UnderstandingState.COMPLETE,
            understanding.state,
        )
        assertEquals(
            "User requested opening the target: the camera.",
            understanding.summary,
        )
        assertEquals(
            UnderstandingIntent.OPEN_TARGET,
            semantics.intent,
        )
        assertEquals(
            UnderstandingActionability.ACTIONABLE,
            semantics.actionability,
        )
        assertEquals(
            "open target",
            semantics.meaning,
        )
        assertEquals(
            "the camera",
            semantics.target,
        )
        assertNull(result.error)
    }

    @Test
    fun `understand defers when evaluation request is unavailable`() {
        val context = createContext(
            "trace-understanding-default-002",
        )

        val result = DefaultUnderstandingAuthority()
            .understand(
                context = context,
                identity =
                    createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                conversationIntake =
                    ConversationIntakeAuthorityResult.create(
                        traceId = context.traceId,
                        status =
                            ConversationIntakeAuthorityStatus.DEFERRED,
                    ),
            )

        assertEquals(
            UnderstandingAuthorityStatus.DEFERRED,
            result.status,
        )
        assertNull(result.understanding)
        assertNull(result.error)
    }

    @Test
    fun `understand preserves failed evaluation request error`() {
        val context = createContext(
            "trace-understanding-default-003",
        )
        val error = createError(context.traceId)

        val authority = DefaultUnderstandingAuthority(
            requestProvider = object :
                UnderstandingEvaluationRequestProvider {
                override fun provide(
                    conversationIntake:
                        ConversationIntakeAuthorityResult,
                ): UnderstandingEvaluationRequestResult {
                    return UnderstandingEvaluationRequestResult
                        .create(
                            traceId = context.traceId,
                            status =
                                UnderstandingEvaluationRequestStatus.FAILED,
                            error = error,
                        )
                }
            },
        )

        val result = authority.understand(
            context = context,
            identity = createIdentity(context.traceId),
            trust = createTrust(context.traceId),
            authorization =
                createAuthorization(context.traceId),
            conversationIntake =
                ConversationIntakeAuthorityResult.create(
                    traceId = context.traceId,
                    status =
                        ConversationIntakeAuthorityStatus.DEFERRED,
                ),
        )

        assertEquals(
            UnderstandingAuthorityStatus.FAILED,
            result.status,
        )
        assertNull(result.understanding)
        assertEquals(error, result.error)
    }

    @Test
    fun `understand rejects identity result from a different trace`() {
        val context = createContext(
            "trace-understanding-default-004",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultUnderstandingAuthority().understand(
                context = context,
                identity = createIdentity(
                    TraceId.from(
                        "trace-understanding-identity-other",
                    ),
                ),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                conversationIntake =
                    ConversationIntakeAuthorityResult.create(
                        traceId = context.traceId,
                        status =
                            ConversationIntakeAuthorityStatus.DEFERRED,
                    ),
            )
        }
    }

    @Test
    fun `understand rejects trust result from a different trace`() {
        val context = createContext(
            "trace-understanding-default-005",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultUnderstandingAuthority().understand(
                context = context,
                identity =
                    createIdentity(context.traceId),
                trust = createTrust(
                    TraceId.from(
                        "trace-understanding-trust-other",
                    ),
                ),
                authorization =
                    createAuthorization(context.traceId),
                conversationIntake =
                    ConversationIntakeAuthorityResult.create(
                        traceId = context.traceId,
                        status =
                            ConversationIntakeAuthorityStatus.DEFERRED,
                    ),
            )
        }
    }

    @Test
    fun `understand rejects authorization result from a different trace`() {
        val context = createContext(
            "trace-understanding-default-006",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultUnderstandingAuthority().understand(
                context = context,
                identity =
                    createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization = createAuthorization(
                    TraceId.from(
                        "trace-understanding-authorization-other",
                    ),
                ),
                conversationIntake =
                    ConversationIntakeAuthorityResult.create(
                        traceId = context.traceId,
                        status =
                            ConversationIntakeAuthorityStatus.DEFERRED,
                    ),
            )
        }
    }

    @Test
    fun `understand rejects conversation intake from a different trace`() {
        val context = createContext(
            "trace-understanding-default-007",
        )

        assertFailsWith<IllegalArgumentException> {
            DefaultUnderstandingAuthority().understand(
                context = context,
                identity =
                    createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                conversationIntake =
                    ConversationIntakeAuthorityResult.create(
                        traceId = TraceId.from(
                            "trace-understanding-intake-other",
                        ),
                        status =
                            ConversationIntakeAuthorityStatus.DEFERRED,
                    ),
            )
        }
    }

    @Test
    fun `understand rejects request result from a different trace`() {
        val context = createContext(
            "trace-understanding-default-008",
        )
        val authority = DefaultUnderstandingAuthority(
            requestProvider = object :
                UnderstandingEvaluationRequestProvider {
                override fun provide(
                    conversationIntake:
                        ConversationIntakeAuthorityResult,
                ): UnderstandingEvaluationRequestResult {
                    return UnderstandingEvaluationRequestResult
                        .create(
                            traceId = TraceId.from(
                                "trace-understanding-request-other",
                            ),
                            status =
                                UnderstandingEvaluationRequestStatus.UNAVAILABLE,
                        )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            authority.understand(
                context = context,
                identity =
                    createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                conversationIntake =
                    ConversationIntakeAuthorityResult.create(
                        traceId = context.traceId,
                        status =
                            ConversationIntakeAuthorityStatus.DEFERRED,
                    ),
            )
        }
    }

    @Test
    fun `understand rejects mapped result from a different trace`() {
        val context = createContext(
            "trace-understanding-default-009",
        )
        val authority = DefaultUnderstandingAuthority(
            resultMapper = object :
                UnderstandingEvaluationResultMapper {
                override fun map(
                    traceId: TraceId,
                    understanding: UnderstandingRecord,
                ): UnderstandingAuthorityResult {
                    return UnderstandingAuthorityResult.create(
                        traceId = TraceId.from(
                            "trace-understanding-mapper-other",
                        ),
                        status =
                            UnderstandingAuthorityStatus.DEFERRED,
                    )
                }
            },
        )

        assertFailsWith<IllegalArgumentException> {
            authority.understand(
                context = context,
                identity =
                    createIdentity(context.traceId),
                trust = createTrust(context.traceId),
                authorization =
                    createAuthorization(context.traceId),
                conversationIntake =
                    createProducedIntake(
                        context = context,
                        state =
                            ConversationIntakeState.ACCEPTED,
                    ),
            )
        }
    }

    private fun createProducedIntake(
        context: ContextEnvelope,
        state: ConversationIntakeState,
    ): ConversationIntakeAuthorityResult {
        return ConversationIntakeAuthorityResult.create(
            traceId = context.traceId,
            status =
                ConversationIntakeAuthorityStatus.PRODUCED,
            intake = ConversationIntakeResult.create(
                record = ConversationIntakeRecord.create(
                    input = ConversationInput.create(
                        context = context,
                        content =
                            "Please open the camera.",
                    ),
                    state = state,
                    rationale =
                        "Bounded conversation intake was established.",
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
                "UNDERSTANDING_EVALUATION_REQUEST_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_070_500L,
                ),
            summary =
                "Understanding evaluation request failed.",
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
                    1_754_000_070_000L,
                ),
        )
    }
}
