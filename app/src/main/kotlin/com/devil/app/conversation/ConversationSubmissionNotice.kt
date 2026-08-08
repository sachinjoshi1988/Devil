package com.devil.app.conversation

/**
 * Represents one truthful UI-local conversation-submission notice.
 *
 * A notice exists only to explain a submission condition that occurred before
 * any runtime submission.
 *
 * It carries no TraceId because no constitutional runtime flow is represented.
 *
 * It does not fabricate runtime acceptance, rejection, execution, verification,
 * outcome, authority, or conversation persistence.
 */
@ConsistentCopyVisibility
data class ConversationSubmissionNotice private constructor(
    val status: ConversationSubmissionNoticeStatus,
    val message: String,
) {
    companion object {

        fun metadataUnavailable(): ConversationSubmissionNotice {
            return ConversationSubmissionNotice(
                status =
                    ConversationSubmissionNoticeStatus.METADATA_UNAVAILABLE,
                message =
                    "Runtime submission is unavailable because required constitutional metadata is not available.",
            )
        }
    }
}
