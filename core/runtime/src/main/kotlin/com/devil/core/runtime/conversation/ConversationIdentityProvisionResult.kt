package com.devil.core.runtime.conversation

import com.devil.core.model.common.TraceId
import com.devil.core.model.conversation.ConversationId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the result of attempting to supply one genuine conversation
 * identity.
 *
 * An available result contains one ConversationId. An unavailable result
 * contains neither conversation identity nor error. A failed result contains
 * one matching error.
 *
 * This result does not generate conversation identity, persist conversation
 * state, create logical memory, authenticate a subject, grant authorization,
 * execute capabilities, or establish a verified outcome.
 */
@ConsistentCopyVisibility
data class ConversationIdentityProvisionResult private constructor(
    val traceId: TraceId,
    val status: ConversationIdentityProvisionStatus,
    val conversationId: ConversationId?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: ConversationIdentityProvisionStatus,
            conversationId: ConversationId? = null,
            error: UniversalErrorRecord? = null,
        ): ConversationIdentityProvisionResult {
            when (status) {
                ConversationIdentityProvisionStatus.AVAILABLE -> {
                    require(conversationId != null && error == null) {
                        "Available conversation identity results require a conversation identity and must not contain an error."
                    }
                }

                ConversationIdentityProvisionStatus.UNAVAILABLE -> {
                    require(conversationId == null && error == null) {
                        "Unavailable conversation identity results must not contain a conversation identity or error."
                    }
                }

                ConversationIdentityProvisionStatus.FAILED -> {
                    require(conversationId == null && error != null) {
                        "Failed conversation identity results require an error and must not contain a conversation identity."
                    }
                }
            }

            require(error == null || error.traceId == traceId) {
                "Conversation identity result and error must use the same trace identity."
            }

            return ConversationIdentityProvisionResult(
                traceId = traceId,
                status = status,
                conversationId = conversationId,
                error = error,
            )
        }
    }
}
