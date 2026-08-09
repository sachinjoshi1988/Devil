package com.devil.core.runtime.conversation

import com.devil.core.model.common.TraceId
import com.devil.core.model.conversation.ConversationInput

/**
 * Supplies one genuine conversation identity for bounded conversation continuity
 * when an approved conversation identity mechanism is available.
 *
 * This provider must not fabricate conversation identities. It does not persist
 * conversation state, create logical memory, authenticate a subject, grant
 * authorization, execute capabilities, or establish a verified outcome.
 */
interface ConversationIdentityProvider {

    fun provide(
        traceId: TraceId,
        input: ConversationInput,
    ): ConversationIdentityProvisionResult
}
