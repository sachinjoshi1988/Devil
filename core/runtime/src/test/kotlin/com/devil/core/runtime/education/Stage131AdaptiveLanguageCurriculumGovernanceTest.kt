package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.AdaptiveLanguageCurriculumRecord
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

class Stage131AdaptiveLanguageCurriculumGovernanceTest {

    @Test
    fun `language education session may prepare bounded adaptive curriculum from explicit inputs`() {
        val traceId =
            TraceId.from(
                "trace-stage131-curriculum-001",
            )

        val languageSession = languageSession()

        val result =
            AdaptiveLanguageCurriculumCoordinator().prepare(
                traceId = traceId,
                languageEducationSession = languageSession,
                curriculumFocus =
                    "Increase conversational practice with workplace vocabulary",
                adaptationRationale =
                    "Learner requested more practical speaking-oriented English",
            )

        assertEquals(
            AdaptiveLanguageCurriculumPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val curriculum =
            requireNotNull(result.curriculum)

        assertSame(
            languageSession,
            curriculum.languageEducationSession,
        )

        assertEquals(
            "Increase conversational practice with workplace vocabulary",
            curriculum.curriculumFocus,
        )

        assertEquals(
            "Learner requested more practical speaking-oriented English",
            curriculum.adaptationRationale,
        )
    }

    @Test
    fun `blank curriculum focus remains deferred`() {
        val result =
            AdaptiveLanguageCurriculumCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage131-curriculum-002",
                    ),
                languageEducationSession = languageSession(),
                curriculumFocus = "   ",
                adaptationRationale =
                    "Learner explicitly requested more speaking practice.",
            )

        assertEquals(
            AdaptiveLanguageCurriculumPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.curriculum,
        )
    }

    @Test
    fun `blank adaptation rationale remains deferred`() {
        val result =
            AdaptiveLanguageCurriculumCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage131-curriculum-003",
                    ),
                languageEducationSession = languageSession(),
                curriculumFocus = "Workplace speaking practice",
                adaptationRationale = "   ",
            )

        assertEquals(
            AdaptiveLanguageCurriculumPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.curriculum,
        )
    }

    @Test
    fun `prepared adaptive curriculum result requires curriculum context`() {
        assertFailsWith<IllegalArgumentException> {
            AdaptiveLanguageCurriculumPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage131-result-001",
                    ),
                status =
                    AdaptiveLanguageCurriculumPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred adaptive curriculum result cannot smuggle curriculum context`() {
        val curriculum =
            AdaptiveLanguageCurriculumRecord.create(
                languageEducationSession = languageSession(),
                curriculumFocus = "Daily speaking practice",
                adaptationRationale = "Learner explicitly requested daily speaking practice",
            )

        assertFailsWith<IllegalArgumentException> {
            AdaptiveLanguageCurriculumPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage131-result-002",
                    ),
                status =
                    AdaptiveLanguageCurriculumPreparationStatus.DEFERRED,
                curriculum = curriculum,
            )
        }
    }

    private fun languageSession(): LanguageEducationSessionRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage131-runtime",
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
