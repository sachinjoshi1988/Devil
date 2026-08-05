package com.devil.core.runtime.decision

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionEvaluationRequest
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultDecisionEvaluationResolverTest {

    @Test
    fun `evaluate preserves understanding and defers without decision policy`() {
        val request = createRequest(
            state = UnderstandingState.COMPLETE,
            summary = "Open the camera application.",
        )
        val resolver: DecisionEvaluationResolver =
            DefaultDecisionEvaluationResolver()

        val decision = resolver.evaluate(request)

        assertEquals(
            request.understanding,
            decision.understanding,
        )
        assertEquals(
            DecisionState.DEFERRED,
            decision.state,
        )
        assertEquals(
            "No constitutional decision policy is available.",
            decision.summary,
        )
    }

    @Test
    fun `evaluate does not select decision from complete understanding alone`() {
        val decision =
            DefaultDecisionEvaluationResolver().evaluate(
                createRequest(
                    state = UnderstandingState.COMPLETE,
                    summary = "Open the camera application.",
                ),
            )

        assertEquals(
            UnderstandingState.COMPLETE,
            decision.understanding.state,
        )
        assertEquals(
            DecisionState.DEFERRED,
            decision.state,
        )
    }

    @Test
    fun `evaluate preserves unsupported understanding without fabricating decision`() {
        val decision =
            DefaultDecisionEvaluationResolver().evaluate(
                createRequest(
                    state = UnderstandingState.UNSUPPORTED,
                    summary =
                        "No structured language-understanding policy is available.",
                ),
            )

        assertEquals(
            UnderstandingState.UNSUPPORTED,
            decision.understanding.state,
        )
        assertEquals(
            DecisionState.DEFERRED,
            decision.state,
        )
    }

    @Test
    fun `evaluate preserves ambiguous understanding without choosing clarification policy`() {
        val decision =
            DefaultDecisionEvaluationResolver().evaluate(
                createRequest(
                    state = UnderstandingState.AMBIGUOUS,
                    summary =
                        "The requested meaning remains ambiguous.",
                ),
            )

        assertEquals(
            UnderstandingState.AMBIGUOUS,
            decision.understanding.state,
        )
        assertEquals(
            DecisionState.DEFERRED,
            decision.state,
        )
    }

    private fun createRequest(
        state: UnderstandingState,
        summary: String,
    ): DecisionEvaluationRequest {
        return DecisionEvaluationRequest.create(
            understanding = UnderstandingRecord.create(
                context = ContextEnvelope.create(
                    traceId = TraceId.from(
                        "trace-default-decision-resolver-001",
                    ),
                    schemaVersion = SchemaVersion.from(1),
                    source = ContextSource.TEXT,
                    trustLevel =
                        ContextTrustLevel.VERIFIED,
                    securityLevel =
                        ContextSecurityLevel.RESTRICTED,
                    observedAt =
                        DevilTimestamp.fromEpochMilliseconds(
                            1_754_000_074_000L,
                        ),
                ),
                state = state,
                summary = summary,
            ),
        )
    }
}
