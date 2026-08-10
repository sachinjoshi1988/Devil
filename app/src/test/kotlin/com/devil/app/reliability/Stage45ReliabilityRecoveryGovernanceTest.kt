package com.devil.app.reliability

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Stage45ReliabilityRecoveryGovernanceTest {

    @Test
    fun `Stage 45 remains bounded recovery governance rather than autonomous recovery`() {
        val applicationSource =
            File(
                "src/main/kotlin/com/devil/app/DevilApplication.kt",
            ).readText()

        assertTrue(
            applicationSource.contains(
                "RecoveryAttemptCoordinator()",
            ),
        )

        assertTrue(
            applicationSource.contains(
                "RecoveryVerificationCoordinator()",
            ),
        )

        assertFalse(
            applicationSource.contains(
                "RecoveryWorker(",
            ),
        )

        assertFalse(
            applicationSource.contains(
                "RecoveryService(",
            ),
        )

        assertFalse(
            applicationSource.contains(
                "RecoveryScheduler(",
            ),
        )

        assertFalse(
            applicationSource.contains(
                "AutomaticRecovery(",
            ),
        )
    }

    @Test
    fun `Stage 45 reliability core has no runtime execution or Android dependency`() {
        val reliabilityDirectory =
            File(
                "../core/model/src/main/kotlin/com/devil/core/model/reliability",
            )

        val source =
            reliabilityDirectory
                .walkTopDown()
                .filter {
                    it.isFile &&
                        it.extension == "kt"
                }
                .joinToString(
                    separator = "\n",
                ) {
                    it.readText()
                }

        assertFalse(
            source.contains(
                "import com.devil.app.",
            ),
        )

        assertFalse(
            source.contains(
                "import android.",
            ),
        )

        assertFalse(
            source.contains(
                "DefaultUnifiedDevilRuntime(",
            ),
        )

        assertFalse(
            source.contains(
                "ConversationInput(",
            ),
        )

        assertFalse(
            source.contains(
                "DefaultAndroidExecutionAdapter(",
            ),
        )

        assertFalse(
            source.contains(
                "RecoveryWorker(",
            ),
        )

        assertFalse(
            source.contains(
                "RecoveryService(",
            ),
        )
    }
}
