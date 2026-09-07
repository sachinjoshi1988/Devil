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
 * - presentation-only attachment of one already-established trace-backed
 *   Outcome,
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

    /**
     * Stage 313 presentation-only attachment of one already-generated assistant response.
     *
     * This method does not invoke a model, perform runtime submission, establish
     * constitutional Verification, create an Outcome, execute capabilities, perform
     * Learning, or create/persist Memory.
     *
     * GENERATED != VERIFIED.
     * ASSISTANT != RUNTIME.
     */
    fun appendGeneratedAssistantResponse(
        state: ConversationUiState,
        assistantEntryId: ConversationEntryId,
        response: com.devil.core.runtime.modelprovider.conversation.GeneratedAssistantResponse,
    ): ConversationUiState {
        require(!state.isSubmitting) {
            "Generated assistant response may be appended only after runtime submission has completed."
        }

        val assistantEntry =
            ConversationTimelineEntry.assistant(
                id = assistantEntryId,
                response = response,
            )

        return state.copy(
            entries =
                boundCompletedTimeline(
                    entries = state.entries + assistantEntry,
                ),
            submissionNotice = null,
        )
    }

    /**
     * Stage 337M presentation-only attachment of one already-obtained bounded
     * trace-backed knowledge result.
     *
     * This method does not read a platform fact, select a capability, establish
     * authorization, create an ExecutionRequest, reinterpret RuntimeStatus,
     * establish Verification or Outcome, update the World Model, perform
     * Learning, or create/persist Memory.
     *
     * KNOWLEDGE != RUNTIME.
     * KNOWLEDGE != VERIFIED_OUTCOME.
     * KNOWLEDGE != MEMORY.
     */
    fun appendKnowledge(
        state: ConversationUiState,
        knowledgeEntryId: ConversationEntryId,
        traceId: com.devil.core.model.common.TraceId,
        message: String,
    ): ConversationUiState {
        require(!state.isSubmitting) {
            "Knowledge presentation may be appended only after runtime submission has completed."
        }

        val knowledgeEntry =
            ConversationTimelineEntry.knowledge(
                id = knowledgeEntryId,
                traceId = traceId,
                content = message,
            )

        return state.copy(
            entries =
                boundCompletedTimeline(
                    entries = state.entries + knowledgeEntry,
                ),
            submissionNotice = null,
        )
    }

    /**
     * Stage 314 presentation-only attachment of one already-established
     * trace-backed Android Outcome.
     *
     * This method does not perform Verification, establish Outcome, reinterpret
     * RuntimeStatus, claim task completion, update the World Model, perform
     * Learning, or create/persist Memory.
     *
     * OUTCOME != RUNTIME.
     * OUTCOME_ESTABLISHED != TASK_COMPLETED.
     */
    fun appendEstablishedOutcome(
        state: ConversationUiState,
        outcomeEntryId: ConversationEntryId,
        traceId: com.devil.core.model.common.TraceId,
        message: String,
    ): ConversationUiState {
        require(!state.isSubmitting) {
            "Established outcome may be appended only after runtime submission has completed."
        }

        val outcomeEntry =
            ConversationTimelineEntry.outcome(
                id = outcomeEntryId,
                traceId = traceId,
                content = message,
            )

        return state.copy(
            entries =
                boundCompletedTimeline(
                    entries = state.entries + outcomeEntry,
                ),
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
