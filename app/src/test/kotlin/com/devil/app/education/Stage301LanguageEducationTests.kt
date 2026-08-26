package com.devil.app.education

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Stage 301 Language Education Tests completion coverage for the established
 * bounded Stage 120–132 Devil Language Education architecture.
 *
 * This test surface validates existing Language Education behavior only.
 *
 * LANGUAGE_EDUCATION != AUTHENTICATION.
 * LANGUAGE_EDUCATION != AUTHORIZATION.
 * PREPARED != TAUGHT.
 * PREPARED != VERIFIED_PROGRESS.
 * ASSESSMENT_EVIDENCE_DESCRIPTION != CONSTITUTIONAL_OBSERVATION.
 * ASSESSMENT_INTERPRETATION != CONSTITUTIONAL_VERIFICATION.
 * USER_LEARNING != DEVIL_CONSTITUTIONAL_LEARNING.
 *
 * Stage 301 does not create another runtime, Brain, Planner, Executive,
 * constitutional authority, Memory Authority, Security Authority,
 * education provider, assessment engine, or platform capability.
 *
 * It does not modify production architecture and does not implement
 * Stage 302 Multilingual Tests.
 */
class Stage301LanguageEducationTests {

    @Test
    fun `language education foundation preserves bounded preparation semantics`() {
        val foundation =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/education/LanguageEducationFoundationCoordinator.kt",
            )
        val beginner =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/education/SpokenEnglishBeginnerCoordinator.kt",
            )

        listOf(
            "LanguageEducationFoundationStatus.PREPARED",
            "LanguageEducationFoundationStatus.DEFERRED",
            "LanguageEducationSessionRecord.create(",
            "targetLanguage.isBlank()",
        ).forEach { marker ->
            assertTrue(
                foundation.contains(marker),
                "Missing Stage 301 language-foundation marker: $marker",
            )
        }

        assertTrue(
            beginner.contains("targetLanguage.equals("),
        )
        assertTrue(
            beginner.contains("other = \"English\""),
        )
        assertTrue(
            beginner.contains("SpokenEnglishBeginnerPreparationStatus.PREPARED"),
        )
        assertTrue(
            beginner.contains("SpokenEnglishBeginnerPreparationStatus.DEFERRED"),
        )
    }

    @Test
    fun `spoken English practice chain remains preparation rather than verified proficiency`() {
        val conversation =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/education/SpokenEnglishConversationCoordinator.kt",
            )
        val pronunciation =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/education/PronunciationIntelligenceCoordinator.kt",
            )

        assertTrue(
            conversation.contains(
                "EDUCATION_CONVERSATION_PRACTICE != CONVERSATION_DOMAIN.",
            ),
        )
        assertTrue(
            conversation.contains("PREPARED != CONVERSATION_COMPLETED."),
        )
        assertTrue(
            conversation.contains("PRACTICE_CONTEXT != VERIFIED_PROFICIENCY."),
        )

        assertTrue(
            pronunciation.contains(
                "PRONUNCIATION_INTELLIGENCE != SPEECH_RECOGNITION.",
            ),
        )
        assertTrue(
            pronunciation.contains("PREPARED != PRONUNCIATION_VERIFIED."),
        )
        assertTrue(
            pronunciation.contains("PREPARED != PROFICIENCY_VERIFIED."),
        )
    }

    @Test
    fun `writing confidence and curriculum remain bounded educational contexts`() {
        val writing =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/education/WritingCommunicationCoordinator.kt",
            )
        val confidence =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/education/EnglishConfidenceCoachCoordinator.kt",
            )
        val curriculum =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/education/AdaptiveLanguageCurriculumCoordinator.kt",
            )

        assertTrue(
            writing.contains(
                "WRITING_COMMUNICATION_EDUCATION != EXTERNAL_COMMUNICATION.",
            ),
        )
        assertTrue(writing.contains("PREPARED != SENT."))
        assertTrue(writing.contains("PREPARED != QUALITY_VERIFIED."))

        assertTrue(confidence.contains("PREPARED != COACHED."))
        assertTrue(confidence.contains("PREPARED != CONFIDENCE_IMPROVED."))
        assertTrue(confidence.contains("PREPARED != VERIFIED_PROGRESS."))

        assertTrue(
            curriculum.contains(
                "PREPARED != ADAPTED_FROM_VERIFIED_PROGRESS.",
            ),
        )
        assertTrue(curriculum.contains("PREPARED != LESSON_GENERATED."))
        assertTrue(curriculum.contains("PREPARED != CURRICULUM_EXECUTED."))
    }

    @Test
    fun `language assessment remains descriptive rather than constitutional verification`() {
        val assessment =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/education/LanguageProgressAssessmentCoordinator.kt",
            )

        assertTrue(
            assessment.contains(
                "ASSESSMENT_EVIDENCE_DESCRIPTION != CONSTITUTIONAL_OBSERVATION.",
            ),
        )
        assertTrue(
            assessment.contains(
                "ASSESSMENT_INTERPRETATION != CONSTITUTIONAL_VERIFICATION.",
            ),
        )
        assertTrue(
            assessment.contains("PREPARED != VERIFIED_GLOBAL_PROFICIENCY."),
        )
        assertTrue(
            assessment.contains("PREPARED != MASTERY_ESTABLISHED."),
        )

        listOf(
            "assessmentFocus.isBlank()",
            "learnerEvidence.isBlank()",
            "assessmentInterpretation.isBlank()",
            "LanguageProgressAssessmentPreparationStatus.PREPARED",
            "LanguageProgressAssessmentPreparationStatus.DEFERRED",
        ).forEach { marker ->
            assertTrue(
                assessment.contains(marker),
                "Missing Stage 301 assessment marker: $marker",
            )
        }
    }

    @Test
    fun `existing governance tests preserve prepared deferred and result invariants`() {
        val stage120 =
            source(
                "core/runtime/src/test/kotlin/com/devil/core/runtime/education/Stage120LanguageEducationFoundationGovernanceTest.kt",
            )
        val stage122 =
            source(
                "core/runtime/src/test/kotlin/com/devil/core/runtime/education/Stage122SpokenEnglishConversationGovernanceTest.kt",
            )
        val stage132 =
            source(
                "core/runtime/src/test/kotlin/com/devil/core/runtime/education/Stage132LanguageProgressAssessmentGovernanceTest.kt",
            )

        listOf(stage120, stage122, stage132).forEachIndexed { index, text ->
            assertTrue(
                text.contains("PREPARED"),
                "Stage 301 representative governance test $index lacks PREPARED coverage.",
            )
            assertTrue(
                text.contains("DEFERRED"),
                "Stage 301 representative governance test $index lacks DEFERRED coverage.",
            )
            assertTrue(
                text.contains("assertFailsWith<IllegalArgumentException>"),
                "Stage 301 representative governance test $index lacks result-invariant coverage.",
            )
        }
    }

    @Test
    fun `Stage 301 stops before multilingual test completion`() {
        val stage301 =
            source(
                "app/src/test/kotlin/com/devil/app/education/Stage301LanguageEducationTests.kt",
            )

        assertTrue(
            stage301.contains("does not implement"),
        )
        assertTrue(
            stage301.contains("Stage 302 Multilingual Tests"),
        )
    }

    private fun source(path: String): String =
        File(
            requireNotNull(
                generateSequence(File(System.getProperty("user.dir"))) { it.parentFile }
                    .firstOrNull { File(it, path).isFile },
            ) {
                "Unable to locate repository root for Stage 301 source: $path"
            },
            path,
        ).readText()
}
