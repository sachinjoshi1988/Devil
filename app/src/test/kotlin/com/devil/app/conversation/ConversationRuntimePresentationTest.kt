package com.devil.app.conversation

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.runtime.RuntimeResult
import com.devil.core.runtime.RuntimeStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class ConversationRuntimePresentationTest {

    @Test
    fun `accepted runtime result remains acceptance rather than success`() {
        val traceId = TraceId.from(
            "trace-conversation-presentation-001",
        )

        val presentation =
            ConversationRuntimePresentation.from(
                RuntimeResult.create(
                    traceId = traceId,
                    status = RuntimeStatus.ACCEPTED,
                ),
            )

        assertEquals(traceId, presentation.traceId)
        assertEquals(
            ConversationRuntimePresentationStatus.ACCEPTED,
            presentation.status,
        )
        assertEquals(
            "Accepted for constitutional processing.",
            presentation.message,
        )
    }

    @Test
    fun `deferred runtime result remains explicitly deferred`() {
        val traceId = TraceId.from(
            "trace-conversation-presentation-002",
        )

        val presentation =
            ConversationRuntimePresentation.from(
                RuntimeResult.create(
                    traceId = traceId,
                    status = RuntimeStatus.DEFERRED,
                ),
            )

        assertEquals(traceId, presentation.traceId)
        assertEquals(
            ConversationRuntimePresentationStatus.DEFERRED,
            presentation.status,
        )
        assertEquals(
            "Deferred by the Devil runtime.",
            presentation.message,
        )
    }

    @Test
    fun `rejected runtime result preserves constitutional error summary`() {
        val traceId = TraceId.from(
            "trace-conversation-presentation-003",
        )
        val error =
            UniversalErrorRecord.create(
                errorCode =
                    ErrorCode.from(
                        "CONVERSATION_UI_REJECTED",
                    ),
                traceId = traceId,
                occurredAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_400_000L,
                    ),
                summary =
                    "The supplied input was rejected.",
            )

        val presentation =
            ConversationRuntimePresentation.from(
                RuntimeResult.create(
                    traceId = traceId,
                    status = RuntimeStatus.REJECTED,
                    error = error,
                ),
            )

        assertEquals(traceId, presentation.traceId)
        assertEquals(
            ConversationRuntimePresentationStatus.REJECTED,
            presentation.status,
        )
        assertEquals(
            "The supplied input was rejected.",
            presentation.message,
        )
    }

    @Test
    fun `presentation preserves distinct runtime meanings`() {
        val accepted =
            ConversationRuntimePresentation.from(
                RuntimeResult.create(
                    traceId =
                        TraceId.from(
                            "trace-conversation-presentation-004",
                        ),
                    status = RuntimeStatus.ACCEPTED,
                ),
            )

        val deferred =
            ConversationRuntimePresentation.from(
                RuntimeResult.create(
                    traceId =
                        TraceId.from(
                            "trace-conversation-presentation-005",
                        ),
                    status = RuntimeStatus.DEFERRED,
                ),
            )

        check(accepted.status != deferred.status)
        check(accepted.message != deferred.message)
    }
}
