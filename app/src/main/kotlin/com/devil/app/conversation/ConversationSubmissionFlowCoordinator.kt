package com.devil.app.conversation

/**
 * Coordinates one complete bounded Stage 24 conversation-submission flow.
 *
 * The flow begins from existing ConversationUiState and may:
 *
 * - reject a blank draft without submission,
 * - preserve an already-active submission without duplication,
 * - prepare one USER presentation entry,
 * - submit the prepared text through ConversationRuntimeSubmissionCoordinator,
 * - attach one genuine runtime presentation when runtime submission occurred,
 * - or truthfully complete without a runtime entry when required metadata was
 *   unavailable.
 *
 * This coordinator does not choose constitutional metadata, create
 * ContextEnvelope, generate TraceId, invoke UnifiedDevilRuntime directly,
 * execute capabilities, persist conversation state, create logical memory, or
 * fabricate runtime results or verified outcomes.
 */
interface ConversationSubmissionFlowCoordinator {

    fun submit(
        state: ConversationUiState,
    ): ConversationUiState
}
