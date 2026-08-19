package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class FrenchEducationStage134Test {

    @Test
    fun `French Education preserves multilingual context and normalizes explicit inputs`() {
        val multilingual = multilingualTeaching("French")

        val record =
            FrenchEducationRecord.create(
                multilingualTeaching = multilingual,
                frenchLearningFocus = "  Everyday French introductions  ",
                frenchLearningObjective = "  Prepare beginner French communication practice  ",
            )

        assertSame(
            multilingual,
            record.multilingualTeaching,
        )

        assertEquals(
            "French",
            record.multilingualTeaching.languageEducationSession.targetLanguage,
        )

        assertEquals(
            "Everyday French introductions",
            record.frenchLearningFocus,
        )

        assertEquals(
            "Prepare beginner French communication practice",
            record.frenchLearningObjective,
        )
    }

    @Test
    fun `French Education accepts case insensitive French target`() {
        val record =
            FrenchEducationRecord.create(
                multilingualTeaching = multilingualTeaching("fReNcH"),
                frenchLearningFocus = "Greetings",
                frenchLearningObjective = "Prepare French learning context",
            )

        assertEquals(
            "fReNcH",
            record.multilingualTeaching.languageEducationSession.targetLanguage,
        )
    }

    @Test
    fun `French Education rejects non French target language`() {
        assertFailsWith<IllegalArgumentException> {
            FrenchEducationRecord.create(
                multilingualTeaching = multilingualTeaching("German"),
                frenchLearningFocus = "Greetings",
                frenchLearningObjective = "Prepare French learning context",
            )
        }
    }

    @Test
    fun `French Education rejects blank learning focus`() {
        assertFailsWith<IllegalArgumentException> {
            FrenchEducationRecord.create(
                multilingualTeaching = multilingualTeaching("French"),
                frenchLearningFocus = "   ",
                frenchLearningObjective = "Prepare French learning context",
            )
        }
    }

    @Test
    fun `French Education rejects blank learning objective`() {
        assertFailsWith<IllegalArgumentException> {
            FrenchEducationRecord.create(
                multilingualTeaching = multilingualTeaching("French"),
                frenchLearningFocus = "Greetings",
                frenchLearningObjective = "   ",
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
                        "education-session:stage134-model:$targetLanguage",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage134-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "Language Education",
                        objective = "Prepare bounded French Education context.",
                    ),
            )

        val languageSession =
            LanguageEducationSessionRecord.create(
                educationSession = educationSession,
                targetLanguage = targetLanguage,
            )

        return MultilingualTeachingRecord.create(
            languageEducationSession = languageSession,
            teachingFocus = "Beginner language communication",
            teachingObjective = "Prepare reusable multilingual teaching context",
        )
    }
}
