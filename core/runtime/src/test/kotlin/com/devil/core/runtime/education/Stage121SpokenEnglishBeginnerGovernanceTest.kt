package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.SpokenEnglishBeginnerSessionRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage121SpokenEnglishBeginnerGovernanceTest {

    @Test
    fun `English language education session may enter beginner Spoken English context`() {
        val traceId =
            TraceId.from(
                "trace-stage121-spoken-english-001",
            )

        val languageSession =
            languageSession("English")

        val result =
            SpokenEnglishBeginnerCoordinator().prepare(
                traceId = traceId,
                languageEducationSession = languageSession,
            )

        assertEquals(
            SpokenEnglishBeginnerPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val beginnerSession =
            requireNotNull(result.beginnerSession)

        assertSame(
            languageSession,
            beginnerSession.languageEducationSession,
        )
    }

    @Test
    fun `non-English language session remains deferred`() {
        val result =
            SpokenEnglishBeginnerCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage121-spoken-english-002",
                    ),
                languageEducationSession = languageSession("French"),
            )

        assertEquals(
            SpokenEnglishBeginnerPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.beginnerSession,
        )
    }

    @Test
    fun `prepared beginner result requires beginner session`() {
        assertFailsWith<IllegalArgumentException> {
            SpokenEnglishBeginnerPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage121-result-001",
                    ),
                status =
                    SpokenEnglishBeginnerPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred beginner result cannot smuggle beginner session`() {
        val beginnerSession =
            SpokenEnglishBeginnerSessionRecord.create(
                languageEducationSession = languageSession("English"),
            )

        assertFailsWith<IllegalArgumentException> {
            SpokenEnglishBeginnerPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage121-result-002",
                    ),
                status =
                    SpokenEnglishBeginnerPreparationStatus.DEFERRED,
                beginnerSession = beginnerSession,
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
                            "education-session:stage121-runtime:$targetLanguage",
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
