package com.devil.core.runtime.decision

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult

/**
 * Default Stage 2 implementation of constitutional decision selection.
 *
 * The current runtime has no reasoning engine capable of selecting a justified
 * DecisionRecord. This implementation therefore preserves trace continuity and
 * defers decision-making without inventing a decision.
 *
 * It performs no identity resolution, trust evaluation, authorization,
 * understanding, task creation, planning, capability selection, execution, or
 * verification.
 */
class DefaultDecisionAuthority : DecisionAuthority {

    override fun decide(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
        authorization: AuthorizationResult,
        understanding: UnderstandingAuthorityResult,
    ): DecisionAuthorityResult {
        require(identity.traceId == context.traceId) {
            "Context and identity result must use the same trace identity."
        }

        require(trust.traceId == context.traceId) {
            "Context and trust result must use the same trace identity."
        }

        require(authorization.traceId == context.traceId) {
            "Context and authorization result must use the same trace identity."
        }

        require(understanding.traceId == context.traceId) {
            "Context and understanding result must use the same trace identity."
        }

        return DecisionAuthorityResult.create(
            traceId = context.traceId,
            status = DecisionAuthorityStatus.DEFERRED,
        )
    }
}
