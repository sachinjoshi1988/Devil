package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.WritingCommunicationPracticeRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage127WritingCommunicationGovernanceTest {

    @Test
    fun `language education session may prepare bounded writing communication context`() {
        val traceId =
            TraceId.from(
                "trace-stage127-writing-001",
            )

        val languageSession = languageSession()

        val result =
            WritingCommunicationCoordinator().prepare(
                traceId = traceId,
                languageEducationSession = languageSession,
                writingTarget = "Write a short self-introduction",
                communicationPurpose = "Clear everyday communication",
            )

        assertEquals(
            WritingCommunicationPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val practice =
            requireNotNull(result.practice)

        assertSame(
            languageSession,
            practice.languageEducationSession,
        )

        assertEquals(
            "Write a short self-introduction",
            practice.writingTarget,
        )

        assertEquals(
            "Clear everyday communication",
            practice.communicationPurpose,
        )
    }

    @Test
    fun `blank writing target remains deferred`() {
        val result =
            WritingCommunicationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage127-writing-002",
                    ),
                languageEducationSession = languageSession(),
                writingTarget = "   ",
                communicationPurpose = "Everyday communication",
            )

        assertEquals(
            WritingCommunicationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.practice,
        )
    }

    @Test
    fun `blank communication purpose remains deferred`() {
        val result =
            WritingCommunicationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage127-writing-003",
                    ),
                languageEducationSession = languageSession(),
                writingTarget = "Write a short paragraph.",
                communicationPurpose = "   ",
            )

        assertEquals(
            WritingCommunicationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.practice,
        )
    }

    @Test
    fun `prepared writing result requires practice context`() {
        assertFailsWith<IllegalArgumentException> {
            WritingCommunicationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage127-result-001",
                    ),
                status =
                    WritingCommunicationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred writing result cannot smuggle practice context`() {
        val practice =
            WritingCommunicationPracticeRecord.create(
                languageEducationSession = languageSession(),
                writingTarget = "Write a short note.",
                communicationPurpose = "Practice clear written communication",
            )

        assertFailsWith<IllegalArgumentException> {
            WritingCommunicationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage127-result-002",
                    ),
                status =
                    WritingCommunicationPreparationStatus.DEFERRED,
                practice = practice,
            )
        }
    }

    private fun languageSession(): LanguageEducationSessionRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage127-runtime",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage127-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "English",
                        objective = "Develop practical writing and communication.",
                    ),
            )

        return LanguageEducationSessionRecord.create(
            educationSession = educationSession,
            targetLanguage = "English",
        )
    }
}
