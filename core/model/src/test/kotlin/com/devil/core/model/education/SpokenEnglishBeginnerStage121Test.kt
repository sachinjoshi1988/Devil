package com.devil.core.model.education

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class SpokenEnglishBeginnerStage121Test {

    @Test
    fun `beginner Spoken English preserves existing English language session`() {
        val languageSession = languageSession("English")

        val record =
            SpokenEnglishBeginnerSessionRecord.create(
                languageEducationSession = languageSession,
            )

        assertSame(
            languageSession,
            record.languageEducationSession,
        )
    }

    @Test
    fun `beginner Spoken English rejects non-English language session`() {
        assertFailsWith<IllegalArgumentException> {
            SpokenEnglishBeginnerSessionRecord.create(
                languageEducationSession = languageSession("French"),
            )
        }
    }

    private fun languageSession(
        targetLanguage: String,
    ): LanguageEducationSessionRecord {
        return LanguageEducationSessionRecord.create(
            educationSession =
                EducationSessionRecord.create(
                    sessionId =
                        EducationSessionId.from(
                            "education-session:stage121-model:$targetLanguage",
                        ),
                    subjectIdentityId =
                        IdentityId.from(
                            "identity:stage121-learner",
                        ),
                    objective =
                        EducationObjective.create(
                            subject = targetLanguage,
                            objective = "Develop practical communication.",
                        ),
                ),
            targetLanguage = targetLanguage,
        )
    }
}
