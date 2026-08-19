package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class ListeningComprehensionStage124Test {

    @Test
    fun `listening practice preserves conversation context and normalizes target`() {
        val conversationPractice = conversationPractice()

        val record =
            ListeningComprehensionPracticeRecord.create(
                conversationPractice = conversationPractice,
                listeningTarget = "  Understand a short self-introduction  ",
            )

        assertSame(
            conversationPractice,
            record.conversationPractice,
        )

        assertEquals(
            "Understand a short self-introduction",
            record.listeningTarget,
        )
    }

    @Test
    fun `listening practice rejects blank target`() {
        assertFailsWith<IllegalArgumentException> {
            ListeningComprehensionPracticeRecord.create(
                conversationPractice = conversationPractice(),
                listeningTarget = "   ",
            )
        }
    }

    private fun conversationPractice(): SpokenEnglishConversationPracticeRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage124-model",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage124-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "English",
                        objective = "Practice beginner listening comprehension.",
                    ),
            )

        val languageSession =
            LanguageEducationSessionRecord.create(
                educationSession = educationSession,
                targetLanguage = "English",
            )

        val beginnerSession =
            SpokenEnglishBeginnerSessionRecord.create(
                languageEducationSession = languageSession,
            )

        return SpokenEnglishConversationPracticeRecord.create(
            beginnerSession = beginnerSession,
            topic = "Self introduction",
        )
    }
}
