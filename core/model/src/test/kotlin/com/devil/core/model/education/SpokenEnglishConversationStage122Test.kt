package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class SpokenEnglishConversationStage122Test {

    @Test
    fun `conversation practice preserves beginner session and normalizes topic`() {
        val beginnerSession = beginnerSession()

        val record =
            SpokenEnglishConversationPracticeRecord.create(
                beginnerSession = beginnerSession,
                topic = "  Introducing yourself  ",
            )

        assertSame(
            beginnerSession,
            record.beginnerSession,
        )

        assertEquals(
            "Introducing yourself",
            record.topic,
        )
    }

    @Test
    fun `conversation practice rejects blank topic`() {
        assertFailsWith<IllegalArgumentException> {
            SpokenEnglishConversationPracticeRecord.create(
                beginnerSession = beginnerSession(),
                topic = "   ",
            )
        }
    }

    private fun beginnerSession(): SpokenEnglishBeginnerSessionRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage122-model",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage122-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "English",
                        objective = "Practice beginner spoken English.",
                    ),
            )

        val languageSession =
            LanguageEducationSessionRecord.create(
                educationSession = educationSession,
                targetLanguage = "English",
            )

        return SpokenEnglishBeginnerSessionRecord.create(
            languageEducationSession = languageSession,
        )
    }
}
