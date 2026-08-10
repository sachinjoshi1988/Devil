package com.devil.core.model.child

/**
 * Stage 44 bounded coordinator for child/guardian policy satisfaction.
 *
 * Flow:
 *
 * ChildPolicySatisfactionRequest
 * -> ChildPolicySatisfactionPolicy
 * -> ChildPolicySatisfactionResult.
 *
 * This coordinator is not another Brain, Identity Authority, Trust Authority,
 * Security Authority, Authorization Authority, Memory Authority, Planner,
 * Executive, runtime, or execution mechanism.
 *
 * A SATISFIED result removes only the Stage 44 child-policy gate represented by
 * the supplied request.
 *
 * It does not grant constitutional authorization or permit execution by itself.
 */
class ChildPolicySatisfactionCoordinator(
    private val policy: ChildPolicySatisfactionPolicy =
        ChildPolicySatisfactionPolicy(),
) {

    fun evaluate(
        request: ChildPolicySatisfactionRequest,
    ): ChildPolicySatisfactionResult {
        return policy.evaluate(
            request = request,
        )
    }
}
