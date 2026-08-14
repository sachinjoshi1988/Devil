package com.devil.core.runtime.goal

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.SchemaVersion
import com.devil.core.model.common.TraceId
import com.devil.core.model.context.ContextEnvelope
import com.devil.core.model.context.ContextSecurityLevel
import com.devil.core.model.context.ContextSource
import com.devil.core.model.context.ContextTrustLevel
import com.devil.core.model.decision.DecisionRecord
import com.devil.core.model.decision.DecisionState
import com.devil.core.model.goal.LongRunningGoalId
import com.devil.core.model.goal.LongRunningGoalState
import com.devil.core.model.task.CompoundWorkRequest
import com.devil.core.model.task.CompoundWorkStep
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage78LongRunningGoalGovernanceTest {

    @Test
    fun `selected decision may establish bounded long-running goal continuity`() {
        val traceId =
            TraceId.from(
                "trace-stage78-long-running-goal-001",
            )

        val decision =
            selectedDecision(
                traceId = traceId,
                summary =
                    "Help me prepare for my examination.",
            )

        val result =
            LongRunningGoalCoordinator().prepare(
                traceId = traceId,
                decision = decision,
                goalId =
                    LongRunningGoalId.from(
                        "goal:exam-preparation",
                    ),
                description =
                    "Prepare for the examination over multiple governed sessions.",
            )

        assertEquals(
            LongRunningGoalPreparationStatus.PREPARED,
            result.status,
        )

        val goal =
            requireNotNull(result.goal)

        assertEquals(
            LongRunningGoalState.ACTIVE,
            goal.state,
        )

        assertSame(
            decision,
            goal.originatingDecision,
        )

        assertNull(
            goal.compoundWork,
        )
    }

    @Test
    fun `long-running goal may preserve governed compound work under same decision`() {
        val traceId =
            TraceId.from(
                "trace-stage78-long-running-goal-002",
            )

        val decision =
            selectedDecision(
                traceId = traceId,
                summary =
                    "Prepare the project and organize its remaining work.",
            )

        val compoundWork =
            CompoundWorkRequest.create(
                decision = decision,
                steps =
                    listOf(
                        CompoundWorkStep.create(
                            position = 1,
                            summary =
                                "Review remaining project work.",
                        ),
                        CompoundWorkStep.create(
                            position = 2,
                            summary =
                                "Organize the next bounded project actions.",
                        ),
                    ),
            )

        val result =
            LongRunningGoalCoordinator().prepare(
                traceId = traceId,
                decision = decision,
                goalId =
                    LongRunningGoalId.from(
                        "goal:project-preparation",
                    ),
                description =
                    "Continue governed project preparation across later sessions.",
                compoundWork = compoundWork,
            )

        val goal =
            requireNotNull(result.goal)

        assertSame(
            compoundWork,
            goal.compoundWork,
        )

        assertSame(
            decision,
            goal.compoundWork?.decision,
        )
    }

    @Test
    fun `unselected decision cannot establish long-running goal`() {
        val traceId =
            TraceId.from(
                "trace-stage78-long-running-goal-003",
            )

        val decision =
            decision(
                traceId = traceId,
                state =
                    DecisionState.REQUIRES_CLARIFICATION,
                summary =
                    "Clarification is required before preserving this goal.",
            )

        val result =
            LongRunningGoalCoordinator().prepare(
                traceId = traceId,
                decision = decision,
                goalId =
                    LongRunningGoalId.from(
                        "goal:deferred",
                    ),
                description =
                    "This goal must not become active.",
            )

        assertEquals(
            LongRunningGoalPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.goal,
        )
    }

    @Test
    fun `blank goal description remains deferred`() {
        val traceId =
            TraceId.from(
                "trace-stage78-long-running-goal-004",
            )

        val result =
            LongRunningGoalCoordinator().prepare(
                traceId = traceId,
                decision =
                    selectedDecision(
                        traceId = traceId,
                        summary =
                            "Preserve one bounded goal.",
                    ),
                goalId =
                    LongRunningGoalId.from(
                        "goal:blank-description",
                    ),
                description = "   ",
            )

        assertEquals(
            LongRunningGoalPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.goal,
        )
    }

    private fun selectedDecision(
        traceId: TraceId,
        summary: String,
    ): DecisionRecord {
        return decision(
            traceId = traceId,
            state = DecisionState.SELECTED,
            summary = summary,
        )
    }

    private fun decision(
        traceId: TraceId,
        state: DecisionState,
        summary: String,
    ): DecisionRecord {
        return DecisionRecord.create(
            understanding =
                UnderstandingRecord.create(
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
                                        1_754_000_780_000L,
                                    ),
                        ),
                    state =
                        UnderstandingState.COMPLETE,
                    summary =
                        "User supplied one bounded goal.",
                ),
            state = state,
            summary = summary,
        )
    }
}
