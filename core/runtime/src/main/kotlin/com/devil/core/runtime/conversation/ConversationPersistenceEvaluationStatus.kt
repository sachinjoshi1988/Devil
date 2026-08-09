package com.devil.core.runtime.conversation

/**
 * Describes the bounded result of constitutional conversation-persistence
 * evaluation.
 *
 * PERSISTABLE means genuine constitutional evidence established that one
 * bounded ConversationPersistenceRequest may proceed to a later explicitly
 * authorized persistence mechanism.
 *
 * UNAVAILABLE means no justified conversation persistence can currently
 * proceed.
 *
 * FAILED represents an operational persistence-evaluation failure.
 *
 * This status does not persist, restore, durably store, order, replicate,
 * encrypt, delete, expose, or recall conversation state.
 *
 * It does not create conversation identity, create logical memory, authenticate
 * a subject, grant authorization, execute capabilities, or establish a verified
 * outcome.
 */
enum class ConversationPersistenceEvaluationStatus {
    PERSISTABLE,
    UNAVAILABLE,
    FAILED,
}
