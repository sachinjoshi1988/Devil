package com.devil.app.conversation

import com.devil.core.model.common.TraceId

/**
 * Represents one immutable presentation entry in the Stage 24 conversation
 * timeline.
 *
 * A USER entry contains user-supplied text and has no runtime trace until work
 * is actually submitted.
 *
 * A RUNTIME entry represents one truth-preserving immediate runtime
 * presentation and therefore requires its matching TraceId.
 *
 * This contract does not represent persistence, logical memory, verified
 * execution success, or a generated Devil conversational response.
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
    }
}
