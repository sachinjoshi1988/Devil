package com.devil.app.ui.voice

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 254 Voice Interface governance tests.
 *
 * These tests protect the bounded presentation contract only.
 *
 * Stage 254 must:
 *
 * - use the existing locked Devil logo;
 * - present existing truthful voice state;
 * - preserve existing voice/hands-free callbacks;
 * - keep voice UI separate from authentication, authorization, execution,
 *   verification, and Stage 255 Memory Interface;
 * - avoid fabricating microphone amplitude or speech-understanding claims.
 */
class Stage254VoiceInterfaceTest {

    @Test
    fun `voice interface uses locked Devil logo`() {
        val source =
            voiceInterfaceSource()

        assertTrue(
            source.contains(
                "R.drawable.devil_primary_logo",
            ),
        )

        assertFalse(
            source.contains(
                "devil_launcher_foreground",
            ),
        )
    }

    @Test
    fun `voice interface presents truthful bounded voice states`() {
        val source =
            voiceInterfaceSource()

        assertTrue(
            source.contains(
                "DEVIL IS LISTENING",
            ),
        )

        assertTrue(
            source.contains(
                "DEVIL IS SPEAKING",
            ),
        )

        assertTrue(
            source.contains(
                "HANDS-FREE ACTIVE",
            ),
        )

        assertTrue(
            source.contains(
                "READY TO LISTEN",
            ),
        )
    }

    @Test
    fun `voice interface preserves constitutional presentation boundaries`() {
        val source =
            voiceInterfaceSource()

        assertTrue(
            source.contains(
                "VOICE_INTERFACE != SPEECH_RECOGNITION.",
            ),
        )

        assertTrue(
            source.contains(
                "VOICE_INTERFACE != AUTHENTICATION.",
            ),
        )

        assertTrue(
            source.contains(
                "VOICE_INTERFACE != AUTHORIZATION.",
            ),
        )

        assertTrue(
            source.contains(
                "VOICE_INTERFACE != EXECUTION.",
            ),
        )

        assertTrue(
            source.contains(
                "VOICE_INTERFACE != VERIFICATION.",
            ),
        )

        assertTrue(
            source.contains(
                "LISTENING != UNDERSTANDING.",
            ),
        )

        assertTrue(
            source.contains(
                "SPEAKING != VERIFIED_OUTCOME.",
            ),
        )

        assertTrue(
            source.contains(
                "WAKE_PHRASE != AUTHENTICATION.",
            ),
        )
    }

    @Test
    fun `voice interface consumes existing state and callbacks`() {
        val source =
            voiceInterfaceSource()

        assertTrue(
            source.contains(
                "isVoiceListening: Boolean",
            ),
        )

        assertTrue(
            source.contains(
                "isVoiceSpeaking: Boolean",
            ),
        )

        assertTrue(
            source.contains(
                "handsFreeEnabled: Boolean",
            ),
        )

        assertTrue(
            source.contains(
                "onVoiceInput: () -> Unit",
            ),
        )

        assertTrue(
            source.contains(
                "onHandsFreeToggle: () -> Unit",
            ),
        )

        assertTrue(
            source.contains(
                "voiceInputMessage: String?",
            ),
        )

        assertTrue(
            source.contains(
                "voiceOutputMessage: String?",
            ),
        )

        assertTrue(
            source.contains(
                "handsFreeMessage: String?",
            ),
        )
    }

    @Test
    fun `voice interface does not fabricate microphone amplitude`() {
        val source =
            voiceInterfaceSource()

        assertFalse(
            source.contains(
                "onRmsChanged",
            ),
        )

        assertFalse(
            source.contains(
                "AudioRecord",
            ),
        )

        assertFalse(
            source.contains(
                "MediaRecorder",
            ),
        )
    }

    @Test
    fun `conversation screen integrates dedicated Stage 254 voice interface`() {
        val source =
            conversationScreenSource()

        assertTrue(
            source.contains(
                "import com.devil.app.ui.voice.DevilVoiceInterface",
            ),
        )

        assertTrue(
            source.contains(
                "DevilVoiceInterface(",
            ),
        )

        assertTrue(
            source.contains(
                "onVoiceInput = onVoiceInput",
            ),
        )

        assertTrue(
            source.contains(
                "onHandsFreeToggle = onHandsFreeToggle",
            ),
        )

        assertTrue(
            source.contains(
                "isSubmitting = state.isSubmitting",
            ),
        )
    }

    @Test
    fun `Stage 254 does not implement Stage 255 Memory Interface`() {
        val source =
            voiceInterfaceSource()

        assertTrue(
            source.contains(
                "Stage 254 does not implement Stage 255 Memory Interface.",
            ),
        )

        assertFalse(
            source.contains(
                "MemoryCoordinator",
            ),
        )

        assertFalse(
            source.contains(
                "MemoryAuthority",
            ),
        )

        assertFalse(
            source.contains(
                "LogicalMemoryRepresentation",
            ),
        )
    }

    private fun voiceInterfaceSource(): String {
        return source(
            "app/src/main/kotlin/com/devil/app/ui/voice/DevilVoiceInterface.kt",
            "src/main/kotlin/com/devil/app/ui/voice/DevilVoiceInterface.kt",
        )
    }

    private fun conversationScreenSource(): String {
        return source(
            "app/src/main/kotlin/com/devil/app/conversation/ConversationScreen.kt",
            "src/main/kotlin/com/devil/app/conversation/ConversationScreen.kt",
        )
    }

    private fun source(
        vararg candidates: String,
    ): String {
        val file =
            candidates
                .map(::File)
                .firstOrNull(File::isFile)

        requireNotNull(file) {
            "Unable to locate Stage 254 production source."
        }

        return file.readText()
    }
}
