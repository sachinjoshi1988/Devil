package com.devil.app.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage326LanguageCurriculumValidationCoordinatorTest {

    private val stage318Coordinator =
        Stage318ForeignLanguageAlphaCoordinator()

    private val coordinator =
        Stage326LanguageCurriculumValidationCoordinator()

    @Test
    fun `validated curriculum preserves exact Stage 318 provenance`() {
        val stage318 = availableStage318()

        val result =
            coordinator.validate(
                traceId =
                    TraceId.from(
                        "stage326-language-curriculum-validation",
                    ),
                foreignLanguageAlphaResult = stage318,
                curriculumFocus =
                    "Everyday French conversation and daily expressions",
                adaptationRationale =
                    "Owner explicitly selected practical French practice.",
                validationFocus =
                    "Bounded French curriculum architecture",
                validationEvidenceDescription =
                    "Existing Stage 318 language and multilingual contexts are preserved.",
            )

        assertEquals(
            Stage326LanguageCurriculumValidationStatus.VALIDATED,
            result.status,
        )

        assertSame(
            stage318,
            result.foreignLanguageAlphaResult,
        )

        val curriculum =
            assertNotNull(
                result.curriculumPreparation?.curriculum,
            )

        assertSame(
            stage318.languageSession,
            curriculum.languageEducationSession,
        )

        assertEquals(
            "Everyday French conversation and daily expressions",
            curriculum.curriculumFocus,
        )

        assertEquals(
            "Owner explicitly selected practical French practice.",
            curriculum.adaptationRationale,
        )

        val validation =
            assertNotNull(
                result.languageEducationValidation?.validation,
            )

        assertSame(
            stage318.multilingualTeaching,
            validation.multilingualTeaching,
        )

        assertEquals(
            "French",
            validation.multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )
    }

    @Test
    fun `deferred Stage 318 fails closed before curriculum preparation`() {
        val stage318 =
            Stage318ForeignLanguageAlphaResult.create(
                status =
                    Stage318ForeignLanguageAlphaStatus.DEFERRED,
            )

        val result =
            coordinator.validate(
                traceId =
                    TraceId.from(
                        "stage326-deferred-stage318",
                    ),
                foreignLanguageAlphaResult = stage318,
                curriculumFocus = "Everyday French",
                adaptationRationale =
                    "Owner explicitly selected practical practice.",
                validationFocus =
                    "Bounded curriculum architecture",
                validationEvidenceDescription =
                    "Existing structural evidence supplied.",
            )

        assertEquals(
            Stage326LanguageCurriculumValidationStatus.DEFERRED,
            result.status,
        )

        assertSame(
            stage318,
            result.foreignLanguageAlphaResult,
        )

        assertNull(result.curriculumPreparation)
        assertNull(result.languageEducationValidation)
    }

    @Test
    fun `blank curriculum input fails closed without partial result`() {
        val result =
            coordinator.validate(
                traceId =
                    TraceId.from(
                        "stage326-blank-curriculum",
                    ),
                foreignLanguageAlphaResult =
                    availableStage318(),
                curriculumFocus = " ",
                adaptationRationale =
                    "Owner explicitly selected practical practice.",
                validationFocus =
                    "Bounded curriculum architecture",
                validationEvidenceDescription =
                    "Existing structural evidence supplied.",
            )

        assertEquals(
            Stage326LanguageCurriculumValidationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.curriculumPreparation)
        assertNull(result.languageEducationValidation)
    }

    @Test
    fun `blank structural validation evidence fails closed without partial result`() {
        val result =
            coordinator.validate(
                traceId =
                    TraceId.from(
                        "stage326-blank-validation",
                    ),
                foreignLanguageAlphaResult =
                    availableStage318(),
                curriculumFocus = "Everyday French",
                adaptationRationale =
                    "Owner explicitly selected practical practice.",
                validationFocus =
                    "Bounded curriculum architecture",
                validationEvidenceDescription = " ",
            )

        assertEquals(
            Stage326LanguageCurriculumValidationStatus.DEFERRED,
            result.status,
        )

        assertNull(result.curriculumPreparation)
        assertNull(result.languageEducationValidation)
    }

    private fun availableStage318(): Stage318ForeignLanguageAlphaResult {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "stage316-owner-alpha-session",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "android-primary-local-subject",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "General Education",
                        objective =
                            "Support bounded owner education alpha testing.",
                    ),
            )

        return stage318Coordinator.prepare(
            traceId =
                TraceId.from(
                    "stage318-foreign-language-alpha",
                ),
            educationSession = educationSession,
            targetLanguage = "French",
            teachingFocus = "Everyday French",
            teachingObjective =
                "Prepare bounded French learning context.",
            frenchLearningFocus = "Daily expressions",
            frenchLearningObjective =
                "Prepare bounded French Alpha specialization.",
        )
    }
}
