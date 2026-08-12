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
import com.devil.core.model.understanding.UnderstandingActionability
import com.devil.core.model.understanding.UnderstandingIntent
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingSemantics
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultDecisionEvaluationResolverTest {

    @Test
    fun `evaluate selects greeting decision from complete greeting semantics`() {
        val decision =
            DefaultDecisionEvaluationResolver().evaluate(
                createSemanticRequest(
                    intent = UnderstandingIntent.GREETING,
                    actionability =
                        UnderstandingActionability.NON_ACTIONABLE,
                    meaning = "greeting",
                ),
            )

        assertEquals(
            DecisionState.SELECTED,
            decision.state,
        )
        assertEquals(
            "Acknowledge the user's greeting.",
            decision.summary,
        )
    }

    @Test
    fun `evaluate selects open target decision without executing target`() {
        val decision =
            DefaultDecisionEvaluationResolver().evaluate(
                createSemanticRequest(
                    intent = UnderstandingIntent.OPEN_TARGET,
                    actionability =
                        UnderstandingActionability.ACTIONABLE,
                    meaning = "open target",
                    target = "camera",
                ),
            )

        assertEquals(
            DecisionState.SELECTED,
            decision.state,
        )
        assertEquals(
            "Proceed with the understood request to open target: camera.",
            decision.summary,
        )
        assertEquals(
            "camera",
            decision.understanding.semantics?.target,
        )
    }

    @Test
    fun `evaluate selects informational conversational decision`() {
        val decision =
            DefaultDecisionEvaluationResolver().evaluate(
                createSemanticRequest(
                    intent = UnderstandingIntent.INFORMATIONAL,
                    actionability =
                        UnderstandingActionability.NON_ACTIONABLE,
                    meaning = "informational statement",
                ),
            )

        assertEquals(
            DecisionState.SELECTED,
            decision.state,
        )
        assertEquals(
            "Accept the supplied informational statement for bounded conversational handling.",
            decision.summary,
        )
    }

    @Test
    fun `evaluate requires clarification for ambiguous understanding`() {
        val decision =
            DefaultDecisionEvaluationResolver().evaluate(
                createStateRequest(
                    state = UnderstandingState.AMBIGUOUS,
                    summary =
                        "The requested meaning remains ambiguous.",
                ),
            )

        assertEquals(
            DecisionState.REQUIRES_CLARIFICATION,
            decision.state,
        )
    }

    @Test
    fun `evaluate defers incomplete understanding`() {
        val decision =
            DefaultDecisionEvaluationResolver().evaluate(
                createStateRequest(
                    state = UnderstandingState.INCOMPLETE,
                    summary =
                        "Understanding remains incomplete.",
                ),
            )

        assertEquals(
            DecisionState.DEFERRED,
            decision.state,
        )
    }

    @Test
    fun `evaluate defers unsupported understanding`() {
        val decision =
            DefaultDecisionEvaluationResolver().evaluate(
                createStateRequest(
                    state = UnderstandingState.UNSUPPORTED,
                    summary =
                        "No bounded language-understanding policy matched the supplied input.",
                ),
            )

        assertEquals(
            DecisionState.DEFERRED,
            decision.state,
        )
    }

    @Test
    fun `evaluate defers complete understanding without structured semantics`() {
        val decision =
            DefaultDecisionEvaluationResolver().evaluate(
                createStateRequest(
                    state = UnderstandingState.COMPLETE,
                    summary =
                        "Meaning was described without structured semantics.",
                ),
            )

        assertEquals(
            DecisionState.DEFERRED,
            decision.state,
        )
    }

    private fun createSemanticRequest(
        intent: UnderstandingIntent,
        actionability: UnderstandingActionability,
        meaning: String,
        target: String? = null,
    ): DecisionEvaluationRequest {
        return DecisionEvaluationRequest.create(
            understanding =
                UnderstandingRecord.create(
                    context = createContext(),
                    state = UnderstandingState.COMPLETE,
                    summary = "Bounded semantic understanding was produced.",
                    semantics =
                        UnderstandingSemantics.create(
                            intent = intent,
                            actionability = actionability,
                            meaning = meaning,
                            target = target,
                        ),
                ),
        )
    }

    private fun createStateRequest(
        state: UnderstandingState,
        summary: String,
    ): DecisionEvaluationRequest {
        return DecisionEvaluationRequest.create(
            understanding =
                UnderstandingRecord.create(
                    context = createContext(),
                    state = state,
                    summary = summary,
                ),
        )
    }

    private fun createContext(): ContextEnvelope {
        return ContextEnvelope.create(
            traceId =
                TraceId.from(
                    "trace-stage-57-decision-resolver-001",
                ),
            schemaVersion = SchemaVersion.from(1),
            source = ContextSource.TEST,
            trustLevel = ContextTrustLevel.VERIFIED,
            securityLevel =
                ContextSecurityLevel.RESTRICTED,
            observedAt =
                DevilTimestamp.fromEpochMilliseconds(
                    1_754_000_077_000L,
                ),
        )
    }
}
