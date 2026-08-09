package com.devil.core.runtime.conversation

import com.devil.core.model.common.TraceId
import com.devil.core.model.conversation.ConversationPersistenceRequest

/**
 * Evaluates one bounded ConversationPersistenceRequest before any later
 * explicitly authorized persistence mechanism may be used.
 *
 * An evaluator must not establish persistence eligibility without genuine
 * approved policy and evidence.
 *
 * It must never persist, restore, durably store, order, replicate, encrypt,
 * delete, expose, or recall conversation state directly.
 *
 * It does not create conversation identity, create logical memory, authenticate
 * a subject, grant authorization, execute capabilities, or establish a verified
 * outcome.
 */
interface ConversationPersistenceEvaluator {

    fun evaluate(
        traceId: TraceId,
        request: ConversationPersistenceRequest,
    ): ConversationPersistenceEvaluationResult
}
