package com.devil.core.runtime.conversation

/**
 * Describes whether one bounded conversation-record request is available.
 *
 * AVAILABLE means one existing produced ConversationIntakeResult is available
 * for bounded conversation-record formation.
 *
 * UNAVAILABLE means no justified conversation-record request can currently be
 * prepared.
 *
 * FAILED means request preparation failed with one matching error.
 *
 * This status does not create conversation identity, create a ConversationRecord,
 * establish multi-turn ordering, persist or restore conversation state, create
 * logical memory, authenticate a subject, grant authorization, execute
 * capabilities, or establish a verified outcome.
 */
enum class ConversationRecordRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
