package com.devil.core.model.child

/**
 * Stage 44 pure child-policy evaluation boundary.
 *
 * This policy evaluates only explicitly supplied child-policy context and an
 * explicitly supplied policy requirement.
 *
 * It performs no identity inference, age inference, authentication, guardian
 * authentication, trust evaluation, authorization, Owner Mode transition,
 * Android permission operation, runtime invocation, memory persistence, or
 * execution.
 *
 * GuardianAuthorityStatus.ESTABLISHED is intentionally not treated as guardian
 * approval for a specific activity.
 *
 * Guardian authority
 * != guardian approval.
 */
class ChildGuardianPolicy {

    fun evaluate(
        request: ChildPolicyRequest,
    ): ChildPolicyDecision {
        return when (request.context.classification) {
            ChildSubjectClassification.NOT_CHILD ->
                ChildPolicyDecision.create(
                    status =
                        ChildPolicyDecisionStatus.NOT_APPLICABLE,
                    context = request.context,
                    requirement = request.requirement,
                    rationale =
                        "Child policy is not applicable because the supplied subject is explicitly classified NOT_CHILD.",
                )

            ChildSubjectClassification.UNKNOWN ->
                ChildPolicyDecision.create(
                    status =
                        ChildPolicyDecisionStatus.UNAVAILABLE,
                    context = request.context,
                    requirement = request.requirement,
                    rationale =
                        "Child policy cannot be determined because the supplied subject classification is UNKNOWN.",
                )

            ChildSubjectClassification.CHILD ->
                evaluateChild(
                    request = request,
                )
        }
    }

    private fun evaluateChild(
        request: ChildPolicyRequest,
    ): ChildPolicyDecision {
        return when (request.requirement) {
            ChildPolicyRequirement.CHILD_ALLOWED ->
                ChildPolicyDecision.create(
                    status =
                        ChildPolicyDecisionStatus.ALLOWED_BY_CHILD_POLICY,
                    context = request.context,
                    requirement = request.requirement,
                    rationale =
                        "The supplied child-policy requirement allows this activity for a subject classified CHILD.",
                )

            ChildPolicyRequirement.GUARDIAN_APPROVAL_REQUIRED ->
                ChildPolicyDecision.create(
                    status =
                        ChildPolicyDecisionStatus.GUARDIAN_APPROVAL_REQUIRED,
                    context = request.context,
                    requirement = request.requirement,
                    rationale =
                        "The supplied child-policy requirement requires a distinct guardian approval before the activity may proceed.",
                )

            ChildPolicyRequirement.CHILD_BLOCKED ->
                ChildPolicyDecision.create(
                    status =
                        ChildPolicyDecisionStatus.BLOCKED_BY_CHILD_POLICY,
                    context = request.context,
                    requirement = request.requirement,
                    rationale =
                        "The supplied child-policy requirement blocks this activity for a subject classified CHILD.",
                )
        }
    }
}
