package com.devil.core.model.child

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GuardianApprovalDecisionTest {

    @Test
    fun `approved decision preserves exact request and normalized rationale`() {
        val request =
            guardianApprovalRequest()

        val decision =
            GuardianApprovalDecision.create(
                status = GuardianApprovalStatus.APPROVED,
                request = request,
                rationale = "  Guardian explicitly approved this request.  ",
            )

        assertEquals(
            GuardianApprovalStatus.APPROVED,
            decision.status,
        )
        assertEquals(request, decision.request)
        assertEquals(
            "Guardian explicitly approved this request.",
            decision.rationale,
        )
    }

    @Test
    fun `decision rejects blank rationale`() {
        assertFailsWith<IllegalArgumentException> {
            GuardianApprovalDecision.create(
                status = GuardianApprovalStatus.DENIED,
                request = guardianApprovalRequest(),
                rationale = "   ",
            )
        }
    }

    private fun guardianApprovalRequest(): GuardianApprovalRequest {
        val childId =
            IdentityId.from(
                "stage-44-child-decision",
            )

        val guardianId =
            IdentityId.from(
                "stage-44-guardian-decision",
            )

        val context =
            ChildGuardianContext.create(
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

        val policyDecision =
            ChildPolicyCoordinator().evaluate(
                ChildPolicyRequest.create(
                    context = context,
                    requirement =
                        ChildPolicyRequirement.GUARDIAN_APPROVAL_REQUIRED,
                ),
            )

        return GuardianApprovalRequest.create(
            policyDecision = policyDecision,
            guardianIdentityId = guardianId,
        )
    }
}
