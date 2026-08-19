package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class MultilingualTeachingStage133Test {

    @Test
    fun `multilingual teaching preserves language session and normalizes explicit inputs`() {
        val languageSession = languageSession(
            targetLanguage = "French",
        )

        val record =
            MultilingualTeachingRecord.create(
                languageEducationSession = languageSession,
                teachingFocus = "  Everyday beginner communication  ",
                teachingObjective = "  Build a reusable multilingual teaching context  ",
            )

        assertSame(
            languageSession,
            record.languageEducationSession,
        )

        assertEquals(
            "French",
            record.languageEducationSession.targetLanguage,
        )

        assertEquals(
            "Everyday beginner communication",
            record.teachingFocus,
        )

        assertEquals(
            "Build a reusable multilingual teaching context",
            record.teachingObjective,
        )
    }

    @Test
    fun `multilingual teaching remains language neutral`() {
        val languageSession = languageSession(
            targetLanguage = "Japanese",
        )

        val record =
            MultilingualTeachingRecord.create(
                languageEducationSession = languageSession,
                teachingFocus = "Beginner communication",
                teachingObjective = "Prepare language-neutral teaching architecture",
            )

        assertEquals(
            "Japanese",
            record.languageEducationSession.targetLanguage,
        )
    }

    @Test
    fun `multilingual teaching rejects blank focus`() {
        assertFailsWith<IllegalArgumentException> {
            MultilingualTeachingRecord.create(
                languageEducationSession =
                    languageSession(
                        targetLanguage = "German",
                    ),
                teachingFocus = "   ",
                teachingObjective = "Prepare beginner teaching context.",
            )
        }
    }

    @Test
    fun `multilingual teaching rejects blank objective`() {
        assertFailsWith<IllegalArgumentException> {
            MultilingualTeachingRecord.create(
                languageEducationSession =
                    languageSession(
                        targetLanguage = "Spanish",
                    ),
                teachingFocus = "Beginner communication",
                teachingObjective = "   ",
            )
        }
    }

    private fun languageSession(
        targetLanguage: String,
    ): LanguageEducationSessionRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage133-model:$targetLanguage",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage133-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "Language Education",
                        objective = "Prepare multilingual teaching architecture.",
                    ),
            )

        return LanguageEducationSessionRecord.create(
            educationSession = educationSession,
            targetLanguage = targetLanguage,
        )
    }
}
