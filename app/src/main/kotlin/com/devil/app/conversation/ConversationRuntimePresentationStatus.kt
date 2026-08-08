package com.devil.app.conversation

/**
 * Describes how one immediate runtime-submission result may be represented by
 * the Stage 24 conversation UI.
 *
 * ACCEPTED means only that the Unified Devil Runtime accepted the supplied work.
 * It does not mean that execution occurred or that any outcome was verified.
 *
 * DEFERRED means the runtime truthfully deferred the supplied work.
 *
 * REJECTED means the runtime rejected the supplied work with a constitutional
 * error.
 */
enum class ConversationRuntimePresentationStatus {
    ACCEPTED,
    DEFERRED,
    REJECTED,
}
