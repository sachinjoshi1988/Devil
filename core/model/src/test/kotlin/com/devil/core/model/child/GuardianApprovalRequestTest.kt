package com.devil.core.model.child

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GuardianApprovalRequestTest {

    private val childId =
        IdentityId.from(
            "stage-44-child-approval-request",
        )

    private val guardianId =
        IdentityId.from(
            "stage-44-guardian-approval-request",
        )

    @Test
    fun `request preserves exact guardian and guardian-gated policy decision`() {
        val decision =
            guardianRequiredDecision()

        val request =
            GuardianApprovalRequest.create(
                policyDecision = decision,
                guardianIdentityId = guardianId,
            )

        assertEquals(decision, request.policyDecision)
        assertEquals(guardianId, request.guardianIdentityId)
    }

    @Test
    fun `request rejects policy decision that does not require guardian approval`() {
        val context =
            childContext()

        val decision =
            ChildPolicyCoordinator().evaluate(
                ChildPolicyRequest.create(
                    context = context,
                    requirement =
                        ChildPolicyRequirement.CHILD_ALLOWED,
                ),
            )

        assertFailsWith<IllegalArgumentException> {
            GuardianApprovalRequest.create(
                policyDecision = decision,
                guardianIdentityId = guardianId,
            )
        }
    }

    @Test
    fun `request rejects different guardian identity`() {
        assertFailsWith<IllegalArgumentException> {
            GuardianApprovalRequest.create(
                policyDecision = guardianRequiredDecision(),
                guardianIdentityId =
                    IdentityId.from(
                        "stage-44-different-guardian",
                    ),
            )
        }
    }

    private fun guardianRequiredDecision(): ChildPolicyDecision {
        return ChildPolicyCoordinator().evaluate(
            ChildPolicyRequest.create(
                context = childContext(),
                requirement =
                    ChildPolicyRequirement.GUARDIAN_APPROVAL_REQUIRED,
            ),
        )
    }

    private fun childContext(): ChildGuardianContext {
        return ChildGuardianContext.create(
            subjectIdentityId = childId,
            classification =
                ChildSubjectClassification.CHILD,
            guardianAuthority =
                GuardianAuthorityRecord.create(
                    childIdentityId = childId,
                    guardianIdentityId = guardianId,
                    status =
                        GuardianAuthorityStatus.ESTABLISHED,
                ),
        )
    }
}
