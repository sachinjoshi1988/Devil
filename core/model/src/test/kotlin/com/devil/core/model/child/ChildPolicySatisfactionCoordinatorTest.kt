package com.devil.core.model.child

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals

class ChildPolicySatisfactionCoordinatorTest {

    @Test
    fun `coordinator returns bounded child policy satisfaction result`() {
        val context =
            ChildGuardianContext.create(
                subjectIdentityId =
                    IdentityId.from(
                        "stage-44-satisfaction-coordinator-child",
                    ),
                classification =
                    ChildSubjectClassification.CHILD,
            )

        val policyDecision =
            ChildGuardianPolicy().evaluate(
                ChildPolicyRequest.create(
                    context = context,
                    requirement =
                        ChildPolicyRequirement.CHILD_ALLOWED,
                ),
            )

        val result =
            ChildPolicySatisfactionCoordinator()
                .evaluate(
                    ChildPolicySatisfactionRequest.create(
                        policyDecision = policyDecision,
                    ),
                )

        assertEquals(
            ChildPolicySatisfactionStatus.SATISFIED,
            result.status,
        )
        assertEquals(
            policyDecision,
            result.request.policyDecision,
        )
    }
}
