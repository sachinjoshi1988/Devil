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
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage77CompoundWorkGovernanceTest {

    @Test
    fun `selected decision may preserve ordered compound work without creating child decisions`() {
        val decision =
            decision(
                traceValue = "trace-stage77-compound-001",
                state = DecisionState.SELECTED,
                summary =
                    "Coordinate the requested bounded compound goal.",
            )

        val result =
            CompoundWorkCoordinator().prepare(
                decision = decision,
                stepSummaries =
                    listOf(
                        "Prepare the bounded message to Rahul.",
                        "Prepare bounded navigation to Rahul's office.",
                    ),
            )

        assertEquals(
            CompoundWorkPreparationStatus.PREPARED,
            result.status,
        )

        val request =
            requireNotNull(result.request)

        assertSame(
            decision,
            request.decision,
        )

        assertEquals(
            decision.understanding.context.traceId,
            result.traceId,
        )

        assertEquals(
            2,
            request.steps.size,
        )

        assertEquals(
            1,
            request.steps[0].position,
        )

        assertEquals(
            "Prepare the bounded message to Rahul.",
            request.steps[0].summary,
        )

        assertEquals(
            2,
            request.steps[1].position,
        )

        assertEquals(
            "Prepare bounded navigation to Rahul's office.",
            request.steps[1].summary,
        )
    }

    @Test
    fun `single bounded action does not become compound work`() {
        val decision =
            decision(
                traceValue = "trace-stage77-compound-002",
                state = DecisionState.SELECTED,
                summary = "Open YouTube.",
            )

        val result =
            CompoundWorkCoordinator().prepare(
                decision = decision,
                stepSummaries =
                    listOf(
                        "Prepare the bounded YouTube opening action.",
                    ),
            )

        assertEquals(
            CompoundWorkPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.request)
    }

    @Test
    fun `unselected decision cannot become compound work`() {
        val decision =
            decision(
                traceValue = "trace-stage77-compound-003",
                state = DecisionState.REQUIRES_CLARIFICATION,
                summary =
                    "Clarification is required before compound work.",
            )

        val result =
            CompoundWorkCoordinator().prepare(
                decision = decision,
                stepSummaries =
                    listOf(
                        "First possible step.",
                        "Second possible step.",
                    ),
            )

        assertEquals(
            CompoundWorkPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.request)
    }

    private fun decision(
        traceValue: String,
        state: DecisionState,
        summary: String,
    ): DecisionRecord {
        val traceId =
            TraceId.from(traceValue)

        val understanding =
            UnderstandingRecord.create(
                context =
                    ContextEnvelope.create(
                        traceId = traceId,
                        schemaVersion = SchemaVersion.from(1),
                        source = ContextSource.TEXT,
                        trustLevel =
                            ContextTrustLevel.VERIFIED,
                        securityLevel =
                            ContextSecurityLevel.RESTRICTED,
                        observedAt =
                            DevilTimestamp.fromEpochMilliseconds(
                                1_754_000_770_000L,
                            ),
                    ),
                state = UnderstandingState.COMPLETE,
                summary =
                    "One bounded compound-goal understanding was supplied.",
            )

        return DecisionRecord.create(
            understanding = understanding,
            state = state,
            summary = summary,
        )
    }
}
