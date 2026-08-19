package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.PronunciationPracticeRecord
import com.devil.core.model.education.SpokenEnglishBeginnerSessionRecord
import com.devil.core.model.education.SpokenEnglishConversationPracticeRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage123PronunciationIntelligenceGovernanceTest {

    @Test
    fun `conversation practice may prepare bounded pronunciation context`() {
        val traceId =
            TraceId.from(
                "trace-stage123-pronunciation-001",
            )

        val conversationPractice =
            conversationPractice()

        val result =
            PronunciationIntelligenceCoordinator().prepare(
                traceId = traceId,
                conversationPractice = conversationPractice,
                target = "comfortable",
            )

        assertEquals(
            PronunciationIntelligencePreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val practice =
            requireNotNull(result.practice)

        assertSame(
            conversationPractice,
            practice.conversationPractice,
        )

        assertEquals(
            "comfortable",
            practice.target,
        )
    }

    @Test
    fun `blank pronunciation target remains deferred`() {
        val result =
            PronunciationIntelligenceCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage123-pronunciation-002",
                    ),
                conversationPractice = conversationPractice(),
                target = "   ",
            )

        assertEquals(
            PronunciationIntelligencePreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.practice,
        )
    }

    @Test
    fun `prepared pronunciation result requires practice context`() {
        assertFailsWith<IllegalArgumentException> {
            PronunciationIntelligencePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage123-result-001",
                    ),
                status =
                    PronunciationIntelligencePreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred pronunciation result cannot smuggle practice context`() {
        val practice =
            PronunciationPracticeRecord.create(
                conversationPractice = conversationPractice(),
                target = "world",
            )

        assertFailsWith<IllegalArgumentException> {
            PronunciationIntelligencePreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage123-result-002",
                    ),
                status =
                    PronunciationIntelligencePreparationStatus.DEFERRED,
                practice = practice,
            )
        }
    }

    private fun conversationPractice(): SpokenEnglishConversationPracticeRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage123-runtime",
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
