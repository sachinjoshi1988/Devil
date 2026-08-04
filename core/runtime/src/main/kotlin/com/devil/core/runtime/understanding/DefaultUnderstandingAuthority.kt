package com.devil.core.runtime.understanding

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.conversation.ConversationIntakeAuthorityResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.trust.TrustResult

/**
 * Default Stage 5 implementation of structured understanding.
 *
 * Conversation intake now supplies a bounded operational result, but no
 * language-understanding policy is available yet. This implementation therefore
 * validates constitutional trace continuity and defers understanding without
 * inventing meaning.
 *
 * It performs no identity resolution, trust evaluation, authorization,
 * conversation intake, decision-making, task creation, planning, execution,
 * observation, or verification.
 */
class DefaultUnderstandingAuthority : UnderstandingAuthority {

    override fun understand(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
        authorization: AuthorizationResult,
        conversationIntake: ConversationIntakeAuthorityResult,
    ): UnderstandingAuthorityResult {
        require(identity.traceId == context.traceId) {
            "Context and identity result must use the same trace identity."
        }

        require(trust.traceId == context.traceId) {
            "Context and trust result must use the same trace identity."
        }

        require(authorization.traceId == context.traceId) {
            "Context and authorization result must use the same trace identity."
        }

        require(conversationIntake.traceId == context.traceId) {
            "Context and conversation-intake result must use the same trace identity."
        }

        return UnderstandingAuthorityResult.create(
            traceId = context.traceId,
            status = UnderstandingAuthorityStatus.DEFERRED,
        )
    }
}
