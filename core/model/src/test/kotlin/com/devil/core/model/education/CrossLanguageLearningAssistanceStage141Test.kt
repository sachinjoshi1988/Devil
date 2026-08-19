package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class CrossLanguageLearningAssistanceStage141Test {

    @Test
    fun `cross language assistance preserves conversation lab and normalizes explicit inputs`() {
        val lab =
            conversationLab(
                targetLanguage = "French",
            )

        val record =
            CrossLanguageLearningAssistanceRecord.create(
                conversationLab = lab,
                supportLanguage = "  English  ",
                assistanceFocus =
                    "  Compare everyday greeting structures  ",
                assistanceObjective =
                    "  Prepare cross-language explanation practice  ",
            )

        assertSame(
            lab,
            record.conversationLab,
        )

        assertEquals(
            "French",
            record.conversationLab
                .multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )

        assertEquals(
            "English",
            record.supportLanguage,
        )

        assertEquals(
            "Compare everyday greeting structures",
            record.assistanceFocus,
        )

        assertEquals(
            "Prepare cross-language explanation practice",
            record.assistanceObjective,
        )
    }

    @Test
    fun `cross language assistance supports additional target languages`() {
        val record =
            CrossLanguageLearningAssistanceRecord.create(
                conversationLab =
                    conversationLab(
                        targetLanguage = "Japanese",
                    ),
                supportLanguage = "English",
                assistanceFocus =
                    "Compare basic sentence patterns",
                assistanceObjective =
                    "Prepare bounded learning assistance",
            )

        assertEquals(
            "Japanese",
            record.conversationLab
                .multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )
    }

    @Test
    fun `cross language assistance rejects same support and target language`() {
        assertFailsWith<IllegalArgumentException> {
            CrossLanguageLearningAssistanceRecord.create(
                conversationLab =
                    conversationLab(
                        targetLanguage = "German",
                    ),
                supportLanguage = "German",
                assistanceFocus =
                    "Compare expressions",
                assistanceObjective =
                    "Prepare assistance",
            )
        }
    }

    @Test
    fun `same language rejection is case insensitive`() {
        assertFailsWith<IllegalArgumentException> {
            CrossLanguageLearningAssistanceRecord.create(
                conversationLab =
                    conversationLab(
                        targetLanguage = "Spanish",
                    ),
                supportLanguage = "sPaNiSh",
                assistanceFocus =
                    "Compare expressions",
                assistanceObjective =
                    "Prepare assistance",
            )
        }
    }

    @Test
    fun `cross language assistance rejects blank support language`() {
        assertFailsWith<IllegalArgumentException> {
            CrossLanguageLearningAssistanceRecord.create(
                conversationLab =
                    conversationLab(
                        targetLanguage = "French",
                    ),
                supportLanguage = "   ",
                assistanceFocus =
                    "Compare greetings",
                assistanceObjective =
                    "Prepare assistance",
            )
        }
    }

    @Test
    fun `cross language assistance rejects blank focus`() {
        assertFailsWith<IllegalArgumentException> {
            CrossLanguageLearningAssistanceRecord.create(
                conversationLab =
                    conversationLab(
                        targetLanguage = "French",
                    ),
                supportLanguage = "English",
                assistanceFocus = "   ",
                assistanceObjective =
                    "Prepare assistance",
            )
        }
    }

    @Test
    fun `cross language assistance rejects blank objective`() {
        assertFailsWith<IllegalArgumentException> {
            CrossLanguageLearningAssistanceRecord.create(
                conversationLab =
                    conversationLab(
                        targetLanguage = "French",
                    ),
                supportLanguage = "English",
                assistanceFocus =
                    "Compare greetings",
                assistanceObjective = "   ",
            )
        }
    }

    private fun conversationLab(
        targetLanguage: String,
    ): MultilingualConversationLabRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage141-model:$targetLanguage",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage141-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "Language Education",
                        objective =
                            "Prepare bounded Cross-Language Learning Assistance context.",
                    ),
            )

        val languageSession =
            LanguageEducationSessionRecord.create(
                educationSession = educationSession,
                targetLanguage = targetLanguage,
            )

        val multilingual =
            MultilingualTeachingRecord.create(
                languageEducationSession = languageSession,
                teachingFocus =
                    "Multilingual communication",
                teachingObjective =
                    "Prepare reusable multilingual teaching context",
            )

        return MultilingualConversationLabRecord.create(
            multilingualTeaching = multilingual,
            conversationScenario =
                "Everyday communication",
            conversationObjective =
                "Prepare bounded multilingual conversation practice",
        )
    }
}
