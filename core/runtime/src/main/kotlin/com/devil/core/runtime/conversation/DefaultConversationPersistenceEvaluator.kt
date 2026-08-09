package com.devil.core.runtime.conversation

import com.devil.core.model.common.TraceId
import com.devil.core.model.conversation.ConversationPersistenceRequest

/**
 * Default Stage 25 constitutional conversation-persistence evaluator.
 *
 * No approved production conversation-persistence policy, storage destination,
 * encryption policy, retention policy, deletion policy, restoration mechanism,
 * persistence evidence source, or authorized durable conversation store exists
 * yet.
 *
 * Therefore this evaluator preserves trace continuity and returns UNAVAILABLE
 * rather than treating a ConversationPersistenceRequest as permission to persist
 * conversation state.
 *
 * It invokes no database, filesystem, cloud service, Android platform API,
 * network service, or external communication mechanism.
 *
 * It does not create logical memory, authenticate a subject, grant
 * authorization, execute capabilities, or establish a verified outcome.
 */
class DefaultConversationPersistenceEvaluator :
    ConversationPersistenceEvaluator {

    override fun evaluate(
        traceId: TraceId,
        request: ConversationPersistenceRequest,
    ): ConversationPersistenceEvaluationResult {
        require(
            request.record.intake.record.input.context.traceId ==
                traceId,
        ) {
            "Conversation persistence evaluator trace and request must use the same trace identity."
        }

        return ConversationPersistenceEvaluationResult.create(
            traceId = traceId,
            status =
                ConversationPersistenceEvaluationStatus.UNAVAILABLE,
        )
    }
}
