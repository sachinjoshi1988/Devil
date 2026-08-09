package com.devil.core.runtime.conversation

import com.devil.core.model.common.TraceId
import com.devil.core.model.conversation.ConversationPersistenceRequest
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the bounded result of preparing one controlled
 * conversation-persistence request.
 *
 * An available result contains one ConversationPersistenceRequest whose
 * preserved ConversationRecord belongs to the same constitutional trace.
 *
 * An unavailable result contains neither request nor error.
 *
 * A failed result contains one matching error and no request.
 *
 * Preserving this result does not persist, restore, durably store, order,
 * replicate, encrypt, delete, expose, or recall conversation state.
 *
 * It does not create conversation identity, create logical memory, authenticate
 * a subject, grant authorization, execute capabilities, or establish a verified
 * outcome.
 */
@ConsistentCopyVisibility
data class ConversationPersistenceRequestResult private constructor(
    val traceId: TraceId,
    val status: ConversationPersistenceRequestStatus,
    val request: ConversationPersistenceRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: ConversationPersistenceRequestStatus,
            request: ConversationPersistenceRequest? = null,
            error: UniversalErrorRecord? = null,
        ): ConversationPersistenceRequestResult {
            when (status) {
                ConversationPersistenceRequestStatus.AVAILABLE -> {
                    require(request != null && error == null) {
                        "Available conversation persistence request results require a request and must not contain an error."
                    }
                }

                ConversationPersistenceRequestStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable conversation persistence request results must not contain a request or error."
                    }
                }

                ConversationPersistenceRequestStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed conversation persistence request results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.record.intake.record.input.context.traceId ==
                    traceId,
            ) {
                "Conversation persistence request result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Conversation persistence request result and error must use the same trace identity."
            }

            return ConversationPersistenceRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
