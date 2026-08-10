package com.devil.core.model.child

import com.devil.core.model.identity.IdentityId

/**
 * One explicit Stage 44 request for guardian approval of one already evaluated
 * child-policy activity.
 *
 * The request preserves:
 *
 * - one existing ChildPolicyDecision;
 * - the guardian identity expected to decide that exact request.
 *
 * A guardian approval request may be created only when child policy explicitly
 * requires guardian approval.
 *
 * Creating this request does not:
 *
 * - establish guardian authority;
 * - authenticate the guardian;
 * - obtain guardian approval;
 * - grant Devil authorization;
 * - enter Owner Mode;
 * - grant Android permission;
 * - approve execution;
 * - persist memory;
 * - or execute an action.
 */
@ConsistentCopyVisibility
data class GuardianApprovalRequest private constructor(
    val policyDecision: ChildPolicyDecision,
    val guardianIdentityId: IdentityId,
) {
    companion object {

        fun create(
            policyDecision: ChildPolicyDecision,
            guardianIdentityId: IdentityId,
        ): GuardianApprovalRequest {
            require(
                policyDecision.status ==
                    ChildPolicyDecisionStatus.GUARDIAN_APPROVAL_REQUIRED,
            ) {
                "Guardian approval may be requested only when child policy requires guardian approval."
            }

            val guardianAuthority =
                policyDecision.context.guardianAuthority

            require(guardianAuthority != null) {
                "Guardian approval request requires explicit guardian-authority context."
            }

            require(
                guardianAuthority.status ==
                    GuardianAuthorityStatus.ESTABLISHED,
            ) {
                "Guardian approval request requires established guardian authority."
            }

            require(
                guardianAuthority.guardianIdentityId ==
                    guardianIdentityId,
            ) {
                "Guardian approval request guardian must match the established guardian-authority record."
            }

            require(
                guardianAuthority.childIdentityId ==
                    policyDecision.context.subjectIdentityId,
            ) {
                "Guardian authority must belong to the child-policy subject."
            }

            return GuardianApprovalRequest(
                policyDecision = policyDecision,
                guardianIdentityId = guardianIdentityId,
            )
        }
    }
}
