package com.devil.app.conversation

/**
 * Default Stage 24 conversation-submission flow coordinator.
 *
 * Presentation-state transitions remain delegated to
 * ConversationInteractionCoordinator.
 *
 * UI-only presentation identity remains delegated to ConversationEntryIdProvider.
 *
 * Runtime submission remains delegated to ConversationRuntimeSubmissionCoordinator.
 *
 * A runtime timeline entry is created only when a genuine runtime-backed
 * ConversationRuntimePresentation is returned.
 *
 * If required runtime-input metadata is unavailable, the submitting state is
 * completed truthfully through the UI-local metadata-unavailable notice and no
 * runtime entry or fabricated TraceId is created.
 */
class DefaultConversationSubmissionFlowCoordinator(
    private val interactionCoordinator: ConversationInteractionCoordinator =
        ConversationInteractionCoordinator(),
    private val entryIdProvider: ConversationEntryIdProvider =
        DefaultConversationEntryIdProvider(),
    private val runtimeSubmissionCoordinator: ConversationRuntimeSubmissionCoordinator,
) : ConversationSubmissionFlowCoordinator {

    override fun submit(
        state: ConversationUiState,
    ): ConversationUiState {
        val startResult =
            interactionCoordinator.beginSubmission(
                state = state,
                userEntryId = entryIdProvider.provide(),
            )

        return when (startResult.status) {
            ConversationSubmissionStartStatus.IGNORED_BLANK ->
                startResult.state

            ConversationSubmissionStartStatus.ALREADY_SUBMITTING ->
                startResult.state

            ConversationSubmissionStartStatus.STARTED -> {
                val content = requireNotNull(startResult.content)

                val submissionResult =
                    runtimeSubmissionCoordinator.submit(
                        content = content,
                    )

                when (submissionResult.status) {
                    ConversationRuntimeSubmissionStatus.METADATA_UNAVAILABLE ->
                        interactionCoordinator.completeMetadataUnavailable(
                            state = startResult.state,
                        )

                    ConversationRuntimeSubmissionStatus.SUBMITTED ->
                        interactionCoordinator.completeSubmission(
                            state = startResult.state,
                            runtimeEntryId = entryIdProvider.provide(),
                            presentation =
                                requireNotNull(
                                    submissionResult.presentation,
                                ),
                        )
                }
            }
        }
    }
}
