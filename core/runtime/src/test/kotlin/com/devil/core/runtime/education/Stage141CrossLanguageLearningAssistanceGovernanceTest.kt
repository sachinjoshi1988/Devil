package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.CrossLanguageLearningAssistanceRecord
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.MultilingualConversationLabRecord
import com.devil.core.model.education.MultilingualTeachingRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage141CrossLanguageLearningAssistanceGovernanceTest {

    @Test
    fun `conversation lab may prepare bounded cross language assistance`() {
        val traceId =
            TraceId.from(
                "trace-stage141-cross-language-001",
            )

        val lab =
            conversationLab(
                targetLanguage = "French",
            )

        val result =
            CrossLanguageLearningAssistanceCoordinator().prepare(
                traceId = traceId,
                conversationLab = lab,
                supportLanguage = "English",
                assistanceFocus =
                    "Compare everyday greeting structures",
                assistanceObjective =
                    "Prepare cross-language explanation practice",
            )

        assertEquals(
            CrossLanguageLearningAssistancePreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val assistance =
            requireNotNull(result.assistance)

        assertSame(
            lab,
            assistance.conversationLab,
        )

        assertEquals(
            "French",
            assistance.conversationLab
                .multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )

        assertEquals(
            "English",
            assistance.supportLanguage,
        )
    }

    @Test
    fun `dedicated and additional target languages share the same assistance architecture`() {
        listOf(
            "French",
            "German",
            "Spanish",
            "Russian",
            "Mandarin Chinese",
            "Japanese",
            "Italian",
        ).forEachIndexed { index, language ->
            val result =
                CrossLanguageLearningAssistanceCoordinator().prepare(
                    traceId =
                        TraceId.from(
                            "trace-stage141-language-${index + 1}",
                        ),
                    conversationLab =
                        conversationLab(
                            targetLanguage = language,
                        ),
                    supportLanguage = "English",
                    assistanceFocus =
                        "Compare everyday expressions",
                    assistanceObjective =
                        "Prepare reusable cross-language assistance",
                )

            if (language.equals("English", ignoreCase = true)) {
                assertEquals(
                    CrossLanguageLearningAssistancePreparationStatus.DEFERRED,
                    result.status,
                )
            } else {
                assertEquals(
                    CrossLanguageLearningAssistancePreparationStatus.PREPARED,
                    result.status,
                )

                assertEquals(
                    language,
                    requireNotNull(result.assistance)
                        .conversationLab
                        .multilingualTeaching
                        .languageEducationSession
                        .targetLanguage,
                )
            }
        }
    }

    @Test
    fun `same support and target language remains deferred`() {
        val result =
            CrossLanguageLearningAssistanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage141-cross-language-002",
                    ),
                conversationLab =
                    conversationLab(
                        targetLanguage = "German",
                    ),
                supportLanguage = "German",
                assistanceFocus =
                    "Compare expressions",
                assistanceObjective =
                    "Prepare learning assistance",
            )

        assertEquals(
            CrossLanguageLearningAssistancePreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.assistance,
        )
    }

    @Test
    fun `same support and target language exclusion is case insensitive`() {
        val result =
            CrossLanguageLearningAssistanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage141-cross-language-003",
                    ),
                conversationLab =
                    conversationLab(
                        targetLanguage = "Spanish",
                    ),
                supportLanguage = "sPaNiSh",
                assistanceFocus =
                    "Compare expressions",
                assistanceObjective =
                    "Prepare learning assistance",
            )

        assertEquals(
            CrossLanguageLearningAssistancePreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.assistance,
        )
    }

    @Test
    fun `blank support language remains deferred`() {
        val result =
            CrossLanguageLearningAssistanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage141-cross-language-004",
                    ),
                conversationLab =
                    conversationLab(
                        targetLanguage = "Japanese",
                    ),
                supportLanguage = "   ",
                assistanceFocus =
                    "Compare greetings",
                assistanceObjective =
                    "Prepare learning assistance",
            )

        assertEquals(
            CrossLanguageLearningAssistancePreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.assistance,
        )
    }

    @Test
    fun `blank assistance focus remains deferred`() {
        val result =
            CrossLanguageLearningAssistanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage141-cross-language-005",
                    ),
                conversationLab =
                    conversationLab(
                        targetLanguage = "French",
                    ),
                supportLanguage = "English",
                assistanceFocus = "   ",
                assistanceObjective =
                    "Prepare learning assistance",
            )

        assertEquals(
            CrossLanguageLearningAssistancePreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.assistance,
        )
    }

    @Test
    fun `blank assistance objective remains deferred`() {
        val result =
            CrossLanguageLearningAssistanceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage141-cross-language-006",
                    ),
                conversationLab =
                    conversationLab(
                        targetLanguage = "French",
                    ),
                supportLanguage = "English",
                assistanceFocus =
                    "Compare greetings",
                assistanceObjective = "   ",
            )

        assertEquals(
            CrossLanguageLearningAssistancePreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.assistance,
        )
    }

    @Test
    fun `prepared cross language assistance result requires assistance context`() {
        assertFailsWith<IllegalArgumentException> {
            CrossLanguageLearningAssistancePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage141-result-001",
                    ),
                status =
                    CrossLanguageLearningAssistancePreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred cross language assistance result cannot smuggle assistance context`() {
        val assistance =
            CrossLanguageLearningAssistanceRecord.create(
                conversationLab =
                    conversationLab(
                        targetLanguage = "French",
                    ),
                supportLanguage = "English",
                assistanceFocus =
                    "Compare greetings",
                assistanceObjective =
                    "Prepare learning assistance",
            )

        assertFailsWith<IllegalArgumentException> {
            CrossLanguageLearningAssistancePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage141-result-002",
                    ),
                status =
                    CrossLanguageLearningAssistancePreparationStatus.DEFERRED,
                assistance = assistance,
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
                        "education-session:stage141-runtime:$targetLanguage",
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
