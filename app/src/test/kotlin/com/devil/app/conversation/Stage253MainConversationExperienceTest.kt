package com.devil.app.conversation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Stage 253 Main Conversation Experience governance.
 *
 * Protects the approved futuristic presentation while preserving established
 * conversation truth and Stage 254 boundaries.
 */
class Stage253MainConversationExperienceTest {

    @Test
    fun `main conversation uses approved Devil visual identity`() {
        val source = conversationScreenSource()

        assertTrue(
            source.contains("Stage 253 Main Conversation Experience"),
        )
        assertTrue(
            source.contains("R.drawable.devil_primary_logo"),
        )
        assertTrue(
            source.contains("\"DEVIL\""),
        )
        assertTrue(
            source.contains("\"MAIN CONVERSATION\""),
        )
        assertTrue(
            source.contains("MaterialTheme.colorScheme.primary"),
        )
    }

    @Test
    fun `timeline distinguishes owner and Devil presentation without altering entry truth`() {
        val source = conversationScreenSource()

        assertTrue(
            source.contains("ConversationEntryRole.USER"),
        )
        assertTrue(
            source.contains("ConversationEntryRole.RUNTIME"),
        )
        assertTrue(
            source.contains("text = entry.content"),
        )
        assertTrue(
            source.contains("OwnerConversationCard"),
        )
        assertTrue(
            source.contains("DevilConversationCard"),
        )
    }

    @Test
    fun `existing interaction callbacks remain wired into the presentation`() {
        val source = conversationScreenSource()

        assertTrue(source.contains("onDraftChange = onDraftChange"))
        assertTrue(source.contains("onClick = onSubmit"))
        assertTrue(source.contains("onVoiceInput = onVoiceInput"))
        assertTrue(source.contains("onHandsFreeToggle = onHandsFreeToggle"))
    }

    @Test
    fun `existing enablement boundaries remain represented`() {
        val source = conversationScreenSource()

        assertTrue(source.contains("!isSubmitting"))
        assertTrue(source.contains("!isVoiceListening"))
        assertTrue(source.contains("!isVoiceSpeaking"))
        assertTrue(source.contains("!handsFreeEnabled"))
        assertTrue(source.contains("draft.isNotBlank()"))
        assertTrue(source.contains("voiceInputEnabled"))
    }

    @Test
    fun `changing presentation state preserves accessibility live region`() {
        val source = conversationScreenSource()

        assertTrue(
            source.contains("LiveRegionMode.Polite"),
        )
        assertTrue(
            source.contains("politeAccessibilityStatus"),
        )
        assertTrue(
            source.contains("\"Conversation timeline\""),
        )
    }

    @Test
    fun `stage 253 does not fabricate unsupported conversation metadata`() {
        val source = conversationScreenSource()

        assertFalse(source.contains("timestamp ="))
        assertFalse(source.contains(".timestamp"))
        assertFalse(source.contains("text = \"Delivered\""))
        assertFalse(source.contains("text = \"Read\""))
        assertFalse(source.contains("readReceipt ="))
        assertFalse(source.contains("verifiedBadge ="))
    }

    @Test
    fun `stage 253 explicitly preserves constitutional presentation boundaries`() {
        val source = conversationScreenSource()

        assertTrue(
            source.contains("CONVERSATION_PRESENTATION != RUNTIME_TRUTH"),
        )
        assertTrue(
            source.contains("CONVERSATION_PRESENTATION != AUTHENTICATION"),
        )
        assertTrue(
            source.contains("CONVERSATION_PRESENTATION != AUTHORIZATION"),
        )
        assertTrue(
            source.contains("CONVERSATION_PRESENTATION != EXECUTION"),
        )
        assertTrue(
            source.contains("CONVERSATION_PRESENTATION != VERIFICATION"),
        )
        assertTrue(
            source.contains("CONVERSATION_PRESENTATION != MEMORY"),
        )
        assertTrue(
            source.contains("implement Stage 254 Voice Interface"),
        )
    }

    private fun conversationScreenSource(): String {
        val candidates =
            listOf(
                File(
                    "app/src/main/kotlin/com/devil/app/conversation/ConversationScreen.kt",
                ),
                File(
                    "src/main/kotlin/com/devil/app/conversation/ConversationScreen.kt",
                ),
            )

        return candidates
            .firstOrNull { it.exists() }
            ?.readText()
            ?: error(
                "Unable to locate production ConversationScreen source.",
            )
    }
}
