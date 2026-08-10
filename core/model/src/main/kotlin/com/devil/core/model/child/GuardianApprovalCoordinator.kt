package com.devil.core.model.child

/**
 * Stage 44 bounded coordinator for explicit guardian approval.
 *
 * Flow:
 *
 * GuardianApprovalRequest
 * -> GuardianApprovalSource
 * -> GuardianApprovalDecision.
 *
 * The coordinator requires the returned decision to refer to the exact request
 * supplied to the source.
 *
 * It is not another Brain, Identity Authority, Trust Authority, Security
 * Authority, Authorization Authority, Memory Authority, Planner, Executive,
 * runtime, or execution mechanism.
 *
 * It does not convert guardian approval into constitutional authorization or
 * execution approval.
 */
class GuardianApprovalCoordinator(
    private val source: GuardianApprovalSource,
) {

    fun decide(
        request: GuardianApprovalRequest,
    ): GuardianApprovalDecision {
        val decision =
            source.decide(
                request = request,
            )

        require(decision.request == request) {
            "Guardian approval source returned a decision for a different request."
        }

        return decision
    }
}
