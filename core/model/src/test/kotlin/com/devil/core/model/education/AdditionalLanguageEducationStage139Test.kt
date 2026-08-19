package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class AdditionalLanguageEducationStage139Test {

    @Test
    fun `additional language expansion preserves multilingual context and normalizes explicit inputs`() {
        val multilingual =
            multilingualTeaching("Japanese")

        val record =
            AdditionalLanguageEducationRecord.create(
                multilingualTeaching = multilingual,
                additionalLanguageLearningFocus =
                    "  Everyday Japanese introductions  ",
                additionalLanguageLearningObjective =
                    "  Prepare reusable Japanese learning context  ",
            )

        assertSame(
            multilingual,
            record.multilingualTeaching,
        )

        assertEquals(
            "Japanese",
            record.multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )

        assertEquals(
            "Everyday Japanese introductions",
            record.additionalLanguageLearningFocus,
        )

        assertEquals(
            "Prepare reusable Japanese learning context",
            record.additionalLanguageLearningObjective,
        )
    }

    @Test
    fun `additional language expansion preserves arbitrary non dedicated target`() {
        val record =
            AdditionalLanguageEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("Korean"),
                additionalLanguageLearningFocus =
                    "Beginner communication",
                additionalLanguageLearningObjective =
                    "Prepare reusable additional-language context",
            )

        assertEquals(
            "Korean",
            record.multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )
    }

    @Test
    fun `additional language expansion rejects dedicated languages`() {
        listOf(
            "French",
            "German",
            "Spanish",
            "Russian",
            "Mandarin Chinese",
        ).forEach { language ->
            assertFailsWith<IllegalArgumentException> {
                AdditionalLanguageEducationRecord.create(
                    multilingualTeaching =
                        multilingualTeaching(language),
                    additionalLanguageLearningFocus =
                        "Beginner communication",
                    additionalLanguageLearningObjective =
                        "Prepare generic language context",
                )
            }
        }
    }

    @Test
    fun `dedicated language rejection is case insensitive`() {
        assertFailsWith<IllegalArgumentException> {
            AdditionalLanguageEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("fReNcH"),
                additionalLanguageLearningFocus =
                    "Beginner communication",
                additionalLanguageLearningObjective =
                    "Prepare generic language context",
            )
        }
    }

    @Test
    fun `additional language expansion rejects blank learning focus`() {
        assertFailsWith<IllegalArgumentException> {
            AdditionalLanguageEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("Japanese"),
                additionalLanguageLearningFocus = "   ",
                additionalLanguageLearningObjective =
                    "Prepare Japanese learning context",
            )
        }
    }

    @Test
    fun `additional language expansion rejects blank learning objective`() {
        assertFailsWith<IllegalArgumentException> {
            AdditionalLanguageEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("Japanese"),
                additionalLanguageLearningFocus =
                    "Beginner communication",
                additionalLanguageLearningObjective = "   ",
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
                        "education-session:stage139-model:$targetLanguage",
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
