package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.GermanEducationRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.MultilingualTeachingRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage135GermanEducationGovernanceTest {

    @Test
    fun `German multilingual context may prepare bounded German Education context`() {
        val traceId =
            TraceId.from(
                "trace-stage135-german-001",
            )

        val multilingual =
            multilingualTeaching(
                targetLanguage = "German",
            )

        val result =
            GermanEducationCoordinator().prepare(
                traceId = traceId,
                multilingualTeaching = multilingual,
                germanLearningFocus =
                    "Everyday German introductions",
                germanLearningObjective =
                    "Prepare beginner German communication practice",
            )

        assertEquals(
            GermanEducationPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val germanEducation =
            requireNotNull(result.germanEducation)

        assertSame(
            multilingual,
            germanEducation.multilingualTeaching,
        )

        assertEquals(
            "German",
            germanEducation.multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )

        assertEquals(
            "Everyday German introductions",
            germanEducation.germanLearningFocus,
        )

        assertEquals(
            "Prepare beginner German communication practice",
            germanEducation.germanLearningObjective,
        )
    }

    @Test
    fun `non German multilingual target remains deferred`() {
        val result =
            GermanEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage135-german-002",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "French",
                    ),
                germanLearningFocus =
                    "German greetings",
                germanLearningObjective =
                    "Prepare German learning context",
            )

        assertEquals(
            GermanEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.germanEducation,
        )
    }

    @Test
    fun `case insensitive German multilingual target may prepare context`() {
        val result =
            GermanEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage135-german-003",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "gErMaN",
                    ),
                germanLearningFocus =
                    "German greetings",
                germanLearningObjective =
                    "Prepare German learning context",
            )

        assertEquals(
            GermanEducationPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            "gErMaN",
            requireNotNull(result.germanEducation)
                .multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )
    }

    @Test
    fun `blank German learning focus remains deferred`() {
        val result =
            GermanEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage135-german-004",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "German",
                    ),
                germanLearningFocus = "   ",
                germanLearningObjective =
                    "Prepare German learning context",
            )

        assertEquals(
            GermanEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.germanEducation,
        )
    }

    @Test
    fun `blank German learning objective remains deferred`() {
        val result =
            GermanEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage135-german-005",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "German",
                    ),
                germanLearningFocus =
                    "German greetings",
                germanLearningObjective = "   ",
            )

        assertEquals(
            GermanEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.germanEducation,
        )
    }

    @Test
    fun `prepared German Education result requires German context`() {
        assertFailsWith<IllegalArgumentException> {
            GermanEducationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage135-result-001",
                    ),
                status =
                    GermanEducationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred German Education result cannot smuggle German context`() {
        val germanEducation =
            GermanEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "German",
                    ),
                germanLearningFocus =
                    "German greetings",
                germanLearningObjective =
                    "Prepare German learning context",
            )

        assertFailsWith<IllegalArgumentException> {
            GermanEducationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage135-result-002",
                    ),
                status =
                    GermanEducationPreparationStatus.DEFERRED,
                germanEducation = germanEducation,
            )
        }
    }

    private fun multilingualTeaching(
        targetLanguage: String,
    ): MultilingualTeachingRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage135-runtime:$targetLanguage",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage135-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "Language Education",
                        objective =
                            "Prepare bounded German Education context.",
                    ),
            )

        val languageSession =
            LanguageEducationSessionRecord.create(
                educationSession = educationSession,
                targetLanguage = targetLanguage,
            )

        return MultilingualTeachingRecord.create(
            languageEducationSession = languageSession,
            teachingFocus =
                "Beginner language communication",
            teachingObjective =
                "Prepare reusable multilingual teaching context",
        )
    }
}
