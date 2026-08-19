package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class LanguageEducationProductionValidationStage142Test {

    @Test
    fun `production validation preserves multilingual context and normalizes explicit inputs`() {
        val multilingual =
            multilingualTeaching("French")

        val record =
            LanguageEducationProductionValidationRecord.create(
                multilingualTeaching = multilingual,
                validationFocus =
                    "  Foreign-language architecture readiness  ",
                validationEvidenceDescription =
                    "  Stage 133 multilingual foundation and governed Stage 134-141 capability boundaries are represented.  ",
            )

        assertSame(
            multilingual,
            record.multilingualTeaching,
        )

        assertEquals(
            "French",
            record.multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )

        assertEquals(
            "Foreign-language architecture readiness",
            record.validationFocus,
        )

        assertEquals(
            "Stage 133 multilingual foundation and governed Stage 134-141 capability boundaries are represented.",
            record.validationEvidenceDescription,
        )
    }

    @Test
    fun `production validation remains language neutral`() {
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
        ).forEach { language ->
            val record =
                LanguageEducationProductionValidationRecord.create(
                    multilingualTeaching =
                        multilingualTeaching(language),
                    validationFocus =
                        "Architecture readiness",
                    validationEvidenceDescription =
                        "Bounded structural validation evidence",
                )

            assertEquals(
                language,
                record.multilingualTeaching
                    .languageEducationSession
                    .targetLanguage,
            )
        }
    }

    @Test
    fun `production validation rejects blank validation focus`() {
        assertFailsWith<IllegalArgumentException> {
            LanguageEducationProductionValidationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("Spanish"),
                validationFocus = "   ",
                validationEvidenceDescription =
                    "Bounded structural evidence",
            )
        }
    }

    @Test
    fun `production validation rejects blank evidence description`() {
        assertFailsWith<IllegalArgumentException> {
            LanguageEducationProductionValidationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("Japanese"),
                validationFocus =
                    "Architecture readiness",
                validationEvidenceDescription = "   ",
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
                        "education-session:stage142-model:$targetLanguage",
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
