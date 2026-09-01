package com.devil.app.ui.education

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 258 Language Learning Interface governance tests.
 *
 * These tests verify the bounded presentation surface and its constitutional
 * non-authority boundaries without treating UI text as execution or verified
 * educational outcome.
 */
class Stage258LanguageLearningInterfaceTest {

    @Test
    fun `language learning interface uses locked Devil identity asset`() {
        val source = languageInterfaceSource()

        assertTrue(
            source.contains("R.drawable.devil_primary_logo"),
        )
        assertTrue(
            source.contains("\"LANGUAGE LEARNING\""),
        )
    }

    @Test
    fun `language learning interface presents bounded session information`() {
        val source = languageInterfaceSource()

        assertTrue(source.contains("\"LANGUAGE SESSION\""))
        assertTrue(source.contains("\"SESSION\""))
        assertTrue(source.contains("\"TARGET LANGUAGE\""))
        assertTrue(source.contains("\"LEARNING OBJECTIVE\""))
    }

    @Test
    fun `language learning interface presents established English learning areas`() {
        val source = languageInterfaceSource()

        for (
            expected in
                listOf(
                    "\"ENGLISH LEARNING\"",
                    "\"SPOKEN ENGLISH\"",
                    "\"PRONUNCIATION\"",
                    "\"LISTENING\"",
                    "\"GRAMMAR\"",
                    "\"VOCABULARY\"",
                    "\"WRITING\"",
                    "\"ADVANCED ENGLISH\"",
                    "\"CONFIDENCE\"",
                    "\"ACADEMIC\"",
                    "\"PROFESSIONAL\"",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 258 English-learning presentation: $expected",
            )
        }
    }

    @Test
    fun `language learning interface presents established multilingual areas`() {
        val source = languageInterfaceSource()

        for (
            expected in
                listOf(
                    "\"MULTILINGUAL LEARNING\"",
                    "\"CURRICULUM\"",
                    "\"MULTILINGUAL TEACHING\"",
                    "\"CONVERSATION LAB\"",
                    "\"CROSS-LANGUAGE SUPPORT\"",
                )
        ) {
            assertTrue(
                source.contains(expected),
                "Missing Stage 258 multilingual presentation: $expected",
            )
        }
    }

    @Test
    fun `language learning interface presents bounded progress and spoken mode information`() {
        val source = languageInterfaceSource()

        assertTrue(source.contains("\"PROGRESS & MODES\""))
        assertTrue(source.contains("\"PROGRESS\""))
        assertTrue(source.contains("\"ASSESSMENT\""))
        assertTrue(source.contains("\"SPOKEN EDUCATION\""))
    }

    @Test
    fun `language learning interface preserves constitutional boundaries`() {
        val source = languageInterfaceSource()

        for (
            boundary in
                listOf(
                    "LANGUAGE_LEARNING_INTERFACE != LANGUAGE_EDUCATION_AUTHORITY.",
                    "LANGUAGE_LEARNING_INTERFACE != LANGUAGE_SESSION_CREATION.",
                    "LANGUAGE_LEARNING_INTERFACE != LESSON_GENERATION.",
                    "LANGUAGE_LEARNING_UI != VERIFIED_PROFICIENCY.",
                    "LANGUAGE_PROGRESS_PRESENTATION != VERIFIED_MASTERY.",
                    "ADAPTIVE_CURRICULUM_PRESENTATION != CURRICULUM_EXECUTION.",
                    "MULTILINGUAL_UI != LANGUAGE_INFERENCE.",
                    "SPOKEN_LANGUAGE_MODE_PRESENTATION != SPEECH_EXECUTED.",
                    "LANGUAGE_LEARNING_UI != MEMORY_COMMITMENT.",
                    "USER_LANGUAGE_LEARNING != DEVIL_CONSTITUTIONAL_LEARNING.",
                )
        ) {
            assertTrue(
                source.contains(boundary),
                "Missing Stage 258 boundary: $boundary",
            )
        }
    }

    @Test
    fun `missing supplied language information remains truthful`() {
        val source = languageInterfaceSource()

        assertTrue(source.contains("\"Unavailable\""))
        assertTrue(
            source.contains(
                "\"No language-learning objective supplied.\"",
            ),
        )
        assertTrue(source.contains("?.trim()"))
        assertTrue(source.contains("?.takeIf(String::isNotEmpty)"))
    }

    @Test
    fun `education interface exposes bounded language learning navigation`() {
        val source = educationInterfaceSource()

        assertTrue(
            source.contains(
                "onLanguageLearningOpen: () -> Unit",
            ),
        )
        assertTrue(
            source.contains(
                "onClick = onLanguageLearningOpen",
            ),
        )
        assertTrue(
            source.contains(
                "text = \"LANGUAGE LEARNING\"",
            ),
        )
    }

    @Test
    fun `language learning back returns to education`() {
        val source = activitySource()
        val languageLearningBranch =
            source
                .substringAfter("DevilLanguageLearningInterface(")
                .substringBefore("} else if (showEducationInterface)")

        assertTrue(
            languageLearningBranch.contains(
                "showLanguageLearningInterface = false",
            ),
        )
        assertTrue(
            languageLearningBranch.contains(
                "showEducationInterface = true",
            ),
            "BACK TO EDUCATION must restore the Education interface.",
        )
    }

    @Test
    fun `activity supplies only bounded Stage 318 foreign language state`() {
        val source = activitySource()
        val languageLearningBranch =
            source
                .substringAfter("DevilLanguageLearningInterface(")
                .substringBefore("} else if (showEducationInterface)")

        assertTrue(
            languageLearningBranch.contains(
                "stage318ForeignLanguageAlphaResult?.languageSession?.educationSession?.sessionId?.value",
            ),
        )
        assertTrue(
            languageLearningBranch.contains(
                "stage318ForeignLanguageAlphaResult?.languageSession?.targetLanguage",
            ),
        )
        assertTrue(
            languageLearningBranch.contains(
                "stage318ForeignLanguageAlphaResult?.languageSession?.educationSession?.objective?.objective",
            ),
        )
        assertTrue(
            languageLearningBranch.contains(
                "\"Teaching context prepared\"",
            ),
        )

        for (
            unavailableState in
                listOf(
                    "spokenEnglishStatus = null",
                    "pronunciationStatus = null",
                    "listeningStatus = null",
                    "grammarStatus = null",
                    "vocabularyStatus = null",
                    "writingStatus = null",
                    "confidenceStatus = null",
                    "academicEnglishStatus = null",
                    "professionalEnglishStatus = null",
                    "curriculumStatus = null",
                    "multilingualConversationStatus = null",
                    "crossLanguageAssistanceStatus = null",
                    "progressStatus = null",
                    "assessmentStatus = null",
                    "spokenEducationStatus = null",
                )
        ) {
            assertTrue(
                languageLearningBranch.contains(unavailableState),
                "Activity must not fabricate unavailable Stage 318 language state: $unavailableState",
            )
        }
    }

    @Test
    fun `language learning interface contains no operational education wiring`() {
        val source = languageInterfaceSource()

        for (
            forbidden in
                listOf(
                    "EducationSessionCoordinator",
                    "LanguageEducationCoordinator",
                    "AdaptiveLanguageCurriculumCoordinator",
                    "MultilingualConversationLabCoordinator",
                    "CrossLanguageLearningAssistanceCoordinator",
                    "AndroidSpeechRecognition",
                    "DefaultAndroidVoiceOutputSource",
                )
        ) {
            assertFalse(
                source.contains(forbidden),
                "Stage 258 UI must not invoke operational education wiring: $forbidden",
            )
        }
    }

    @Test
    fun `Stage 258 does not implement Stage 259 or later UI work`() {
        val source = languageInterfaceSource()

        assertTrue(
            source.contains(
                "Stage 258 does not implement Stage 259 or later UI work.",
            ),
        )
    }

    private fun languageInterfaceSource(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/ui/education/DevilLanguageLearningInterface.kt",
            "src/main/kotlin/com/devil/app/ui/education/DevilLanguageLearningInterface.kt",
        )

    private fun educationInterfaceSource(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/ui/education/DevilEducationInterface.kt",
            "src/main/kotlin/com/devil/app/ui/education/DevilEducationInterface.kt",
        )

    private fun activitySource(): String =
        readSource(
            "app/src/main/kotlin/com/devil/app/DevilActivity.kt",
            "src/main/kotlin/com/devil/app/DevilActivity.kt",
        )

    private fun readSource(
        vararg candidates: String,
    ): String {
        return candidates
            .asSequence()
            .map(::File)
            .firstOrNull(File::isFile)
            ?.readText()
            ?: error(
                "Unable to locate Stage 258 source from: ${candidates.joinToString()}",
            )
    }
}
