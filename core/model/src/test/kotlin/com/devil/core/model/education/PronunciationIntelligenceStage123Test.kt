package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class PronunciationIntelligenceStage123Test {

    @Test
    fun `pronunciation practice preserves conversation context and normalizes target`() {
        val conversationPractice = conversationPractice()

        val record =
            PronunciationPracticeRecord.create(
                conversationPractice = conversationPractice,
                target = "  comfortable  ",
            )

        assertSame(
            conversationPractice,
            record.conversationPractice,
        )

        assertEquals(
            "comfortable",
            record.target,
        )
    }

    @Test
    fun `pronunciation practice rejects blank target`() {
        assertFailsWith<IllegalArgumentException> {
            PronunciationPracticeRecord.create(
                conversationPractice = conversationPractice(),
                target = "   ",
            )
        }
    }

    private fun conversationPractice(): SpokenEnglishConversationPracticeRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage123-model",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage123-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "English",
                        objective = "Practice spoken English pronunciation.",
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
            topic = "Daily conversation",
        )
    }
}
