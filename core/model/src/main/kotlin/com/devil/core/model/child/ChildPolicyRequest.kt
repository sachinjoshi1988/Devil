package com.devil.core.model.child

/**
 * One explicit Stage 44 request for bounded child-policy evaluation.
 *
 * The request preserves:
 *
 * - one already supplied ChildGuardianContext;
 * - one already classified ChildPolicyRequirement.
 *
 * Creating this request does not:
 *
 * - infer child status;
 * - authenticate a subject;
 * - authenticate a guardian;
 * - establish guardian authority;
 * - obtain guardian approval;
 * - establish trust;
 * - grant authorization;
 * - enter Owner Mode;
 * - grant Android permission;
 * - approve execution;
 * - create memory;
 * - or execute an action.
 */
@ConsistentCopyVisibility
data class ChildPolicyRequest private constructor(
    val context: ChildGuardianContext,
    val requirement: ChildPolicyRequirement,
) {
    companion object {

        fun create(
            context: ChildGuardianContext,
            requirement: ChildPolicyRequirement,
        ): ChildPolicyRequest {
            return ChildPolicyRequest(
                context = context,
                requirement = requirement,
            )
        }
    }
}
