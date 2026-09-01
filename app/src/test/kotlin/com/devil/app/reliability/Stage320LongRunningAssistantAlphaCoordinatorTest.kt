package com.devil.app.reliability

import com.devil.app.performance.DevilLongRunningStabilityEvidence
import com.devil.app.performance.DevilLongRunningStabilityStatus
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
import com.devil.core.model.goal.LongRunningGoalRecord
import com.devil.core.model.goal.LongRunningGoalState
import com.devil.core.model.understanding.UnderstandingRecord
import com.devil.core.model.understanding.UnderstandingState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage320LongRunningAssistantAlphaCoordinatorTest {

    @Test
    fun `Long Running Assistant Alpha preserves goal and Stage 272 provenance`() {
        val goal = longRunningGoal()

        val evidence =
            DevilLongRunningStabilityEvidence(
                voiceInputLifecycleBounded = true,
                voiceOutputLifecycleBounded = true,
                cameraResourceLifecycleBounded = true,
                networkConnectionLifecycleBounded = true,
                uncontrolledBackgroundWorkAbsent = true,
                automaticRecoveryLoopAbsent = true,
            )

        val result =
            Stage320LongRunningAssistantAlphaCoordinator()
                .prepare(
                    goal = goal,
                    stabilityEvidence = evidence,
                )

        assertEquals(
            Stage320LongRunningAssistantAlphaStatus.AVAILABLE,
            result.status,
        )

        assertSame(
            goal,
            result.goal,
        )

        val stability =
            requireNotNull(result.stability)

        assertEquals(
            DevilLongRunningStabilityStatus.STABLE,
            stability.status,
        )

        assertSame(
            evidence,
            stability.evidence,
        )
    }

    @Test
    fun `incomplete Stage 272 evidence fails closed`() {
        val result =
            Stage320LongRunningAssistantAlphaCoordinator()
                .prepare(
                    goal = longRunningGoal(),
                    stabilityEvidence =
                        DevilLongRunningStabilityEvidence(
                            voiceInputLifecycleBounded = true,
                            voiceOutputLifecycleBounded = true,
                            cameraResourceLifecycleBounded = true,
                            networkConnectionLifecycleBounded = true,
                            uncontrolledBackgroundWorkAbsent = false,
                            automaticRecoveryLoopAbsent = true,
                        ),
                )

        assertEquals(
            Stage320LongRunningAssistantAlphaStatus.DEFERRED,
            result.status,
        )

        assertNull(result.goal)
        assertNull(result.stability)
    }

    @Test
    fun `Stage 320 does not mutate supplied long running goal state`() {
        val goal = longRunningGoal()

        val result =
            Stage320LongRunningAssistantAlphaCoordinator()
                .prepare(
                    goal = goal,
                    stabilityEvidence = completeEvidence(),
                )

        assertEquals(
            LongRunningGoalState.ACTIVE,
            goal.state,
        )

        assertSame(
            goal,
            result.goal,
        )
    }

    @Test
    fun `AVAILABLE result rejects non stable Stage 272 evidence`() {
        val nonStable =
            com.devil.app.performance.DevilLongRunningStabilityCoordinator()
                .evaluate(
                    evidence =
                        completeEvidence().copy(
                            automaticRecoveryLoopAbsent = false,
                        ),
                )

        assertEquals(
            DevilLongRunningStabilityStatus.STABILITY_NOT_ESTABLISHED,
            nonStable.status,
        )

        assertFailsWith<IllegalArgumentException> {
            Stage320LongRunningAssistantAlphaResult.create(
                status = Stage320LongRunningAssistantAlphaStatus.AVAILABLE,
                goal = longRunningGoal(),
                stability = nonStable,
            )
        }
    }

    private fun completeEvidence():
        DevilLongRunningStabilityEvidence =
        DevilLongRunningStabilityEvidence(
            voiceInputLifecycleBounded = true,
            voiceOutputLifecycleBounded = true,
            cameraResourceLifecycleBounded = true,
            networkConnectionLifecycleBounded = true,
            uncontrolledBackgroundWorkAbsent = true,
            automaticRecoveryLoopAbsent = true,
        )

    private fun longRunningGoal(): LongRunningGoalRecord {
        val traceId =
            TraceId.from(
                "trace-stage320-long-running-assistant-alpha",
            )

        val decision =
            DecisionRecord.create(
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
                            "User supplied one bounded long-running assistant goal.",
                    ),
                state =
                    DecisionState.SELECTED,
                summary =
                    "Preserve one bounded long-running assistant goal.",
            )

        return LongRunningGoalRecord.create(
            goalId =
                LongRunningGoalId.from(
                    "goal:stage320-long-running-assistant-alpha",
                ),
            originatingDecision = decision,
            state = LongRunningGoalState.ACTIVE,
            description =
                "Preserve bounded assistant goal continuity across governed cycles.",
        )
    }
}
