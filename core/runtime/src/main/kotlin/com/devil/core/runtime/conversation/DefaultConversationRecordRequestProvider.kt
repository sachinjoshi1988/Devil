package com.devil.core.runtime.conversation

import com.devil.core.model.conversation.ConversationRecordRequest

/**
 * Default Stage 25 bounded conversation-record request provider.
 *
 * Any PRODUCED ConversationIntakeAuthorityResult contains one established bounded
 * ConversationIntakeResult and therefore remains available for conversation-domain
 * recording without reinterpreting whether that intake was ACCEPTED, DEFERRED, or
 * REJECTED.
 *
 * A deferred Conversation Intake Authority result remains unavailable.
 * Conversation-intake failure preserves its matching error.
 *
 * This implementation does not decide whether constitutional processing may
 * continue, create conversation identity, create a ConversationRecord, establish
 * multi-turn ordering, persist or restore conversation state, create logical
 * memory, authenticate a subject, grant authorization, execute capabilities, or
 * establish a verified outcome.
 */
class DefaultConversationRecordRequestProvider :
    ConversationRecordRequestProvider {

    override fun provide(
        conversationIntake: ConversationIntakeAuthorityResult,
    ): ConversationRecordRequestResult {
        return when (conversationIntake.status) {
            ConversationIntakeAuthorityStatus.PRODUCED ->
                ConversationRecordRequestResult.create(
                    traceId = conversationIntake.traceId,
                    status =
                        ConversationRecordRequestStatus.AVAILABLE,
                    request =
                        ConversationRecordRequest.create(
                            intake =
                                requireNotNull(
                                    conversationIntake.intake,
                                ),
                        ),
                )

            ConversationIntakeAuthorityStatus.DEFERRED ->
                ConversationRecordRequestResult.create(
                    traceId = conversationIntake.traceId,
                    status =
                        ConversationRecordRequestStatus.UNAVAILABLE,
                )

            ConversationIntakeAuthorityStatus.FAILED ->
                ConversationRecordRequestResult.create(
                    traceId = conversationIntake.traceId,
                    status =
                        ConversationRecordRequestStatus.FAILED,
                    error =
                        requireNotNull(
                            conversationIntake.error,
                        ),
                )
        }
    }
}
