package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.ListeningComprehensionPracticeRecord
import com.devil.core.model.education.SpokenEnglishBeginnerSessionRecord
import com.devil.core.model.education.SpokenEnglishConversationPracticeRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage124ListeningComprehensionGovernanceTest {

    @Test
    fun `conversation practice may prepare bounded listening comprehension context`() {
        val traceId =
            TraceId.from(
                "trace-stage124-listening-001",
            )

        val conversationPractice = conversationPractice()

        val result =
            ListeningComprehensionCoordinator().prepare(
                traceId = traceId,
                conversationPractice = conversationPractice,
                listeningTarget = "Understand a short self-introduction",
            )

        assertEquals(
            ListeningComprehensionPreparationStatus.PREPARED,
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
            "Understand a short self-introduction",
            practice.listeningTarget,
        )
    }

    @Test
    fun `blank listening target remains deferred`() {
        val result =
            ListeningComprehensionCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage124-listening-002",
                    ),
                conversationPractice = conversationPractice(),
                listeningTarget = "   ",
            )

        assertEquals(
            ListeningComprehensionPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.practice,
        )
    }

    @Test
    fun `prepared listening result requires practice context`() {
        assertFailsWith<IllegalArgumentException> {
            ListeningComprehensionPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage124-result-001",
                    ),
                status =
                    ListeningComprehensionPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred listening result cannot smuggle practice context`() {
        val practice =
            ListeningComprehensionPracticeRecord.create(
                conversationPractice = conversationPractice(),
                listeningTarget = "Understand a greeting",
            )

        assertFailsWith<IllegalArgumentException> {
            ListeningComprehensionPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage124-result-002",
                    ),
                status =
                    ListeningComprehensionPreparationStatus.DEFERRED,
                practice = practice,
            )
        }
    }

    private fun conversationPractice(): SpokenEnglishConversationPracticeRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage124-runtime",
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
