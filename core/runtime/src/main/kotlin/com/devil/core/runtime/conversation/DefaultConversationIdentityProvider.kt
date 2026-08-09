package com.devil.core.runtime.conversation

import com.devil.core.model.common.TraceId
import com.devil.core.model.conversation.ConversationInput

/**
 * Default Stage 25 conversation identity provider.
 *
 * No approved production conversation identity mechanism exists yet. Therefore
 * this provider preserves trace continuity and reports that no conversation
 * identity is available rather than fabricating one.
 *
 * This implementation does not generate conversation identities, persist
 * conversation state, create logical memory, authenticate a subject, grant
 * authorization, execute capabilities, or establish a verified outcome.
 */
class DefaultConversationIdentityProvider :
    ConversationIdentityProvider {

    override fun provide(
        traceId: TraceId,
        input: ConversationInput,
    ): ConversationIdentityProvisionResult {
        require(input.context.traceId == traceId) {
            "Conversation identity trace and conversation input must use the same trace identity."
        }

        return ConversationIdentityProvisionResult.create(
            traceId = traceId,
            status = ConversationIdentityProvisionStatus.UNAVAILABLE,
        )
    }
}
