package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class ReadingVocabularyStage126Test {

    @Test
    fun `reading vocabulary practice preserves language session and normalizes targets`() {
        val languageSession = languageSession()

        val record =
            ReadingVocabularyPracticeRecord.create(
                languageEducationSession = languageSession,
                readingTarget = "  Read a short story about daily routines  ",
                vocabularyTarget = "  routine, schedule, usually  ",
            )

        assertSame(
            languageSession,
            record.languageEducationSession,
        )

        assertEquals(
            "Read a short story about daily routines",
            record.readingTarget,
        )

        assertEquals(
            "routine, schedule, usually",
            record.vocabularyTarget,
        )
    }

    @Test
    fun `reading vocabulary practice rejects blank reading target`() {
        assertFailsWith<IllegalArgumentException> {
            ReadingVocabularyPracticeRecord.create(
                languageEducationSession = languageSession(),
                readingTarget = "   ",
                vocabularyTarget = "routine",
            )
        }
    }

    @Test
    fun `reading vocabulary practice rejects blank vocabulary target`() {
        assertFailsWith<IllegalArgumentException> {
            ReadingVocabularyPracticeRecord.create(
                languageEducationSession = languageSession(),
                readingTarget = "Read a short passage.",
                vocabularyTarget = "   ",
            )
        }
    }

    private fun languageSession(): LanguageEducationSessionRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage126-model",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage126-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "English",
                        objective = "Develop reading and vocabulary skills.",
                    ),
            )

        return LanguageEducationSessionRecord.create(
            educationSession = educationSession,
            targetLanguage = "English",
        )
    }
}
