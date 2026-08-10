package com.devil.core.model.child

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals

class ChildGuardianPolicyTest {

    private val policy =
        ChildGuardianPolicy()

    @Test
    fun `child allowed requirement produces child-policy allowance only`() {
        val decision =
            evaluate(
                classification =
                    ChildSubjectClassification.CHILD,
                requirement =
                    ChildPolicyRequirement.CHILD_ALLOWED,
            )

        assertEquals(
            ChildPolicyDecisionStatus.ALLOWED_BY_CHILD_POLICY,
            decision.status,
        )
    }

    @Test
    fun `guardian-gated requirement remains guardian approval required`() {
        val decision =
            evaluate(
                classification =
                    ChildSubjectClassification.CHILD,
                requirement =
                    ChildPolicyRequirement.GUARDIAN_APPROVAL_REQUIRED,
            )

        assertEquals(
            ChildPolicyDecisionStatus.GUARDIAN_APPROVAL_REQUIRED,
            decision.status,
        )
    }

    @Test
    fun `blocked child requirement remains blocked`() {
        val decision =
            evaluate(
                classification =
                    ChildSubjectClassification.CHILD,
                requirement =
                    ChildPolicyRequirement.CHILD_BLOCKED,
            )

        assertEquals(
            ChildPolicyDecisionStatus.BLOCKED_BY_CHILD_POLICY,
            decision.status,
        )
    }

    @Test
    fun `not-child subject makes child policy not applicable`() {
        val decision =
            evaluate(
                classification =
                    ChildSubjectClassification.NOT_CHILD,
                requirement =
                    ChildPolicyRequirement.CHILD_BLOCKED,
            )

        assertEquals(
            ChildPolicyDecisionStatus.NOT_APPLICABLE,
            decision.status,
        )
    }

    @Test
    fun `unknown child classification fails closed as unavailable`() {
        val decision =
            evaluate(
                classification =
                    ChildSubjectClassification.UNKNOWN,
                requirement =
                    ChildPolicyRequirement.CHILD_ALLOWED,
            )

        assertEquals(
            ChildPolicyDecisionStatus.UNAVAILABLE,
            decision.status,
        )
    }

    @Test
    fun `established guardian authority does not become guardian approval`() {
        val childIdentityId =
            IdentityId.from(
                "child-policy-established-child",
            )

        val context =
            ChildGuardianContext.create(
                subjectIdentityId = childIdentityId,
                classification =
                    ChildSubjectClassification.CHILD,
                guardianAuthority =
                    GuardianAuthorityRecord.create(
                        childIdentityId =
                            childIdentityId,
                        guardianIdentityId =
                            IdentityId.from(
                                "child-policy-established-guardian",
                            ),
                        status =
                            GuardianAuthorityStatus.ESTABLISHED,
                    ),
            )

        val decision =
            policy.evaluate(
                ChildPolicyRequest.create(
                    context = context,
                    requirement =
                        ChildPolicyRequirement.GUARDIAN_APPROVAL_REQUIRED,
                ),
            )

        assertEquals(
            ChildPolicyDecisionStatus.GUARDIAN_APPROVAL_REQUIRED,
            decision.status,
        )
        assertEquals(
            GuardianAuthorityStatus.ESTABLISHED,
            decision.context.guardianAuthority?.status,
        )
    }

    private fun evaluate(
        classification: ChildSubjectClassification,
        requirement: ChildPolicyRequirement,
    ): ChildPolicyDecision {
        val context =
            ChildGuardianContext.create(
                subjectIdentityId =
                    IdentityId.from(
                        "child-policy-test-subject",
                    ),
                classification = classification,
            )

        return policy.evaluate(
            ChildPolicyRequest.create(
                context = context,
                requirement = requirement,
            ),
        )
    }
}
