package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage120LanguageEducationFoundationGovernanceTest {

    @Test
    fun `existing bounded education session may enter Language Education foundation`() {
        val traceId =
            TraceId.from(
                "trace-stage120-language-001",
            )

        val educationSession = educationSession()

        val result =
            LanguageEducationFoundationCoordinator().prepare(
                traceId = traceId,
                educationSession = educationSession,
                targetLanguage = "English",
            )

        assertEquals(
            LanguageEducationFoundationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val languageSession =
            requireNotNull(result.languageSession)

        assertSame(
            educationSession,
            languageSession.educationSession,
        )

        assertEquals(
            "English",
            languageSession.targetLanguage,
        )
    }

    @Test
    fun `blank target language remains deferred`() {
        val result =
            LanguageEducationFoundationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage120-language-002",
                    ),
                educationSession = educationSession(),
                targetLanguage = "   ",
            )

        assertEquals(
            LanguageEducationFoundationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.languageSession,
        )
    }

    @Test
    fun `prepared Language Education result requires language session`() {
        assertFailsWith<IllegalArgumentException> {
            LanguageEducationFoundationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage120-language-result-001",
                    ),
                status = LanguageEducationFoundationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred Language Education result cannot smuggle language session`() {
        val languageSession =
            LanguageEducationSessionRecord.create(
                educationSession = educationSession(),
                targetLanguage = "English",
            )

        assertFailsWith<IllegalArgumentException> {
            LanguageEducationFoundationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage120-language-result-002",
                    ),
                status = LanguageEducationFoundationStatus.DEFERRED,
                languageSession = languageSession,
            )
        }
    }

    private fun educationSession(): EducationSessionRecord {
        return EducationSessionRecord.create(
            sessionId =
                EducationSessionId.from(
                    "education-session:stage120-runtime",
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
