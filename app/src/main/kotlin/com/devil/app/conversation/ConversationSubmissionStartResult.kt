package com.devil.app.conversation

/**
 * Represents the UI-local result of attempting to begin one conversation
 * submission.
 *
 * content is present only when STARTED and represents the normalized text that
 * may later be handed to a properly authorized runtime-submission boundary.
 *
 * This result does not create constitutional context, select classifications,
 * generate a TraceId, invoke the runtime, or represent runtime acceptance.
 */
@ConsistentCopyVisibility
data class ConversationSubmissionStartResult private constructor(
    val status: ConversationSubmissionStartStatus,
    val state: ConversationUiState,
    val content: String?,
) {
    companion object {

        fun started(
            state: ConversationUiState,
            content: String,
        ): ConversationSubmissionStartResult {
            val normalizedContent = content.trim()

            require(normalizedContent.isNotEmpty()) {
                "Started conversation submission requires non-blank content."
            }

            require(state.isSubmitting) {
                "Started conversation submission requires submitting UI state."
            }

            return ConversationSubmissionStartResult(
                status = ConversationSubmissionStartStatus.STARTED,
                state = state,
                content = normalizedContent,
            )
        }

        fun ignoredBlank(
            state: ConversationUiState,
        ): ConversationSubmissionStartResult {
            return ConversationSubmissionStartResult(
                status = ConversationSubmissionStartStatus.IGNORED_BLANK,
                state = state,
                content = null,
            )
        }

        fun alreadySubmitting(
            state: ConversationUiState,
        ): ConversationSubmissionStartResult {
            require(state.isSubmitting) {
                "Already-submitting result requires submitting UI state."
            }

            return ConversationSubmissionStartResult(
                status =
                    ConversationSubmissionStartStatus.ALREADY_SUBMITTING,
                state = state,
                content = null,
            )
        }
    }
}
