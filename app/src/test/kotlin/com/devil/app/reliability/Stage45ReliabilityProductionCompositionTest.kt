package com.devil.app.reliability

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class Stage45ReliabilityProductionCompositionTest {

    @Test
    fun `Devil application owns one bounded Stage 45 reliability composition`() {
        val source =
            File(
                "src/main/kotlin/com/devil/app/DevilApplication.kt",
            ).readText()

        assertTrue(
            source.contains(
                "val reliabilityCoordinator:",
            ),
        )

        assertTrue(
            source.contains(
                "ReliabilityCoordinator()",
            ),
        )

        assertTrue(
            source.contains(
                "val recoveryRequestCoordinator:",
            ),
        )

        assertTrue(
            source.contains(
                "RecoveryRequestCoordinator()",
            ),
        )

        assertTrue(
            source.contains(
                "val recoveryAttemptCoordinator:",
            ),
        )

        assertTrue(
            source.contains(
                "RecoveryAttemptCoordinator()",
            ),
        )

        assertTrue(
            source.contains(
                "val recoveryVerificationCoordinator:",
            ),
        )

        assertTrue(
            source.contains(
                "RecoveryVerificationCoordinator()",
            ),
        )
    }
}
