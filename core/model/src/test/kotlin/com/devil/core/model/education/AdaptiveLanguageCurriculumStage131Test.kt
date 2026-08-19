package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class AdaptiveLanguageCurriculumStage131Test {

    @Test
    fun `adaptive curriculum preserves language session and normalizes explicit inputs`() {
        val languageSession = languageSession()

        val record =
            AdaptiveLanguageCurriculumRecord.create(
                languageEducationSession = languageSession,
                curriculumFocus = "  Increase conversational practice with workplace vocabulary  ",
                adaptationRationale = "  Learner requested more practical speaking-oriented English  ",
            )

        assertSame(
            languageSession,
            record.languageEducationSession,
        )

        assertEquals(
            "Increase conversational practice with workplace vocabulary",
            record.curriculumFocus,
        )

        assertEquals(
            "Learner requested more practical speaking-oriented English",
            record.adaptationRationale,
        )
    }

    @Test
    fun `adaptive curriculum rejects blank curriculum focus`() {
        assertFailsWith<IllegalArgumentException> {
            AdaptiveLanguageCurriculumRecord.create(
                languageEducationSession = languageSession(),
                curriculumFocus = "   ",
                adaptationRationale = "Learner explicitly requested more speaking practice.",
            )
        }
    }

    @Test
    fun `adaptive curriculum rejects blank adaptation rationale`() {
        assertFailsWith<IllegalArgumentException> {
            AdaptiveLanguageCurriculumRecord.create(
                languageEducationSession = languageSession(),
                curriculumFocus = "Workplace speaking practice",
                adaptationRationale = "   ",
            )
        }
    }

    private fun languageSession(): LanguageEducationSessionRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage131-model",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage131-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "English",
                        objective = "Prepare a bounded adaptive language curriculum context.",
                    ),
            )

        return LanguageEducationSessionRecord.create(
            educationSession = educationSession,
            targetLanguage = "English",
        )
    }
}
