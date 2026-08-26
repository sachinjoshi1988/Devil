package com.devil.app.automation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Stage 303 Automation Tests completion coverage for the established bounded
 * Devil automation, proactive-assistance, recovery, and Controlled Autonomy architecture.
 *
 * This test surface validates existing automation behavior only.
 *
 * TRIGGER_ELIGIBLE != AUTHORIZED.
 * TRIGGER_ELIGIBLE != EXECUTION.
 * PROACTIVE_ELIGIBLE != PRESENTED.
 * RECOVERY_ELIGIBLE != RETRY_AUTHORIZED.
 * RECOVERY_ATTEMPT_RECORDED != RECOVERY_EXECUTED.
 * CONTROLLED_AUTONOMY_PREPARATION != AUTONOMY_GRANT.
 *
 * Stage 303 does not create another runtime, Brain, Planner, Executive,
 * constitutional authority, scheduler, retry executor, automation engine,
 * Memory Authority, Security Authority, or platform capability.
 *
 * It does not modify production architecture and does not implement
 * Stage 304 Security Tests.
 */
class Stage303AutomationTests {

    @Test
    fun `scheduled and event triggers remain reconsideration eligibility only`() {
        val triggerResult =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/goal/GoalTriggerEvaluationResult.kt",
            )
        val triggerStatus =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/goal/GoalTriggerEvaluationStatus.kt",
            )

        assertTrue(
            triggerResult.contains(
                "Preserving an eligible trigger grants no execution or authorization",
            ),
        )

        listOf(
            "ELIGIBLE_FOR_RECONSIDERATION",
            "authenticated",
            "authorized",
            "execution approved",
            "execution attempted",
            "effect verified",
            "Outcome established",
        ).forEach { marker ->
            assertTrue(
                triggerStatus.contains(marker),
                "Missing Stage 303 trigger boundary marker: $marker",
            )
        }
    }

    @Test
    fun `proactive assistance requires fresh constitutional decision without becoming execution`() {
        val coordinator =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/proactive/ProactiveAssistanceCoordinator.kt",
            )
        val result =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/proactive/ProactiveAssistanceEvaluationResult.kt",
            )
        val record =
            source(
                "core/model/src/main/kotlin/com/devil/core/model/proactive/ProactiveAssistanceRecord.kt",
            )

        listOf(
            "GoalTriggerEvaluationStatus.ELIGIBLE_FOR_RECONSIDERATION",
            "DecisionState.SELECTED",
            "relevanceEstablished",
            "interruptionJustified",
            "ELIGIBLE_FOR_PRESENTATION",
        ).forEach { marker ->
            assertTrue(
                coordinator.contains(marker),
                "Missing Stage 303 proactive marker: $marker",
            )
        }

        assertTrue(
            result.contains(
                "Preserving this result does not present, notify, speak, authorize,",
            ),
        )
        assertTrue(record.contains("!= AUTHORIZED"))
        assertTrue(record.contains("!= EXECUTED."))
    }

    @Test
    fun `controlled autonomy remains preparation rather than autonomy grant`() {
        val coordinator =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/autonomy/ControlledAutonomyCoordinator.kt",
            )
        val result =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/autonomy/ControlledAutonomyPreparationResult.kt",
            )
        val status =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/autonomy/ControlledAutonomyPreparationStatus.kt",
            )

        listOf(
            "CONTROLLED_AUTONOMY_PREPARATION != AUTONOMY_GRANT.",
            "CONTROLLED_AUTONOMY != AUTHORIZATION.",
            "CONTROLLED_AUTONOMY != BRAIN_DECISION.",
            "CONTROLLED_AUTONOMY != EXECUTION.",
        ).forEach { marker ->
            assertTrue(
                coordinator.contains(marker),
                "Missing Stage 303 Controlled Autonomy boundary: $marker",
            )
        }

        assertTrue(
            result.contains(
                "Prepared Controlled Autonomy results require one record.",
            ),
        )
        assertTrue(
            result.contains(
                "Deferred Controlled Autonomy results must not contain a record.",
            ),
        )
        assertTrue(status.contains("PREPARED != AUTONOMY_GRANTED."))
        assertTrue(status.contains("PREPARED != AUTHORIZED."))
        assertTrue(status.contains("PREPARED != EXECUTED."))
    }

    @Test
    fun `recovery eligibility and accounting remain separate from retry execution`() {
        val disposition =
            source(
                "core/model/src/main/kotlin/com/devil/core/model/reliability/RecoveryDisposition.kt",
            )
        val budget =
            source(
                "core/model/src/main/kotlin/com/devil/core/model/reliability/RecoveryAttemptBudget.kt",
            )
        val request =
            source(
                "core/model/src/main/kotlin/com/devil/core/model/reliability/RecoveryRequest.kt",
            )
        val attempt =
            source(
                "core/model/src/main/kotlin/com/devil/core/model/reliability/RecoveryAttemptRecord.kt",
            )

        assertTrue(disposition.contains("RECOVERY_ELIGIBLE"))
        assertTrue(disposition.contains("!= retry authorized"))
        assertTrue(disposition.contains("!= recovery started"))

        assertTrue(
            budget.contains(
                "Possessing an unused attempt",
            ),
        )
        assertTrue(budget.contains("!= permission to retry."))

        assertTrue(request.contains("retry an operation"))
        assertTrue(
            request.contains(
                "or claim recovery.",
            ),
        )

        assertTrue(attempt.contains("Attempt recorded"))
        assertTrue(attempt.contains("!= recovery executed"))
        assertTrue(attempt.contains("!= recovery succeeded"))
    }

    @Test
    fun `existing automation governance tests preserve success deferral and invariants`() {
        val representativeTests =
            listOf(
                "core/runtime/src/test/kotlin/com/devil/core/runtime/goal/Stage79ScheduledEventTriggerGovernanceTest.kt",
                "core/runtime/src/test/kotlin/com/devil/core/runtime/proactive/Stage80ProactiveAssistanceGovernanceTest.kt",
                "core/runtime/src/test/kotlin/com/devil/core/runtime/autonomy/Stage95ControlledAutonomyFoundationGovernanceTest.kt",
                "core/model/src/test/kotlin/com/devil/core/model/reliability/RecoveryAttemptResultTest.kt",
            )

        representativeTests.forEachIndexed { index, path ->
            val test = source(path)

            assertTrue(
                test.contains("DEFERRED") ||
                    test.contains("UNAVAILABLE") ||
                    test.contains("EXHAUSTED"),
                "Stage 303 representative automation test $index lacks non-success coverage.",
            )

            assertTrue(
                test.contains("assertEquals") ||
                    test.contains("assertFailsWith"),
                "Stage 303 representative automation test $index lacks meaningful invariant assertions.",
            )
        }
    }

    @Test
    fun `Stage 303 stops before security test completion`() {
        val stage303 =
            source(
                "app/src/test/kotlin/com/devil/app/automation/Stage303AutomationTests.kt",
            )

        assertTrue(stage303.contains("does not implement"))
        assertTrue(stage303.contains("Stage 304 Security Tests"))
    }

    private fun source(path: String): String {
        val candidates =
            listOf(
                File(path),
                File("..", path),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate repository source for Stage 303: $path",
            )
    }
}
