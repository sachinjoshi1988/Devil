package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class EnglishConfidenceCoachStage128Test {

    @Test
    fun `confidence coach practice preserves language session and normalizes inputs`() {
        val languageSession = languageSession()

        val record =
            EnglishConfidenceCoachPracticeRecord.create(
                languageEducationSession = languageSession,
                confidenceTarget = "  Speaking English during a work meeting  ",
                coachingObjective = "  Practice responding calmly and clearly  ",
            )

        assertSame(
            languageSession,
            record.languageEducationSession,
        )

        assertEquals(
            "Speaking English during a work meeting",
            record.confidenceTarget,
        )

        assertEquals(
            "Practice responding calmly and clearly",
            record.coachingObjective,
        )
    }

    @Test
    fun `confidence coach practice rejects blank confidence target`() {
        assertFailsWith<IllegalArgumentException> {
            EnglishConfidenceCoachPracticeRecord.create(
                languageEducationSession = languageSession(),
                confidenceTarget = "   ",
                coachingObjective = "Practice clear responses.",
            )
        }
    }

    @Test
    fun `confidence coach practice rejects blank coaching objective`() {
        assertFailsWith<IllegalArgumentException> {
            EnglishConfidenceCoachPracticeRecord.create(
                languageEducationSession = languageSession(),
                confidenceTarget = "Speaking in class",
                coachingObjective = "   ",
            )
        }
    }

    private fun languageSession(): LanguageEducationSessionRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage128-model",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage128-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "English",
                        objective = "Build confidence using English in practical situations.",
                    ),
            )

        return LanguageEducationSessionRecord.create(
            educationSession = educationSession,
            targetLanguage = "English",
        )
    }
}
