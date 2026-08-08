package com.devil.app.conversation

/**
 * Coordinates one prepared textual conversation input toward the existing
 * Android runtime-input boundary.
 *
 * The coordinator must obtain complete runtime-input metadata before submission.
 *
 * It does not choose or invent schema version, source, trust classification, or
 * security classification. It does not create ContextEnvelope directly, create
 * TraceId, interpret input, make decisions, execute capabilities, persist
 * conversations, or create logical memory.
 */
interface ConversationRuntimeSubmissionCoordinator {

    fun submit(
        content: String,
    ): ConversationRuntimeSubmissionResult
}
