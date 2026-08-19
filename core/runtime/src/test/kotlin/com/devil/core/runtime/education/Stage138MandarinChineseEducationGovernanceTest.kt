package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.MandarinChineseEducationRecord
import com.devil.core.model.education.MultilingualTeachingRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage138MandarinChineseEducationGovernanceTest {

    @Test
    fun `Mandarin Chinese multilingual context may prepare bounded Mandarin Chinese Education context`() {
        val traceId =
            TraceId.from(
                "trace-stage138-mandarin-chinese-001",
            )

        val multilingual =
            multilingualTeaching(
                targetLanguage = "Mandarin Chinese",
            )

        val result =
            MandarinChineseEducationCoordinator().prepare(
                traceId = traceId,
                multilingualTeaching = multilingual,
                mandarinChineseLearningFocus =
                    "Everyday Mandarin Chinese introductions",
                mandarinChineseLearningObjective =
                    "Prepare beginner Mandarin Chinese communication practice",
            )

        assertEquals(
            MandarinChineseEducationPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val mandarinChineseEducation =
            requireNotNull(result.mandarinChineseEducation)

        assertSame(
            multilingual,
            mandarinChineseEducation.multilingualTeaching,
        )

        assertEquals(
            "Mandarin Chinese",
            mandarinChineseEducation
                .multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )

        assertEquals(
            "Everyday Mandarin Chinese introductions",
            mandarinChineseEducation
                .mandarinChineseLearningFocus,
        )

        assertEquals(
            "Prepare beginner Mandarin Chinese communication practice",
            mandarinChineseEducation
                .mandarinChineseLearningObjective,
        )
    }

    @Test
    fun `non Mandarin Chinese multilingual target remains deferred`() {
        val result =
            MandarinChineseEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage138-mandarin-chinese-002",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "Russian",
                    ),
                mandarinChineseLearningFocus =
                    "Mandarin Chinese greetings",
                mandarinChineseLearningObjective =
                    "Prepare Mandarin Chinese learning context",
            )

        assertEquals(
            MandarinChineseEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.mandarinChineseEducation,
        )
    }

    @Test
    fun `case insensitive Mandarin Chinese target may prepare context`() {
        val result =
            MandarinChineseEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage138-mandarin-chinese-003",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage =
                            "mAnDaRiN cHiNeSe",
                    ),
                mandarinChineseLearningFocus =
                    "Mandarin Chinese greetings",
                mandarinChineseLearningObjective =
                    "Prepare Mandarin Chinese learning context",
            )

        assertEquals(
            MandarinChineseEducationPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            "mAnDaRiN cHiNeSe",
            requireNotNull(result.mandarinChineseEducation)
                .multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )
    }

    @Test
    fun `generic Chinese target remains deferred`() {
        val result =
            MandarinChineseEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage138-mandarin-chinese-004",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "Chinese",
                    ),
                mandarinChineseLearningFocus =
                    "Mandarin Chinese greetings",
                mandarinChineseLearningObjective =
                    "Prepare Mandarin Chinese learning context",
            )

        assertEquals(
            MandarinChineseEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.mandarinChineseEducation,
        )
    }

    @Test
    fun `blank Mandarin Chinese learning focus remains deferred`() {
        val result =
            MandarinChineseEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage138-mandarin-chinese-005",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "Mandarin Chinese",
                    ),
                mandarinChineseLearningFocus = "   ",
                mandarinChineseLearningObjective =
                    "Prepare Mandarin Chinese learning context",
            )

        assertEquals(
            MandarinChineseEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.mandarinChineseEducation,
        )
    }

    @Test
    fun `blank Mandarin Chinese learning objective remains deferred`() {
        val result =
            MandarinChineseEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage138-mandarin-chinese-006",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "Mandarin Chinese",
                    ),
                mandarinChineseLearningFocus =
                    "Mandarin Chinese greetings",
                mandarinChineseLearningObjective = "   ",
            )

        assertEquals(
            MandarinChineseEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.mandarinChineseEducation,
        )
    }

    @Test
    fun `prepared Mandarin Chinese Education result requires Mandarin Chinese context`() {
        assertFailsWith<IllegalArgumentException> {
            MandarinChineseEducationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage138-result-001",
                    ),
                status =
                    MandarinChineseEducationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred Mandarin Chinese Education result cannot smuggle Mandarin Chinese context`() {
        val mandarinChineseEducation =
            MandarinChineseEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "Mandarin Chinese",
                    ),
                mandarinChineseLearningFocus =
                    "Mandarin Chinese greetings",
                mandarinChineseLearningObjective =
                    "Prepare Mandarin Chinese learning context",
            )

        assertFailsWith<IllegalArgumentException> {
            MandarinChineseEducationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage138-result-002",
                    ),
                status =
                    MandarinChineseEducationPreparationStatus.DEFERRED,
                mandarinChineseEducation =
                    mandarinChineseEducation,
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
                        "education-session:stage138-runtime:$targetLanguage",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage138-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "Language Education",
                        objective =
                            "Prepare bounded Mandarin Chinese Education context.",
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
