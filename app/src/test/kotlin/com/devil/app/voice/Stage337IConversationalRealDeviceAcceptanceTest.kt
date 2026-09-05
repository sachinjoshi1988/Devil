package com.devil.app.voice

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 337I — Conversational Real-Device Acceptance.
 *
 * The test protects an already-established physical evidence record.
 *
 * It does not manufacture physical-device evidence and does not introduce a
 * production acceptance authority.
 *
 * VOICE_LANGUAGE_SELECTION != DETECTED_LANGUAGE.
 * RECOGNIZED != UNDERSTOOD.
 * RECOGNIZED != AUTHORIZED.
 * PHYSICAL_TRANSCRIPT != INTERNAL_UNDERSTANDING_PROOF.
 * RUNTIME_ENTRY_SPOKEN != ASSISTANT_RESPONSE_GENERATED.
 * SPOKEN != VERIFIED.
 * MULTILINGUAL_CONVERSATION != MULTILINGUAL_AUTHENTICATION.
 */
class Stage337IConversationalRealDeviceAcceptanceTest {

    @Test
    fun `Stage 337I remains anchored to exact frozen Stage 337H artifact`() {
        val record =
            acceptanceRecord()

        assertContainsAll(
            record,
            "6c8a2a96d030b95ba4aaecdbaf0063c4e4594661",
            "devil-stage-337h-complete",
            "288ec6fad2864f66dd72749dcaefe0c6d6c0a709db328953e429e8228ea3522a",
            "Redmi Note 12",
            "Android 14",
        )
    }

    @Test
    fun `Stage 337I records English Hindi and Marathi manual physical acceptance`() {
        val record =
            acceptanceRecord()

        assertContainsAll(
            record,
            "`EN`",
            "`en-IN`",
            "`Hello Devil`",
            "`hello devil`",
            "`HI`",
            "`hi-IN`",
            "`सेटिंग खोलो`",
            "`MR`",
            "`mr-IN`",
            "`सेटिंग उघडा`",
            "`Deferred by the Devil runtime.`",
            "audible conversational TTS",
            "`EN -> HI -> MR`",
        )
    }

    @Test
    fun `Stage 337I does not rewrite deferred runtime result as Android execution failure`() {
        val record =
            acceptanceRecord()

        assertContainsAll(
            record,
            "Android Settings did not open",
            "Stage 337I does not activate Android execution",
            "`RUNTIME_DEFERRED != ACTION_FAILED`",
        )

        assertFalse(
            record.contains("RUNTIME_DEFERRED = ACTION_FAILED"),
            "Stage 337I must not rewrite runtime DEFERRED as Android action failure.",
        )
    }

    @Test
    fun `Stage 337I truthfully records hands free wake non progression for HI and EN control`() {
        val record =
            acceptanceRecord()

        assertContainsAll(
            record,
            "`Hands-Free active. Listening for Devil.`",
            "`Devil, Code Red`",
            "Devil produced no response",
            "no authentication request was presented",
            "`EN` selected.",
            "only microphone on/off behavior was observed",
            "`HANDS_FREE_WAKE_PROGRESSION_NOT_ESTABLISHED`",
            "does not claim that the wake phrase was successfully",
            "recognized on either run.",
        )

        assertFalse(
            record.contains("WAKE_RECOGNIZED = true"),
            "Stage 337I must not manufacture successful wake recognition.",
        )

        assertFalse(
            record.contains("AUTHENTICATION_REQUESTED = true"),
            "Stage 337I must not manufacture an authentication request.",
        )
    }

    @Test
    fun `Stage 337I preserves conversational acceptance boundaries`() {
        val record =
            acceptanceRecord()

        listOf(
            "APK_BUILT != APK_INSTALLED",
            "VOICE_LANGUAGE_SELECTION != DETECTED_LANGUAGE",
            "RECOGNITION_LOCALE != UNDERSTANDING_LANGUAGE_TRUTH",
            "TTS_LOCALE != RESPONSE_LANGUAGE_TRUTH",
            "VOICE_SOURCE != SPEAKER_AUTHENTICATED",
            "RECOGNIZED != UNDERSTOOD",
            "RECOGNIZED != AUTHORIZED",
            "PHYSICAL_TRANSCRIPT != INTERNAL_UNDERSTANDING_PROOF",
            "RUNTIME_ENTRY_SPOKEN != ASSISTANT_RESPONSE_GENERATED",
            "SPOKEN != VERIFIED",
            "DEVICE_OBSERVATION != CONSTITUTIONAL_VERIFICATION",
            "MULTILINGUAL_CONVERSATION != MULTILINGUAL_AUTHENTICATION",
            "HANDS_FREE_WAKE_PROGRESSION_NOT_ESTABLISHED",
            "RUNTIME_DEFERRED != ACTION_FAILED",
            "STAGE_337I != STAGE_337J_GENERAL_INTENT_CAPABILITY_ROUTER",
        ).forEach { boundary ->
            assertTrue(
                record.contains(boundary),
                "Missing Stage 337I boundary: $boundary",
            )
        }
    }

    @Test
    fun `Stage 337I creates no production acceptance authority`() {
        val productionRoots =
            listOf(
                "app/src/main",
                "core/model/src/main",
                "core/runtime/src/main",
            )
                .map(::repositoryFile)
                .filter(File::exists)

        val forbiddenTerms =
            listOf(
                "Stage337IConversationalRealDeviceAcceptanceCoordinator",
                "Stage337IConversationalRealDeviceAcceptanceAuthority",
                "Stage337IDeviceAcceptanceAuthority",
            )

        val violations =
            productionRoots
                .flatMap { root ->
                    root.walkTopDown()
                        .filter { file ->
                            file.isFile &&
                                file.extension == "kt"
                        }
                        .toList()
                }
                .flatMap { file ->
                    val source =
                        file.readText()

                    forbiddenTerms
                        .filter(source::contains)
                        .map { term ->
                            "${file.path}: $term"
                        }
                }

        assertTrue(
            violations.isEmpty(),
            "Stage 337I must not introduce production acceptance authority: $violations",
        )
    }

    private fun acceptanceRecord(): String {
        return repositoryFile(
            "docs/release/STAGE_337I_CONVERSATIONAL_REAL_DEVICE_ACCEPTANCE.md",
        ).readText()
    }

    private fun assertContainsAll(
        source: String,
        vararg expected: String,
    ) {
        expected.forEach { text ->
            assertTrue(
                source.contains(text),
                "Expected Stage 337I evidence to contain: $text",
            )
        }
    }

    private fun repositoryFile(
        path: String,
    ): File {
        return File(
            repositoryRoot(),
            path,
        )
    }

    private fun repositoryRoot(): File {
        var current =
            File(
                System.getProperty("user.dir")
                    ?: error("JVM user.dir is unavailable."),
            ).absoluteFile

        while (true) {
            val hasSettings =
                File(
                    current,
                    "settings.gradle.kts",
                ).isFile ||
                    File(
                        current,
                        "settings.gradle",
                    ).isFile

            if (
                hasSettings &&
                File(
                    current,
                    "app",
                ).isDirectory
            ) {
                return current
            }

            current =
                current.parentFile
                    ?: error(
                        "Unable to locate Devil repository root from JVM user.dir.",
                    )
        }
    }
}
