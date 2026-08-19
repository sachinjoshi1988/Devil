package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.MultilingualTeachingRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage133MultilingualTeachingGovernanceTest {

    @Test
    fun `language education session may prepare bounded multilingual teaching context`() {
        val traceId =
            TraceId.from(
                "trace-stage133-multilingual-001",
            )

        val languageSession =
            languageSession(
                targetLanguage = "French",
            )

        val result =
            MultilingualTeachingCoordinator().prepare(
                traceId = traceId,
                languageEducationSession = languageSession,
                teachingFocus = "Everyday beginner communication",
                teachingObjective =
                    "Build a reusable multilingual teaching context",
            )

        assertEquals(
            MultilingualTeachingPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val teaching =
            requireNotNull(result.teaching)

        assertSame(
            languageSession,
            teaching.languageEducationSession,
        )

        assertEquals(
            "French",
            teaching.languageEducationSession.targetLanguage,
        )

        assertEquals(
            "Everyday beginner communication",
            teaching.teachingFocus,
        )

        assertEquals(
            "Build a reusable multilingual teaching context",
            teaching.teachingObjective,
        )
    }

    @Test
    fun `non English target language remains valid`() {
        val result =
            MultilingualTeachingCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage133-multilingual-002",
                    ),
                languageEducationSession =
                    languageSession(
                        targetLanguage = "Mandarin Chinese",
                    ),
                teachingFocus = "Beginner communication",
                teachingObjective = "Prepare reusable teaching architecture",
            )

        assertEquals(
            MultilingualTeachingPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            "Mandarin Chinese",
            requireNotNull(result.teaching)
                .languageEducationSession
                .targetLanguage,
        )
    }

    @Test
    fun `blank teaching focus remains deferred`() {
        val result =
            MultilingualTeachingCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage133-multilingual-003",
                    ),
                languageEducationSession =
                    languageSession(
                        targetLanguage = "German",
                    ),
                teachingFocus = "   ",
                teachingObjective = "Prepare beginner teaching context.",
            )

        assertEquals(
            MultilingualTeachingPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.teaching,
        )
    }

    @Test
    fun `blank teaching objective remains deferred`() {
        val result =
            MultilingualTeachingCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage133-multilingual-004",
                    ),
                languageEducationSession =
                    languageSession(
                        targetLanguage = "Spanish",
                    ),
                teachingFocus = "Beginner communication",
                teachingObjective = "   ",
            )

        assertEquals(
            MultilingualTeachingPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.teaching,
        )
    }

    @Test
    fun `prepared multilingual teaching result requires teaching context`() {
        assertFailsWith<IllegalArgumentException> {
            MultilingualTeachingPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage133-result-001",
                    ),
                status =
                    MultilingualTeachingPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred multilingual teaching result cannot smuggle teaching context`() {
        val teaching =
            MultilingualTeachingRecord.create(
                languageEducationSession =
                    languageSession(
                        targetLanguage = "Russian",
                    ),
                teachingFocus = "Beginner communication",
                teachingObjective = "Prepare reusable teaching architecture",
            )

        assertFailsWith<IllegalArgumentException> {
            MultilingualTeachingPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage133-result-002",
                    ),
                status =
                    MultilingualTeachingPreparationStatus.DEFERRED,
                teaching = teaching,
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
                        "education-session:stage133-runtime:$targetLanguage",
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
