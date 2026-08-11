package com.devil.app.conversation

/**
 * Coordinates bounded conversation UI-state transitions.
 *
 * This coordinator manages only presentation state:
 *
 * - draft updates,
 * - preparation of one USER timeline entry,
 * - duplicate-submission protection,
 * - attachment of a truthful runtime presentation after an external submission
 *   boundary has completed,
 * - truthful completion of a submission attempt that never entered the runtime
 *   because required metadata was unavailable,
 * - and bounded retention of completed presentation timeline entries.
 *
 * The presentation timeline is resource-bounded only after a submission reaches
 * a terminal UI state. An active submission is never trimmed after its USER
 * entry is appended and before that submission completes.
 *
 * Timeline bounding affects only UI presentation history. It does not mutate
 * logical Memory, persisted conversation data, runtime state, constitutional
 * state, verified outcomes, or any other authority.
 *
 * It does not choose constitutional classifications, create ContextEnvelope,
 * generate TraceId, invoke AndroidRuntimeInputCoordinator, call the Unified
 * Devil Runtime, execute capabilities, persist conversations, create memory, or
 * fabricate Devil responses or outcomes.
 */
class ConversationInteractionCoordinator {

    fun updateDraft(
        state: ConversationUiState,
        draft: String,
    ): ConversationUiState {
        if (state.isSubmitting) {
            return state
        }

        return state.copy(
            draft = draft,
            submissionNotice = null,
        )
    }

    fun beginSubmission(
        state: ConversationUiState,
        userEntryId: ConversationEntryId,
    ): ConversationSubmissionStartResult {
        if (state.isSubmitting) {
            return ConversationSubmissionStartResult.alreadySubmitting(
                state = state,
            )
        }

        val normalizedDraft = state.draft.trim()

        if (normalizedDraft.isEmpty()) {
            return ConversationSubmissionStartResult.ignoredBlank(
                state = state,
            )
        }

        val userEntry =
            ConversationTimelineEntry.user(
                id = userEntryId,
                content = normalizedDraft,
            )

        val submittingState =
            state.copy(
                entries = state.entries + userEntry,
                draft = "",
                isSubmitting = true,
                submissionNotice = null,
            )

        return ConversationSubmissionStartResult.started(
            state = submittingState,
            content = normalizedDraft,
        )
    }

    fun completeSubmission(
        state: ConversationUiState,
        runtimeEntryId: ConversationEntryId,
        presentation: ConversationRuntimePresentation,
    ): ConversationUiState {
        require(state.isSubmitting) {
            "Conversation submission completion requires submitting UI state."
        }

        val runtimeEntry =
            ConversationTimelineEntry.runtime(
                id = runtimeEntryId,
                presentation = presentation,
            )

        return state.copy(
            entries =
                boundCompletedTimeline(
                    entries = state.entries + runtimeEntry,
                ),
            isSubmitting = false,
            submissionNotice = null,
        )
    }

    fun completeMetadataUnavailable(
        state: ConversationUiState,
    ): ConversationUiState {
        require(state.isSubmitting) {
            "Metadata-unavailable completion requires submitting UI state."
        }

        return state.copy(
            entries =
                boundCompletedTimeline(
                    entries = state.entries,
                ),
            isSubmitting = false,
            submissionNotice =
                ConversationSubmissionNotice.metadataUnavailable(),
        )
    }

    private fun boundCompletedTimeline(
        entries: List<ConversationTimelineEntry>,
    ): List<ConversationTimelineEntry> {
        if (entries.size <= MAX_PRESENTATION_TIMELINE_ENTRIES) {
            return entries
        }

        return entries.takeLast(
            MAX_PRESENTATION_TIMELINE_ENTRIES,
        )
    }

    companion object {
        internal const val MAX_PRESENTATION_TIMELINE_ENTRIES: Int =
            100
    }
}
