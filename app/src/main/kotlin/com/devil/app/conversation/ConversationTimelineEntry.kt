package com.devil.app.conversation

import com.devil.core.model.common.TraceId
import com.devil.core.runtime.modelprovider.conversation.GeneratedAssistantResponse

/**
 * Represents one immutable presentation entry in the conversation timeline.
 *
 * A USER entry contains user-supplied text and has no runtime trace until work
 * is actually submitted.
 *
 * A RUNTIME entry represents one truth-preserving immediate runtime
 * presentation and therefore requires its matching TraceId.
 *
 * An ASSISTANT entry represents generated assistant-facing conversational text
 * and preserves the TraceId of the bounded generated response.
 *
 * ASSISTANT is deliberately distinct from RUNTIME. Generated conversational
 * text does not become runtime acceptance, constitutional Verification,
 * verified truth, verified Outcome, execution success, Learning, or Memory
 * merely because it is presented in the conversation timeline.
 *
 * This contract does not represent persistence or logical Memory.
 *
 * ASSISTANT != RUNTIME.
 * GENERATED != VERIFIED.
 */
@ConsistentCopyVisibility
data class ConversationTimelineEntry private constructor(
    val id: ConversationEntryId,
    val role: ConversationEntryRole,
    val content: String,
    val traceId: TraceId?,
) {
    companion object {

        fun user(
            id: ConversationEntryId,
            content: String,
        ): ConversationTimelineEntry {
            val normalizedContent = content.trim()

            require(normalizedContent.isNotEmpty()) {
                "User conversation entry content must not be blank."
            }

            return ConversationTimelineEntry(
                id = id,
                role = ConversationEntryRole.USER,
                content = normalizedContent,
                traceId = null,
            )
        }

        fun runtime(
            id: ConversationEntryId,
            presentation: ConversationRuntimePresentation,
        ): ConversationTimelineEntry {
            return ConversationTimelineEntry(
                id = id,
                role = ConversationEntryRole.RUNTIME,
                content = presentation.message,
                traceId = presentation.traceId,
            )
        }

        fun assistant(
            id: ConversationEntryId,
            response: GeneratedAssistantResponse,
        ): ConversationTimelineEntry {
            return ConversationTimelineEntry(
                id = id,
                role = ConversationEntryRole.ASSISTANT,
                content = response.content,
                traceId = response.traceId,
            )
        }
    }
}
