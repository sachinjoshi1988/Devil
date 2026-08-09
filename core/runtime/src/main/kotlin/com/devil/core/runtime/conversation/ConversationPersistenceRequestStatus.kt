package com.devil.core.runtime.conversation

/**
 * Describes whether one bounded conversation-persistence request is available.
 *
 * AVAILABLE means one existing ConversationRecord is available for controlled
 * conversation-persistence evaluation.
 *
 * UNAVAILABLE means no justified conversation-persistence request can currently
 * be prepared.
 *
 * FAILED means request preparation failed with one matching error.
 *
 * This status does not persist, restore, durably store, order, replicate,
 * encrypt, delete, expose, or recall conversation state.
 *
 * It does not create conversation identity, create logical memory, authenticate
 * a subject, grant authorization, execute capabilities, or establish a verified
 * outcome.
 */
enum class ConversationPersistenceRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
