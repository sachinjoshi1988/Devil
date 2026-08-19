package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.MultilingualTeachingRecord
import com.devil.core.model.education.RussianEducationRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage137RussianEducationGovernanceTest {

    @Test
    fun `Russian multilingual context may prepare bounded Russian Education context`() {
        val traceId =
            TraceId.from(
                "trace-stage137-russian-001",
            )

        val multilingual =
            multilingualTeaching(
                targetLanguage = "Russian",
            )

        val result =
            RussianEducationCoordinator().prepare(
                traceId = traceId,
                multilingualTeaching = multilingual,
                russianLearningFocus =
                    "Everyday Russian introductions",
                russianLearningObjective =
                    "Prepare beginner Russian communication practice",
            )

        assertEquals(
            RussianEducationPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val russianEducation =
            requireNotNull(result.russianEducation)

        assertSame(
            multilingual,
            russianEducation.multilingualTeaching,
        )

        assertEquals(
            "Russian",
            russianEducation.multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )

        assertEquals(
            "Everyday Russian introductions",
            russianEducation.russianLearningFocus,
        )

        assertEquals(
            "Prepare beginner Russian communication practice",
            russianEducation.russianLearningObjective,
        )
    }

    @Test
    fun `non Russian multilingual target remains deferred`() {
        val result =
            RussianEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage137-russian-002",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "Spanish",
                    ),
                russianLearningFocus =
                    "Russian greetings",
                russianLearningObjective =
                    "Prepare Russian learning context",
            )

        assertEquals(
            RussianEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.russianEducation,
        )
    }

    @Test
    fun `case insensitive Russian multilingual target may prepare context`() {
        val result =
            RussianEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage137-russian-003",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "rUsSiAn",
                    ),
                russianLearningFocus =
                    "Russian greetings",
                russianLearningObjective =
                    "Prepare Russian learning context",
            )

        assertEquals(
            RussianEducationPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            "rUsSiAn",
            requireNotNull(result.russianEducation)
                .multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )
    }

    @Test
    fun `blank Russian learning focus remains deferred`() {
        val result =
            RussianEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage137-russian-004",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "Russian",
                    ),
                russianLearningFocus = "   ",
                russianLearningObjective =
                    "Prepare Russian learning context",
            )

        assertEquals(
            RussianEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.russianEducation,
        )
    }

    @Test
    fun `blank Russian learning objective remains deferred`() {
        val result =
            RussianEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage137-russian-005",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "Russian",
                    ),
                russianLearningFocus =
                    "Russian greetings",
                russianLearningObjective = "   ",
            )

        assertEquals(
            RussianEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.russianEducation,
        )
    }

    @Test
    fun `prepared Russian Education result requires Russian context`() {
        assertFailsWith<IllegalArgumentException> {
            RussianEducationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage137-result-001",
                    ),
                status =
                    RussianEducationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred Russian Education result cannot smuggle Russian context`() {
        val russianEducation =
            RussianEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "Russian",
                    ),
                russianLearningFocus =
                    "Russian greetings",
                russianLearningObjective =
                    "Prepare Russian learning context",
            )

        assertFailsWith<IllegalArgumentException> {
            RussianEducationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage137-result-002",
                    ),
                status =
                    RussianEducationPreparationStatus.DEFERRED,
                russianEducation = russianEducation,
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
                        "education-session:stage137-runtime:$targetLanguage",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage137-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "Language Education",
                        objective =
                            "Prepare bounded Russian Education context.",
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
