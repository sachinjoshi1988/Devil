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
import com.devil.core.model.conversation.ConversationPersistenceRequest
import com.devil.core.model.conversation.ConversationRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class DefaultConversationPersistenceEvaluatorTest {

    @Test
    fun `evaluate returns unavailable without fabricating persistence eligibility`() {
        val traceId =
            TraceId.from(
                "trace-default-conversation-persistence-evaluator-001",
            )
        val evaluator: ConversationPersistenceEvaluator =
            DefaultConversationPersistenceEvaluator()

        val result =
            evaluator.evaluate(
                traceId = traceId,
                request = createRequest(traceId),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationPersistenceEvaluationStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate remains unavailable for valid bounded persistence request`() {
        val traceId =
            TraceId.from(
                "trace-default-conversation-persistence-evaluator-002",
            )

        val result =
            DefaultConversationPersistenceEvaluator().evaluate(
                traceId = traceId,
                request = createRequest(traceId),
            )

        assertEquals(
            ConversationPersistenceEvaluationStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `evaluate rejects request from different trace`() {
        assertFailsWith<IllegalArgumentException> {
            DefaultConversationPersistenceEvaluator().evaluate(
                traceId =
                    TraceId.from(
                        "trace-default-conversation-persistence-evaluator-003",
                    ),
                request =
                    createRequest(
                        TraceId.from(
                            "trace-default-conversation-persistence-request-other",
                        ),
                    ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): ConversationPersistenceRequest {
        return ConversationPersistenceRequest.create(
            record =
                ConversationRecord.create(
                    conversationId =
                        ConversationId.from(
                            "conversation-default-persistence-evaluator",
                        ),
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
                                                                1_754_000_209_000L,
                                                            ),
                                                ),
                                            content =
                                                "Bounded conversation persistence evaluator input.",
                                        ),
                                    state =
                                        ConversationIntakeState.ACCEPTED,
                                    rationale =
                                        "Preserve the established conversation intake.",
                                ),
                        ),
                ),
        )
    }
}
