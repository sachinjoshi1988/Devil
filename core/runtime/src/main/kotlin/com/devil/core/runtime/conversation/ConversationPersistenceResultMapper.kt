package com.devil.core.runtime.conversation

import com.devil.core.model.common.TraceId

/**
 * Maps one bounded conversation-persistence evaluation into the stable
 * ConversationPersistenceResult contract.
 *
 * This mapper does not persist, restore, durably store, order, replicate,
 * encrypt, delete, expose, or recall conversation state.
 *
 * It does not create conversation identity, logical memory, authorization,
 * capability execution, or verified outcome evidence.
 */
interface ConversationPersistenceResultMapper {

    fun map(
        traceId: TraceId,
        evaluation: ConversationPersistenceEvaluationResult,
    ): ConversationPersistenceResult
}
