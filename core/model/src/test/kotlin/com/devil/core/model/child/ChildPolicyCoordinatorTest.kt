package com.devil.core.model.child

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals

class ChildPolicyCoordinatorTest {

    @Test
    fun `coordinator preserves bounded child-policy decision`() {
        val context =
            ChildGuardianContext.create(
                subjectIdentityId =
                    IdentityId.from(
                        "child-policy-coordinator-subject",
                    ),
                classification =
                    ChildSubjectClassification.CHILD,
            )

        val request =
            ChildPolicyRequest.create(
                context = context,
                requirement =
                    ChildPolicyRequirement.CHILD_BLOCKED,
            )

        val result =
            ChildPolicyCoordinator()
                .evaluate(
                    request = request,
                )

        assertEquals(
            ChildPolicyDecisionStatus.BLOCKED_BY_CHILD_POLICY,
            result.status,
        )
        assertEquals(
            context,
            result.context,
        )
        assertEquals(
            ChildPolicyRequirement.CHILD_BLOCKED,
            result.requirement,
        )
    }
}
