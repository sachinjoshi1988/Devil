package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class GrammarInConversationStage125Test {

    @Test
    fun `grammar practice preserves conversation context and normalizes target`() {
        val conversationPractice = conversationPractice()

        val record =
            GrammarInConversationPracticeRecord.create(
                conversationPractice = conversationPractice,
                grammarTarget = "  simple present tense  ",
            )

        assertSame(
            conversationPractice,
            record.conversationPractice,
        )

        assertEquals(
            "simple present tense",
            record.grammarTarget,
        )
    }

    @Test
    fun `grammar practice rejects blank target`() {
        assertFailsWith<IllegalArgumentException> {
            GrammarInConversationPracticeRecord.create(
                conversationPractice = conversationPractice(),
                grammarTarget = "   ",
            )
        }
    }

    private fun conversationPractice(): SpokenEnglishConversationPracticeRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage125-model",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage125-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "English",
                        objective = "Practice grammar in spoken conversation.",
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
            topic = "Daily routine",
        )
    }
}
