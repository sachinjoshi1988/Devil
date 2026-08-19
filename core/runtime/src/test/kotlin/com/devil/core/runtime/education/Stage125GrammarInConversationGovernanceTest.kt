package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.GrammarInConversationPracticeRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.SpokenEnglishBeginnerSessionRecord
import com.devil.core.model.education.SpokenEnglishConversationPracticeRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage125GrammarInConversationGovernanceTest {

    @Test
    fun `conversation practice may prepare bounded grammar context`() {
        val traceId =
            TraceId.from(
                "trace-stage125-grammar-001",
            )

        val conversationPractice =
            conversationPractice()

        val result =
            GrammarInConversationCoordinator().prepare(
                traceId = traceId,
                conversationPractice = conversationPractice,
                grammarTarget = "simple present tense",
            )

        assertEquals(
            GrammarInConversationPreparationStatus.PREPARED,
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
            "simple present tense",
            practice.grammarTarget,
        )
    }

    @Test
    fun `blank grammar target remains deferred`() {
        val result =
            GrammarInConversationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage125-grammar-002",
                    ),
                conversationPractice = conversationPractice(),
                grammarTarget = "   ",
            )

        assertEquals(
            GrammarInConversationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.practice,
        )
    }

    @Test
    fun `prepared grammar result requires practice context`() {
        assertFailsWith<IllegalArgumentException> {
            GrammarInConversationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage125-result-001",
                    ),
                status =
                    GrammarInConversationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred grammar result cannot smuggle practice context`() {
        val practice =
            GrammarInConversationPracticeRecord.create(
                conversationPractice = conversationPractice(),
                grammarTarget = "subject-verb agreement",
            )

        assertFailsWith<IllegalArgumentException> {
            GrammarInConversationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage125-result-002",
                    ),
                status =
                    GrammarInConversationPreparationStatus.DEFERRED,
                practice = practice,
            )
        }
    }

    private fun conversationPractice(): SpokenEnglishConversationPracticeRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage125-runtime",
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
