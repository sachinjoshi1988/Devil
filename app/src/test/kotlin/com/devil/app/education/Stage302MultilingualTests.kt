package com.devil.app.education

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Stage 302 Multilingual Tests completion coverage for the established
 * bounded Stage 133–141 Devil multilingual education architecture
 * and the existing Stage 198 Android multilingual speech boundary.
 *
 * This test surface validates existing multilingual behavior only.
 *
 * MULTILINGUAL_CONTEXT != TRANSLATION_ENGINE.
 * PREPARED != LANGUAGE_TAUGHT.
 * PREPARED != LEARNING_VERIFIED.
 * SUPPORT_LANGUAGE != SECOND_TARGET_LANGUAGE.
 * MULTILINGUAL_RECOGNITION != TRANSLATION.
 * MULTILINGUAL_RECOGNITION != AUTHENTICATION.
 *
 * Stage 302 does not create another runtime, Brain, Planner, Executive,
 * constitutional authority, Memory Authority, Security Authority,
 * translation engine, language provider, education provider,
 * speech authority, or platform capability.
 *
 * It does not modify production architecture and does not implement
 * Stage 303 Automation Tests.
 */
class Stage302MultilingualTests {

    @Test
    fun `multilingual foundation preserves target language and bounded preparation`() {
        val coordinator =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/education/MultilingualTeachingCoordinator.kt",
            )
        val result =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/education/MultilingualTeachingPreparationResult.kt",
            )

        listOf(
            "MultilingualTeachingPreparationStatus.PREPARED",
            "MultilingualTeachingPreparationStatus.DEFERRED",
            "languageEducationSession = languageEducationSession",
            "MULTILINGUAL_CONTEXT != TRANSLATION_ENGINE.",
            "PREPARED != LANGUAGE_TAUGHT.",
            "PREPARED != LEARNING_VERIFIED.",
        ).forEach { marker ->
            assertTrue(
                coordinator.contains(marker),
                "Missing Stage 302 multilingual foundation marker: $marker",
            )
        }

        assertTrue(
            result.contains(
                "Prepared Multilingual Teaching results require one teaching context.",
            ),
        )
        assertTrue(
            result.contains(
                "Deferred Multilingual Teaching results must not contain a teaching context.",
            ),
        )
    }

    @Test
    fun `dedicated multilingual education preserves language specialization boundaries`() {
        val french =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/education/FrenchEducationCoordinator.kt",
            )
        val german =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/education/GermanEducationCoordinator.kt",
            )
        val spanish =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/education/SpanishEducationCoordinator.kt",
            )
        val russian =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/education/RussianEducationCoordinator.kt",
            )
        val mandarin =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/education/MandarinChineseEducationCoordinator.kt",
            )

        listOf(
            french to "\"French\"",
            german to "\"German\"",
            spanish to "\"Spanish\"",
            russian to "\"Russian\"",
            mandarin to "\"Mandarin Chinese\"",
        ).forEachIndexed { index, (text, language) ->
            assertTrue(
                text.contains(language),
                "Stage 302 dedicated language surface $index must preserve its exact target language.",
            )
            assertTrue(
                text.contains("ignoreCase = true"),
                "Stage 302 dedicated language surface $index must preserve bounded case-insensitive target matching.",
            )
            assertTrue(
                text.contains("PreparationStatus.PREPARED"),
                "Stage 302 dedicated language surface $index lacks PREPARED coverage.",
            )
            assertTrue(
                text.contains("PreparationStatus.DEFERRED"),
                "Stage 302 dedicated language surface $index lacks DEFERRED coverage.",
            )
        }
    }

    @Test
    fun `additional language expansion remains separate from dedicated specializations`() {
        val additional =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/education/AdditionalLanguageEducationCoordinator.kt",
            )

        listOf(
            "ADDITIONAL_LANGUAGE_EXPANSION != LANGUAGE_REGISTRY.",
            "GENERIC_EXPANSION != DEDICATED_LANGUAGE_SPECIALIZATION.",
            "isDedicatedLanguage(targetLanguage)",
            "\"French\"",
            "\"German\"",
            "\"Spanish\"",
            "\"Russian\"",
            "\"Mandarin Chinese\"",
            "AdditionalLanguageEducationPreparationStatus.PREPARED",
            "AdditionalLanguageEducationPreparationStatus.DEFERRED",
        ).forEach { marker ->
            assertTrue(
                additional.contains(marker),
                "Missing Stage 302 additional-language marker: $marker",
            )
        }
    }

    @Test
    fun `multilingual conversation and cross language assistance remain descriptive education contexts`() {
        val conversation =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/education/MultilingualConversationLabCoordinator.kt",
            )
        val assistance =
            source(
                "core/runtime/src/main/kotlin/com/devil/core/runtime/education/CrossLanguageLearningAssistanceCoordinator.kt",
            )

        assertTrue(
            conversation.contains(
                "MULTILINGUAL_CONVERSATION_LAB != CONVERSATION_DOMAIN.",
            ),
        )
        assertTrue(
            conversation.contains("PREPARED != CONVERSATION_OCCURRED."),
        )
        assertTrue(
            conversation.contains("PREPARED != PROFICIENCY_VERIFIED."),
        )

        assertTrue(
            assistance.contains(
                "CROSS_LANGUAGE_ASSISTANCE != TRANSLATION_ENGINE.",
            ),
        )
        assertTrue(
            assistance.contains(
                "SUPPORT_LANGUAGE != SECOND_TARGET_LANGUAGE.",
            ),
        )
        assertTrue(
            assistance.contains("PREPARED != TRANSLATION_PERFORMED."),
        )
        assertTrue(
            assistance.contains("PREPARED != LEARNING_VERIFIED."),
        )
        assertTrue(
            assistance.contains(
                "supportLanguage.trim().equals(",
            ),
        )
    }

    @Test
    fun `Android multilingual speech remains recognition metadata not translation authentication or education authority`() {
        val coordinator =
            source(
                "app/src/main/kotlin/com/devil/app/voice/AndroidMultilingualSpeechRecognitionCoordinator.kt",
            )

        listOf(
            "LANGUAGE_TAG != DETECTED_LANGUAGE.",
            "MULTILINGUAL_RECOGNITION != TRANSLATION.",
            "MULTILINGUAL_RECOGNITION != AUTHENTICATION.",
            "AndroidMultilingualSpeechRecognitionStatus.AVAILABLE",
            "AndroidMultilingualSpeechRecognitionStatus.DEFERRED",
        ).forEach { marker ->
            assertTrue(
                coordinator.contains(marker),
                "Missing Stage 302 multilingual speech marker: $marker",
            )
        }

        assertTrue(!coordinator.contains("EducationAuthority"))
        assertTrue(!coordinator.contains("MemoryAuthority"))
        assertTrue(!coordinator.contains("UnifiedDevilRuntime("))
    }

    @Test
    fun `existing multilingual governance tests cover preparation deferral and result invariants`() {
        val representativeTests =
            listOf(
                "core/runtime/src/test/kotlin/com/devil/core/runtime/education/Stage133MultilingualTeachingGovernanceTest.kt",
                "core/runtime/src/test/kotlin/com/devil/core/runtime/education/Stage134FrenchEducationGovernanceTest.kt",
                "core/runtime/src/test/kotlin/com/devil/core/runtime/education/Stage138MandarinChineseEducationGovernanceTest.kt",
                "core/runtime/src/test/kotlin/com/devil/core/runtime/education/Stage139AdditionalLanguageEducationGovernanceTest.kt",
                "core/runtime/src/test/kotlin/com/devil/core/runtime/education/Stage140MultilingualConversationLabGovernanceTest.kt",
                "core/runtime/src/test/kotlin/com/devil/core/runtime/education/Stage141CrossLanguageLearningAssistanceGovernanceTest.kt",
            )

        representativeTests.forEachIndexed { index, path ->
            val test = source(path)

            assertTrue(
                test.contains("PREPARED"),
                "Stage 302 representative governance test $index lacks PREPARED coverage.",
            )
            assertTrue(
                test.contains("DEFERRED"),
                "Stage 302 representative governance test $index lacks DEFERRED coverage.",
            )
            assertTrue(
                test.contains("assertFailsWith<IllegalArgumentException>"),
                "Stage 302 representative governance test $index lacks result-invariant coverage.",
            )
        }
    }

    @Test
    fun `Stage 302 stops before automation test completion`() {
        val stage302 =
            source(
                "app/src/test/kotlin/com/devil/app/education/Stage302MultilingualTests.kt",
            )

        assertTrue(
            stage302.contains("does not implement"),
        )
        assertTrue(
            stage302.contains("Stage 303 Automation Tests"),
        )
    }

    private fun source(path: String): String {
        val candidates =
            listOf(
                File(path),
                File("..", path),
            )

        return candidates
            .firstOrNull { it.isFile }
            ?.readText()
            ?: error(
                "Unable to locate repository source for Stage 302: $path",
            )
    }
}
