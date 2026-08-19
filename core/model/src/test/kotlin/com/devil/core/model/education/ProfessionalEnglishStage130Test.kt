package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class ProfessionalEnglishStage130Test {

    @Test
    fun `professional English practice preserves language session and normalizes inputs`() {
        val languageSession = languageSession()

        val record =
            ProfessionalEnglishPracticeRecord.create(
                languageEducationSession = languageSession,
                professionalTarget = "  Participating in a workplace meeting  ",
                professionalObjective = "  Practice concise and professional responses  ",
            )

        assertSame(
            languageSession,
            record.languageEducationSession,
        )

        assertEquals(
            "Participating in a workplace meeting",
            record.professionalTarget,
        )

        assertEquals(
            "Practice concise and professional responses",
            record.professionalObjective,
        )
    }

    @Test
    fun `professional English practice rejects blank professional target`() {
        assertFailsWith<IllegalArgumentException> {
            ProfessionalEnglishPracticeRecord.create(
                languageEducationSession = languageSession(),
                professionalTarget = "   ",
                professionalObjective = "Practice professional responses.",
            )
        }
    }

    @Test
    fun `professional English practice rejects blank professional objective`() {
        assertFailsWith<IllegalArgumentException> {
            ProfessionalEnglishPracticeRecord.create(
                languageEducationSession = languageSession(),
                professionalTarget = "Workplace meeting communication",
                professionalObjective = "   ",
            )
        }
    }

    private fun languageSession(): LanguageEducationSessionRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage130-model",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage130-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "English",
                        objective = "Develop bounded Professional English skills.",
                    ),
            )

        return LanguageEducationSessionRecord.create(
            educationSession = educationSession,
            targetLanguage = "English",
        )
    }
}
