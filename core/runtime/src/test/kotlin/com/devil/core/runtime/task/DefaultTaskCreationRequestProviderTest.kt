package com.devil.core.runtime.task

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
import com.devil.core.model.understanding.UnderstandingState
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.decision.DecisionAuthorityStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DefaultTaskCreationRequestProviderTest {

    @Test
    fun `provide returns available request for selected decision`() {
        val decision = createProducedDecision(
            traceValue = "trace-task-provider-001",
            state = DecisionState.SELECTED,
        )
        val provider: TaskCreationRequestProvider =
            DefaultTaskCreationRequestProvider()

        val result = provider.provide(decision)

        assertEquals(decision.traceId, result.traceId)
        assertEquals(
            TaskCreationRequestStatus.AVAILABLE,
            result.status,
        )
        assertEquals(
            requireNotNull(decision.decision),
            requireNotNull(result.request).decision,
        )
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for deferred decision record`() {
        val result =
            DefaultTaskCreationRequestProvider().provide(
                createProducedDecision(
                    traceValue = "trace-task-provider-002",
                    state = DecisionState.DEFERRED,
                ),
            )

        assertEquals(
            TaskCreationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for clarification decision`() {
        val result =
            DefaultTaskCreationRequestProvider().provide(
                createProducedDecision(
                    traceValue = "trace-task-provider-003",
                    state =
                        DecisionState.REQUIRES_CLARIFICATION,
                ),
            )

        assertEquals(
            TaskCreationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for rejected decision`() {
        val result =
            DefaultTaskCreationRequestProvider().provide(
                createProducedDecision(
                    traceValue = "trace-task-provider-004",
                    state = DecisionState.REJECTED,
                ),
            )

        assertEquals(
            TaskCreationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide returns unavailable for deferred authority result`() {
        val traceId = TraceId.from(
            "trace-task-provider-005",
        )

        val result =
            DefaultTaskCreationRequestProvider().provide(
                DecisionAuthorityResult.create(
                    traceId = traceId,
                    status = DecisionAuthorityStatus.DEFERRED,
                ),
            )

        assertEquals(traceId, result.traceId)
        assertEquals(
            TaskCreationRequestStatus.UNAVAILABLE,
            result.status,
        )
        assertNull(result.request)
        assertNull(result.error)
    }

    @Test
    fun `provide preserves failed decision error`() {
        val traceId = TraceId.from(
            "trace-task-provider-006",
        )
        val error = createError(traceId)

        val result =
            DefaultTaskCreationRequestProvider().provide(
                DecisionAuthorityResult.create(
                    traceId = traceId,
                    status = DecisionAuthorityStatus.FAILED,
                    error = error,
                ),
            )

        assertEquals(
            TaskCreationRequestStatus.FAILED,
            result.status,
        )
        assertNull(result.request)
        assertEquals(error, result.error)
    }

    private fun createProducedDecision(
        traceValue: String,
        state: DecisionState,
    ): DecisionAuthorityResult {
        val traceId = TraceId.from(traceValue)

        return DecisionAuthorityResult.create(
            traceId = traceId,
            status = DecisionAuthorityStatus.PRODUCED,
            decision = DecisionRecord.create(
                understanding = UnderstandingRecord.create(
                    context = ContextEnvelope.create(
                        traceId = traceId,
                        schemaVersion = SchemaVersion.from(1),
                        source = ContextSource.TEXT,
                        trustLevel =
                            ContextTrustLevel.VERIFIED,
                        securityLevel =
                            ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_754_000_079_000L,
                            ),
                    ),
                    state = UnderstandingState.COMPLETE,
                    summary =
                        "Bounded understanding was produced.",
                ),
                state = state,
                summary =
                    "Bounded constitutional decision was produced.",
            ),
        )
    }

    private fun createError(
        traceId: TraceId,
    ): UniversalErrorRecord {
        return UniversalErrorRecord.create(
            errorCode = ErrorCode.from(
                "DECISION_FAILED",
            ),
            traceId = traceId,
            occurredAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_079_500L,
                ),
            summary = "Decision failed.",
        )
    }
}
