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
import com.devil.core.model.error.ErrorCode
import com.devil.core.model.error.UniversalErrorRecord
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class ConversationIntakeAuthorityResultTest {

    @Test
    fun `create preserves produced result with matching intake`() {
        val traceId = TraceId.from(
            "trace-conversation-authority-result-001",
        )
        val intake = createIntake(traceId)

        val result = ConversationIntakeAuthorityResult.create(
            traceId = traceId,
            status = ConversationIntakeAuthorityStatus.PRODUCED,
            intake = intake,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationIntakeAuthorityStatus.PRODUCED,
            result.status,
        )
        assertEquals(intake, result.intake)
        assertNull(result.error)
    }

    @Test
    fun `create preserves deferred result without intake or error`() {
        val traceId = TraceId.from(
            "trace-conversation-authority-result-002",
        )

        val result = ConversationIntakeAuthorityResult.create(
            traceId = traceId,
            status = ConversationIntakeAuthorityStatus.DEFERRED,
        )

        assertEquals(traceId, result.traceId)
        assertEquals(
            ConversationIntakeAuthorityStatus.DEFERRED,
            result.status,
        )
        assertNull(result.intake)
        assertNull(result.error)
    }

    @Test
    fun `create preserves failed result with matching error`() {
        val traceId = TraceId.from(
            "trace-conversation-authority-result-003",
        )
        val error = createError(traceId)

        val result = ConversationIntakeAuthorityResult.create(
            traceId = traceId,
            status = ConversationIntakeAuthorityStatus.FAILED,
            error = error,
        )

        assertEquals(
            ConversationIntakeAuthorityStatus.FAILED,
            result.status,
        )
        assertNull(result.intake)
        assertEquals(error, result.error)
    }

    @Test
    fun `create rejects produced result without intake`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationIntakeAuthorityResult.create(
                traceId = TraceId.from(
                    "trace-conversation-authority-result-004",
                ),
                status = ConversationIntakeAuthorityStatus.PRODUCED,
            )
        }
    }

    @Test
    fun `create rejects produced intake from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationIntakeAuthorityResult.create(
                traceId = TraceId.from(
                    "trace-conversation-authority-result-005",
                ),
                status = ConversationIntakeAuthorityStatus.PRODUCED,
                intake = createIntake(
                    TraceId.from(
                        "trace-conversation-authority-result-other",
                    ),
                ),
            )
        }
    }

    @Test
    fun `create rejects deferred result with intake`() {
        val traceId = TraceId.from(
            "trace-conversation-authority-result-006",
        )

        assertFailsWith<IllegalArgumentException> {
            ConversationIntakeAuthorityResult.create(
                traceId = traceId,
                status = ConversationIntakeAuthorityStatus.DEFERRED,
                intake = createIntake(traceId),
            )
        }
    }

    @Test
    fun `create rejects failed result without error`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationIntakeAuthorityResult.create(
                traceId = TraceId.from(
                    "trace-conversation-authority-result-007",
                ),
                status = ConversationIntakeAuthorityStatus.FAILED,
            )
        }
    }

    @Test
    fun `create rejects failed error from a different trace`() {
        assertFailsWith<IllegalArgumentException> {
            ConversationIntakeAuthorityResult.create(
                traceId = TraceId.from(
                    "trace-conversation-authority-result-008",
                ),
                status = ConversationIntakeAuthorityStatus.FAILED,
                error = createError(
                    TraceId.from(
                        "trace-conversation-authority-error-other",
                    ),
                ),
            )
        }
    }

    private fun createIntake(
        traceId: TraceId,
    ): ConversationIntakeResult {
        return ConversationIntakeResult.create(
            record = ConversationIntakeRecord.create(
                input = ConversationInput.create(
                    context = ContextEnvelope.create(
                        traceId = traceId,
                        schemaVersion = SchemaVersion.from(1),
                        source = ContextSource.TEXT,
                        trustLevel = ContextTrustLevel.VERIFIED,
                        securityLevel = ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_754_000_061_000L,
                            ),
                    ),
                    content = "Please read my latest notification.",
                ),
                state = ConversationIntakeState.ACCEPTED,
                rationale =
                    "Input satisfied bounded conversation-intake requirements.",
            ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "CONVERSATION_INTAKE_FAILED",
            ),
            traceId = traceId,
            occurredAt = DevilTimestamp.fromEpochMilliseconds(
                1_754_000_061_500L,
            ),
            summary = "Conversation intake failed.",
        )
    }
}
