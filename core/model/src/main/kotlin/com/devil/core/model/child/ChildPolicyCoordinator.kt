package com.devil.core.model.child

/**
 * Stage 44 bounded coordinator for child-policy evaluation.
 *
 * Flow:
 *
 * ChildPolicyRequest
 * -> ChildGuardianPolicy
 * -> ChildPolicyDecision.
 *
 * This coordinator is not another Brain, Identity Authority, Trust Authority,
 * Security Authority, Authorization Authority, Memory Authority, Planner,
 * Executive, runtime, or execution mechanism.
 *
 * It does not invoke UnifiedDevilRuntime.
 *
 * It does not obtain guardian approval.
 *
 * It does not execute an action.
 */
class ChildPolicyCoordinator(
    private val policy: ChildGuardianPolicy =
        ChildGuardianPolicy(),
) {

    fun evaluate(
        request: ChildPolicyRequest,
    ): ChildPolicyDecision {
        return policy.evaluate(
            request = request,
        )
    }
}
