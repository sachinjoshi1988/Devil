package com.devil.app.child

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse

class Stage44ChildGuardianAuthorityGovernanceTest {

    @Test
    fun `Stage 44 production composition does not create security authorization execution or memory authority`() {
        val source =
            File(
                "src/main/kotlin/com/devil/app/DevilApplication.kt",
            ).readText()

        val stage44Start =
            source.indexOf(
                "Stage 44 process-scoped bounded Child and Guardian Policy evaluation.",
            )

        require(stage44Start >= 0) {
            "Stage 44 production composition documentation is missing."
        }

        val notificationStart =
            source.indexOf(
                "val notificationAnalysisCoordinator:",
                startIndex = stage44Start,
            )

        require(notificationStart > stage44Start) {
            "Stage 44 production composition boundary could not be isolated."
        }

        val stage44Composition =
            source.substring(
                stage44Start,
                notificationStart,
            )

        assertFalse(
            stage44Composition.contains(
                "DefaultUnifiedDevilRuntime(",
            ),
        )

        assertFalse(
            stage44Composition.contains(
                "ConversationInput(",
            ),
        )

        assertFalse(
            stage44Composition.contains(
                "AndroidExecutionAdapter",
            ),
        )

        assertFalse(
            stage44Composition.contains(
                "MemoryPersistence",
            ),
        )

        assertFalse(
            stage44Composition.contains(
                "SecurityTransition",
            ),
        )

        assertFalse(
            stage44Composition.contains(
                "AuthorizationAssessment",
            ),
        )
    }
}
