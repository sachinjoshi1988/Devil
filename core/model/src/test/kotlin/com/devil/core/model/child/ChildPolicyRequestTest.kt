package com.devil.core.model.child

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals

class ChildPolicyRequestTest {

    @Test
    fun `request preserves explicit child context and policy requirement`() {
        val context =
            ChildGuardianContext.create(
                subjectIdentityId =
                    IdentityId.from(
                        "child-policy-request-subject",
                    ),
                classification =
                    ChildSubjectClassification.CHILD,
            )

        val request =
            ChildPolicyRequest.create(
                context = context,
                requirement =
                    ChildPolicyRequirement.GUARDIAN_APPROVAL_REQUIRED,
            )

        assertEquals(
            context,
            request.context,
        )
        assertEquals(
            ChildPolicyRequirement.GUARDIAN_APPROVAL_REQUIRED,
            request.requirement,
        )
    }
}
