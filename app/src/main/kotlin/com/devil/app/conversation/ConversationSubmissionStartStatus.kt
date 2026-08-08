package com.devil.app.conversation

/**
 * Describes the UI-local result of attempting to begin one conversation
 * submission.
 *
 * STARTED means one non-blank draft was converted into a bounded USER timeline
 * entry and the UI entered its submitting state.
 *
 * IGNORED_BLANK means no submission was started because the draft contained no
 * meaningful text.
 *
 * ALREADY_SUBMITTING means an existing UI submission is still in progress and
 * duplicate submission was prevented.
 *
 * None of these states imply that work entered the Unified Devil Runtime.
 */
enum class ConversationSubmissionStartStatus {
    STARTED,
    IGNORED_BLANK,
    ALREADY_SUBMITTING,
}
