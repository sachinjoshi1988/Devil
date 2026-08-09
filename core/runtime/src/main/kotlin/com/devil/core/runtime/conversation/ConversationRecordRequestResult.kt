package com.devil.core.runtime.conversation

import com.devil.core.model.common.TraceId
import com.devil.core.model.conversation.ConversationRecordRequest
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the bounded result of preparing one conversation-record request.
 *
 * An available result contains one ConversationRecordRequest whose preserved
 * intake belongs to the same constitutional trace.
 *
 * An unavailable result contains neither request nor error.
 *
 * A failed result contains one matching error and no request.
 *
 * Preserving this result does not create conversation identity, create a
 * ConversationRecord, establish multi-turn ordering, persist or restore
 * conversation state, create logical memory, authenticate a subject, grant
 * authorization, execute capabilities, or establish a verified outcome.
 */
@ConsistentCopyVisibility
data class ConversationRecordRequestResult private constructor(
    val traceId: TraceId,
    val status: ConversationRecordRequestStatus,
    val request: ConversationRecordRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: ConversationRecordRequestStatus,
            request: ConversationRecordRequest? = null,
            error: UniversalErrorRecord? = null,
        ): ConversationRecordRequestResult {
            when (status) {
                ConversationRecordRequestStatus.AVAILABLE -> {
                    require(request != null && error == null) {
                        "Available conversation-record request results require a request and must not contain an error."
                    }
                }

                ConversationRecordRequestStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable conversation-record request results must not contain a request or error."
                    }
                }

                ConversationRecordRequestStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed conversation-record request results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.intake.record.input.context.traceId == traceId,
            ) {
                "Conversation-record request result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Conversation-record request result and error must use the same trace identity."
            }

            return ConversationRecordRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
