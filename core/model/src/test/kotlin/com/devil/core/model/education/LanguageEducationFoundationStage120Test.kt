package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class LanguageEducationFoundationStage120Test {

    @Test
    fun `language education record preserves existing education session and normalizes target language`() {
        val educationSession = educationSession()

        val record =
            LanguageEducationSessionRecord.create(
                educationSession = educationSession,
                targetLanguage = "  English  ",
            )

        assertSame(
            educationSession,
            record.educationSession,
        )

        assertEquals(
            "English",
            record.targetLanguage,
        )
    }

    @Test
    fun `language education record rejects blank target language`() {
        assertFailsWith<IllegalArgumentException> {
            LanguageEducationSessionRecord.create(
                educationSession = educationSession(),
                targetLanguage = "   ",
            )
        }
    }

    private fun educationSession(): EducationSessionRecord {
        return EducationSessionRecord.create(
            sessionId =
                EducationSessionId.from(
                    "education-session:stage120-model",
                ),
            subjectIdentityId =
                IdentityId.from(
                    "identity:stage120-learner",
                ),
            objective =
                EducationObjective.create(
                    subject = "English",
                    objective = "Develop practical English communication.",
                ),
        )
    }
}
