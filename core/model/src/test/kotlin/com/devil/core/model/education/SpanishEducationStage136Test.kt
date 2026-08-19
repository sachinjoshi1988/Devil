package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class SpanishEducationStage136Test {

    @Test
    fun `Spanish Education preserves multilingual context and normalizes explicit inputs`() {
        val multilingual =
            multilingualTeaching("Spanish")

        val record =
            SpanishEducationRecord.create(
                multilingualTeaching = multilingual,
                spanishLearningFocus =
                    "  Everyday Spanish introductions  ",
                spanishLearningObjective =
                    "  Prepare beginner Spanish communication practice  ",
            )

        assertSame(
            multilingual,
            record.multilingualTeaching,
        )

        assertEquals(
            "Spanish",
            record.multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )

        assertEquals(
            "Everyday Spanish introductions",
            record.spanishLearningFocus,
        )

        assertEquals(
            "Prepare beginner Spanish communication practice",
            record.spanishLearningObjective,
        )
    }

    @Test
    fun `Spanish Education accepts case insensitive Spanish target`() {
        val record =
            SpanishEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("sPaNiSh"),
                spanishLearningFocus = "Greetings",
                spanishLearningObjective =
                    "Prepare Spanish learning context",
            )

        assertEquals(
            "sPaNiSh",
            record.multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )
    }

    @Test
    fun `Spanish Education rejects non Spanish target language`() {
        assertFailsWith<IllegalArgumentException> {
            SpanishEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("German"),
                spanishLearningFocus = "Greetings",
                spanishLearningObjective =
                    "Prepare Spanish learning context",
            )
        }
    }

    @Test
    fun `Spanish Education rejects blank learning focus`() {
        assertFailsWith<IllegalArgumentException> {
            SpanishEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("Spanish"),
                spanishLearningFocus = "   ",
                spanishLearningObjective =
                    "Prepare Spanish learning context",
            )
        }
    }

    @Test
    fun `Spanish Education rejects blank learning objective`() {
        assertFailsWith<IllegalArgumentException> {
            SpanishEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching("Spanish"),
                spanishLearningFocus = "Greetings",
                spanishLearningObjective = "   ",
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
                        "education-session:stage136-model:$targetLanguage",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage136-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "Language Education",
                        objective =
                            "Prepare bounded Spanish Education context.",
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
