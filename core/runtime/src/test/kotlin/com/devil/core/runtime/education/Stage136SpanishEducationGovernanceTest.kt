package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.MultilingualTeachingRecord
import com.devil.core.model.education.SpanishEducationRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage136SpanishEducationGovernanceTest {

    @Test
    fun `Spanish multilingual context may prepare bounded Spanish Education context`() {
        val traceId =
            TraceId.from(
                "trace-stage136-spanish-001",
            )

        val multilingual =
            multilingualTeaching(
                targetLanguage = "Spanish",
            )

        val result =
            SpanishEducationCoordinator().prepare(
                traceId = traceId,
                multilingualTeaching = multilingual,
                spanishLearningFocus =
                    "Everyday Spanish introductions",
                spanishLearningObjective =
                    "Prepare beginner Spanish communication practice",
            )

        assertEquals(
            SpanishEducationPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val spanishEducation =
            requireNotNull(result.spanishEducation)

        assertSame(
            multilingual,
            spanishEducation.multilingualTeaching,
        )

        assertEquals(
            "Spanish",
            spanishEducation.multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )

        assertEquals(
            "Everyday Spanish introductions",
            spanishEducation.spanishLearningFocus,
        )

        assertEquals(
            "Prepare beginner Spanish communication practice",
            spanishEducation.spanishLearningObjective,
        )
    }

    @Test
    fun `non Spanish multilingual target remains deferred`() {
        val result =
            SpanishEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage136-spanish-002",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "German",
                    ),
                spanishLearningFocus =
                    "Spanish greetings",
                spanishLearningObjective =
                    "Prepare Spanish learning context",
            )

        assertEquals(
            SpanishEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.spanishEducation,
        )
    }

    @Test
    fun `case insensitive Spanish multilingual target may prepare context`() {
        val result =
            SpanishEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage136-spanish-003",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "sPaNiSh",
                    ),
                spanishLearningFocus =
                    "Spanish greetings",
                spanishLearningObjective =
                    "Prepare Spanish learning context",
            )

        assertEquals(
            SpanishEducationPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            "sPaNiSh",
            requireNotNull(result.spanishEducation)
                .multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )
    }

    @Test
    fun `blank Spanish learning focus remains deferred`() {
        val result =
            SpanishEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage136-spanish-004",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "Spanish",
                    ),
                spanishLearningFocus = "   ",
                spanishLearningObjective =
                    "Prepare Spanish learning context",
            )

        assertEquals(
            SpanishEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.spanishEducation,
        )
    }

    @Test
    fun `blank Spanish learning objective remains deferred`() {
        val result =
            SpanishEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage136-spanish-005",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "Spanish",
                    ),
                spanishLearningFocus =
                    "Spanish greetings",
                spanishLearningObjective = "   ",
            )

        assertEquals(
            SpanishEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.spanishEducation,
        )
    }

    @Test
    fun `prepared Spanish Education result requires Spanish context`() {
        assertFailsWith<IllegalArgumentException> {
            SpanishEducationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage136-result-001",
                    ),
                status =
                    SpanishEducationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred Spanish Education result cannot smuggle Spanish context`() {
        val spanishEducation =
            SpanishEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "Spanish",
                    ),
                spanishLearningFocus =
                    "Spanish greetings",
                spanishLearningObjective =
                    "Prepare Spanish learning context",
            )

        assertFailsWith<IllegalArgumentException> {
            SpanishEducationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage136-result-002",
                    ),
                status =
                    SpanishEducationPreparationStatus.DEFERRED,
                spanishEducation = spanishEducation,
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
                        "education-session:stage136-runtime:$targetLanguage",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage136-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "Language Education",
                        objective =
                            "Prepare bounded Spanish Education context.",
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
