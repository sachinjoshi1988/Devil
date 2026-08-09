package com.devil.core.runtime.conversation

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.conversation.ConversationId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ConversationIdentityProvisionResultTest {

    @Test
    fun `create preserves available result with conversation identity`() {
        val traceId =
            TraceId.from(
                "trace-conversation-identity-result-001",
            )
        val conversationId =
            ConversationId.from(
                "conversation-identity-result-001",
            )

        val result =
            ConversationIdentityProvisionResult.create(
                traceId = traceId,
                status =
                    ConversationIdentityProvisionStatus.AVAILABLE,
                conversationId = conversationId,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationIdentityProvisionStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            conversationId,
            result.conversationId,
        )
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result without identity or error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-identity-result-002",
            )

        val result =
            ConversationIdentityProvisionResult.create(
                traceId = traceId,
                status =
                    ConversationIdentityProvisionStatus.UNAVAILABLE,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationIdentityProvisionStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.conversationId)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-identity-result-003",
            )
        val error = createError(traceId)

        val result =
            ConversationIdentityProvisionResult.create(
                traceId = traceId,
                status =
                    ConversationIdentityProvisionStatus.FAILED,
                error = error,
            )

        assertEquals(
            ConversationIdentityProvisionStatus.FAILED,
            result.status,
        )
        assertNull(result.conversationId)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without conversation identity`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationIdentityProvisionResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-identity-result-004",
                    ),
                status =
                    ConversationIdentityProvisionStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects available result with error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-identity-result-005",
            )

        assertFailsWith<IllegalArgumentException> {
            ConversationIdentityProvisionResult.create(
                traceId = traceId,
                status =
                    ConversationIdentityProvisionStatus.AVAILABLE,
                conversationId =
                    ConversationId.from(
                        "conversation-identity-result-005",
                    ),
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with conversation identity`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationIdentityProvisionResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-identity-result-006",
                    ),
                status =
                    ConversationIdentityProvisionStatus.UNAVAILABLE,
                conversationId =
                    ConversationId.from(
                        "conversation-identity-result-006",
                    ),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationIdentityProvisionResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-identity-result-007",
                    ),
                status =
                    ConversationIdentityProvisionStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationIdentityProvisionResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-identity-result-008",
                    ),
                status =
                    ConversationIdentityProvisionStatus.FAILED,
                error =
                    createError(
                        TraceId.from(
                            "trace-conversation-identity-error-other",
                        ),
                    ),
            )
        }
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "CONVERSATION_IDENTITY_PROVISION_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_200_000L,
                ),
            summary =
                "Conversation identity provision failed.",
        )
    }
}
