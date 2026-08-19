package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class LanguageProgressAssessmentStage132Test {

    @Test
    fun `language progress assessment preserves session and normalizes explicit inputs`() {
        val languageSession = languageSession()

        val record =
            LanguageProgressAssessmentRecord.create(
                languageEducationSession = languageSession,
                assessmentFocus = "  Workplace speaking clarity  ",
                learnerEvidence =
                    "  Learner completed three supplied role-play responses with fewer pauses  ",
                assessmentInterpretation =
                    "  Evidence suggests improvement in fluency for this bounded practice context  ",
            )

        assertSame(
            languageSession,
            record.languageEducationSession,
        )

        assertEquals(
            "Workplace speaking clarity",
            record.assessmentFocus,
        )

        assertEquals(
            "Learner completed three supplied role-play responses with fewer pauses",
            record.learnerEvidence,
        )

        assertEquals(
            "Evidence suggests improvement in fluency for this bounded practice context",
            record.assessmentInterpretation,
        )
    }

    @Test
    fun `language progress assessment rejects blank assessment focus`() {
        assertFailsWith<IllegalArgumentException> {
            LanguageProgressAssessmentRecord.create(
                languageEducationSession = languageSession(),
                assessmentFocus = "   ",
                learnerEvidence = "Learner completed a bounded exercise.",
                assessmentInterpretation =
                    "Evidence suggests stable performance in this context.",
            )
        }
    }

    @Test
    fun `language progress assessment rejects blank learner evidence`() {
        assertFailsWith<IllegalArgumentException> {
            LanguageProgressAssessmentRecord.create(
                languageEducationSession = languageSession(),
                assessmentFocus = "Speaking fluency",
                learnerEvidence = "   ",
                assessmentInterpretation =
                    "Evidence suggests improvement in this bounded context.",
            )
        }
    }

    @Test
    fun `language progress assessment rejects blank interpretation`() {
        assertFailsWith<IllegalArgumentException> {
            LanguageProgressAssessmentRecord.create(
                languageEducationSession = languageSession(),
                assessmentFocus = "Speaking fluency",
                learnerEvidence = "Learner completed a bounded speaking exercise.",
                assessmentInterpretation = "   ",
            )
        }
    }

    private fun languageSession(): LanguageEducationSessionRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage132-model",
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
