package com.devil.app.conversation

/**
 * Represents the bounded result of one Stage 24 conversation runtime-submission
 * attempt.
 *
 * SUBMITTED requires one genuine trace-backed runtime presentation.
 *
 * METADATA_UNAVAILABLE contains no runtime presentation because no runtime
 * submission occurred and therefore no runtime TraceId exists.
 *
 * This contract does not fabricate runtime acceptance, execution success,
 * verified outcomes, classifications, or trace identity.
 */
@ConsistentCopyVisibility
data class ConversationRuntimeSubmissionResult private constructor(
    val status: ConversationRuntimeSubmissionStatus,
    val presentation: ConversationRuntimePresentation?,
) {
    companion object {

        fun submitted(
            presentation: ConversationRuntimePresentation,
        ): ConversationRuntimeSubmissionResult {
            return ConversationRuntimeSubmissionResult(
                status = ConversationRuntimeSubmissionStatus.SUBMITTED,
                presentation = presentation,
            )
        }

        fun metadataUnavailable(): ConversationRuntimeSubmissionResult {
            return ConversationRuntimeSubmissionResult(
                status =
                    ConversationRuntimeSubmissionStatus.METADATA_UNAVAILABLE,
                presentation = null,
            )
        }
    }
}
