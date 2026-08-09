package com.devil.core.runtime.conversation

import com.devil.core.model.common.TraceId

/**
 * Default Stage 25 mapping from bounded conversation-persistence evaluation into
 * the stable ConversationPersistenceResult contract.
 *
 * Genuine constitutional persistence eligibility maps to PERSISTABLE.
 * Evaluation unavailability maps to DEFERRED.
 * Evaluation failure preserves its matching error.
 *
 * This mapper performs no persistence or other side effect.
 */
class DefaultConversationPersistenceResultMapper :
    ConversationPersistenceResultMapper {

    override fun map(
        traceId: TraceId,
        evaluation: ConversationPersistenceEvaluationResult,
    ): ConversationPersistenceResult {
        require(evaluation.traceId == traceId) {
            "Conversation persistence result mapper trace and evaluation result must use the same trace identity."
        }

        return when (evaluation.status) {
            ConversationPersistenceEvaluationStatus.PERSISTABLE ->
                ConversationPersistenceResult.create(
                    traceId = traceId,
                    status = ConversationPersistenceStatus.PERSISTABLE,
                    request = requireNotNull(evaluation.request),
                )

            ConversationPersistenceEvaluationStatus.UNAVAILABLE ->
                ConversationPersistenceResult.create(
                    traceId = traceId,
                    status = ConversationPersistenceStatus.DEFERRED,
                )

            ConversationPersistenceEvaluationStatus.FAILED ->
                ConversationPersistenceResult.create(
                    traceId = traceId,
                    status = ConversationPersistenceStatus.FAILED,
                    error = requireNotNull(evaluation.error),
                )
        }
    }
}
