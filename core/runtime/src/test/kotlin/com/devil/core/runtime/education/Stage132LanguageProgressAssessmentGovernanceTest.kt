package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.LanguageProgressAssessmentRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage132LanguageProgressAssessmentGovernanceTest {

    @Test
    fun `language education session may prepare bounded language progress assessment`() {
        val traceId =
            TraceId.from(
                "trace-stage132-assessment-001",
            )

        val languageSession = languageSession()

        val result =
            LanguageProgressAssessmentCoordinator().prepare(
                traceId = traceId,
                languageEducationSession = languageSession,
                assessmentFocus = "Workplace speaking clarity",
                learnerEvidence =
                    "Learner completed three supplied role-play responses with fewer pauses",
                assessmentInterpretation =
                    "Evidence suggests improvement in fluency for this bounded practice context",
            )

        assertEquals(
            LanguageProgressAssessmentPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val assessment =
            requireNotNull(result.assessment)

        assertSame(
            languageSession,
            assessment.languageEducationSession,
        )

        assertEquals(
            "Workplace speaking clarity",
            assessment.assessmentFocus,
        )

        assertEquals(
            "Learner completed three supplied role-play responses with fewer pauses",
            assessment.learnerEvidence,
        )

        assertEquals(
            "Evidence suggests improvement in fluency for this bounded practice context",
            assessment.assessmentInterpretation,
        )
    }

    @Test
    fun `blank assessment focus remains deferred`() {
        val result =
            LanguageProgressAssessmentCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage132-assessment-002",
                    ),
                languageEducationSession = languageSession(),
                assessmentFocus = "   ",
                learnerEvidence = "Learner completed a bounded exercise.",
                assessmentInterpretation =
                    "Evidence suggests stable performance in this context.",
            )

        assertEquals(
            LanguageProgressAssessmentPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.assessment,
        )
    }

    @Test
    fun `blank learner evidence remains deferred`() {
        val result =
            LanguageProgressAssessmentCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage132-assessment-003",
                    ),
                languageEducationSession = languageSession(),
                assessmentFocus = "Speaking fluency",
                learnerEvidence = "   ",
                assessmentInterpretation =
                    "Evidence suggests improvement in this bounded context.",
            )

        assertEquals(
            LanguageProgressAssessmentPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.assessment,
        )
    }

    @Test
    fun `blank assessment interpretation remains deferred`() {
        val result =
            LanguageProgressAssessmentCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage132-assessment-004",
                    ),
                languageEducationSession = languageSession(),
                assessmentFocus = "Speaking fluency",
                learnerEvidence = "Learner completed a bounded speaking exercise.",
                assessmentInterpretation = "   ",
            )

        assertEquals(
            LanguageProgressAssessmentPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.assessment,
        )
    }

    @Test
    fun `prepared language progress assessment requires assessment context`() {
        assertFailsWith<IllegalArgumentException> {
            LanguageProgressAssessmentPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage132-result-001",
                    ),
                status =
                    LanguageProgressAssessmentPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred language progress assessment cannot smuggle assessment context`() {
        val assessment =
            LanguageProgressAssessmentRecord.create(
                languageEducationSession = languageSession(),
                assessmentFocus = "Vocabulary recall",
                learnerEvidence =
                    "Learner supplied correct responses in this bounded exercise.",
                assessmentInterpretation =
                    "Evidence suggests stable recall in this bounded context.",
            )

        assertFailsWith<IllegalArgumentException> {
            LanguageProgressAssessmentPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage132-result-002",
                    ),
                status =
                    LanguageProgressAssessmentPreparationStatus.DEFERRED,
                assessment = assessment,
            )
        }
    }

    private fun languageSession(): LanguageEducationSessionRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage132-runtime",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage132-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "English",
                        objective = "Prepare a bounded language progress assessment context.",
                    ),
            )

        return LanguageEducationSessionRecord.create(
            educationSession = educationSession,
            targetLanguage = "English",
        )
    }
}
