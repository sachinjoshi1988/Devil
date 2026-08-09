package com.devil.core.runtime.conversation

import com.devil.core.model.conversation.ConversationPersistenceRequest

/**
 * Default Stage 25 bounded conversation-persistence request provider.
 *
 * A request is available only when conversation-record formation produced one
 * bounded ConversationRecord.
 *
 * A deferred conversation-record result remains unavailable.
 * Conversation-record failure preserves its matching error.
 *
 * Availability establishes only that one bounded persistence request can be
 * evaluated by a later explicitly authorized conversation-persistence
 * mechanism.
 *
 * This implementation does not persist, restore, durably store, order,
 * replicate, encrypt, delete, expose, or recall conversation state.
 *
 * It invokes no database, filesystem, cloud service, Android platform API,
 * network service, or external communication mechanism.
 *
 * It does not create conversation identity, create another ConversationRecord,
 * create logical memory, authenticate a subject, grant authorization, execute
 * capabilities, or establish a verified outcome.
 */
class DefaultConversationPersistenceRequestProvider :
    ConversationPersistenceRequestProvider {

    override fun provide(
        conversationRecord: ConversationRecordResult,
    ): ConversationPersistenceRequestResult {
        return when (conversationRecord.status) {
            ConversationRecordStatus.PRODUCED ->
                ConversationPersistenceRequestResult.create(
                    traceId = conversationRecord.traceId,
                    status =
                        ConversationPersistenceRequestStatus.AVAILABLE,
                    request =
                        ConversationPersistenceRequest.create(
                            record =
                                requireNotNull(
                                    conversationRecord.record,
                                ),
                        ),
                )

            ConversationRecordStatus.DEFERRED ->
                ConversationPersistenceRequestResult.create(
                    traceId = conversationRecord.traceId,
                    status =
                        ConversationPersistenceRequestStatus.UNAVAILABLE,
                )

            ConversationRecordStatus.FAILED ->
                ConversationPersistenceRequestResult.create(
                    traceId = conversationRecord.traceId,
                    status =
                        ConversationPersistenceRequestStatus.FAILED,
                    error =
                        requireNotNull(
                            conversationRecord.error,
                        ),
                )
        }
    }
}
