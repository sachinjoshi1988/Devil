package com.devil.app.voice

import com.devil.app.conversation.ConversationEntryRole
import com.devil.app.conversation.ConversationTimelineEntry

/**
 * Determines which already-established conversation presentation may be spoken.
 *
 * RUNTIME entries remain eligible as truthful immediate runtime presentation.
 *
 * Stage 337M additionally permits bounded KNOWLEDGE entries to be spoken without
 * relabeling them as RuntimeStatus, generated model output, Verification, or
 * Outcome.
 *
 * USER, ASSISTANT, and OUTCOME remain outside this policy.
 *
 * This policy does not generate text, reinterpret runtime status, establish a
 * device fact, establish success, or perform Verification.
 *
 * SPOKEN_DEVICE_KNOWLEDGE != RUNTIME_STATUS.
 * SPOKEN_DEVICE_KNOWLEDGE != VERIFIED_OUTCOME.
 */
class VoiceOutputPresentationPolicy {

    fun speakableText(
        entry: ConversationTimelineEntry,
    ): String? {
        if (
            entry.role != ConversationEntryRole.RUNTIME &&
            entry.role != ConversationEntryRole.KNOWLEDGE
        ) {
            return null
        }

        val normalizedContent =
            entry.content.trim()

        return normalizedContent.ifEmpty {
            null
        }
    }

    /**
     * Returns the newest already-established entry that this same presentation
     * policy permits Devil to speak.
     *
     * Entry order is presentation order only. Selecting an entry here does not
     * change its role, establish a device fact, reinterpret RuntimeStatus,
     * authorize work, or establish Verification or Outcome.
     *
     * SPEAKABLE_ENTRY_SELECTED != AUTHORIZATION.
     * SPEAKABLE_ENTRY_SELECTED != VERIFIED_OUTCOME.
     */
    fun newestSpeakableEntry(
        entries: List<ConversationTimelineEntry>,
    ): ConversationTimelineEntry? {
        return entries.lastOrNull { entry ->
            speakableText(entry) != null
        }
    }
}
