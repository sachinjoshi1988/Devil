package com.devil.core.model.child

/**
 * Immutable Stage 44 result of one bounded child-policy evaluation.
 *
 * The original context and requirement remain attached so the decision's basis
 * is explicit.
 *
 * A decision never grants constitutional authority.
 *
 * In particular:
 *
 * ALLOWED_BY_CHILD_POLICY
 * != Devil authorization
 * != Android permission
 * != Executive readiness
 * != Execution APPROVED.
 *
 * GUARDIAN_APPROVAL_REQUIRED
 * != guardian approval obtained.
 */
@ConsistentCopyVisibility
data class ChildPolicyDecision private constructor(
    val status: ChildPolicyDecisionStatus,
    val context: ChildGuardianContext,
    val requirement: ChildPolicyRequirement,
    val rationale: String,
) {
    companion object {

        fun create(
            status: ChildPolicyDecisionStatus,
            context: ChildGuardianContext,
            requirement: ChildPolicyRequirement,
            rationale: String,
        ): ChildPolicyDecision {
            val normalizedRationale =
                rationale.trim()

            require(normalizedRationale.isNotEmpty()) {
                "Child-policy decision rationale must not be blank."
            }

            return ChildPolicyDecision(
                status = status,
                context = context,
                requirement = requirement,
                rationale = normalizedRationale,
            )
        }
    }
}
