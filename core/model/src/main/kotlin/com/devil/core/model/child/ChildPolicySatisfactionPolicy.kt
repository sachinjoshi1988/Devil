package com.devil.core.model.child

/**
 * Stage 44 pure policy for deciding whether one previously evaluated
 * child/guardian policy requirement is satisfied.
 *
 * It does not:
 *
 * - perform identity resolution;
 * - authenticate a subject or guardian;
 * - establish guardian authority;
 * - create guardian approval;
 * - evaluate constitutional authorization;
 * - enter Owner Mode;
 * - inspect Android permission;
 * - invoke UnifiedDevilRuntime;
 * - execute an action;
 * - persist logical memory;
 * - or claim an Outcome.
 *
 * Guardian approval may satisfy only the Stage 44 guardian-gated child-policy
 * requirement represented by the exact supplied policy decision.
 *
 * Guardian approval
 * != general Devil authorization.
 */
class ChildPolicySatisfactionPolicy {

    fun evaluate(
        request: ChildPolicySatisfactionRequest,
    ): ChildPolicySatisfactionResult {
        return when (request.policyDecision.status) {
            ChildPolicyDecisionStatus.ALLOWED_BY_CHILD_POLICY ->
                ChildPolicySatisfactionResult.create(
                    status =
                        ChildPolicySatisfactionStatus.SATISFIED,
                    request = request,
                    rationale =
                        "The supplied child-policy decision already allows this activity for the child context.",
                )

            ChildPolicyDecisionStatus.BLOCKED_BY_CHILD_POLICY ->
                ChildPolicySatisfactionResult.create(
                    status =
                        ChildPolicySatisfactionStatus.BLOCKED,
                    request = request,
                    rationale =
                        "The supplied child-policy decision explicitly blocks this activity.",
                )

            ChildPolicyDecisionStatus.NOT_APPLICABLE ->
                ChildPolicySatisfactionResult.create(
                    status =
                        ChildPolicySatisfactionStatus.NOT_APPLICABLE,
                    request = request,
                    rationale =
                        "Child policy is not applicable to the supplied NOT_CHILD context.",
                )

            ChildPolicyDecisionStatus.UNAVAILABLE ->
                ChildPolicySatisfactionResult.create(
                    status =
                        ChildPolicySatisfactionStatus.UNAVAILABLE,
                    request = request,
                    rationale =
                        "Child-policy satisfaction cannot be determined because the child-policy decision is unavailable.",
                )

            ChildPolicyDecisionStatus.GUARDIAN_APPROVAL_REQUIRED ->
                evaluateGuardianRequirement(
                    request = request,
                )
        }
    }

    private fun evaluateGuardianRequirement(
        request: ChildPolicySatisfactionRequest,
    ): ChildPolicySatisfactionResult {
        val approval =
            request.guardianApprovalDecision
                ?: return ChildPolicySatisfactionResult.create(
                    status =
                        ChildPolicySatisfactionStatus.UNSATISFIED,
                    request = request,
                    rationale =
                        "Child policy requires explicit guardian approval, but no guardian approval decision was supplied.",
                )

        if (
            approval.request.policyDecision !=
            request.policyDecision
        ) {
            return ChildPolicySatisfactionResult.create(
                status =
                    ChildPolicySatisfactionStatus.UNSATISFIED,
                request = request,
                rationale =
                    "The supplied guardian approval decision does not belong to the supplied child-policy decision.",
            )
        }

        return when (approval.status) {
            GuardianApprovalStatus.APPROVED ->
                ChildPolicySatisfactionResult.create(
                    status =
                        ChildPolicySatisfactionStatus.SATISFIED,
                    request = request,
                    rationale =
                        "The exact guardian-gated child-policy requirement has explicit guardian approval.",
                )

            GuardianApprovalStatus.DENIED ->
                ChildPolicySatisfactionResult.create(
                    status =
                        ChildPolicySatisfactionStatus.UNSATISFIED,
                    request = request,
                    rationale =
                        "Explicit guardian approval was denied for the guardian-gated child-policy requirement.",
                )

            GuardianApprovalStatus.UNAVAILABLE ->
                ChildPolicySatisfactionResult.create(
                    status =
                        ChildPolicySatisfactionStatus.UNAVAILABLE,
                    request = request,
                    rationale =
                        "Guardian approval is unavailable for the guardian-gated child-policy requirement.",
                )
        }
    }
}
