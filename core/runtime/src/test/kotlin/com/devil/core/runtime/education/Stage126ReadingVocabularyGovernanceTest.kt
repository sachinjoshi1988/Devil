package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.LanguageEducationSessionRecord
import com.devil.core.model.education.ReadingVocabularyPracticeRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage126ReadingVocabularyGovernanceTest {

    @Test
    fun `language education session may prepare bounded reading vocabulary context`() {
        val traceId =
            TraceId.from(
                "trace-stage126-reading-vocabulary-001",
            )

        val languageSession = languageSession()

        val result =
            ReadingVocabularyCoordinator().prepare(
                traceId = traceId,
                languageEducationSession = languageSession,
                readingTarget = "Read a short story about daily routines",
                vocabularyTarget = "routine, schedule, usually",
            )

        assertEquals(
            ReadingVocabularyPreparationStatus.PREPARED,
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
            "Read a short story about daily routines",
            practice.readingTarget,
        )

        assertEquals(
            "routine, schedule, usually",
            practice.vocabularyTarget,
        )
    }

    @Test
    fun `blank reading target remains deferred`() {
        val result =
            ReadingVocabularyCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage126-reading-vocabulary-002",
                    ),
                languageEducationSession = languageSession(),
                readingTarget = "   ",
                vocabularyTarget = "routine",
            )

        assertEquals(
            ReadingVocabularyPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.practice,
        )
    }

    @Test
    fun `blank vocabulary target remains deferred`() {
        val result =
            ReadingVocabularyCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage126-reading-vocabulary-003",
                    ),
                languageEducationSession = languageSession(),
                readingTarget = "Read a short passage.",
                vocabularyTarget = "   ",
            )

        assertEquals(
            ReadingVocabularyPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.practice,
        )
    }

    @Test
    fun `prepared reading vocabulary result requires practice context`() {
        assertFailsWith<IllegalArgumentException> {
            ReadingVocabularyPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage126-result-001",
                    ),
                status =
                    ReadingVocabularyPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred reading vocabulary result cannot smuggle practice context`() {
        val practice =
            ReadingVocabularyPracticeRecord.create(
                languageEducationSession = languageSession(),
                readingTarget = "Read a short passage.",
                vocabularyTarget = "journey",
            )

        assertFailsWith<IllegalArgumentException> {
            ReadingVocabularyPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage126-result-002",
                    ),
                status =
                    ReadingVocabularyPreparationStatus.DEFERRED,
                practice = practice,
            )
        }
    }

    private fun languageSession(): LanguageEducationSessionRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage126-runtime",
                    ),
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage126-learner",
                    ),
                objective =
                    EducationObjective.create(
                        subject = "English",
                        objective = "Develop reading and vocabulary skills.",
                    ),
            )

        return LanguageEducationSessionRecord.create(
            educationSession = educationSession,
            targetLanguage = "English",
        )
    }
}
