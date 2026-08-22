package com.devil.app.device.tablet

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.embodiment.EmbodimentId
import com.devil.core.model.embodiment.EmbodimentPlatformId
import com.devil.core.model.embodiment.EmbodimentRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class Stage215EducationTabletExperienceTest {

    @Test
    fun `available tablet embodiment produces available education tablet experience`() {
        val tablet =
            availableTabletEmbodiment()

        val educationSession =
            educationSession()

        val result =
            AndroidEducationTabletExperienceCoordinator()
                .integrate(
                    tabletEmbodiment = tablet,
                    educationSession = educationSession,
                )

        assertEquals(
            AndroidEducationTabletExperienceStatus.AVAILABLE,
            result.status,
        )
        assertSame(
            tablet,
            result.tabletEmbodiment,
        )
        assertSame(
            educationSession,
            result.educationSession,
        )
    }

    @Test
    fun `deferred tablet embodiment keeps education tablet experience deferred`() {
        val embodiment =
            androidEmbodiment(
                id = "embodiment:stage215:phone",
            )

        val assessment =
            AndroidTabletFormFactorAssessmentResult.create(
                traceId = TraceId.from("trace-stage215-phone"),
                status =
                    AndroidTabletFormFactorAssessmentStatus.NON_TABLET,
                embodiment = embodiment,
                evidence =
                    AndroidTabletFormFactorEvidence.create(
                        smallestScreenWidthDp = 411,
                    ),
            )

        val tablet =
            AndroidTabletEmbodimentCoordinator()
                .integrate(assessment)

        val educationSession =
            educationSession()

        val result =
            AndroidEducationTabletExperienceCoordinator()
                .integrate(
                    tabletEmbodiment = tablet,
                    educationSession = educationSession,
                )

        assertEquals(
            AndroidEducationTabletExperienceStatus.DEFERRED,
            result.status,
        )
        assertSame(
            tablet,
            result.tabletEmbodiment,
        )
        assertSame(
            educationSession,
            result.educationSession,
        )
    }

    @Test
    fun `available result requires available Stage 214 tablet embodiment`() {
        val embodiment =
            androidEmbodiment(
                id = "embodiment:stage215:invalid",
            )

        val assessment =
            AndroidTabletFormFactorAssessmentResult.create(
                traceId = TraceId.from("trace-stage215-invalid"),
                status =
                    AndroidTabletFormFactorAssessmentStatus.DEFERRED,
                embodiment = embodiment,
            )

        val tablet =
            AndroidTabletEmbodimentCoordinator()
                .integrate(assessment)

        assertFailsWith<IllegalArgumentException> {
            AndroidEducationTabletExperienceResult.create(
                status =
                    AndroidEducationTabletExperienceStatus.AVAILABLE,
                tabletEmbodiment = tablet,
                educationSession = educationSession(),
            )
        }
    }

    @Test
    fun `deferred result preserves exact upstream objects`() {
        val embodiment =
            androidEmbodiment(
                id = "embodiment:stage215:deferred",
            )

        val assessment =
            AndroidTabletFormFactorAssessmentResult.create(
                traceId = TraceId.from("trace-stage215-deferred"),
                status =
                    AndroidTabletFormFactorAssessmentStatus.DEFERRED,
                embodiment = embodiment,
            )

        val tablet =
            AndroidTabletEmbodimentCoordinator()
                .integrate(assessment)

        val educationSession =
            educationSession()

        val result =
            AndroidEducationTabletExperienceResult.create(
                status =
                    AndroidEducationTabletExperienceStatus.DEFERRED,
                tabletEmbodiment = tablet,
                educationSession = educationSession,
            )

        assertSame(
            tablet,
            result.tabletEmbodiment,
        )
        assertSame(
            educationSession,
            result.educationSession,
        )
    }

    private fun availableTabletEmbodiment():
        AndroidTabletEmbodimentResult {
        val embodiment =
            androidEmbodiment(
                id = "embodiment:stage215:tablet",
            )

        val assessment =
            AndroidTabletFormFactorAssessmentResult.create(
                traceId = TraceId.from("trace-stage215-tablet"),
                status =
                    AndroidTabletFormFactorAssessmentStatus.TABLET,
                embodiment = embodiment,
                evidence =
                    AndroidTabletFormFactorEvidence.create(
                        smallestScreenWidthDp = 720,
                    ),
            )

        return AndroidTabletEmbodimentCoordinator()
            .integrate(assessment)
    }

    private fun educationSession(): EducationSessionRecord {
        return EducationSessionRecord.create(
            sessionId =
                EducationSessionId.from(
                    "education-session:stage215",
                ),
            subjectIdentityId =
                IdentityId.from(
                    "identity:stage215-learner",
                ),
            objective =
                EducationObjective.create(
                    subject = "Tablet Education",
                    objective =
                        "Preserve bounded education context on a tablet embodiment.",
                ),
        )
    }

    private fun androidEmbodiment(
        id: String,
    ): EmbodimentRecord {
        return EmbodimentRecord.create(
            embodimentId = EmbodimentId.from(id),
            platformId = EmbodimentPlatformId.from("android"),
            description = "Stage 215 bounded Android embodiment.",
        )
    }
}
