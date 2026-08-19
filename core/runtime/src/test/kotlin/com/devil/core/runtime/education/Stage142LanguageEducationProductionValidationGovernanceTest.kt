package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.LanguageEducationProductionValidationRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.MultilingualTeachingRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage142LanguageEducationProductionValidationGovernanceTest {

    @Test
    fun `multilingual foundation may prepare bounded production validation context`() {
        val traceId =
            TraceId.from(
                "trace-stage142-validation-001",
            )

        val multilingual =
            multilingualTeaching(
                targetLanguage = "French",
            )

        val result =
            LanguageEducationProductionValidationCoordinator().prepare(
                traceId = traceId,
                multilingualTeaching = multilingual,
                validationFocus =
                    "Foreign-language architecture readiness",
                validationEvidenceDescription =
                    "Stage 133-141 structural language-education boundaries are represented.",
            )

        assertEquals(
            LanguageEducationProductionValidationPreparationStatus.VALIDATED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val validation =
            requireNotNull(result.validation)

        assertSame(
            multilingual,
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
    fun `dedicated and additional languages share production validation architecture`() {
        listOf(
            "French",
            "German",
            "Spanish",
            "Russian",
            "Mandarin Chinese",
            "Japanese",
            "Korean",
            "Italian",
            "Arabic",
        ).forEachIndexed { index, language ->
            val result =
                LanguageEducationProductionValidationCoordinator().prepare(
                    traceId =
                        TraceId.from(
                            "trace-stage142-language-${index + 1}",
                        ),
                    multilingualTeaching =
                        multilingualTeaching(
                            targetLanguage = language,
                        ),
                    validationFocus =
                        "Architecture readiness",
                    validationEvidenceDescription =
                        "Bounded structural validation evidence",
                )

            assertEquals(
                LanguageEducationProductionValidationPreparationStatus.VALIDATED,
                result.status,
            )

            assertEquals(
                language,
                requireNotNull(result.validation)
                    .multilingualTeaching
                    .languageEducationSession
                    .targetLanguage,
            )
        }
    }

    @Test
    fun `blank validation focus remains deferred`() {
        val result =
            LanguageEducationProductionValidationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage142-validation-002",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "Spanish",
                    ),
                validationFocus = "   ",
                validationEvidenceDescription =
                    "Bounded structural validation evidence",
            )

        assertEquals(
            LanguageEducationProductionValidationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.validation,
        )
    }

    @Test
    fun `blank validation evidence remains deferred`() {
        val result =
            LanguageEducationProductionValidationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage142-validation-003",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "Japanese",
                    ),
                validationFocus =
                    "Architecture readiness",
                validationEvidenceDescription = "   ",
            )

        assertEquals(
            LanguageEducationProductionValidationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.validation,
        )
    }

    @Test
    fun `validated production validation result requires validation context`() {
        assertFailsWith<IllegalArgumentException> {
            LanguageEducationProductionValidationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage142-result-001",
                    ),
                status =
                    LanguageEducationProductionValidationPreparationStatus.VALIDATED,
            )
        }
    }

    @Test
    fun `deferred production validation result cannot smuggle validation context`() {
        val validation =
            LanguageEducationProductionValidationRecord.create(
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "German",
                    ),
                validationFocus =
                    "Architecture readiness",
                validationEvidenceDescription =
                    "Bounded structural validation evidence",
            )

        assertFailsWith<IllegalArgumentException> {
            LanguageEducationProductionValidationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage142-result-002",
                    ),
                status =
                    LanguageEducationProductionValidationPreparationStatus.DEFERRED,
                validation = validation,
            )
        }
    }

    private fun multilingualTeaching(
        targetLanguage: String,
    ): MultilingualTeachingRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage142-runtime:$targetLanguage",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage142-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "Language Education",
                        objective =
                            "Prepare bounded Language Education Production Validation context.",
                    ),
            )

        val languageSession =
            LanguageEducationSessionRecord.create(
                educationSession = educationSession,
                targetLanguage = targetLanguage,
            )

        return MultilingualTeachingRecord.create(
            languageEducationSession = languageSession,
            teachingFocus =
                "Foreign-language education",
            teachingObjective =
                "Preserve reusable multilingual teaching architecture",
        )
    }
}
