package com.devil.app.conversation

/**
 * Describes the bounded result of attempting to submit one prepared
 * conversation input through the Android runtime boundary.
 *
 * SUBMITTED means complete runtime-input metadata was available and the input
 * was submitted exactly once through AndroidRuntimeInputCoordinator.
 *
 * METADATA_UNAVAILABLE means the required runtime-input metadata was not
 * available, so no ContextEnvelope was created and no runtime submission
 * occurred.
 *
 * Neither state represents execution success or a verified outcome.
 */
enum class ConversationRuntimeSubmissionStatus {
    SUBMITTED,
    METADATA_UNAVAILABLE,
}
