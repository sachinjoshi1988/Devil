package com.devil.app.conversation

/**
 * Coordinates bounded Stage 24 conversation UI-state transitions.
 *
 * This coordinator manages only presentation state:
 *
 * - draft updates,
 * - preparation of one USER timeline entry,
 * - duplicate-submission protection,
 * - and attachment of a truthful runtime presentation after an external
 *   submission boundary has completed.
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
            entries = state.entries + runtimeEntry,
            isSubmitting = false,
        )
    }
}
