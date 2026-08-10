package com.devil.core.model.child

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ChildPolicySatisfactionResultTest {

    @Test
    fun `result preserves status request and normalized rationale`() {
        val request =
            ChildPolicySatisfactionRequest.create(
                policyDecision =
                    childAllowedDecision(),
            )

        val result =
            ChildPolicySatisfactionResult.create(
                status =
                    ChildPolicySatisfactionStatus.SATISFIED,
                request = request,
                rationale = "  Child policy is satisfied.  ",
            )

        assertEquals(
            ChildPolicySatisfactionStatus.SATISFIED,
            result.status,
        )
        assertEquals(request, result.request)
        assertEquals(
            "Child policy is satisfied.",
            result.rationale,
        )
    }

    @Test
    fun `result rejects blank rationale`() {
        assertFailsWith<IllegalArgumentException> {
            ChildPolicySatisfactionResult.create(
                status =
                    ChildPolicySatisfactionStatus.UNAVAILABLE,
                request =
                    ChildPolicySatisfactionRequest.create(
                        policyDecision =
                            childAllowedDecision(),
                    ),
                rationale = "   ",
            )
        }
    }

    private fun childAllowedDecision(): ChildPolicyDecision {
        val context =
            ChildGuardianContext.create(
                subjectIdentityId =
                    IdentityId.from(
                        "stage-44-satisfaction-result-child",
                    ),
                classification =
                    ChildSubjectClassification.CHILD,
            )

        return ChildGuardianPolicy().evaluate(
            ChildPolicyRequest.create(
                context = context,
                requirement =
                    ChildPolicyRequirement.CHILD_ALLOWED,
            ),
        )
    }
}
