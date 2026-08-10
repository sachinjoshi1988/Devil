package com.devil.core.model.child

/**
 * One explicit Stage 44 request to determine whether a previously evaluated
 * child-policy requirement has been satisfied.
 *
 * The request preserves:
 *
 * - one ChildPolicyDecision;
 * - an optional explicit GuardianApprovalDecision.
 *
 * Guardian approval is relevant only when child policy previously returned
 * GUARDIAN_APPROVAL_REQUIRED.
 *
 * Creating this request does not:
 *
 * - authenticate a child;
 * - authenticate a guardian;
 * - establish guardian authority;
 * - invent guardian approval;
 * - grant Devil authorization;
 * - enter Owner Mode;
 * - grant Android permission;
 * - approve execution;
 * - persist memory;
 * - or execute an action.
 */
@ConsistentCopyVisibility
data class ChildPolicySatisfactionRequest private constructor(
    val policyDecision: ChildPolicyDecision,
    val guardianApprovalDecision: GuardianApprovalDecision?,
) {
    companion object {

        fun create(
            policyDecision: ChildPolicyDecision,
            guardianApprovalDecision: GuardianApprovalDecision? = null,
        ): ChildPolicySatisfactionRequest {
            require(
                guardianApprovalDecision == null ||
                    policyDecision.status ==
                    ChildPolicyDecisionStatus.GUARDIAN_APPROVAL_REQUIRED,
            ) {
                "Guardian approval evidence may be supplied only when child policy requires guardian approval."
            }

            return ChildPolicySatisfactionRequest(
                policyDecision = policyDecision,
                guardianApprovalDecision = guardianApprovalDecision,
            )
        }
    }
}
