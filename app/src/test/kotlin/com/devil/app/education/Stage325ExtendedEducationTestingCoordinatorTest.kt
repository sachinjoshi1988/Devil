package com.devil.app.education

import com.devil.app.device.tablet.AndroidEducationTabletExperienceStatus
import com.devil.app.vision.AndroidEducationalVisionStatus
import com.devil.app.voice.AndroidSpokenEducationModeStatus
import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage325ExtendedEducationTestingCoordinatorTest {

    private val stage316Coordinator =
        Stage316EducationAlphaCoordinator()

    private val coordinator =
        Stage325ExtendedEducationTestingCoordinator()

    @Test
    fun `available Stage 316 provenance is preserved exactly`() {
        val stage316 =
            stage316Coordinator.prepare(
                traceId =
                    TraceId.from(
                        "stage325-available-trace",
                    ),
                sessionId =
                    EducationSessionId.from(
                        "stage325-available-session",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "android-primary-local-subject",
                    ),
                subject = "General Education",
                objective =
                    "Validate bounded extended education behavior.",
            )

        val result =
            coordinator.validate(
                educationAlphaResult = stage316,
                spokenEducationStatus =
                    AndroidSpokenEducationModeStatus.AVAILABLE,
                educationalVisionStatus =
                    AndroidEducationalVisionStatus.DEFERRED,
                tabletEducationStatus =
                    AndroidEducationTabletExperienceStatus.AVAILABLE,
            )

        assertEquals(
            Stage325ExtendedEducationTestingStatus.AVAILABLE,
            result.status,
        )

        assertSame(
            stage316,
            result.educationAlphaResult,
        )

        val session =
            assertNotNull(
                result.educationAlphaResult.session,
            )

        assertEquals(
            "stage325-available-session",
            session.sessionId.value,
        )

        assertEquals(
            "General Education",
            session.objective.subject,
        )

        assertEquals(
            "Validate bounded extended education behavior.",
            session.objective.objective,
        )

        assertEquals(
            AndroidSpokenEducationModeStatus.AVAILABLE,
            result.spokenEducationStatus,
        )

        assertEquals(
            AndroidEducationalVisionStatus.DEFERRED,
            result.educationalVisionStatus,
        )

        assertEquals(
            AndroidEducationTabletExperienceStatus.AVAILABLE,
            result.tabletEducationStatus,
        )
    }

    @Test
    fun `integration status signals remain optional and do not create claims`() {
        val stage316 =
            stage316Coordinator.prepare(
                traceId =
                    TraceId.from(
                        "stage325-optional-signals-trace",
                    ),
                sessionId =
                    EducationSessionId.from(
                        "stage325-optional-signals-session",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "android-primary-local-subject",
                    ),
                subject = "General Education",
                objective =
                    "Validate education presentation boundaries.",
            )

        val result =
            coordinator.validate(
                educationAlphaResult = stage316,
            )

        assertEquals(
            Stage325ExtendedEducationTestingStatus.AVAILABLE,
            result.status,
        )

        assertNull(result.spokenEducationStatus)
        assertNull(result.educationalVisionStatus)
        assertNull(result.tabletEducationStatus)
    }

    @Test
    fun `deferred Stage 316 fails closed without education session`() {
        val stage316 =
            stage316Coordinator.prepare(
                traceId =
                    TraceId.from(
                        "stage325-deferred-trace",
                    ),
                sessionId =
                    EducationSessionId.from(
                        "stage325-deferred-session",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "android-primary-local-subject",
                    ),
                subject = " ",
                objective =
                    "Validate bounded extended education behavior.",
            )

        val result =
            coordinator.validate(
                educationAlphaResult = stage316,
                spokenEducationStatus =
                    AndroidSpokenEducationModeStatus.AVAILABLE,
                educationalVisionStatus =
                    AndroidEducationalVisionStatus.AVAILABLE,
                tabletEducationStatus =
                    AndroidEducationTabletExperienceStatus.AVAILABLE,
            )

        assertEquals(
            Stage325ExtendedEducationTestingStatus.DEFERRED,
            result.status,
        )

        assertSame(
            stage316,
            result.educationAlphaResult,
        )

        assertNull(
            result.educationAlphaResult.session,
        )

        /*
         * Existing integration-status inputs cannot manufacture an Education
         * session or convert the deferred Stage 316 boundary into AVAILABLE.
         */
        assertEquals(
            AndroidSpokenEducationModeStatus.AVAILABLE,
            result.spokenEducationStatus,
        )

        assertEquals(
            AndroidEducationalVisionStatus.AVAILABLE,
            result.educationalVisionStatus,
        )

        assertEquals(
            AndroidEducationTabletExperienceStatus.AVAILABLE,
            result.tabletEducationStatus,
        )
    }
}
