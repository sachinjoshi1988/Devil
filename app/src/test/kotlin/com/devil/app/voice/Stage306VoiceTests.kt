package com.devil.app.voice

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 306 Voice Tests completion coverage for the already-established
 * Devil voice architecture.
 *
 * This is test-only completion evidence. It does not modify production
 * architecture or establish new voice capability.
 *
 * Protected boundaries:
 *
 * WAKE_MATCHED != AUTHENTICATED.
 * ATTENTION_ESTABLISHED != ACTIVE_SESSION.
 * SPEECH_RECOGNIZED != SPEAKER_IDENTIFIED.
 * SPEECH_RECOGNIZED != AUTHENTICATED.
 * TRANSCRIPT != INTENT.
 * MULTILINGUAL_RECOGNITION != TRANSLATION.
 * MULTILINGUAL_RECOGNITION != AUTHENTICATION.
 * DEVIL_VOICE_PROFILE != SPOKEN_OUTPUT.
 * PREFERRED_VOICE != AVAILABLE_VOICE.
 * SPEAKING != LISTENING.
 * NATURAL_TURN_TAKING != BARGE_IN.
 * TURN_STATE != AUTHORIZATION.
 * AUDIO_LEVEL != SPEECH_CONTENT.
 * VOICE_ACTIVITY != RECOGNIZED_SPEECH.
 * NOISE != UNKNOWN_SPEAKER.
 * VOICE_ACTIVITY != AUTHENTICATION.
 * VOCAL_TONE != EMOTIONAL_STATE.
 * VOCAL_TONE != MENTAL_HEALTH_STATE.
 * VOCAL_TONE != INTENT.
 * VOCAL_TONE != SPEAKER_IDENTITY.
 * TONE_AWARENESS != RESPONSE_GENERATION.
 * SPOKEN_EDUCATION_MODE != LESSON_GENERATION.
 * SPOKEN_EDUCATION_MODE != SPEECH_EXECUTED.
 * EDUCATION_CONTEXT != VERIFIED_PROFICIENCY.
 * VOICE_PRODUCTION_VALIDATED != CONSTITUTIONAL_VERIFICATION.
 * VOICE_PRODUCTION_VALIDATED != SPEECH_EXECUTED.
 * VOICE_PRODUCTION_VALIDATED != OWNER_VOICE_VERIFIED.
 * VOICE_PRODUCTION_VALIDATED != REAL_DEVICE_VALIDATED.
 *
 * Authentication handoff != authenticated session.
 *
 * Stage 306 does not start SpeechRecognizer, invoke TextToSpeech,
 * authenticate or identify a speaker, grant authorization, establish
 * execution / Verification / Outcome, claim real-device validation,
 * alter wake-phrase policy, or implement Stage 307 Vision Tests.
 */
class Stage306VoiceTests {

    @Test
    fun `Stage 306 preserves Stage 195 voice architecture boundaries`() {
        val source =
            source(
                "app/src/main/kotlin/com/devil/app/voice/" +
                    "AndroidVoiceArchitectureV2Coordinator.kt",
            )

        assertContainsAll(
            source,
            "start SpeechRecognizer listening",
            "invoke TextToSpeech",
            "authenticate a speaker",
            "establish ACTIVE_SESSION",
            "grant Devil authorization",
            "WAKE_PHRASE != AUTHENTICATION.",
        )
    }

    @Test
    fun `Stage 306 preserves wake attention without authentication`() {
        val source =
            source(
                "app/src/main/kotlin/com/devil/app/voice/" +
                    "AndroidWakePhraseV2Policy.kt",
            )

        assertContainsAll(
            source,
            "A match establishes attention only.",
            "WAKE_MATCHED != AUTHENTICATED.",
            "CODE_RED_RECOGNIZED != ACCESS_GRANTED.",
            "ATTENTION_ESTABLISHED != ACTIVE_SESSION.",
        )

        assertTrue(source.contains("\"devil\""))
        assertTrue(source.contains("\"hello devil\""))
    }

    @Test
    fun `Stage 306 preserves recognition provenance boundaries`() {
        val source =
            source(
                "app/src/main/kotlin/com/devil/app/voice/" +
                    "AndroidSpeechRecognitionV2Coordinator.kt",
            )

        assertContainsAll(
            source,
            "SPEECH_RECOGNIZED != SPEAKER_IDENTIFIED.",
            "SPEECH_RECOGNIZED != AUTHENTICATED.",
            "TRANSCRIPT != INTENT.",
            "NO_MATCH, CANCELLED, and FAILED remain DEFERRED.",
        )
    }

    @Test
    fun `Stage 306 preserves multilingual recognition boundaries`() {
        val source =
            source(
                "app/src/main/kotlin/com/devil/app/voice/" +
                    "AndroidMultilingualSpeechRecognitionCoordinator.kt",
            )

        assertContainsAll(
            source,
            "LANGUAGE_TAG != DETECTED_LANGUAGE.",
            "MULTILINGUAL_RECOGNITION != TRANSLATION.",
            "MULTILINGUAL_RECOGNITION != AUTHENTICATION.",
        )
    }

    @Test
    fun `Stage 306 preserves Devil voice presentation boundaries`() {
        val source =
            source(
                "app/src/main/kotlin/com/devil/app/voice/" +
                    "DevilVoiceCoordinator.kt",
            )

        assertContainsAll(
            source,
            "DEVIL_VOICE_PROFILE != SPOKEN_OUTPUT.",
            "PREFERRED_VOICE != AVAILABLE_VOICE.",
            "invoke Android TextToSpeech",
        )
    }

    @Test
    fun `Stage 306 preserves natural turn taking boundaries`() {
        val source =
            source(
                "app/src/main/kotlin/com/devil/app/voice/" +
                    "AndroidNaturalTurnTakingCoordinator.kt",
            )

        assertContainsAll(
            source,
            "SPEAKING != LISTENING.",
            "NATURAL_TURN_TAKING != BARGE_IN.",
            "TURN_STATE != AUTHORIZATION.",
            "must not accept simultaneous listening and speaking",
        )
    }

    @Test
    fun `Stage 306 preserves voice activity and noise boundaries`() {
        val source =
            source(
                "app/src/main/kotlin/com/devil/app/voice/" +
                    "AndroidVoiceActivityCoordinator.kt",
            )

        assertContainsAll(
            source,
            "AUDIO_LEVEL != SPEECH_CONTENT.",
            "VOICE_ACTIVITY != RECOGNIZED_SPEECH.",
            "NOISE != UNKNOWN_SPEAKER.",
            "VOICE_ACTIVITY != AUTHENTICATION.",
        )
    }

    @Test
    fun `Stage 306 preserves vocal tone awareness boundaries`() {
        val source =
            source(
                "app/src/main/kotlin/com/devil/app/voice/" +
                    "AndroidEmotionalToneAwarenessCoordinator.kt",
            )

        assertContainsAll(
            source,
            "VOCAL_TONE != EMOTIONAL_STATE.",
            "VOCAL_TONE != MENTAL_HEALTH_STATE.",
            "VOCAL_TONE != INTENT.",
            "VOCAL_TONE != SPEAKER_IDENTITY.",
            "TONE_AWARENESS != RESPONSE_GENERATION.",
        )
    }

    @Test
    fun `Stage 306 preserves spoken education voice boundaries`() {
        val source =
            source(
                "app/src/main/kotlin/com/devil/app/voice/" +
                    "AndroidSpokenEducationModeCoordinator.kt",
            )

        assertContainsAll(
            source,
            "SPOKEN_EDUCATION_MODE != LESSON_GENERATION.",
            "SPOKEN_EDUCATION_MODE != SPEECH_EXECUTED.",
            "EDUCATION_CONTEXT != VERIFIED_PROFICIENCY.",
        )
    }

    @Test
    fun `Stage 306 preserves structural voice production validation boundaries`() {
        val source =
            source(
                "app/src/main/kotlin/com/devil/app/voice/" +
                    "AndroidVoiceProductionValidationCoordinator.kt",
            )

        assertContainsAll(
            source,
            "VOICE_PRODUCTION_VALIDATED != CONSTITUTIONAL_VERIFICATION.",
            "VOICE_PRODUCTION_VALIDATED != SPEECH_EXECUTED.",
            "VOICE_PRODUCTION_VALIDATED != OWNER_VOICE_VERIFIED.",
            "VOICE_PRODUCTION_VALIDATED != REAL_DEVICE_VALIDATED.",
        )
    }

    @Test
    fun `Stage 306 representative Stage 195 through 204 tests retain bounded evidence`() {
        val tests =
            listOf(
                "Stage195VoiceArchitectureV2Test.kt",
                "Stage196WakePhraseV2Test.kt",
                "Stage197SpeechRecognitionV2Test.kt",
                "Stage198MultilingualSpeechRecognitionTest.kt",
                "Stage199DevilVoiceTest.kt",
                "Stage200NaturalTurnTakingTest.kt",
                "Stage201VoiceActivityNoiseHandlingTest.kt",
                "Stage202EmotionalToneAwarenessTest.kt",
                "Stage203SpokenEducationModeTest.kt",
                "Stage204VoiceProductionValidationTest.kt",
            ).map {
                source("app/src/test/kotlin/com/devil/app/voice/$it")
            }

        tests.forEachIndexed { index, test ->
            assertTrue(
                test.contains("@Test"),
                "Stage 306 representative voice test $index lacks test evidence.",
            )

            assertTrue(
                test.contains("assertEquals") ||
                    test.contains("assertTrue") ||
                    test.contains("assertFalse"),
                "Stage 306 representative voice test $index lacks positive assertions.",
            )

            assertTrue(
                test.contains("DEFERRED") ||
                    test.contains("NOT_MATCHED") ||
                    test.contains("assertFailsWith") ||
                    test.contains("null"),
                "Stage 306 representative voice test $index lacks non-success coverage.",
            )

            assertTrue(
                test.contains("assertFailsWith") ||
                    test.contains("assertEquals") ||
                    test.contains("assertTrue"),
                "Stage 306 representative voice test $index lacks invariant/provenance coverage.",
            )
        }
    }

    @Test
    fun `Stage 306 preserves hands free authentication separation`() {
        val production =
            source(
                "app/src/main/kotlin/com/devil/app/voice/" +
                    "HandsFreeProductionCoordinator.kt",
            )
        val handoff =
            source(
                "app/src/main/kotlin/com/devil/app/voice/" +
                    "DefaultHandsFreeAuthenticationHandoff.kt",
            )

        assertContainsAll(
            production,
            "Wake != authentication.",
            "Code Red != authentication.",
            "Authentication handoff != authenticated session.",
        )

        assertContainsAll(
            handoff,
            "returns UNAVAILABLE",
            "Code Red != Authentication.",
        )
    }

    @Test
    fun `Stage 306 completion test remains test only`() {
        val stage306 =
            source(
                "app/src/test/kotlin/com/devil/app/voice/" +
                    "Stage306VoiceTests.kt",
            )

        assertContainsAll(
            stage306,
            "This is test-only completion evidence.",
            "does not modify production",
            "does not start SpeechRecognizer",
            "invoke TextToSpeech",
            "grant authorization",
        )

        assertFalse(
            stage306.contains("class Stage306Voice" + "Coordinator"),
        )
    }

    @Test
    fun `Stage 306 stops before vision test completion`() {
        val stage306 =
            source(
                "app/src/test/kotlin/com/devil/app/voice/" +
                    "Stage306VoiceTests.kt",
            )

        assertTrue(stage306.contains("does not"))
        assertTrue(stage306.contains("Stage 307 Vision Tests"))
    }

    private fun assertContainsAll(
        source: String,
        vararg markers: String,
    ) {
        markers.forEach { marker ->
            assertTrue(
                source.contains(marker),
                "Missing Stage 306 voice boundary: $marker",
            )
        }
    }

    private fun source(path: String): String {
        val candidates =
            listOf(
                File(path),
                File("../$path"),
                File("../../$path"),
            )

        val file =
            candidates.firstOrNull { it.isFile }
                ?: error(
                    "Unable to locate repository source for Stage 306: $path",
                )

        return file.readText()
    }
}
