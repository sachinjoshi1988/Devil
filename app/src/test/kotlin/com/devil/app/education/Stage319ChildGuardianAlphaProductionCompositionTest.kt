package com.devil.app.education

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Stage319ChildGuardianAlphaProductionCompositionTest {

    @Test
    fun `Devil application composes bounded Stage 319 coordinator`() {
        val source =
            File(
                "src/main/kotlin/com/devil/app/DevilApplication.kt",
            ).readText()

        assertTrue(
            source.contains(
                "val stage319ChildGuardianAlphaCoordinator:",
            ),
        )

        assertTrue(
            source.contains(
                "Stage319ChildGuardianAlphaCoordinator()",
            ),
        )
    }

    @Test
    fun `Android education presentation remains fail closed without governed child evidence`() {
        val source =
            File(
                "src/main/kotlin/com/devil/app/DevilActivity.kt",
            ).readText()

        assertTrue(source.contains("childEducationStatus = null,"))
        assertTrue(source.contains("teachingLevel = null,"))
        assertTrue(source.contains("teachingApproach = null,"))
        assertTrue(source.contains("guardianPolicyStatus = null,"))
        assertTrue(source.contains("privacyBoundaryStatus = null,"))

        assertFalse(
            source.contains(
                "stage319ChildGuardianAlphaCoordinator",
            ),
        )
    }
}
