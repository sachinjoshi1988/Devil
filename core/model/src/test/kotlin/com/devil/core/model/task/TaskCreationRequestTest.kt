package com.devil.core.model.task

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals

class TaskCreationRequestTest {

    @Test
    fun `create preserves selected decision record`() {
        val decision = createDecision(
            state = DecisionState.SELECTED,
            summary = "Open the camera application.",
        )

        val request = TaskCreationRequest.create(
            decision = decision,
        )

        assertEquals(decision, request.decision)
        assertEquals(
            decision.understanding.context,
            request.decision.understanding.context,
        )
        assertEquals(
            DecisionState.SELECTED,
            request.decision.state,
        )
        assertEquals(
            "Open the camera application.",
            request.decision.summary,
        )
    }

    @Test
    fun `create preserves deferred decision without creating task state`() {
        val request = TaskCreationRequest.create(
            decision = createDecision(
                state = DecisionState.DEFERRED,
                summary =
                    "No constitutional decision policy is available.",
            ),
        )

        assertEquals(
            DecisionState.DEFERRED,
            request.decision.state,
        )
        assertEquals(
            "No constitutional decision policy is available.",
            request.decision.summary,
        )
    }

    @Test
    fun `create preserves clarification decision state`() {
        val request = TaskCreationRequest.create(
            decision = createDecision(
                state =
                    DecisionState.REQUIRES_CLARIFICATION,
                summary =
                    "Clarification is required before task creation.",
            ),
        )

        assertEquals(
            DecisionState.REQUIRES_CLARIFICATION,
            request.decision.state,
        )
    }

    private fun createDecision(
        state: DecisionState,
        summary: String,
    ): DecisionRecord {
        return DecisionRecord.create(
            understanding = UnderstandingRecord.create(
                context = ContextEnvelope.create(
                    traceId = TraceId.from(
                        "trace-task-creation-request-001",
                    ),
                    schemaVersion = SchemaVersion.from(1),
                    source = ContextSource.TEXT,
                    trustLevel =
                        ContextTrustLevel.VERIFIED,
                    securityLevel =
                        ContextSecurityLevel.RESTRICTED,
                    observedAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            1_754_000_077_000L,
                        ),
                ),
                state = UnderstandingState.COMPLETE,
                summary =
                    "Bounded understanding was produced.",
            ),
            state = state,
            summary = summary,
        )
    }
}
