package com.devil.core.runtime.conversation

import com.devil.core.model.common.TraceId
import com.devil.core.model.conversation.ConversationPersistenceRequest
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the bounded result of constitutional conversation-persistence
 * evaluation.
 *
 * A persistable result preserves one evaluated ConversationPersistenceRequest.
 * Preserving that request does not mean conversation state was persisted.
 *
 * An unavailable result contains neither request nor error.
 *
 * A failed result contains one matching error.
 *
 * This result performs no storage, restoration, replication, encryption,
 * deletion, exposure, recall, logical-memory mutation, capability execution,
 * or verified-outcome establishment.
 */
@ConsistentCopyVisibility
data class ConversationPersistenceEvaluationResult private constructor(
    val traceId: TraceId,
    val status: ConversationPersistenceEvaluationStatus,
    val request: ConversationPersistenceRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: ConversationPersistenceEvaluationStatus,
            request: ConversationPersistenceRequest? = null,
            error: UniversalErrorRecord? = null,
        ): ConversationPersistenceEvaluationResult {
            when (status) {
                ConversationPersistenceEvaluationStatus.PERSISTABLE -> {
                    require(request != null && error == null) {
                        "Persistable conversation persistence evaluation results require a request and must not contain an error."
                    }
                }

                ConversationPersistenceEvaluationStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable conversation persistence evaluation results must not contain a request or error."
                    }
                }

                ConversationPersistenceEvaluationStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed conversation persistence evaluation results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.record.intake.record.input.context.traceId ==
                    traceId,
            ) {
                "Conversation persistence evaluation result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Conversation persistence evaluation result and error must use the same trace identity."
            }

            return ConversationPersistenceEvaluationResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
