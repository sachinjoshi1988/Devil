package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class GermanEducationStage135Test {

    @Test
    fun `German Education preserves multilingual context and normalizes explicit inputs`() {
        val multilingual =
            multilingualTeaching("German")

        val record =
            GermanEducationRecord.create(
                multilingualTeaching = multilingual,
                germanLearningFocus = "  Everyday German introductions  ",
                germanLearningObjective =
                    "  Prepare beginner German communication practice  ",
            )

        assertSame(
            multilingual,
            record.multilingualTeaching,
        )

        assertEquals(
            "German",
            record.multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )

        assertEquals(
            "Everyday German introductions",
            record.germanLearningFocus,
        )

        assertEquals(
            "Prepare beginner German communication practice",
            record.germanLearningObjective,
        )
    }

    @Test
    fun `German Education accepts case insensitive German target`() {
        val record =
            GermanEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("gErMaN"),
                germanLearningFocus = "Greetings",
                germanLearningObjective =
                    "Prepare German learning context",
            )

        assertEquals(
            "gErMaN",
            record.multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )
    }

    @Test
    fun `German Education rejects non German target language`() {
        assertFailsWith<IllegalArgumentException> {
            GermanEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("French"),
                germanLearningFocus = "Greetings",
                germanLearningObjective =
                    "Prepare German learning context",
            )
        }
    }

    @Test
    fun `German Education rejects blank learning focus`() {
        assertFailsWith<IllegalArgumentException> {
            GermanEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("German"),
                germanLearningFocus = "   ",
                germanLearningObjective =
                    "Prepare German learning context",
            )
        }
    }

    @Test
    fun `German Education rejects blank learning objective`() {
        assertFailsWith<IllegalArgumentException> {
            GermanEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("German"),
                germanLearningFocus = "Greetings",
                germanLearningObjective = "   ",
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
                        "education-session:stage135-model:$targetLanguage",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage135-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "Language Education",
                        objective =
                            "Prepare bounded German Education context.",
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
