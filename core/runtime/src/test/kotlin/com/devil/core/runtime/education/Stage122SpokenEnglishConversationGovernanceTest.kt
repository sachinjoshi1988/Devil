package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.SpokenEnglishBeginnerSessionRecord
import com.devil.core.model.education.SpokenEnglishConversationPracticeRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage122SpokenEnglishConversationGovernanceTest {

    @Test
    fun `beginner Spoken English session may prepare bounded conversation practice`() {
        val traceId =
            TraceId.from(
                "trace-stage122-conversation-001",
            )

        val beginnerSession = beginnerSession()

        val result =
            SpokenEnglishConversationCoordinator().prepare(
                traceId = traceId,
                beginnerSession = beginnerSession,
                topic = "Introducing yourself",
            )

        assertEquals(
            SpokenEnglishConversationPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val practice =
            requireNotNull(result.practice)

        assertSame(
            beginnerSession,
            practice.beginnerSession,
        )

        assertEquals(
            "Introducing yourself",
            practice.topic,
        )
    }

    @Test
    fun `blank conversation topic remains deferred`() {
        val result =
            SpokenEnglishConversationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage122-conversation-002",
                    ),
                beginnerSession = beginnerSession(),
                topic = "   ",
            )

        assertEquals(
            SpokenEnglishConversationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.practice,
        )
    }

    @Test
    fun `prepared conversation result requires practice context`() {
        assertFailsWith<IllegalArgumentException> {
            SpokenEnglishConversationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage122-result-001",
                    ),
                status =
                    SpokenEnglishConversationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred conversation result cannot smuggle practice context`() {
        val practice =
            SpokenEnglishConversationPracticeRecord.create(
                beginnerSession = beginnerSession(),
                topic = "Daily routine",
            )

        assertFailsWith<IllegalArgumentException> {
            SpokenEnglishConversationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage122-result-002",
                    ),
                status =
                    SpokenEnglishConversationPreparationStatus.DEFERRED,
                practice = practice,
            )
        }
    }

    private fun beginnerSession(): SpokenEnglishBeginnerSessionRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage122-runtime",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage122-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "English",
                        objective = "Practice beginner spoken English.",
                    ),
            )

        val languageSession =
            LanguageEducationSessionRecord.create(
                educationSession = educationSession,
                targetLanguage = "English",
            )

        return SpokenEnglishBeginnerSessionRecord.create(
            languageEducationSession = languageSession,
        )
    }
}
