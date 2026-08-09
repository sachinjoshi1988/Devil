package com.devil.core.runtime.conversation

/**
 * Describes the stable operational result of bounded conversation-persistence
 * evaluation.
 *
 * PERSISTABLE means genuine constitutional persistence eligibility was
 * established for one bounded ConversationPersistenceRequest.
 *
 * DEFERRED means conversation persistence is not currently justified or
 * available.
 *
 * FAILED means conversation-persistence evaluation failed with one matching
 * error.
 *
 * PERSISTABLE does not mean that conversation state was persisted, restored,
 * durably stored, ordered, replicated, encrypted, deleted, exposed, or recalled.
 *
 * This status does not create logical memory, authenticate a subject, grant
 * authorization, execute capabilities, or establish a verified outcome.
 */
enum class ConversationPersistenceStatus {
    PERSISTABLE,
    DEFERRED,
    FAILED,
}
