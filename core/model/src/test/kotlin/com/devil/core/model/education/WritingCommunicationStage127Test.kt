package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class WritingCommunicationStage127Test {

    @Test
    fun `writing communication practice preserves language session and normalizes targets`() {
        val languageSession = languageSession()

        val record =
            WritingCommunicationPracticeRecord.create(
                languageEducationSession = languageSession,
                writingTarget = "  Write a short self-introduction  ",
                communicationPurpose = "  Clear everyday communication  ",
            )

        assertSame(
            languageSession,
            record.languageEducationSession,
        )

        assertEquals(
            "Write a short self-introduction",
            record.writingTarget,
        )

        assertEquals(
            "Clear everyday communication",
            record.communicationPurpose,
        )
    }

    @Test
    fun `writing communication practice rejects blank writing target`() {
        assertFailsWith<IllegalArgumentException> {
            WritingCommunicationPracticeRecord.create(
                languageEducationSession = languageSession(),
                writingTarget = "   ",
                communicationPurpose = "Everyday communication",
            )
        }
    }

    @Test
    fun `writing communication practice rejects blank communication purpose`() {
        assertFailsWith<IllegalArgumentException> {
            WritingCommunicationPracticeRecord.create(
                languageEducationSession = languageSession(),
                writingTarget = "Write a short paragraph.",
                communicationPurpose = "   ",
            )
        }
    }

    private fun languageSession(): LanguageEducationSessionRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage127-model",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage127-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "English",
                        objective = "Develop practical writing and communication.",
                    ),
            )

        return LanguageEducationSessionRecord.create(
            educationSession = educationSession,
            targetLanguage = "English",
        )
    }
}
