package com.devil.core.model.education

import com.devil.core.model.child.ChildGuardianContext
import com.devil.core.model.child.ChildPolicyDecision
import com.devil.core.model.child.ChildPolicyDecisionStatus
import com.devil.core.model.child.ChildPolicyRequirement
import com.devil.core.model.child.ChildPolicySatisfactionRequest
import com.devil.core.model.child.ChildPolicySatisfactionResult
import com.devil.core.model.child.ChildPolicySatisfactionStatus
import com.devil.core.model.child.ChildSubjectClassification
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class AgeAppropriateTeachingStage145Test {

    @Test
    fun `age appropriate teaching preserves guardian policy provenance`() {
        val guardianPolicy = guardianEducationPolicy()

        val record =
            AgeAppropriateTeachingRecord.create(
                guardianEducationPolicy = guardianPolicy,
                teachingLevel = "  Early learner  ",
                teachingApproach = "  Short guided explanations  ",
                teachingObjective = "  Build understanding safely  ",
            )

        assertSame(
            guardianPolicy,
            record.guardianEducationPolicy,
        )

        assertEquals(
            "Early learner",
            record.teachingLevel,
        )

        assertEquals(
            "Short guided explanations",
            record.teachingApproach,
        )

        assertEquals(
            "Build understanding safely",
            record.teachingObjective,
        )
    }

    @Test
    fun `blank teaching level is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            AgeAppropriateTeachingRecord.create(
                guardianEducationPolicy = guardianEducationPolicy(),
                teachingLevel = "   ",
                teachingApproach = "Guided explanation",
                teachingObjective = "Support learning",
            )
        }
    }

    @Test
    fun `blank teaching approach is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            AgeAppropriateTeachingRecord.create(
                guardianEducationPolicy = guardianEducationPolicy(),
                teachingLevel = "Early learner",
                teachingApproach = "   ",
                teachingObjective = "Support learning",
            )
        }
    }

    @Test
    fun `blank teaching objective is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            AgeAppropriateTeachingRecord.create(
                guardianEducationPolicy = guardianEducationPolicy(),
                teachingLevel = "Early learner",
                teachingApproach = "Guided explanation",
                teachingObjective = "   ",
            )
        }
    }

    private fun guardianEducationPolicy(): GuardianEducationPolicyRecord {
        val identity =
            IdentityId.from(
                "identity:stage145-model-child",
            )

        val childContext =
            ChildGuardianContext.create(
                subjectIdentityId = identity,
                classification =
                    ChildSubjectClassification.CHILD,
            )

        val childEducation =
            ChildEducationRecord.create(
                educationSession =
                    EducationSessionRecord.create(
                        sessionId =
                            EducationSessionId.from(
                                "education-session:stage145-model",
                            ),
                        subjectIdentityId = identity,
                        objective =
                            EducationObjective.create(
                                subject = "Child Education",
                                objective =
                                    "Prepare bounded age-appropriate teaching context.",
                            ),
                    ),
                childGuardianContext = childContext,
                childEducationFocus =
                    "Child learning",
                childEducationObjective =
                    "Support bounded child education",
            )

        val decision =
            ChildPolicyDecision.create(
                status =
                    ChildPolicyDecisionStatus.ALLOWED_BY_CHILD_POLICY,
                context = childContext,
                requirement =
                    ChildPolicyRequirement.CHILD_ALLOWED,
                rationale =
                    "Supplied child policy allows this bounded educational activity.",
            )

        val satisfaction =
            ChildPolicySatisfactionResult.create(
                status =
                    ChildPolicySatisfactionStatus.SATISFIED,
                request =
                    ChildPolicySatisfactionRequest.create(
                        policyDecision = decision,
                    ),
                rationale =
                    "The supplied child-policy requirement is satisfied.",
            )

        return GuardianEducationPolicyRecord.create(
            childEducation = childEducation,
            policyDecision = decision,
            policySatisfaction = satisfaction,
            guardianPolicyFocus =
                "Preserve child-policy governance",
        )
    }
}
