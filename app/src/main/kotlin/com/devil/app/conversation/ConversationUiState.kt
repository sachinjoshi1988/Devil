package com.devil.app.conversation

/**
 * Represents the bounded immutable state rendered by the Stage 24 conversation
 * screen.
 *
 * The timeline contains presentation entries only. Draft text is local UI input
 * that has not necessarily entered the constitutional runtime.
 *
 * isSubmitting represents only whether one UI submission operation is currently
 * in progress. It does not represent execution progress or verified outcome
 * state.
 *
 * This contract performs no runtime submission, persistence, memory mutation,
 * capability execution, or outcome fabrication.
 */
data class ConversationUiState(
    val entries: List<ConversationTimelineEntry> = emptyList(),
    val draft: String = "",
    val isSubmitting: Boolean = false,
) {
    init {
        require(
            entries.map { it.id }.distinct().size == entries.size,
        ) {
            "Conversation timeline entries must use unique presentation identities."
        }
    }
}
