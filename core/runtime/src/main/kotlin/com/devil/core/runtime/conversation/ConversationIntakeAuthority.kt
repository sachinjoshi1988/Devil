package com.devil.core.runtime.conversation

import com.devil.core.model.conversation.ConversationInput
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.trust.TrustResult

/**
 * Performs bounded conversation intake after constitutionally ordered identity,
 * trust, and authorization processing.
 *
 * The supplied ConversationInput owns the authoritative ContextEnvelope. This
 * authority must not duplicate or alter that constitutional context.
 *
 * This authority does not resolve identity, evaluate trust, grant
 * authorization, interpret language, establish understanding, create memory,
 * make decisions, create tasks, plan work, execute capabilities, observe
 * results, or verify outcomes.
 */
interface ConversationIntakeAuthority {

    fun intake(
        input: ConversationInput,
        identity: IdentityResult,
        trust: TrustResult,
        authorization: AuthorizationResult,
    ): ConversationIntakeAuthorityResult
}
