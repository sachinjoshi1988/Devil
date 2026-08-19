package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class MandarinChineseEducationStage138Test {

    @Test
    fun `Mandarin Chinese Education preserves multilingual context and normalizes explicit inputs`() {
        val multilingual =
            multilingualTeaching("Mandarin Chinese")

        val record =
            MandarinChineseEducationRecord.create(
                multilingualTeaching = multilingual,
                mandarinChineseLearningFocus =
                    "  Everyday Mandarin Chinese introductions  ",
                mandarinChineseLearningObjective =
                    "  Prepare beginner Mandarin Chinese communication practice  ",
            )

        assertSame(
            multilingual,
            record.multilingualTeaching,
        )

        assertEquals(
            "Mandarin Chinese",
            record.multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )

        assertEquals(
            "Everyday Mandarin Chinese introductions",
            record.mandarinChineseLearningFocus,
        )

        assertEquals(
            "Prepare beginner Mandarin Chinese communication practice",
            record.mandarinChineseLearningObjective,
        )
    }

    @Test
    fun `Mandarin Chinese Education accepts case insensitive target`() {
        val record =
            MandarinChineseEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("mAnDaRiN cHiNeSe"),
                mandarinChineseLearningFocus =
                    "Greetings",
                mandarinChineseLearningObjective =
                    "Prepare Mandarin Chinese learning context",
            )

        assertEquals(
            "mAnDaRiN cHiNeSe",
            record.multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )
    }

    @Test
    fun `Mandarin Chinese Education rejects non Mandarin Chinese target language`() {
        assertFailsWith<IllegalArgumentException> {
            MandarinChineseEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("Russian"),
                mandarinChineseLearningFocus =
                    "Greetings",
                mandarinChineseLearningObjective =
                    "Prepare Mandarin Chinese learning context",
            )
        }
    }

    @Test
    fun `Mandarin Chinese Education rejects generic Chinese target`() {
        assertFailsWith<IllegalArgumentException> {
            MandarinChineseEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("Chinese"),
                mandarinChineseLearningFocus =
                    "Greetings",
                mandarinChineseLearningObjective =
                    "Prepare Mandarin Chinese learning context",
            )
        }
    }

    @Test
    fun `Mandarin Chinese Education rejects blank learning focus`() {
        assertFailsWith<IllegalArgumentException> {
            MandarinChineseEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("Mandarin Chinese"),
                mandarinChineseLearningFocus = "   ",
                mandarinChineseLearningObjective =
                    "Prepare Mandarin Chinese learning context",
            )
        }
    }

    @Test
    fun `Mandarin Chinese Education rejects blank learning objective`() {
        assertFailsWith<IllegalArgumentException> {
            MandarinChineseEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("Mandarin Chinese"),
                mandarinChineseLearningFocus =
                    "Greetings",
                mandarinChineseLearningObjective = "   ",
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
                        "education-session:stage138-model:$targetLanguage",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage138-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "Language Education",
                        objective =
                            "Prepare bounded Mandarin Chinese Education context.",
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
