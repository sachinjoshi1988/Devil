package com.devil.core.model.decision

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals

class DecisionEvaluationRequestTest {

    @Test
    fun `create preserves completed understanding record`() {
        val understanding = createUnderstanding(
            state = UnderstandingState.COMPLETE,
            summary = "Open the camera application.",
        )

        val request = DecisionEvaluationRequest.create(
            understanding = understanding,
        )

        assertEquals(understanding, request.understanding)
        assertEquals(
            understanding.context,
            request.understanding.context,
        )
        assertEquals(
            UnderstandingState.COMPLETE,
            request.understanding.state,
        )
        assertEquals(
            "Open the camera application.",
            request.understanding.summary,
        )
    }

    @Test
    fun `create preserves unsupported understanding without inventing a decision`() {
        val request = DecisionEvaluationRequest.create(
            understanding = createUnderstanding(
                state = UnderstandingState.UNSUPPORTED,
                summary =
                    "No structured language-understanding policy is available.",
            ),
        )

        assertEquals(
            UnderstandingState.UNSUPPORTED,
            request.understanding.state,
        )
        assertEquals(
            "No structured language-understanding policy is available.",
            request.understanding.summary,
        )
    }

    @Test
    fun `create preserves ambiguous understanding quality`() {
        val request = DecisionEvaluationRequest.create(
            understanding = createUnderstanding(
                state = UnderstandingState.AMBIGUOUS,
                summary =
                    "The requested meaning remains ambiguous.",
            ),
        )

        assertEquals(
            UnderstandingState.AMBIGUOUS,
            request.understanding.state,
        )
    }

    private fun createUnderstanding(
        state: UnderstandingState,
        summary: String,
    ): UnderstandingRecord {
        return UnderstandingRecord.create(
            context = ContextEnvelope.create(
                traceId = TraceId.from(
                    "trace-decision-evaluation-request-001",
                ),
                schemaVersion = SchemaVersion.from(1),
                source = ContextSource.TEXT,
                trustLevel = ContextTrustLevel.VERIFIED,
                securityLevel =
                    ContextSecurityLevel.RESTRICTED,
                observedAt =
                    DevilTimestamp.fromEpochMilliseconds(
                        1_754_000_071_000L,
                    ),
            ),
            state = state,
            summary = summary,
        )
    }
}
