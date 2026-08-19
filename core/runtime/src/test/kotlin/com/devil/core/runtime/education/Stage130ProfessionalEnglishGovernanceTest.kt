package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.ProfessionalEnglishPracticeRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage130ProfessionalEnglishGovernanceTest {

    @Test
    fun `language education session may prepare bounded professional English context`() {
        val traceId =
            TraceId.from(
                "trace-stage130-professional-001",
            )

        val languageSession = languageSession()

        val result =
            ProfessionalEnglishCoordinator().prepare(
                traceId = traceId,
                languageEducationSession = languageSession,
                professionalTarget = "Participating in a workplace meeting",
                professionalObjective = "Practice concise and professional responses",
            )

        assertEquals(
            ProfessionalEnglishPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val practice =
            requireNotNull(result.practice)

        assertSame(
            languageSession,
            practice.languageEducationSession,
        )

        assertEquals(
            "Participating in a workplace meeting",
            practice.professionalTarget,
        )

        assertEquals(
            "Practice concise and professional responses",
            practice.professionalObjective,
        )
    }

    @Test
    fun `blank professional target remains deferred`() {
        val result =
            ProfessionalEnglishCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage130-professional-002",
                    ),
                languageEducationSession = languageSession(),
                professionalTarget = "   ",
                professionalObjective = "Practice professional responses.",
            )

        assertEquals(
            ProfessionalEnglishPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.practice,
        )
    }

    @Test
    fun `blank professional objective remains deferred`() {
        val result =
            ProfessionalEnglishCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage130-professional-003",
                    ),
                languageEducationSession = languageSession(),
                professionalTarget = "Workplace meeting communication",
                professionalObjective = "   ",
            )

        assertEquals(
            ProfessionalEnglishPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.practice,
        )
    }

    @Test
    fun `prepared professional English result requires practice context`() {
        assertFailsWith<IllegalArgumentException> {
            ProfessionalEnglishPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage130-result-001",
                    ),
                status =
                    ProfessionalEnglishPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred professional English result cannot smuggle practice context`() {
        val practice =
            ProfessionalEnglishPracticeRecord.create(
                languageEducationSession = languageSession(),
                professionalTarget = "Professional email phrasing",
                professionalObjective = "Practice clear formal wording",
            )

        assertFailsWith<IllegalArgumentException> {
            ProfessionalEnglishPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage130-result-002",
                    ),
                status =
                    ProfessionalEnglishPreparationStatus.DEFERRED,
                practice = practice,
            )
        }
    }

    private fun languageSession(): LanguageEducationSessionRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage130-runtime",
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
