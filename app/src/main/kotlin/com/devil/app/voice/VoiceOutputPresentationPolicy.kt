package com.devil.app.voice

import com.devil.app.conversation.ConversationTimelineEntry
import com.devil.app.conversation.ConversationEntryRole

/**
 * Stage 36 policy determining which already-established conversation
 * presentation may be spoken.
 *
 * Only RUNTIME timeline entries are eligible because they preserve genuine
 * runtime-backed presentation truth.
 *
 * USER entries are never treated as Devil speech.
 *
 * This policy does not generate text, reinterpret runtime status, or establish
 * success.
 */
class VoiceOutputPresentationPolicy {

    fun speakableText(
        entry: ConversationTimelineEntry,
    ): String? {
        if (entry.role != ConversationEntryRole.RUNTIME) {
            return null
        }

        val normalizedContent = entry.content.trim()

        return normalizedContent.ifEmpty {
            null
        }
    }
}
