package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.FrenchEducationRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.MultilingualTeachingRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage134FrenchEducationGovernanceTest {

    @Test
    fun `French multilingual context may prepare bounded French Education context`() {
        val traceId =
            TraceId.from(
                "trace-stage134-french-001",
            )

        val multilingual =
            multilingualTeaching(
                targetLanguage = "French",
            )

        val result =
            FrenchEducationCoordinator().prepare(
                traceId = traceId,
                multilingualTeaching = multilingual,
                frenchLearningFocus = "Everyday French introductions",
                frenchLearningObjective =
                    "Prepare beginner French communication practice",
            )

        assertEquals(
            FrenchEducationPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val frenchEducation =
            requireNotNull(result.frenchEducation)

        assertSame(
            multilingual,
            frenchEducation.multilingualTeaching,
        )

        assertEquals(
            "French",
            frenchEducation.multilingualTeaching
                .languageEducationSession
                .targetLanguage,
        )

        assertEquals(
            "Everyday French introductions",
            frenchEducation.frenchLearningFocus,
        )

        assertEquals(
            "Prepare beginner French communication practice",
            frenchEducation.frenchLearningObjective,
        )
    }

    @Test
    fun `non French multilingual target remains deferred`() {
        val result =
            FrenchEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage134-french-002",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "German",
                    ),
                frenchLearningFocus = "French greetings",
                frenchLearningObjective = "Prepare French learning context",
            )

        assertEquals(
            FrenchEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.frenchEducation,
        )
    }

    @Test
    fun `blank French learning focus remains deferred`() {
        val result =
            FrenchEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage134-french-003",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "French",
                    ),
                frenchLearningFocus = "   ",
                frenchLearningObjective = "Prepare French learning context",
            )

        assertEquals(
            FrenchEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.frenchEducation,
        )
    }

    @Test
    fun `blank French learning objective remains deferred`() {
        val result =
            FrenchEducationCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage134-french-004",
                    ),
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "French",
                    ),
                frenchLearningFocus = "French greetings",
                frenchLearningObjective = "   ",
            )

        assertEquals(
            FrenchEducationPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.frenchEducation,
        )
    }

    @Test
    fun `prepared French Education result requires French context`() {
        assertFailsWith<IllegalArgumentException> {
            FrenchEducationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage134-result-001",
                    ),
                status =
                    FrenchEducationPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred French Education result cannot smuggle French context`() {
        val frenchEducation =
            FrenchEducationRecord.create(
                multilingualTeaching =
                    multilingualTeaching(
                        targetLanguage = "French",
                    ),
                frenchLearningFocus = "French greetings",
                frenchLearningObjective = "Prepare French learning context",
            )

        assertFailsWith<IllegalArgumentException> {
            FrenchEducationPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage134-result-002",
                    ),
                status =
                    FrenchEducationPreparationStatus.DEFERRED,
                frenchEducation = frenchEducation,
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
                        "education-session:stage134-runtime:$targetLanguage",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage134-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "Language Education",
                        objective = "Prepare bounded French Education context.",
                    ),
            )

        val languageSession =
            LanguageEducationSessionRecord.create(
                educationSession = educationSession,
                targetLanguage = targetLanguage,
            )

        return MultilingualTeachingRecord.create(
            languageEducationSession = languageSession,
            teachingFocus = "Beginner language communication",
            teachingObjective = "Prepare reusable multilingual teaching context",
        )
    }
}
