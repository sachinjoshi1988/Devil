package com.devil.core.runtime

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.conversation.ConversationInput
import com.devil.core.runtime.conversation.ConversationPersistenceAuthority
import com.devil.core.runtime.conversation.ConversationPersistenceResult
import com.devil.core.runtime.conversation.ConversationPersistenceStatus
import com.devil.core.runtime.conversation.ConversationRecordAuthority
import com.devil.core.runtime.conversation.ConversationRecordResult
import com.devil.core.runtime.conversation.ConversationRecordStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultUnifiedDevilRuntimeConversationPersistenceTest {

    @Test
    fun `accept coordinates bounded conversation record and persistence before continuing runtime path`() {
        val input =
            createInput(
                "trace-runtime-conversation-persistence-001",
            )

        var recordedTraceId: TraceId? = null
        var persistedTraceId: TraceId? = null

        val runtime =
            DefaultUnifiedDevilRuntime(
                conversationRecordAuthority =
                    object : ConversationRecordAuthority {
                        override fun record(
                            conversationIntake:
                                com.devil.core.runtime.conversation.ConversationIntakeAuthorityResult,
                        ): ConversationRecordResult {
                            recordedTraceId =
                                conversationIntake.traceId

                            return ConversationRecordResult.create(
                                traceId = conversationIntake.traceId,
                                status =
                                    ConversationRecordStatus.DEFERRED,
                            )
                        }
                    },
                conversationPersistenceAuthority =
                    object : ConversationPersistenceAuthority {
                        override fun evaluatePersistence(
                            conversationRecord:
                                ConversationRecordResult,
                        ): ConversationPersistenceResult {
                            persistedTraceId =
                                conversationRecord.traceId

                            return ConversationPersistenceResult.create(
                                traceId = conversationRecord.traceId,
                                status =
                                    ConversationPersistenceStatus.DEFERRED,
                            )
                        }
                    },
            )

        val result = runtime.accept(input)

        assertEquals(
            input.context.traceId,
            recordedTraceId,
        )
        assertEquals(
            input.context.traceId,
            persistedTraceId,
        )
        assertEquals(
            input.context.traceId,
            result.traceId,
        )
        assertEquals(
            RuntimeStatus.DEFERRED,
            result.status,
        )
        assertNull(result.error)
    }

    @Test
    fun `conversation persistence deferral does not fabricate runtime success`() {
        val input =
            createInput(
                "trace-runtime-conversation-persistence-002",
            )

        val runtime =
            DefaultUnifiedDevilRuntime(
                conversationRecordAuthority =
                    deferredRecordAuthority(),
                conversationPersistenceAuthority =
                    deferredPersistenceAuthority(),
            )

        val result = runtime.accept(input)

        assertEquals(
            RuntimeStatus.DEFERRED,
            result.status,
        )
        assertNull(result.error)
    }

    @Test
    fun `accept rejects conversation record result from another trace`() {
        val input =
            createInput(
                "trace-runtime-conversation-persistence-003",
            )

        val runtime =
            DefaultUnifiedDevilRuntime(
                conversationRecordAuthority =
                    object : ConversationRecordAuthority {
                        override fun record(
                            conversationIntake:
                                com.devil.core.runtime.conversation.ConversationIntakeAuthorityResult,
                        ): ConversationRecordResult {
                            return ConversationRecordResult.create(
                                traceId =
                                    TraceId.from(
                                        "trace-runtime-conversation-record-other",
                                    ),
                                status =
                                    ConversationRecordStatus.DEFERRED,
                            )
                        }
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            runtime.accept(input)
        }
    }

    @Test
    fun `accept rejects conversation persistence result from another trace`() {
        val input =
            createInput(
                "trace-runtime-conversation-persistence-004",
            )

        val runtime =
            DefaultUnifiedDevilRuntime(
                conversationRecordAuthority =
                    deferredRecordAuthority(),
                conversationPersistenceAuthority =
                    object : ConversationPersistenceAuthority {
                        override fun evaluatePersistence(
                            conversationRecord:
                                ConversationRecordResult,
                        ): ConversationPersistenceResult {
                            return ConversationPersistenceResult.create(
                                traceId =
                                    TraceId.from(
                                        "trace-runtime-conversation-persistence-other",
                                    ),
                                status =
                                    ConversationPersistenceStatus.DEFERRED,
                            )
                        }
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            runtime.accept(input)
        }
    }

    private fun deferredRecordAuthority():
        ConversationRecordAuthority {
        return object : ConversationRecordAuthority {
            override fun record(
                conversationIntake:
                    com.devil.core.runtime.conversation.ConversationIntakeAuthorityResult,
            ): ConversationRecordResult {
                return ConversationRecordResult.create(
                    traceId = conversationIntake.traceId,
                    status =
                        ConversationRecordStatus.DEFERRED,
                )
            }
        }
    }

    private fun deferredPersistenceAuthority():
        ConversationPersistenceAuthority {
        return object : ConversationPersistenceAuthority {
            override fun evaluatePersistence(
                conversationRecord:
                    ConversationRecordResult,
            ): ConversationPersistenceResult {
                return ConversationPersistenceResult.create(
                    traceId = conversationRecord.traceId,
                    status =
                        ConversationPersistenceStatus.DEFERRED,
                )
            }
        }
    }

    private fun createInput(
        traceValue: String,
    ): ConversationInput {
        return ConversationInput.create(
            context =
                ContextEnvelope.create(
                    traceId = TraceId.from(traceValue),
                    schemaVersion =
                        SchemaVersion.from(1),
                    source = ContextSource.TEST,
                    trustLevel =
                        ContextTrustLevel.VERIFIED,
                    securityLevel =
                        ContextSecurityLevel.RESTRICTED,
                    observedAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            1_754_000_208_000L,
                        ),
                ),
            content =
                "Bounded Stage 25 conversation persistence integration input.",
        )
    }
}
