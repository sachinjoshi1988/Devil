package com.devil.core.runtime.conversation

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.conversation.ConversationId
import com.devil.core.model.conversation.ConversationInput
import com.devil.core.model.conversation.ConversationIntakeRecord
import com.devil.core.model.conversation.ConversationIntakeResult
import com.devil.core.model.conversation.ConversationIntakeState
import com.devil.core.model.conversation.ConversationRecordRequest
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultConversationRecordAuthorityTest {

    @Test
    fun `record defers when conversation identity is unavailable`() {
        val intake =
            createProducedIntake(
                traceValue =
                    "trace-conversation-record-authority-001",
            )

        val result =
            DefaultConversationRecordAuthority().record(
                conversationIntake = intake,
            )

        assertEquals(
            intake.traceId,
            result.traceId,
        )
        assertEquals(
            ConversationRecordStatus.DEFERRED,
            result.status,
        )
        assertNull(result.record)
        assertNull(result.error)
    }

    @Test
    fun `record produces bounded conversation record when genuine identity is available`() {
        val intake =
            createProducedIntake(
                traceValue =
                    "trace-conversation-record-authority-002",
            )
        val conversationId =
            ConversationId.from(
                "conversation-record-authority-002",
            )

        val authority =
            DefaultConversationRecordAuthority(
                identityProvider =
                    object : ConversationIdentityProvider {
                        override fun provide(
                            traceId: TraceId,
                            input: ConversationInput,
                        ): ConversationIdentityProvisionResult {
                            return ConversationIdentityProvisionResult.create(
                                traceId = traceId,
                                status =
                                    ConversationIdentityProvisionStatus.AVAILABLE,
                                conversationId = conversationId,
                            )
                        }
                    },
            )

        val result =
            authority.record(
                conversationIntake = intake,
            )

        assertEquals(
            ConversationRecordStatus.PRODUCED,
            result.status,
        )
        assertEquals(
            conversationId,
            result.record?.conversationId,
        )
        assertEquals(
            requireNotNull(intake.intake),
            result.record?.intake,
        )
        assertNull(result.error)
    }

    @Test
    fun `record preserves failed request error`() {
        val intake =
            createProducedIntake(
                traceValue =
                    "trace-conversation-record-authority-003",
            )
        val error =
            createError(
                traceId = intake.traceId,
                code =
                    "CONVERSATION_RECORD_REQUEST_FAILED",
            )

        val authority =
            DefaultConversationRecordAuthority(
                requestProvider =
                    object : ConversationRecordRequestProvider {
                        override fun provide(
                            conversationIntake: ConversationIntakeAuthorityResult,
                        ): ConversationRecordRequestResult {
                            return ConversationRecordRequestResult.create(
                                traceId = conversationIntake.traceId,
                                status =
                                    ConversationRecordRequestStatus.FAILED,
                                error = error,
                            )
                        }
                    },
            )

        val result =
            authority.record(
                conversationIntake = intake,
            )

        assertEquals(
            ConversationRecordStatus.FAILED,
            result.status,
        )
        assertNull(result.record)
        assertEquals(
            error,
            result.error,
        )
    }

    @Test
    fun `record preserves failed conversation identity error`() {
        val intake =
            createProducedIntake(
                traceValue =
                    "trace-conversation-record-authority-004",
            )
        val error =
            createError(
                traceId = intake.traceId,
                code =
                    "CONVERSATION_IDENTITY_PROVISION_FAILED",
            )

        val authority =
            DefaultConversationRecordAuthority(
                identityProvider =
                    object : ConversationIdentityProvider {
                        override fun provide(
                            traceId: TraceId,
                            input: ConversationInput,
                        ): ConversationIdentityProvisionResult {
                            return ConversationIdentityProvisionResult.create(
                                traceId = traceId,
                                status =
                                    ConversationIdentityProvisionStatus.FAILED,
                                error = error,
                            )
                        }
                    },
            )

        val result =
            authority.record(
                conversationIntake = intake,
            )

        assertEquals(
            ConversationRecordStatus.FAILED,
            result.status,
        )
        assertNull(result.record)
        assertEquals(
            error,
            result.error,
        )
    }

    @Test
    fun `record rejects request result from a different trace`() {
        val intake =
            createProducedIntake(
                traceValue =
                    "trace-conversation-record-authority-005",
            )

        val authority =
            DefaultConversationRecordAuthority(
                requestProvider =
                    object : ConversationRecordRequestProvider {
                        override fun provide(
                            conversationIntake: ConversationIntakeAuthorityResult,
                        ): ConversationRecordRequestResult {
                            return ConversationRecordRequestResult.create(
                                traceId =
                                    TraceId.from(
                                        "trace-conversation-record-request-other",
                                    ),
                                status =
                                    ConversationRecordRequestStatus.UNAVAILABLE,
                            )
                        }
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            authority.record(
                conversationIntake = intake,
            )
        }
    }

    @Test
    fun `record rejects conversation identity result from a different trace`() {
        val intake =
            createProducedIntake(
                traceValue =
                    "trace-conversation-record-authority-006",
            )

        val authority =
            DefaultConversationRecordAuthority(
                identityProvider =
                    object : ConversationIdentityProvider {
                        override fun provide(
                            traceId: TraceId,
                            input: ConversationInput,
                        ): ConversationIdentityProvisionResult {
                            return ConversationIdentityProvisionResult.create(
                                traceId =
                                    TraceId.from(
                                        "trace-conversation-identity-result-other",
                                    ),
                                status =
                                    ConversationIdentityProvisionStatus.UNAVAILABLE,
                            )
                        }
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            authority.record(
                conversationIntake = intake,
            )
        }
    }

    @Test
    fun `record does not request conversation identity when record request is unavailable`() {
        val traceId =
            TraceId.from(
                "trace-conversation-record-authority-007",
            )
        var identityCalls = 0

        val authority =
            DefaultConversationRecordAuthority(
                identityProvider =
                    object : ConversationIdentityProvider {
                        override fun provide(
                            traceId: TraceId,
                            input: ConversationInput,
                        ): ConversationIdentityProvisionResult {
                            identityCalls += 1

                            return ConversationIdentityProvisionResult.create(
                                traceId = traceId,
                                status =
                                    ConversationIdentityProvisionStatus.UNAVAILABLE,
                            )
                        }
                    },
            )

        val result =
            authority.record(
                conversationIntake =
                    ConversationIntakeAuthorityResult.create(
                        traceId = traceId,
                        status =
                            ConversationIntakeAuthorityStatus.DEFERRED,
                    ),
            )

        assertEquals(
            ConversationRecordStatus.DEFERRED,
            result.status,
        )
        assertEquals(
            0,
            identityCalls,
        )
    }

    private fun createProducedIntake(
        traceValue: String,
    ): ConversationIntakeAuthorityResult {
        val traceId =
            TraceId.from(
                traceValue,
            )

        return ConversationIntakeAuthorityResult.create(
            traceId = traceId,
            status =
                ConversationIntakeAuthorityStatus.PRODUCED,
            intake =
                ConversationIntakeResult.create(
                    record =
                        ConversationIntakeRecord.create(
                            input =
                                ConversationInput.create(
                                    context =
                                        ContextEnvelope.create(
                                            traceId = traceId,
                                            schemaVersion =
                                                SchemaVersion.from(1),
                                            source =
                                                ContextSource.TEXT,
                                            trustLevel =
                                                ContextTrustLevel.VERIFIED,
                                            securityLevel =
                                                ContextSecurityLevel.RESTRICTED,
                                            observedAt =
                                                DevilTimestamp
                                                    .fromEpochMilliseconds(
                                                        1_754_000_207_000L,
                                                    ),
                                        ),
                                    content =
                                        "Bounded conversation record authority test input.",
                                ),
                            state =
                                ConversationIntakeState.ACCEPTED,
                            rationale =
                                "Conversation intake was established for bounded recording.",
                        ),
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
        code: String,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    code,
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_207_500L,
                ),
            summary =
                "Bounded conversation record authority dependency failed.",
        )
    }
}
