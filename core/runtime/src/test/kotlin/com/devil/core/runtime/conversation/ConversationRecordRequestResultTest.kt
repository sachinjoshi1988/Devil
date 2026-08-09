package com.devil.core.runtime.conversation

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
import com.devil.core.model.conversation.ConversationRecordRequest
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ConversationRecordRequestResultTest {

    @Test
    fun `create preserves available result with matching request`() {
        val traceId =
            TraceId.from(
                "trace-conversation-record-request-result-001",
            )
        val request = createRequest(traceId)

        val result =
            ConversationRecordRequestResult.create(
                traceId = traceId,
                status =
                    ConversationRecordRequestStatus.AVAILABLE,
                request = request,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationRecordRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(request, result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves unavailable result without request or error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-record-request-result-002",
            )

        val result =
            ConversationRecordRequestResult.create(
                traceId = traceId,
                status =
                    ConversationRecordRequestStatus.UNAVAILABLE,
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationRecordRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-record-request-result-003",
            )
        val error = createError(traceId)

        val result =
            ConversationRecordRequestResult.create(
                traceId = traceId,
                status =
                    ConversationRecordRequestStatus.FAILED,
                error = error,
            )

        assertEquals(
            ConversationRecordRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects available result without request`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationRecordRequestResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-record-request-result-004",
                    ),
                status =
                    ConversationRecordRequestStatus.AVAILABLE,
            )
        }
    }

    @Test
    fun `create rejects available result with error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-record-request-result-005",
            )

        assertFailsWith<IllegalArgumentException> {
            ConversationRecordRequestResult.create(
                traceId = traceId,
                status =
                    ConversationRecordRequestStatus.AVAILABLE,
                request = createRequest(traceId),
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects available request from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationRecordRequestResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-record-request-result-006",
                    ),
                status =
                    ConversationRecordRequestStatus.AVAILABLE,
                request =
                    createRequest(
                        TraceId.from(
                            "trace-conversation-record-request-result-other",
                        ),
                    ),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with request`() {
        val traceId =
            TraceId.from(
                "trace-conversation-record-request-result-007",
            )

        assertFailsWith<IllegalArgumentException> {
            ConversationRecordRequestResult.create(
                traceId = traceId,
                status =
                    ConversationRecordRequestStatus.UNAVAILABLE,
                request = createRequest(traceId),
            )
        }
    }

    @Test
    fun `create rejects unavailable result with error`() {
        val traceId =
            TraceId.from(
                "trace-conversation-record-request-result-008",
            )

        assertFailsWith<IllegalArgumentException> {
            ConversationRecordRequestResult.create(
                traceId = traceId,
                status =
                    ConversationRecordRequestStatus.UNAVAILABLE,
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationRecordRequestResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-record-request-result-009",
                    ),
                status =
                    ConversationRecordRequestStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed result with request`() {
        val traceId =
            TraceId.from(
                "trace-conversation-record-request-result-010",
            )

        assertFailsWith<IllegalArgumentException> {
            ConversationRecordRequestResult.create(
                traceId = traceId,
                status =
                    ConversationRecordRequestStatus.FAILED,
                request = createRequest(traceId),
                error = createError(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationRecordRequestResult.create(
                traceId =
                    TraceId.from(
                        "trace-conversation-record-request-result-011",
                    ),
                status =
                    ConversationRecordRequestStatus.FAILED,
                error =
                    createError(
                        TraceId.from(
                            "trace-conversation-record-request-error-other",
                        ),
                    ),
            )
        }
    }

    private fun createRequest(
        traceId: TraceId,
    ): ConversationRecordRequest {
        return ConversationRecordRequest.create(
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
                                                        1_754_000_205_000L,
                                                    ),
                                        ),
                                    content =
                                        "Bounded conversation record request result test input.",
                                ),
                            state =
                                ConversationIntakeState.ACCEPTED,
                            rationale =
                                "Preserve the established conversation intake.",
                        ),
                ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode =
                ErrorCode.from(
                    "CONVERSATION_RECORD_REQUEST_FAILED",
                ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_205_500L,
                ),
            summary =
                "Conversation record request preparation failed.",
        )
    }
}
