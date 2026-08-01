package com.devil.core.runtime.task

import com.devil.core.model.context.ContextEnvelope
import com.devil.core.runtime.authorization.AuthorizationResult
import com.devil.core.runtime.decision.DecisionAuthorityResult
import com.devil.core.runtime.identity.IdentityResult
import com.devil.core.runtime.trust.TrustResult
import com.devil.core.runtime.understanding.UnderstandingAuthorityResult

/**
 * Default Stage 2 implementation of constitutional task creation.
 *
 * The current runtime has no task-construction engine capable of creating a
 * justified TaskRecord. This implementation therefore preserves trace
 * continuity and defers task creation without inventing a task.
 *
 * It performs no identity resolution, trust evaluation, authorization,
 * understanding, decision selection, planning, capability binding, execution,
 * or verification.
 */
class DefaultTaskAuthority : TaskAuthority {

    override fun createTask(
        context: ContextEnvelope,
        identity: IdentityResult,
        trust: TrustResult,
        authorization: AuthorizationResult,
        understanding: UnderstandingAuthorityResult,
        decision: DecisionAuthorityResult,
    ): TaskAuthorityResult {
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

        require(decision.traceId == context.traceId) {
            "Context and decision result must use the same trace identity."
        }

        return TaskAuthorityResult.create(
            traceId = context.traceId,
            status = TaskAuthorityStatus.DEFERRED,
        )
    }
}
