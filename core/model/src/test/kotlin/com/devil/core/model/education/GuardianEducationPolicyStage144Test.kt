package com.devil.core.model.education

import com.devil.core.model.child.ChildGuardianContext
import com.devil.core.model.child.ChildGuardianPolicy
import com.devil.core.model.child.ChildPolicyDecision
import com.devil.core.model.child.ChildPolicyRequirement
import com.devil.core.model.child.ChildPolicyRequest
import com.devil.core.model.child.ChildPolicySatisfactionPolicy
import com.devil.core.model.child.ChildPolicySatisfactionRequest
import com.devil.core.model.child.ChildPolicySatisfactionResult
import com.devil.core.model.child.ChildSubjectClassification
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class GuardianEducationPolicyStage144Test {

    @Test
    fun `guardian policy preserves child education and existing Stage 44 evidence`() {
        val childEducation = childEducation()
        val policyDecision =
            policyDecision(
                childEducation = childEducation,
            )
        val satisfaction =
            policySatisfaction(
                policyDecision = policyDecision,
            )

        val record =
            GuardianEducationPolicyRecord.create(
                childEducation = childEducation,
                policyDecision = policyDecision,
                policySatisfaction = satisfaction,
                guardianPolicyFocus =
                    "  Preserve bounded child education policy  ",
            )

        assertSame(
            childEducation,
            record.childEducation,
        )
        assertSame(
            policyDecision,
            record.policyDecision,
        )
        assertSame(
            satisfaction,
            record.policySatisfaction,
        )
        assertEquals(
            "Preserve bounded child education policy",
            record.guardianPolicyFocus,
        )
    }

    @Test
    fun `guardian policy rejects different child guardian context`() {
        val childEducation = childEducation()
        val otherChildEducation =
            childEducation(
                identity =
                    IdentityId.from(
                        "identity:stage144-model-other",
                    ),
            )

        val policyDecision =
            policyDecision(
                childEducation = otherChildEducation,
            )

        val satisfaction =
            policySatisfaction(
                policyDecision = policyDecision,
            )

        assertFailsWith<IllegalArgumentException> {
            GuardianEducationPolicyRecord.create(
                childEducation = childEducation,
                policyDecision = policyDecision,
                policySatisfaction = satisfaction,
                guardianPolicyFocus =
                    "Guardian policy",
            )
        }
    }

    @Test
    fun `guardian policy rejects unrelated satisfaction evidence`() {
        val childEducation = childEducation()

        val policyDecision =
            policyDecision(
                childEducation = childEducation,
            )

        val secondDecision =
            policyDecision(
                childEducation = childEducation,
            )

        val unrelatedSatisfaction =
            policySatisfaction(
                policyDecision = secondDecision,
            )

        assertFailsWith<IllegalArgumentException> {
            GuardianEducationPolicyRecord.create(
                childEducation = childEducation,
                policyDecision = policyDecision,
                policySatisfaction =
                    unrelatedSatisfaction,
                guardianPolicyFocus =
                    "Guardian policy",
            )
        }
    }

    @Test
    fun `guardian policy rejects blank focus`() {
        val childEducation = childEducation()
        val policyDecision =
            policyDecision(
                childEducation = childEducation,
            )

        assertFailsWith<IllegalArgumentException> {
            GuardianEducationPolicyRecord.create(
                childEducation = childEducation,
                policyDecision = policyDecision,
                policySatisfaction =
                    policySatisfaction(
                        policyDecision = policyDecision,
                    ),
                guardianPolicyFocus = "   ",
            )
        }
    }

    private fun childEducation(
        identity: IdentityId =
            IdentityId.from(
                "identity:stage144-model-child",
            ),
    ): ChildEducationRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage144-model:${identity.value}",
                    ),
                subjectIdentityId = identity,
                objective =
                    EducationObjective.create(
                        subject = "Child Education",
                        objective =
                            "Prepare bounded Child Education context.",
                    ),
            )

        val context =
            ChildGuardianContext.create(
                subjectIdentityId = identity,
                classification =
                    ChildSubjectClassification.CHILD,
            )

        return ChildEducationRecord.create(
            educationSession = educationSession,
            childGuardianContext = context,
            childEducationFocus =
                "Age-bounded learning support",
            childEducationObjective =
                "Prepare child education context",
        )
    }

    private fun policyDecision(
        childEducation: ChildEducationRecord,
    ): ChildPolicyDecision {
        return ChildGuardianPolicy().evaluate(
            ChildPolicyRequest.create(
                context =
                    childEducation.childGuardianContext,
                requirement =
                    ChildPolicyRequirement.CHILD_ALLOWED,
            ),
        )
    }

    private fun policySatisfaction(
        policyDecision: ChildPolicyDecision,
    ): ChildPolicySatisfactionResult {
        return ChildPolicySatisfactionPolicy().evaluate(
            ChildPolicySatisfactionRequest.create(
                policyDecision = policyDecision,
            ),
        )
    }
}
