package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
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

class Stage140MultilingualConversationLabGovernanceTest {

    @Test
    fun `multilingual context may prepare bounded conversation lab`() {
        val traceId =
            TraceId.from(
                "trace-stage140-lab-001",
            )

        val multilingual =
            multilingualTeaching(
                targetLanguage = "French",
            )

        val result =
            MultilingualConversationLabCoordinator().prepare(
                traceId = traceId,
                multilingualTeaching = multilingual,
                conversationScenario =
                    "Ordering food at a restaurant",
                conversationObjective =
                    "Prepare bounded multilingual conversation practice",
            )

        assertEquals(
            MultilingualConversationLabPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val lab =
            requireNotNull(result.lab)

        assertSame(
            multilingual,
            lab.multilingualTeaching,
        )

        assertEquals(
            "French",
            lab.multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )
    }

    @Test
    fun `dedicated and additional languages share the same lab architecture`() {
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
                MultilingualConversationLabCoordinator().prepare(
                    traceId =
                        TraceId.from(
                            "trace-stage140-language-${index + 1}",
                        ),
                    multilingualTeaching =
                        multilingualTeaching(
                            targetLanguage = language,
                        ),
                    conversationScenario =
                        "Everyday communication",
                    conversationObjective =
                        "Prepare reusable speaking practice",
                )

            assertEquals(
                MultilingualConversationLabPreparationStatus.PREPARED,
                result.status,
            )

            assertEquals(
                language,
                requireNotNull(result.lab)
                    .multilingualTeaching
                    .languageEducationSession
                    .targetLanguage,
            )
        }
    }

    @Test
    fun `blank conversation scenario remains deferred`() {
        val result =
            MultilingualConversationLabCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage140-lab-002",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "Spanish",
                    ),
                conversationScenario = "   ",
                conversationObjective =
                    "Prepare multilingual conversation practice",
            )

        assertEquals(
            MultilingualConversationLabPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.lab,
        )
    }

    @Test
    fun `blank conversation objective remains deferred`() {
        val result =
            MultilingualConversationLabCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage140-lab-003",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "Japanese",
                    ),
                conversationScenario =
                    "Travel conversation",
                conversationObjective = "   ",
            )

        assertEquals(
            MultilingualConversationLabPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.lab,
        )
    }

    @Test
    fun `prepared lab result requires lab context`() {
        assertFailsWith<IllegalArgumentException> {
            MultilingualConversationLabPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage140-result-001",
                    ),
                status =
                    MultilingualConversationLabPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred lab result cannot smuggle lab context`() {
        val lab =
            MultilingualConversationLabRecord.create(
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "German",
                    ),
                conversationScenario =
                    "Everyday communication",
                conversationObjective =
                    "Prepare conversation practice",
            )

        assertFailsWith<IllegalArgumentException> {
            MultilingualConversationLabPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage140-result-002",
                    ),
                status =
                    MultilingualConversationLabPreparationStatus.DEFERRED,
                lab = lab,
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
                        "education-session:stage140-runtime:$targetLanguage",
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
