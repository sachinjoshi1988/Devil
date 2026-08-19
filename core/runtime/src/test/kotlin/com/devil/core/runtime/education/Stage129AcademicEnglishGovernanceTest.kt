package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.AcademicEnglishPracticeRecord
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

class Stage129AcademicEnglishGovernanceTest {

    @Test
    fun `language education session may prepare bounded academic English context`() {
        val traceId =
            TraceId.from(
                "trace-stage129-academic-001",
            )

        val languageSession = languageSession()

        val result =
            AcademicEnglishCoordinator().prepare(
                traceId = traceId,
                languageEducationSession = languageSession,
                academicTarget = "Structuring a short academic essay",
                academicObjective = "Practice a clear introduction body and conclusion",
            )

        assertEquals(
            AcademicEnglishPreparationStatus.PREPARED,
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
            "Structuring a short academic essay",
            practice.academicTarget,
        )

        assertEquals(
            "Practice a clear introduction body and conclusion",
            practice.academicObjective,
        )
    }

    @Test
    fun `blank academic target remains deferred`() {
        val result =
            AcademicEnglishCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage129-academic-002",
                    ),
                languageEducationSession = languageSession(),
                academicTarget = "   ",
                academicObjective = "Practice formal academic structure.",
            )

        assertEquals(
            AcademicEnglishPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.practice,
        )
    }

    @Test
    fun `blank academic objective remains deferred`() {
        val result =
            AcademicEnglishCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage129-academic-003",
                    ),
                languageEducationSession = languageSession(),
                academicTarget = "Academic paragraph structure",
                academicObjective = "   ",
            )

        assertEquals(
            AcademicEnglishPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.practice,
        )
    }

    @Test
    fun `prepared academic English result requires practice context`() {
        assertFailsWith<IllegalArgumentException> {
            AcademicEnglishPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage129-result-001",
                    ),
                status =
                    AcademicEnglishPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred academic English result cannot smuggle practice context`() {
        val practice =
            AcademicEnglishPracticeRecord.create(
                languageEducationSession = languageSession(),
                academicTarget = "Formal academic vocabulary",
                academicObjective = "Practice appropriate academic phrasing",
            )

        assertFailsWith<IllegalArgumentException> {
            AcademicEnglishPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage129-result-002",
                    ),
                status =
                    AcademicEnglishPreparationStatus.DEFERRED,
                practice = practice,
            )
        }
    }

    private fun languageSession(): LanguageEducationSessionRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage129-runtime",
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
