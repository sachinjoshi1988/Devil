package com.devil.app.education

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Stage 300 Education Tests completion coverage for the established
 * bounded Devil education architecture.
 *
 * This test surface validates existing education behavior only.
 *
 * EDUCATION_SESSION != AUTHENTICATION.
 * EDUCATION_SESSION != AUTHORIZATION.
 * CHILD_EDUCATION != CHILD_CLASSIFICATION_AUTHORITY.
 * GUARDIAN_POLICY != GUARDIAN_AUTHORITY.
 * USER_LEARNING != DEVIL_CONSTITUTIONAL_LEARNING.
 * EDUCATION_UI != EDUCATION_AUTHORITY.
 *
 * Stage 300 does not create another runtime, Brain, Planner, Executive,
 * constitutional authority, Education Authority, Memory Authority,
 * Security Authority, or platform capability.
 *
 * It does not modify production architecture and does not implement
 * Stage 301 Language Education Tests or Stage 302 Multilingual Tests.
 */
class Stage300EducationTests {

    @Test
    fun `education foundation preserves bounded session preparation and result invariants`() {
        val coordinator =
            source(
                "../core/runtime/src/main/kotlin/com/devil/core/runtime/education/EducationSessionCoordinator.kt",
            )
        val result =
            source(
                "../core/runtime/src/main/kotlin/com/devil/core/runtime/education/EducationSessionPreparationResult.kt",
            )
        val session =
            source(
                "../core/model/src/main/kotlin/com/devil/core/model/education/EducationSessionRecord.kt",
            )

        listOf(
            "EducationSessionPreparationStatus.PREPARED",
            "EducationSessionPreparationStatus.DEFERRED",
            "EducationObjective.create(",
            "EducationSessionRecord.create(",
        ).forEach { marker ->
            assertTrue(
                coordinator.contains(marker),
                "Missing Stage 300 education-foundation marker: $marker",
            )
        }

        assertTrue(
            result.contains("Prepared education-session results require one session."),
        )
        assertTrue(
            result.contains("Deferred education-session results must not contain a session."),
        )
        assertTrue(
            session.contains("EDUCATION_SESSION != SECURITY_SESSION."),
        )
        assertTrue(
            session.contains("USER_LEARNING != DEVIL_CONSTITUTIONAL_LEARNING."),
        )
    }

    @Test
    fun `child and guardian education remain bounded descriptive governance`() {
        val child =
            source(
                "../core/runtime/src/main/kotlin/com/devil/core/runtime/education/ChildEducationCoordinator.kt",
            )
        val guardian =
            source(
                "../core/runtime/src/main/kotlin/com/devil/core/runtime/education/GuardianEducationPolicyCoordinator.kt",
            )
        val privacy =
            source(
                "../core/runtime/src/main/kotlin/com/devil/core/runtime/education/ChildPrivacyBoundaryCoordinator.kt",
            )

        assertTrue(child.contains("ChildEducationPreparationStatus"))
        assertTrue(guardian.contains("GuardianEducationPolicyPreparationStatus"))
        assertTrue(privacy.contains("ChildPrivacyBoundaryPreparationStatus"))

        listOf(child, guardian, privacy).forEachIndexed { index, text ->
            assertTrue(
                !text.contains("UnifiedDevilRuntime("),
                "Stage 300 child/guardian surface $index must not create another runtime.",
            )
        }
    }

    @Test
    fun `education Android embodiments preserve existing education provenance only`() {
        val tablet =
            source(
                "src/main/kotlin/com/devil/app/device/tablet/AndroidEducationTabletExperienceCoordinator.kt",
            )
        val spoken =
            source(
                "src/main/kotlin/com/devil/app/voice/AndroidSpokenEducationModeCoordinator.kt",
            )
        val vision =
            source(
                "src/main/kotlin/com/devil/app/vision/AndroidEducationalVisionCoordinator.kt",
            )

        assertTrue(tablet.contains("AndroidEducationTabletExperience"))
        assertTrue(spoken.contains("AndroidSpokenEducationMode"))
        assertTrue(vision.contains("AndroidEducationalVision"))

        listOf(tablet, spoken, vision).forEachIndexed { index, text ->
            assertTrue(
                !text.contains("LearningAuthority"),
                "Stage 300 Android education surface $index must not become constitutional Learning.",
            )
            assertTrue(
                !text.contains("MemoryAuthority"),
                "Stage 300 Android education surface $index must not become Memory Authority.",
            )
        }
    }

    @Test
    fun `education interface remains presentation only`() {
        val ui =
            source(
                "src/main/kotlin/com/devil/app/ui/education/DevilEducationInterface.kt",
            )

        assertTrue(!ui.contains("EducationSessionCoordinator"))
        assertTrue(!ui.contains("LearningProgressCoordinator"))
        assertTrue(!ui.contains("StudyCompanionCoordinator"))
        assertTrue(!ui.contains("HomeworkAssistanceCoordinator"))
        assertTrue(!ui.contains("UnifiedDevilRuntime"))
    }

    @Test
    fun `Stage 300 stops before language and multilingual test completion stages`() {
        val stage300 =
            source(
                "src/test/kotlin/com/devil/app/education/Stage300EducationTests.kt",
            )

        assertTrue(
            stage300.contains("does not implement"),
        )
        assertTrue(
            stage300.contains("Stage 301 Language Education Tests"),
        )
        assertTrue(
            stage300.contains("Stage 302 Multilingual Tests"),
        )
    }

    private fun source(path: String): String =
        File(path).readText()
}
