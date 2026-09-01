package com.devil.app.reliability

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Stage320LongRunningAssistantAlphaProductionCompositionTest {

    @Test
    fun `Devil application composes bounded Stage 320 coordinator`() {
        val source =
            File(
                "src/main/kotlin/com/devil/app/DevilApplication.kt",
            ).readText()

        assertTrue(
            source.contains(
                "val stage320LongRunningAssistantAlphaCoordinator:",
            ),
        )

        assertTrue(
            source.contains(
                "Stage320LongRunningAssistantAlphaCoordinator()",
            ),
        )
    }

    @Test
    fun `Android Activity does not fabricate Stage 320 long running goal evidence`() {
        val source =
            File(
                "src/main/kotlin/com/devil/app/DevilActivity.kt",
            ).readText()

        assertFalse(
            source.contains(
                "stage320LongRunningAssistantAlphaCoordinator",
            ),
        )
    }

    @Test
    fun `Stage 320 coordinator contains no operational continuation wiring`() {
        val source =
            File(
                "src/main/kotlin/com/devil/app/reliability/" +
                    "Stage320LongRunningAssistantAlphaCoordinator.kt",
            ).readText()
                .replace(
                    Regex("(?s)/\\*.*?\\*/"),
                    "",
                )
                .replace(
                    Regex("(?m)//.*$"),
                    "",
                )

        listOf(
            "WorkManager",
            "JobScheduler",
            "AlarmManager",
            "WakeLock",
            "newSingleThreadExecutor",
            "ScheduledExecutor",
            "HandlerThread(",
            "Timer(",
            "while (true)",
            "startService(",
            "startForegroundService(",
            "GoalTriggerRecord",
            "GoalTriggerCoordinator",
            "RecoveryAttemptCoordinator(",
            "RecoveryVerificationCoordinator(",
        ).forEach { forbidden ->
            assertFalse(
                source.contains(forbidden),
                "Stage 320 must not introduce operational continuation wiring: $forbidden",
            )
        }
    }
}
