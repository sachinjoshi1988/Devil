package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.AdditionalLanguageEducationRecord
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.MultilingualTeachingRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage139AdditionalLanguageEducationGovernanceTest {

    @Test
    fun `Japanese multilingual context may prepare bounded additional language context`() {
        val traceId =
            TraceId.from(
                "trace-stage139-additional-language-001",
            )

        val multilingual =
            multilingualTeaching(
                targetLanguage = "Japanese",
            )

        val result =
            AdditionalLanguageEducationCoordinator().prepare(
                traceId = traceId,
                multilingualTeaching = multilingual,
                additionalLanguageLearningFocus =
                    "Everyday Japanese introductions",
                additionalLanguageLearningObjective =
                    "Prepare reusable Japanese learning context",
            )

        assertEquals(
            AdditionalLanguageEducationPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val education =
            requireNotNull(
                result.additionalLanguageEducation,
            )

        assertSame(
            multilingual,
            education.multilingualTeaching,
        )

        assertEquals(
            "Japanese",
            education.multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )

        assertEquals(
            "Everyday Japanese introductions",
            education.additionalLanguageLearningFocus,
        )

        assertEquals(
            "Prepare reusable Japanese learning context",
            education.additionalLanguageLearningObjective,
        )
    }

    @Test
    fun `another non dedicated language may use generic expansion`() {
        val result =
            AdditionalLanguageEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage139-additional-language-002",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "Italian",
                    ),
                additionalLanguageLearningFocus =
                    "Beginner communication",
                additionalLanguageLearningObjective =
                    "Prepare reusable Italian learning context",
            )

        assertEquals(
            AdditionalLanguageEducationPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            "Italian",
            requireNotNull(result.additionalLanguageEducation)
                .multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )
    }

    @Test
    fun `dedicated language specializations remain outside generic expansion`() {
        listOf(
            "French",
            "German",
            "Spanish",
            "Russian",
            "Mandarin Chinese",
        ).forEachIndexed { index, language ->
            val result =
                AdditionalLanguageEducationCoordinator().prepare(
                    traceId =
                        TraceId.from(
                            "trace-stage139-dedicated-${index + 1}",
                        ),
                    multilingualTeaching =
                        multilingualTeaching(
                            targetLanguage = language,
                        ),
                    additionalLanguageLearningFocus =
                        "Beginner communication",
                    additionalLanguageLearningObjective =
                        "Prepare generic learning context",
                )

            assertEquals(
                AdditionalLanguageEducationPreparationStatus.DEFERRED,
                result.status,
            )

            assertNull(
                result.additionalLanguageEducation,
            )
        }
    }

    @Test
    fun `dedicated language exclusion is case insensitive`() {
        val result =
            AdditionalLanguageEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage139-additional-language-003",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "mAnDaRiN cHiNeSe",
                    ),
                additionalLanguageLearningFocus =
                    "Beginner communication",
                additionalLanguageLearningObjective =
                    "Prepare generic learning context",
            )

        assertEquals(
            AdditionalLanguageEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.additionalLanguageEducation,
        )
    }

    @Test
    fun `blank additional language learning focus remains deferred`() {
        val result =
            AdditionalLanguageEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage139-additional-language-004",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "Japanese",
                    ),
                additionalLanguageLearningFocus = "   ",
                additionalLanguageLearningObjective =
                    "Prepare Japanese learning context",
            )

        assertEquals(
            AdditionalLanguageEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.additionalLanguageEducation,
        )
    }

    @Test
    fun `blank additional language learning objective remains deferred`() {
        val result =
            AdditionalLanguageEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage139-additional-language-005",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "Japanese",
                    ),
                additionalLanguageLearningFocus =
                    "Beginner communication",
                additionalLanguageLearningObjective = "   ",
            )

        assertEquals(
            AdditionalLanguageEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.additionalLanguageEducation,
        )
    }

    @Test
    fun `prepared additional language result requires education context`() {
        assertFailsWith<IllegalArgumentException> {
            AdditionalLanguageEducationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage139-result-001",
                    ),
                status =
                    AdditionalLanguageEducationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred additional language result cannot smuggle education context`() {
        val education =
            AdditionalLanguageEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "Japanese",
                    ),
                additionalLanguageLearningFocus =
                    "Beginner communication",
                additionalLanguageLearningObjective =
                    "Prepare Japanese learning context",
            )

        assertFailsWith<IllegalArgumentException> {
            AdditionalLanguageEducationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage139-result-002",
                    ),
                status =
                    AdditionalLanguageEducationPreparationStatus.DEFERRED,
                additionalLanguageEducation = education,
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
                        "education-session:stage139-runtime:$targetLanguage",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage139-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "Language Education",
                        objective =
                            "Prepare bounded Additional Language Expansion context.",
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
                "Beginner language communication",
            teachingObjective =
                "Prepare reusable multilingual teaching context",
        )
    }
}
