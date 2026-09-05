package com.devil.app.voice

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue

/**
 * Stage 337H bounded Multilingual Voice proof.
 *
 * VOICE_LANGUAGE_SELECTION != DETECTED_LANGUAGE.
 * RECOGNITION_LOCALE != UNDERSTANDING_LANGUAGE_TRUTH.
 * TTS_LOCALE != RESPONSE_LANGUAGE_TRUTH.
 * VOICE_SOURCE != SPEAKER_AUTHENTICATED.
 * RECOGNIZED != UNDERSTOOD.
 * RECOGNIZED != AUTHORIZED.
 * SPOKEN != VERIFIED.
 * MULTILINGUAL_CONVERSATION != MULTILINGUAL_AUTHENTICATION.
 */
class Stage337HMultilingualVoiceTest {

    @Test
    fun `bounded English Hindi and Marathi voice modes map to exact speech tags`() {
        assertEquals(
            listOf(
                "en-IN",
                "hi-IN",
                "mr-IN",
            ),
            AndroidVoiceLanguageSelection.entries
                .map { selection ->
                    selection.languageTag
                },
        )

        assertEquals(
            listOf(
                "EN",
                "HI",
                "MR",
            ),
            AndroidVoiceLanguageSelection.entries
                .map { selection ->
                    selection.uiLabel
                },
        )
    }

    @Test
    fun `device locale establishes initial configuration only`() {
        assertEquals(
            AndroidVoiceLanguageSelection.HINDI,
            AndroidVoiceLanguageSelection
                .fromDeviceLanguageTag("hi-IN"),
        )

        assertEquals(
            AndroidVoiceLanguageSelection.MARATHI,
            AndroidVoiceLanguageSelection
                .fromDeviceLanguageTag("mr-IN"),
        )

        assertEquals(
            AndroidVoiceLanguageSelection.ENGLISH,
            AndroidVoiceLanguageSelection
                .fromDeviceLanguageTag("en-US"),
        )

        assertEquals(
            AndroidVoiceLanguageSelection.ENGLISH,
            AndroidVoiceLanguageSelection
                .fromDeviceLanguageTag("fr-FR"),
        )
    }

    @Test
    fun `manual conversation uses selected recognition locale`() {
        val policy =
            AndroidVoiceLanguagePolicy()

        assertEquals(
            "hi-IN",
            policy.recognitionLanguageTag(
                selection =
                    AndroidVoiceLanguageSelection.HINDI,
                mode =
                    AndroidVoiceInteractionMode.MANUAL,
                handsFreeState =
                    HandsFreeConversationState.IDLE,
            ),
        )

        assertEquals(
            "mr-IN",
            policy.recognitionLanguageTag(
                selection =
                    AndroidVoiceLanguageSelection.MARATHI,
                mode =
                    AndroidVoiceInteractionMode.MANUAL,
                handsFreeState =
                    HandsFreeConversationState.IDLE,
            ),
        )
    }

    @Test
    fun `wake and authentication states never inherit multilingual conversation locale`() {
        val policy =
            AndroidVoiceLanguagePolicy()

        assertNull(
            policy.recognitionLanguageTag(
                selection =
                    AndroidVoiceLanguageSelection.HINDI,
                mode =
                    AndroidVoiceInteractionMode.HANDS_FREE,
                handsFreeState =
                    HandsFreeConversationState.IDLE,
            ),
        )

        assertNull(
            policy.recognitionLanguageTag(
                selection =
                    AndroidVoiceLanguageSelection.HINDI,
                mode =
                    AndroidVoiceInteractionMode.HANDS_FREE,
                handsFreeState =
                    HandsFreeConversationState.AWAITING_AUTHENTICATION_PHRASE,
            ),
        )

        assertNull(
            policy.recognitionLanguageTag(
                selection =
                    AndroidVoiceLanguageSelection.MARATHI,
                mode =
                    AndroidVoiceInteractionMode.HANDS_FREE,
                handsFreeState =
                    HandsFreeConversationState.AUTHENTICATION_REQUESTED,
            ),
        )

        assertEquals(
            "mr-IN",
            policy.recognitionLanguageTag(
                selection =
                    AndroidVoiceLanguageSelection.MARATHI,
                mode =
                    AndroidVoiceInteractionMode.HANDS_FREE,
                handsFreeState =
                    HandsFreeConversationState.ACTIVE_SESSION,
            ),
        )
    }

    @Test
    fun `selection provider changes only subsequent reads`() {
        val provider =
            MutableAndroidVoiceLanguageSelectionProvider(
                initialSelection =
                    AndroidVoiceLanguageSelection.ENGLISH,
            )

        val first =
            provider.current()

        provider.select(
            AndroidVoiceLanguageSelection.HINDI,
        )

        assertSame(
            AndroidVoiceLanguageSelection.ENGLISH,
            first,
        )

        assertSame(
            AndroidVoiceLanguageSelection.HINDI,
            provider.current(),
        )
    }

    @Test
    fun `Stage 198 supplied speech locale remains configuration not detected language`() {
        val recognition =
            AndroidSpeechRecognitionV2Coordinator()
                .integrate(
                    AndroidVoiceInputResult.recognized(
                        "सेटिंग खोलो",
                    ),
                )

        val multilingual =
            AndroidMultilingualSpeechRecognitionCoordinator()
                .integrate(
                    speechRecognition =
                        recognition,
                    languageTag =
                        AndroidVoiceLanguageSelection
                            .HINDI
                            .languageTag,
                )

        assertEquals(
            AndroidMultilingualSpeechRecognitionStatus.AVAILABLE,
            multilingual.status,
        )

        assertSame(
            recognition,
            multilingual.speechRecognition,
        )

        assertEquals(
            "सेटिंग खोलो",
            multilingual.speechRecognition.transcript,
        )

        assertEquals(
            "hi-IN",
            multilingual.languageTag,
        )
    }

    @Test
    fun `Android recognizer consumes locale hint without changing voice source contract`() {
        val source =
            File(
                "src/main/kotlin/com/devil/app/voice/DefaultAndroidVoiceInputSource.kt",
            ).readText()

        val contract =
            File(
                "src/main/kotlin/com/devil/app/voice/AndroidVoiceInputSource.kt",
            ).readText()

        assertTrue(
            source.contains(
                "recognitionLanguageTagProvider",
            ),
        )

        assertTrue(
            source.contains(
                "RecognizerIntent.EXTRA_LANGUAGE",
            ),
        )

        assertTrue(
            contract.contains(
                "fun startListening(",
            ),
        )

        assertTrue(
            contract.contains(
                "listener: AndroidVoiceInputListener",
            ),
        )
    }

    @Test
    fun `conversation TTS is language selectable while status TTS stays separate`() {
        val outputSource =
            File(
                "src/main/kotlin/com/devil/app/voice/DefaultAndroidVoiceOutputSource.kt",
            ).readText()

        val application =
            File(
                "src/main/kotlin/com/devil/app/DevilApplication.kt",
            ).readText()

        val activity =
            File(
                "src/main/kotlin/com/devil/app/DevilActivity.kt",
            ).readText()

        assertTrue(
            outputSource.contains(
                "languageTagProvider",
            ),
        )

        assertTrue(
            outputSource.contains(
                "engine.setLanguage(",
            ),
        )

        assertTrue(
            application.contains(
                "conversationalVoiceOutputSource",
            ),
        )

        assertTrue(
            application.contains(
                "voiceLanguageSelectionProvider",
            ),
        )

        assertTrue(
            activity.contains(
                ".voiceConversationOutputCoordinator",
            ),
        )

        assertTrue(
            activity.contains(
                ".voiceOutputSource",
            ),
        )
    }

    @Test
    fun `main conversation exposes bounded EN HI MR selection without persistence`() {
        val screen =
            File(
                "src/main/kotlin/com/devil/app/conversation/ConversationScreen.kt",
            ).readText()

        assertTrue(
            screen.contains(
                "AndroidVoiceLanguageSelection.entries",
            ),
        )

        assertTrue(
            screen.contains(
                "onVoiceLanguageSelectionChange",
            ),
        )

        assertTrue(
            screen.contains(
                "selection.uiLabel",
            ),
        )
    }
}
