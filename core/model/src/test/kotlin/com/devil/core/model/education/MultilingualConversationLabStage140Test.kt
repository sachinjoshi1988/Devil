package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class MultilingualConversationLabStage140Test {

    @Test
    fun `conversation lab preserves multilingual context and normalizes explicit inputs`() {
        val multilingual =
            multilingualTeaching("French")

        val record =
            MultilingualConversationLabRecord.create(
                multilingualTeaching = multilingual,
                conversationScenario =
                    "  Ordering food at a restaurant  ",
                conversationObjective =
                    "  Prepare bounded multilingual speaking practice  ",
            )

        assertSame(
            multilingual,
            record.multilingualTeaching,
        )

        assertEquals(
            "French",
            record.multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )

        assertEquals(
            "Ordering food at a restaurant",
            record.conversationScenario,
        )

        assertEquals(
            "Prepare bounded multilingual speaking practice",
            record.conversationObjective,
        )
    }

    @Test
    fun `conversation lab remains language neutral`() {
        listOf(
            "German",
            "Spanish",
            "Russian",
            "Mandarin Chinese",
            "Japanese",
        ).forEach { language ->
            val record =
                MultilingualConversationLabRecord.create(
                    multilingualTeaching =
                        multilingualTeaching(language),
                    conversationScenario =
                        "Everyday conversation",
                    conversationObjective =
                        "Prepare practice context",
                )

            assertEquals(
                language,
                record.multilingualTeaching
                    .languageEducationSession
                    .targetLanguage,
            )
        }
    }

    @Test
    fun `conversation lab rejects blank scenario`() {
        assertFailsWith<IllegalArgumentException> {
            MultilingualConversationLabRecord.create(
                multilingualTeaching =
                    multilingualTeaching("French"),
                conversationScenario = "   ",
                conversationObjective =
                    "Prepare practice context",
            )
        }
    }

    @Test
    fun `conversation lab rejects blank objective`() {
        assertFailsWith<IllegalArgumentException> {
            MultilingualConversationLabRecord.create(
                multilingualTeaching =
                    multilingualTeaching("Japanese"),
                conversationScenario =
                    "Everyday conversation",
                conversationObjective = "   ",
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
                        "education-session:stage140-model:$targetLanguage",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage140-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "Language Education",
                        objective =
                            "Prepare bounded Multilingual Conversation Lab context.",
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
                "Multilingual communication",
            teachingObjective =
                "Prepare reusable multilingual teaching context",
        )
    }
}
