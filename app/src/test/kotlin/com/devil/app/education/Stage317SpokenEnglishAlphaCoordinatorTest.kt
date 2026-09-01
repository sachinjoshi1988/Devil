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

class Stage317SpokenEnglishAlphaCoordinatorTest {

    private val coordinator =
        Stage317SpokenEnglishAlphaCoordinator()

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
    fun `English Alpha preserves Stage 120 through 123 provenance`() {
        val result =
            coordinator.prepare(
                traceId =
                    TraceId.from(
                        "stage317-spoken-english-alpha",
                    ),
                educationSession = educationSession(),
                targetLanguage = "English",
                conversationTopic = "Daily conversation",
                pronunciationTarget = "Good morning",
            )

        assertEquals(
            Stage317SpokenEnglishAlphaStatus.AVAILABLE,
            result.status,
        )

        val languageSession =
            assertNotNull(result.languageSession)
        val beginnerSession =
            assertNotNull(result.beginnerSession)
        val conversationPractice =
            assertNotNull(result.conversationPractice)
        val pronunciationPractice =
            assertNotNull(result.pronunciationPractice)

        assertEquals(
            "English",
            languageSession.targetLanguage,
        )
        assertEquals(
            languageSession,
            beginnerSession.languageEducationSession,
        )
        assertEquals(
            beginnerSession,
            conversationPractice.beginnerSession,
        )
        assertEquals(
            "Daily conversation",
            conversationPractice.topic,
        )
        assertEquals(
            conversationPractice,
            pronunciationPractice.conversationPractice,
        )
        assertEquals(
            "Good morning",
            pronunciationPractice.target,
        )
    }

    @Test
    fun `non English Alpha fails closed before Spoken English context`() {
        val result =
            coordinator.prepare(
                traceId =
                    TraceId.from(
                        "stage317-non-english-alpha",
                    ),
                educationSession = educationSession(),
                targetLanguage = "French",
                conversationTopic = "Daily conversation",
                pronunciationTarget = "Good morning",
            )

        assertEquals(
            Stage317SpokenEnglishAlphaStatus.DEFERRED,
            result.status,
        )
        assertNull(result.languageSession)
        assertNull(result.beginnerSession)
        assertNull(result.conversationPractice)
        assertNull(result.pronunciationPractice)
    }

    @Test
    fun `blank conversation topic fails closed without partial Alpha result`() {
        val result =
            coordinator.prepare(
                traceId =
                    TraceId.from(
                        "stage317-blank-topic-alpha",
                    ),
                educationSession = educationSession(),
                targetLanguage = "English",
                conversationTopic = " ",
                pronunciationTarget = "Good morning",
            )

        assertEquals(
            Stage317SpokenEnglishAlphaStatus.DEFERRED,
            result.status,
        )
        assertNull(result.languageSession)
        assertNull(result.beginnerSession)
        assertNull(result.conversationPractice)
        assertNull(result.pronunciationPractice)
    }

    @Test
    fun `blank pronunciation target fails closed without partial Alpha result`() {
        val result =
            coordinator.prepare(
                traceId =
                    TraceId.from(
                        "stage317-blank-pronunciation-alpha",
                    ),
                educationSession = educationSession(),
                targetLanguage = "English",
                conversationTopic = "Daily conversation",
                pronunciationTarget = " ",
            )

        assertEquals(
            Stage317SpokenEnglishAlphaStatus.DEFERRED,
            result.status,
        )
        assertNull(result.languageSession)
        assertNull(result.beginnerSession)
        assertNull(result.conversationPractice)
        assertNull(result.pronunciationPractice)
    }
}
