package com.devil.app.child

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Stage44ChildGuardianProductionCompositionTest {

    @Test
    fun `Devil application composes bounded Stage 44 policy coordinators without fabricated child data`() {
        val source =
            File(
                "src/main/kotlin/com/devil/app/DevilApplication.kt",
            ).readText()

        assertTrue(
            source.contains(
                "val childPolicyCoordinator:",
            ),
        )

        assertTrue(
            source.contains(
                "ChildPolicyCoordinator()",
            ),
        )

        assertTrue(
            source.contains(
                "val childPolicySatisfactionCoordinator:",
            ),
        )

        assertTrue(
            source.contains(
                "ChildPolicySatisfactionCoordinator()",
            ),
        )

        assertFalse(
            source.contains(
                "ChildGuardianContext.create(",
            ),
        )

        assertFalse(
            source.contains(
                "GuardianAuthorityRecord.create(",
            ),
        )

        assertFalse(
            source.contains(
                "GuardianApprovalDecision.create(",
            ),
        )

        assertFalse(
            source.contains(
                "ChildSubjectClassification.CHILD",
            ),
        )
    }
}
