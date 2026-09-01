package com.devil.app.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class Stage318ForeignLanguageAlphaCoordinatorTest {

    private val coordinator =
        Stage318ForeignLanguageAlphaCoordinator()

    private fun educationSession(): EducationSessionRecord =
        EducationSessionRecord.create(
            sessionId =
                EducationSessionId.from(
                    "stage316-owner-alpha-session",
                ),
            subjectIdentityId =
                IdentityId.from(
                    "android-primary-local-subject",
                ),
            objective =
                EducationObjective.create(
                    subject = "General Education",
                    objective =
                        "Support bounded owner education alpha testing.",
                ),
        )

    @Test
    fun `French Alpha preserves Stage 120 through 133 and 134 provenance`() {
        val result =
            coordinator.prepare(
                traceId =
                    TraceId.from(
                        "stage318-foreign-language-alpha",
                    ),
                educationSession = educationSession(),
                targetLanguage = "French",
                teachingFocus = "Everyday French",
                teachingObjective =
                    "Prepare bounded French learning context.",
                frenchLearningFocus = "Daily expressions",
                frenchLearningObjective =
                    "Prepare bounded French Alpha specialization.",
            )

        assertEquals(
            Stage318ForeignLanguageAlphaStatus.AVAILABLE,
            result.status,
        )

        val languageSession =
            assertNotNull(result.languageSession)
        val multilingualTeaching =
            assertNotNull(result.multilingualTeaching)
        val frenchEducation =
            assertNotNull(result.frenchEducation)

        assertEquals(
            "French",
            languageSession.targetLanguage,
        )
        assertEquals(
            languageSession,
            multilingualTeaching.languageEducationSession,
        )
        assertEquals(
            "Everyday French",
            multilingualTeaching.teachingFocus,
        )
        assertEquals(
            "Prepare bounded French learning context.",
            multilingualTeaching.teachingObjective,
        )
        assertEquals(
            multilingualTeaching,
            frenchEducation.multilingualTeaching,
        )
        assertEquals(
            "Daily expressions",
            frenchEducation.frenchLearningFocus,
        )
        assertEquals(
            "Prepare bounded French Alpha specialization.",
            frenchEducation.frenchLearningObjective,
        )
    }

    @Test
    fun `non French Alpha fails closed before French specialization`() {
        val result =
            coordinator.prepare(
                traceId =
                    TraceId.from(
                        "stage318-non-french-alpha",
                    ),
                educationSession = educationSession(),
                targetLanguage = "German",
                teachingFocus = "Everyday German",
                teachingObjective =
                    "Prepare bounded German learning context.",
                frenchLearningFocus = "Daily expressions",
                frenchLearningObjective =
                    "Prepare bounded French Alpha specialization.",
            )

        assertEquals(
            Stage318ForeignLanguageAlphaStatus.DEFERRED,
            result.status,
        )
        assertNull(result.languageSession)
        assertNull(result.multilingualTeaching)
        assertNull(result.frenchEducation)
    }

    @Test
    fun `blank multilingual teaching context fails closed without partial Alpha result`() {
        val result =
            coordinator.prepare(
                traceId =
                    TraceId.from(
                        "stage318-blank-multilingual-alpha",
                    ),
                educationSession = educationSession(),
                targetLanguage = "French",
                teachingFocus = " ",
                teachingObjective =
                    "Prepare bounded French learning context.",
                frenchLearningFocus = "Daily expressions",
                frenchLearningObjective =
                    "Prepare bounded French Alpha specialization.",
            )

        assertEquals(
            Stage318ForeignLanguageAlphaStatus.DEFERRED,
            result.status,
        )
        assertNull(result.languageSession)
        assertNull(result.multilingualTeaching)
        assertNull(result.frenchEducation)
    }

    @Test
    fun `blank French specialization context fails closed without partial Alpha result`() {
        val result =
            coordinator.prepare(
                traceId =
                    TraceId.from(
                        "stage318-blank-french-alpha",
                    ),
                educationSession = educationSession(),
                targetLanguage = "French",
                teachingFocus = "Everyday French",
                teachingObjective =
                    "Prepare bounded French learning context.",
                frenchLearningFocus = " ",
                frenchLearningObjective =
                    "Prepare bounded French Alpha specialization.",
            )

        assertEquals(
            Stage318ForeignLanguageAlphaStatus.DEFERRED,
            result.status,
        )
        assertNull(result.languageSession)
        assertNull(result.multilingualTeaching)
        assertNull(result.frenchEducation)
    }
}
