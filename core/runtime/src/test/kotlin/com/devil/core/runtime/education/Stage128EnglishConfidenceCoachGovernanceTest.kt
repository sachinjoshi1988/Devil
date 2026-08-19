package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.EnglishConfidenceCoachPracticeRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage128EnglishConfidenceCoachGovernanceTest {

    @Test
    fun `language education session may prepare bounded confidence coaching context`() {
        val traceId =
            TraceId.from(
                "trace-stage128-confidence-001",
            )

        val languageSession = languageSession()

        val result =
            EnglishConfidenceCoachCoordinator().prepare(
                traceId = traceId,
                languageEducationSession = languageSession,
                confidenceTarget = "Speaking English during a work meeting",
                coachingObjective = "Practice responding calmly and clearly",
            )

        assertEquals(
            EnglishConfidenceCoachPreparationStatus.PREPARED,
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
            "Speaking English during a work meeting",
            practice.confidenceTarget,
        )

        assertEquals(
            "Practice responding calmly and clearly",
            practice.coachingObjective,
        )
    }

    @Test
    fun `blank confidence target remains deferred`() {
        val result =
            EnglishConfidenceCoachCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage128-confidence-002",
                    ),
                languageEducationSession = languageSession(),
                confidenceTarget = "   ",
                coachingObjective = "Practice clear responses.",
            )

        assertEquals(
            EnglishConfidenceCoachPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.practice,
        )
    }

    @Test
    fun `blank coaching objective remains deferred`() {
        val result =
            EnglishConfidenceCoachCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage128-confidence-003",
                    ),
                languageEducationSession = languageSession(),
                confidenceTarget = "Speaking in class",
                coachingObjective = "   ",
            )

        assertEquals(
            EnglishConfidenceCoachPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.practice,
        )
    }

    @Test
    fun `prepared confidence coach result requires practice context`() {
        assertFailsWith<IllegalArgumentException> {
            EnglishConfidenceCoachPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage128-result-001",
                    ),
                status =
                    EnglishConfidenceCoachPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred confidence coach result cannot smuggle practice context`() {
        val practice =
            EnglishConfidenceCoachPracticeRecord.create(
                languageEducationSession = languageSession(),
                confidenceTarget = "Introducing yourself",
                coachingObjective = "Practice speaking clearly",
            )

        assertFailsWith<IllegalArgumentException> {
            EnglishConfidenceCoachPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage128-result-002",
                    ),
                status =
                    EnglishConfidenceCoachPreparationStatus.DEFERRED,
                practice = practice,
            )
        }
    }

    private fun languageSession(): LanguageEducationSessionRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage128-runtime",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage128-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "English",
                        objective = "Build confidence using English in practical situations.",
                    ),
            )

        return LanguageEducationSessionRecord.create(
            educationSession = educationSession,
            targetLanguage = "English",
        )
    }
}
