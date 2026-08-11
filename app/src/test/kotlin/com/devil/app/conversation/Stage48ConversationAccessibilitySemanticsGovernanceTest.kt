package com.devil.app.conversation

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Stage48ConversationAccessibilitySemanticsGovernanceTest {

    @Test
    fun `conversation surface exposes bounded heading semantics`() {
        val source =
            conversationScreenSource()

        assertTrue(
            source.contains(
                "import androidx.compose.ui.semantics.heading",
            ),
        )

        assertTrue(
            source.contains(
                "heading()",
            ),
        )
    }

    @Test
    fun `changing conversation status uses polite accessibility live region`() {
        val source =
            conversationScreenSource()

        assertTrue(
            source.contains(
                "LiveRegionMode.Polite",
            ),
        )

        assertTrue(
            source.contains(
                "politeAccessibilityStatus()",
            ),
        )

        assertTrue(
            source.contains(
                "liveRegion =",
            ),
        )
    }

    @Test
    fun `accessibility semantics remain presentation only`() {
        val source =
            conversationScreenSource()

        assertFalse(
            source.contains(
                "DefaultUnifiedDevilRuntime(",
            ),
        )

        assertFalse(
            source.contains(
                "ConversationInput.create(",
            ),
        )

        assertFalse(
            source.contains(
                "MemoryProposal",
            ),
        )

        assertFalse(
            source.contains(
                "performAction(",
            ),
        )
    }

    private fun conversationScreenSource(): String {
        val candidates =
            listOf(
                File(
                    "src/main/kotlin/com/devil/app/conversation/ConversationScreen.kt",
                ),
                File(
                    "app/src/main/kotlin/com/devil/app/conversation/ConversationScreen.kt",
                ),
            )

        val sourceFile =
            candidates.firstOrNull {
                it.isFile
            }
                ?: error(
                    "Unable to locate production ConversationScreen source.",
                )

        return sourceFile.readText()
    }
}
