package com.devil.core.runtime.conversation

import com.devil.core.model.common.TraceId
import com.devil.core.model.conversation.ConversationPersistenceRequest
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the stable operational result of bounded conversation-persistence
 * evaluation.
 *
 * A persistable result preserves one ConversationPersistenceRequest for which
 * genuine constitutional persistence eligibility was established.
 *
 * Preserving that request does not mean conversation state was persisted.
 *
 * A deferred result contains neither request nor error.
 *
 * A failed result contains one matching error.
 *
 * This result performs no storage, restoration, replication, encryption,
 * deletion, exposure, recall, logical-memory mutation, capability execution,
 * or verified-outcome establishment.
 */
@ConsistentCopyVisibility
data class ConversationPersistenceResult private constructor(
    val traceId: TraceId,
    val status: ConversationPersistenceStatus,
    val request: ConversationPersistenceRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: ConversationPersistenceStatus,
            request: ConversationPersistenceRequest? = null,
            error: UniversalErrorRecord? = null,
        ): ConversationPersistenceResult {
            when (status) {
                ConversationPersistenceStatus.PERSISTABLE -> {
                    require(request != null && error == null) {
                        "Persistable conversation persistence results require a request and must not contain an error."
                    }
                }

                ConversationPersistenceStatus.DEFERRED -> {
                    require(request == null && error == null) {
                        "Deferred conversation persistence results must not contain a request or error."
                    }
                }

                ConversationPersistenceStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed conversation persistence results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.record.intake.record.input.context.traceId ==
                    traceId,
            ) {
                "Conversation persistence result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Conversation persistence result and error must use the same trace identity."
            }

            return ConversationPersistenceResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
