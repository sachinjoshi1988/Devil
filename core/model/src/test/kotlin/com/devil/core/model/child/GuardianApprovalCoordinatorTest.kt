package com.devil.core.model.child

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GuardianApprovalCoordinatorTest {

    @Test
    fun `coordinator preserves explicit approval for exact request`() {
        val request =
            guardianApprovalRequest(
                suffix = "approved",
            )

        val coordinator =
            GuardianApprovalCoordinator(
                source =
                    GuardianApprovalSource { suppliedRequest ->
                        GuardianApprovalDecision.create(
                            status =
                                GuardianApprovalStatus.APPROVED,
                            request = suppliedRequest,
                            rationale =
                                "Explicit guardian approval was supplied.",
                        )
                    },
            )

        val decision =
            coordinator.decide(
                request = request,
            )

        assertEquals(
            GuardianApprovalStatus.APPROVED,
            decision.status,
        )
        assertEquals(request, decision.request)
    }

    @Test
    fun `coordinator rejects decision belonging to another request`() {
        val requested =
            guardianApprovalRequest(
                suffix = "requested",
            )

        val different =
            guardianApprovalRequest(
                suffix = "different",
            )

        val coordinator =
            GuardianApprovalCoordinator(
                source =
                    GuardianApprovalSource {
                        GuardianApprovalDecision.create(
                            status =
                                GuardianApprovalStatus.APPROVED,
                            request = different,
                            rationale =
                                "Approval belongs to another request.",
                        )
                    },
            )

        assertFailsWith<IllegalArgumentException> {
            coordinator.decide(
                request = requested,
            )
        }
    }

    private fun guardianApprovalRequest(
        suffix: String,
    ): GuardianApprovalRequest {
        val childId =
            IdentityId.from(
                "stage-44-child-$suffix",
            )

        val guardianId =
            IdentityId.from(
                "stage-44-guardian-$suffix",
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

        val decision =
            ChildPolicyCoordinator().evaluate(
                ChildPolicyRequest.create(
                    context = context,
                    requirement =
                        ChildPolicyRequirement.GUARDIAN_APPROVAL_REQUIRED,
                ),
            )

        return GuardianApprovalRequest.create(
            policyDecision = decision,
            guardianIdentityId = guardianId,
        )
    }
}
