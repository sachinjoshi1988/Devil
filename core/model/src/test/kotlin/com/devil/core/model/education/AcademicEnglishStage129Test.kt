package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class AcademicEnglishStage129Test {

    @Test
    fun `academic English practice preserves language session and normalizes inputs`() {
        val languageSession = languageSession()

        val record =
            AcademicEnglishPracticeRecord.create(
                languageEducationSession = languageSession,
                academicTarget = "  Structuring a short academic essay  ",
                academicObjective = "  Practice a clear introduction body and conclusion  ",
            )

        assertSame(
            languageSession,
            record.languageEducationSession,
        )

        assertEquals(
            "Structuring a short academic essay",
            record.academicTarget,
        )

        assertEquals(
            "Practice a clear introduction body and conclusion",
            record.academicObjective,
        )
    }

    @Test
    fun `academic English practice rejects blank academic target`() {
        assertFailsWith<IllegalArgumentException> {
            AcademicEnglishPracticeRecord.create(
                languageEducationSession = languageSession(),
                academicTarget = "   ",
                academicObjective = "Practice formal academic structure.",
            )
        }
    }

    @Test
    fun `academic English practice rejects blank academic objective`() {
        assertFailsWith<IllegalArgumentException> {
            AcademicEnglishPracticeRecord.create(
                languageEducationSession = languageSession(),
                academicTarget = "Academic paragraph structure",
                academicObjective = "   ",
            )
        }
    }

    private fun languageSession(): LanguageEducationSessionRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage129-model",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage129-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "English",
                        objective = "Develop bounded Academic English skills.",
                    ),
            )

        return LanguageEducationSessionRecord.create(
            educationSession = educationSession,
            targetLanguage = "English",
        )
    }
}
