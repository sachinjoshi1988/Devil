package com.devil.app.conversation

/**
 * Describes one truthful UI-local notice associated with a conversation
 * submission attempt.
 *
 * METADATA_UNAVAILABLE means the constitutional metadata required to enter the
 * Android runtime boundary was not available, so no runtime submission occurred.
 *
 * This status is presentation state only. It does not represent a RuntimeResult,
 * constitutional rejection, execution failure, verified outcome, or TraceId.
 */
enum class ConversationSubmissionNoticeStatus {
    METADATA_UNAVAILABLE,
}
