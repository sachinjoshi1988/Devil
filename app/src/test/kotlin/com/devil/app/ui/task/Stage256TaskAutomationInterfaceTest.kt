package com.devil.app.ui.task

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 256 Task & Automation Interface governance tests.
 *
 * Stage 256 is presentation only.
 */
class Stage256TaskAutomationInterfaceTest {

    @Test
    fun `task automation interface uses locked Devil identity asset`() {
        val source = taskAutomationInterfaceSource()

        assertTrue(
            source.contains(
                "R.drawable.devil_primary_logo",
            ),
        )
    }

    @Test
    fun `task automation interface presents bounded task information`() {
        val source = taskAutomationInterfaceSource()

        assertTrue(source.contains("TASK CONTROL"))
        assertTrue(source.contains("TASK STATUS"))
        assertTrue(source.contains("TASK ID"))
        assertTrue(source.contains("STATE"))
        assertTrue(source.contains("SUMMARY"))
    }

    @Test
    fun `task automation interface presents bounded automation information`() {
        val source = taskAutomationInterfaceSource()

        assertTrue(source.contains("AUTOMATION STATUS"))
        assertTrue(source.contains("TRIGGER"))
        assertTrue(source.contains("TRIGGER CONDITION"))
        assertTrue(source.contains("PROACTIVE"))
        assertTrue(source.contains("PROACTIVE MESSAGE"))
        assertTrue(source.contains("CONTROLLED AUTONOMY"))
        assertTrue(source.contains("BOUNDED SCOPE"))
    }

    @Test
    fun `task automation interface presents bounded recovery information`() {
        val source = taskAutomationInterfaceSource()

        assertTrue(source.contains("RECOVERY GOVERNANCE"))
        assertTrue(source.contains("DISPOSITION"))
        assertTrue(source.contains("ATTEMPT STATUS"))
    }

    @Test
    fun `task automation interface preserves constitutional boundaries`() {
        val source = taskAutomationInterfaceSource()

        assertTrue(
            source.contains(
                "TASK_INTERFACE != TASK_AUTHORITY.",
            ),
        )

        assertTrue(
            source.contains(
                "TASK_STATE != EXECUTION.",
            ),
        )

        assertTrue(
            source.contains(
                "SCHEDULED != TRIGGERED.",
            ),
        )

        assertTrue(
            source.contains(
                "TRIGGERED != AUTHORIZED.",
            ),
        )

        assertTrue(
            source.contains(
                "PROACTIVE_ELIGIBILITY != PRESENTATION_DELIVERY.",
            ),
        )

        assertTrue(
            source.contains(
                "CONTROLLED_AUTONOMY_PREPARED != AUTONOMY_GRANTED.",
            ),
        )

        assertTrue(
            source.contains(
                "CONTROLLED_AUTONOMY != AUTHORIZATION.",
            ),
        )

        assertTrue(
            source.contains(
                "RECOVERY_ELIGIBLE != RETRY_AUTHORIZED.",
            ),
        )

        assertTrue(
            source.contains(
                "RECOVERY_ATTEMPT_RECORDED != RECOVERY_EXECUTED.",
            ),
        )
    }

    @Test
    fun `task automation interface does not invoke task or execution authorities`() {
        val source = taskAutomationInterfaceSource()

        assertFalse(source.contains("TaskAuthority"))
        assertFalse(source.contains("PlanAuthority"))
        assertFalse(source.contains("ExecutionRequest"))
        assertFalse(source.contains("UnifiedDevilRuntime"))
        assertFalse(source.contains("GoalTriggerCoordinator"))
        assertFalse(source.contains("ProactiveAssistanceCoordinator"))
        assertFalse(source.contains("ControlledAutonomyCoordinator"))
        assertFalse(source.contains("RecoveryRequestCoordinator"))
        assertFalse(source.contains("RecoveryAttemptCoordinator"))
    }

    @Test
    fun `task automation interface contains no operational controls`() {
        val source = taskAutomationInterfaceSource()

        assertFalse(source.contains("CREATE TASK"))
        assertFalse(source.contains("START TASK"))
        assertFalse(source.contains("CANCEL TASK"))
        assertFalse(source.contains("RUN NOW"))
        assertFalse(source.contains("RETRY NOW"))
        assertFalse(source.contains("AUTHORIZE NOW"))
        assertFalse(source.contains("GRANT AUTONOMY"))
    }

    @Test
    fun `missing supplied task automation information remains truthful`() {
        val source = taskAutomationInterfaceSource()

        assertTrue(
            source.contains(
                "?: \"Unavailable\"",
            ),
        )

        assertTrue(
            source.contains(
                "No task summary supplied.",
            ),
        )

        assertTrue(
            source.contains(
                "No trigger condition supplied.",
            ),
        )

        assertTrue(
            source.contains(
                "No proactive presentation supplied.",
            ),
        )

        assertTrue(
            source.contains(
                "No Controlled Autonomy scope supplied.",
            ),
        )
    }

    @Test
    fun `task automation interface preserves conversation return control`() {
        val source = taskAutomationInterfaceSource()

        assertTrue(source.contains("onClick = onBack"))
        assertTrue(source.contains("BACK TO CONVERSATION"))
    }

    @Test
    fun `Stage 256 does not implement Stage 257 Education Interface`() {
        val source = taskAutomationInterfaceSource()

        assertTrue(
            source.contains(
                "Stage 256 does not implement Stage 257 Education Interface.",
            ),
        )

        assertFalse(source.contains("EducationInterface"))
        assertFalse(source.contains("LearningCurriculum"))
    }

    private fun taskAutomationInterfaceSource(): String {
        val candidates =
            listOf(
                "app/src/main/kotlin/com/devil/app/ui/task/DevilTaskAutomationInterface.kt",
                "src/main/kotlin/com/devil/app/ui/task/DevilTaskAutomationInterface.kt",
            )

        val file =
            candidates
                .map(::File)
                .firstOrNull(File::isFile)

        requireNotNull(file) {
            "Unable to locate Stage 256 DevilTaskAutomationInterface source."
        }

        return file.readText()
    }
}
