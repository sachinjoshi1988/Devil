package com.devil.core.model.child

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals

class ChildPolicySatisfactionPolicyTest {

    private val policy =
        ChildPolicySatisfactionPolicy()

    @Test
    fun `child allowed policy is satisfied without guardian approval`() {
        val decision =
            policyDecision(
                requirement =
                    ChildPolicyRequirement.CHILD_ALLOWED,
            )

        val result =
            policy.evaluate(
                ChildPolicySatisfactionRequest.create(
                    policyDecision = decision,
                ),
            )

        assertEquals(
            ChildPolicySatisfactionStatus.SATISFIED,
            result.status,
        )
    }

    @Test
    fun `child blocked policy remains blocked`() {
        val decision =
            policyDecision(
                requirement =
                    ChildPolicyRequirement.CHILD_BLOCKED,
            )

        val result =
            policy.evaluate(
                ChildPolicySatisfactionRequest.create(
                    policyDecision = decision,
                ),
            )

        assertEquals(
            ChildPolicySatisfactionStatus.BLOCKED,
            result.status,
        )
    }

    @Test
    fun `guardian gated policy is unsatisfied without guardian decision`() {
        val decision =
            policyDecision(
                requirement =
                    ChildPolicyRequirement.GUARDIAN_APPROVAL_REQUIRED,
                includeGuardianAuthority = true,
            )

        val result =
            policy.evaluate(
                ChildPolicySatisfactionRequest.create(
                    policyDecision = decision,
                ),
            )

        assertEquals(
            ChildPolicySatisfactionStatus.UNSATISFIED,
            result.status,
        )
    }

    @Test
    fun `matching explicit approved guardian decision satisfies guardian gate`() {
        val decision =
            policyDecision(
                requirement =
                    ChildPolicyRequirement.GUARDIAN_APPROVAL_REQUIRED,
                includeGuardianAuthority = true,
            )

        val guardianRequest =
            GuardianApprovalRequest.create(
                policyDecision = decision,
                guardianIdentityId =
                    requireNotNull(
                        decision.context.guardianAuthority,
                    ).guardianIdentityId,
            )

        val approval =
            GuardianApprovalDecision.create(
                status =
                    GuardianApprovalStatus.APPROVED,
                request = guardianRequest,
                rationale =
                    "The established guardian explicitly approved this bounded request.",
            )

        val result =
            policy.evaluate(
                ChildPolicySatisfactionRequest.create(
                    policyDecision = decision,
                    guardianApprovalDecision = approval,
                ),
            )

        assertEquals(
            ChildPolicySatisfactionStatus.SATISFIED,
            result.status,
        )
    }

    @Test
    fun `explicit guardian denial does not satisfy guardian gate`() {
        val decision =
            policyDecision(
                requirement =
                    ChildPolicyRequirement.GUARDIAN_APPROVAL_REQUIRED,
                includeGuardianAuthority = true,
            )

        val guardianRequest =
            GuardianApprovalRequest.create(
                policyDecision = decision,
                guardianIdentityId =
                    requireNotNull(
                        decision.context.guardianAuthority,
                    ).guardianIdentityId,
            )

        val approval =
            GuardianApprovalDecision.create(
                status =
                    GuardianApprovalStatus.DENIED,
                request = guardianRequest,
                rationale =
                    "The established guardian denied this bounded request.",
            )

        val result =
            policy.evaluate(
                ChildPolicySatisfactionRequest.create(
                    policyDecision = decision,
                    guardianApprovalDecision = approval,
                ),
            )

        assertEquals(
            ChildPolicySatisfactionStatus.UNSATISFIED,
            result.status,
        )
    }

    @Test
    fun `not child policy remains not applicable`() {
        val context =
            ChildGuardianContext.create(
                subjectIdentityId =
                    IdentityId.from(
                        "stage-44-satisfaction-not-child",
                    ),
                classification =
                    ChildSubjectClassification.NOT_CHILD,
            )

        val decision =
            ChildGuardianPolicy().evaluate(
                ChildPolicyRequest.create(
                    context = context,
                    requirement =
                        ChildPolicyRequirement.CHILD_ALLOWED,
                ),
            )

        val result =
            policy.evaluate(
                ChildPolicySatisfactionRequest.create(
                    policyDecision = decision,
                ),
            )

        assertEquals(
            ChildPolicySatisfactionStatus.NOT_APPLICABLE,
            result.status,
        )
    }

    @Test
    fun `unknown child classification remains unavailable`() {
        val context =
            ChildGuardianContext.create(
                subjectIdentityId =
                    IdentityId.from(
                        "stage-44-satisfaction-unknown-child",
                    ),
                classification =
                    ChildSubjectClassification.UNKNOWN,
            )

        val decision =
            ChildGuardianPolicy().evaluate(
                ChildPolicyRequest.create(
                    context = context,
                    requirement =
                        ChildPolicyRequirement.CHILD_ALLOWED,
                ),
            )

        val result =
            policy.evaluate(
                ChildPolicySatisfactionRequest.create(
                    policyDecision = decision,
                ),
            )

        assertEquals(
            ChildPolicySatisfactionStatus.UNAVAILABLE,
            result.status,
        )
    }

    private fun policyDecision(
        requirement: ChildPolicyRequirement,
        includeGuardianAuthority: Boolean = false,
    ): ChildPolicyDecision {
        val childIdentityId =
            IdentityId.from(
                "stage-44-satisfaction-child",
            )

        val guardianAuthority =
            if (includeGuardianAuthority) {
                GuardianAuthorityRecord.create(
                    childIdentityId = childIdentityId,
                    guardianIdentityId =
                        IdentityId.from(
                            "stage-44-satisfaction-guardian",
                        ),
                    status =
                        GuardianAuthorityStatus.ESTABLISHED,
                )
            } else {
                null
            }

        val context =
            ChildGuardianContext.create(
                subjectIdentityId = childIdentityId,
                classification =
                    ChildSubjectClassification.CHILD,
                guardianAuthority = guardianAuthority,
            )

        return ChildGuardianPolicy().evaluate(
            ChildPolicyRequest.create(
                context = context,
                requirement = requirement,
            ),
        )
    }
}
