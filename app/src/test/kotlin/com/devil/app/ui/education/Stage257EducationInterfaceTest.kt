package com.devil.app.ui.education

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 257 Education Interface governance tests.
 *
 * Stage 257 is presentation only.
 */
class Stage257EducationInterfaceTest {

    @Test
    fun `education interface uses locked Devil identity asset`() {
        val source = educationInterfaceSource()

        assertTrue(
            source.contains(
                "R.drawable.devil_primary_logo",
            ),
        )
    }

    @Test
    fun `education interface presents bounded education session information`() {
        val source = educationInterfaceSource()

        assertTrue(source.contains("\"EDUCATION\""))
        assertTrue(source.contains("\"EDUCATION SESSION\""))
        assertTrue(source.contains("\"SESSION\""))
        assertTrue(source.contains("\"SUBJECT\""))
        assertTrue(source.contains("\"LEARNING OBJECTIVE\""))
        assertTrue(source.contains("\"TARGET LANGUAGE\""))
    }

    @Test
    fun `education interface presents bounded study support information`() {
        val source = educationInterfaceSource()

        assertTrue(source.contains("\"STUDY SUPPORT\""))
        assertTrue(source.contains("\"STUDY FOCUS\""))
        assertTrue(source.contains("\"STUDY APPROACH\""))
        assertTrue(source.contains("\"LEARNER SUPPORT\""))
    }

    @Test
    fun `education interface presents bounded learner progress information`() {
        val source = educationInterfaceSource()

        assertTrue(source.contains("\"LEARNING PROGRESS\""))
        assertTrue(source.contains("\"PROGRESS FOCUS\""))
        assertTrue(source.contains("\"LEARNER EVIDENCE\""))
        assertTrue(source.contains("\"PROGRESS INTERPRETATION\""))
    }

    @Test
    fun `education interface presents child guardian boundaries`() {
        val source = educationInterfaceSource()

        assertTrue(source.contains("\"CHILD & GUARDIAN\""))
        assertTrue(source.contains("\"CHILD EDUCATION\""))
        assertTrue(source.contains("\"TEACHING LEVEL\""))
        assertTrue(source.contains("\"TEACHING APPROACH\""))
        assertTrue(source.contains("\"GUARDIAN POLICY\""))
        assertTrue(source.contains("\"PRIVACY BOUNDARY\""))
    }

    @Test
    fun `education interface presents bounded education modes`() {
        val source = educationInterfaceSource()

        assertTrue(source.contains("\"EDUCATION MODES\""))
        assertTrue(source.contains("\"SPOKEN EDUCATION\""))
        assertTrue(source.contains("\"EDUCATIONAL VISION\""))
        assertTrue(source.contains("\"TABLET EDUCATION\""))
    }

    @Test
    fun `education interface preserves education constitutional boundaries`() {
        val source = educationInterfaceSource()

        assertTrue(
            source.contains(
                "EDUCATION_INTERFACE != EDUCATION_AUTHORITY.",
            ),
        )

        assertTrue(
            source.contains(
                "EDUCATION_SESSION != SECURITY_SESSION.",
            ),
        )

        assertTrue(
            source.contains(
                "USER_LEARNING != DEVIL_CONSTITUTIONAL_LEARNING.",
            ),
        )

        assertTrue(
            source.contains(
                "EDUCATION_OBJECTIVE != DECISION.",
            ),
        )

        assertTrue(
            source.contains(
                "EDUCATION_OBJECTIVE != TASK.",
            ),
        )

        assertTrue(
            source.contains(
                "LANGUAGE_EDUCATION_SESSION != AUTHORIZATION.",
            ),
        )

        assertTrue(
            source.contains(
                "LANGUAGE_EDUCATION_SESSION != VERIFIED_LEARNING_PROGRESS.",
            ),
        )
    }

    @Test
    fun `education interface preserves study and progress boundaries`() {
        val source = educationInterfaceSource()

        assertTrue(
            source.contains(
                "STUDY_COMPANION != STUDY_SCHEDULER.",
            ),
        )

        assertTrue(
            source.contains(
                "STUDY_COMPANION != TASK_CREATION.",
            ),
        )

        assertTrue(
            source.contains(
                "STUDY_COMPANION != VERIFIED_MASTERY.",
            ),
        )

        assertTrue(
            source.contains(
                "HOMEWORK_ASSISTANCE != HOMEWORK_COMPLETION.",
            ),
        )

        assertTrue(
            source.contains(
                "HOMEWORK_ASSISTANCE != ASSIGNMENT_SUBMISSION.",
            ),
        )

        assertTrue(
            source.contains(
                "HOMEWORK_ASSISTANCE != VERIFIED_CORRECTNESS.",
            ),
        )

        assertTrue(
            source.contains(
                "LEARNING_PROGRESS != VERIFIED_MASTERY.",
            ),
        )

        assertTrue(
            source.contains(
                "LEARNING_PROGRESS != CONSTITUTIONAL_VERIFICATION.",
            ),
        )

        assertTrue(
            source.contains(
                "PROGRESS_INTERPRETATION != VERIFIED_OUTCOME.",
            ),
        )
    }

    @Test
    fun `education interface preserves child guardian privacy boundaries`() {
        val source = educationInterfaceSource()

        assertTrue(
            source.contains(
                "CHILD_EDUCATION_CONTEXT != CHILD_CLASSIFICATION.",
            ),
        )

        assertTrue(
            source.contains(
                "CHILD_CLASSIFICATION != AUTHENTICATION.",
            ),
        )

        assertTrue(
            source.contains(
                "CHILD_EDUCATION_INTEGRATION != GUARDIAN_AUTHORITY.",
            ),
        )

        assertTrue(
            source.contains(
                "CHILD_EDUCATION_INTEGRATION != GUARDIAN_APPROVAL.",
            ),
        )

        assertTrue(
            source.contains(
                "AGE_APPROPRIATE_TEACHING != AGE_INFERENCE.",
            ),
        )

        assertTrue(
            source.contains(
                "GUARDIAN_POLICY_FOUNDATION != GUARDIAN_AUTHORITY.",
            ),
        )

        assertTrue(
            source.contains(
                "GUARDIAN_POLICY_FOUNDATION != GUARDIAN_APPROVAL.",
            ),
        )

        assertTrue(
            source.contains(
                "CHILD_PRIVACY_BOUNDARY != PRIVACY_AUTHORIZATION.",
            ),
        )

        assertTrue(
            source.contains(
                "PRIVACY_BOUNDARY != DISCLOSURE_OCCURRED.",
            ),
        )
    }

    @Test
    fun `education interface preserves embodiment boundaries`() {
        val source = educationInterfaceSource()

        assertTrue(
            source.contains(
                "SPOKEN_EDUCATION_MODE != SPEECH_EXECUTED.",
            ),
        )

        assertTrue(
            source.contains(
                "EDUCATIONAL_VISION != VERIFIED_CORRECTNESS.",
            ),
        )

        assertTrue(
            source.contains(
                "TABLET_CONTEXT != AUTHORIZATION.",
            ),
        )
    }

    @Test
    fun `education interface remains presentation only`() {
        val source = educationInterfaceSource()

        assertTrue(
            source.contains(
                "\"EDUCATION PRESENTATION ONLY\"",
            ),
        )

        assertTrue(
            source.contains(
                "\"BACK TO CONVERSATION\"",
            ),
        )

        assertFalse(source.contains("EducationSessionCoordinator"))
        assertFalse(source.contains("LearningProgressCoordinator"))
        assertFalse(source.contains("StudyCompanionCoordinator"))
        assertFalse(source.contains("HomeworkAssistanceCoordinator"))
        assertFalse(source.contains("GuardianEducationPolicyCoordinator"))
        assertFalse(source.contains("ChildPrivacyBoundaryCoordinator"))
        assertFalse(source.contains("LearningAuthority"))
        assertFalse(source.contains("TaskAuthority"))
        assertFalse(source.contains("PlanAuthority"))
        assertFalse(source.contains("UnifiedDevilRuntime"))
    }

    @Test
    fun `education interface contains no operational education controls`() {
        val source = educationInterfaceSource()

        assertFalse(source.contains("\"START LESSON\""))
        assertFalse(source.contains("\"GENERATE LESSON\""))
        assertFalse(source.contains("\"DO HOMEWORK\""))
        assertFalse(source.contains("\"SUBMIT ASSIGNMENT\""))
        assertFalse(source.contains("\"GRADE\""))
        assertFalse(source.contains("\"SCHEDULE STUDY\""))
        assertFalse(source.contains("\"CREATE TASK\""))
        assertFalse(source.contains("\"VERIFY MASTERY\""))
    }

    @Test
    fun `missing supplied education information remains truthful`() {
        val source = educationInterfaceSource()

        assertTrue(
            source.contains(
                "?: \"Unavailable\"",
            ),
        )

        assertTrue(
            source.contains(
                "\"No education objective supplied.\"",
            ),
        )

        assertTrue(
            source.contains(
                "\"No learner evidence supplied.\"",
            ),
        )

        assertTrue(
            source.contains(
                "\"No progress interpretation supplied.\"",
            ),
        )
    }

    @Test
    fun `Stage 257 does not implement Stage 258 or later UI work`() {
        val source = educationInterfaceSource()

        assertTrue(
            source.contains(
                "Stage 257 does not implement Stage 258 or later UI work.",
            ),
        )
    }

    private fun educationInterfaceSource(): String {
        val candidates =
            listOf(
                "app/src/main/kotlin/com/devil/app/ui/education/DevilEducationInterface.kt",
                "src/main/kotlin/com/devil/app/ui/education/DevilEducationInterface.kt",
            )

        val file =
            candidates
                .map(::File)
                .firstOrNull(File::isFile)

        requireNotNull(file) {
            "Unable to locate Stage 257 DevilEducationInterface source."
        }

        return file.readText()
    }
}
