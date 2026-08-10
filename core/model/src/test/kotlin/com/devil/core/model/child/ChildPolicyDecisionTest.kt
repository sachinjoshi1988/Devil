package com.devil.core.model.child

import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ChildPolicyDecisionTest {

    @Test
    fun `decision preserves status context requirement and normalized rationale`() {
        val context =
            childContext()

        val decision =
            ChildPolicyDecision.create(
                status =
                    ChildPolicyDecisionStatus.ALLOWED_BY_CHILD_POLICY,
                context = context,
                requirement =
                    ChildPolicyRequirement.CHILD_ALLOWED,
                rationale =
                    "  Child policy permits the activity.  ",
            )

        assertEquals(
            ChildPolicyDecisionStatus.ALLOWED_BY_CHILD_POLICY,
            decision.status,
        )
        assertEquals(
            context,
            decision.context,
        )
        assertEquals(
            ChildPolicyRequirement.CHILD_ALLOWED,
            decision.requirement,
        )
        assertEquals(
            "Child policy permits the activity.",
            decision.rationale,
        )
    }

    @Test
    fun `decision rejects blank rationale`() {
        assertFailsWith<IllegalArgumentException> {
            ChildPolicyDecision.create(
                status =
                    ChildPolicyDecisionStatus.UNAVAILABLE,
                context = childContext(),
                requirement =
                    ChildPolicyRequirement.CHILD_ALLOWED,
                rationale = "   ",
            )
        }
    }

    private fun childContext(): ChildGuardianContext {
        return ChildGuardianContext.create(
            subjectIdentityId =
                IdentityId.from(
                    "child-policy-decision-subject",
                ),
            classification =
                ChildSubjectClassification.CHILD,
        )
    }
}
