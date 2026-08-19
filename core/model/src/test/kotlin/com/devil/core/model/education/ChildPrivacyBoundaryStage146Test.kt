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
import com.devil.core.model.privacy.PrivacyDataClassification
import com.devil.core.model.privacy.PrivacyDisclosureCoordinator
import com.devil.core.model.privacy.PrivacyExposureCoordinator
import com.devil.core.model.privacy.PrivacyExposureRequest
import com.devil.core.model.privacy.PrivacyExposureTarget
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertSame

class ChildPrivacyBoundaryStage146Test {

    @Test
    fun `record preserves exact Stage 145 and Stage 46 provenance`() {
        val teaching = ageAppropriateTeaching()
        val exposure =
            PrivacyExposureCoordinator().assess(
                PrivacyExposureRequest.create(
                    classification = PrivacyDataClassification.PUBLIC,
                    target = PrivacyExposureTarget.INTERNAL_PROCESSING,
                ),
            )
        val disclosure =
            PrivacyDisclosureCoordinator().evaluate(exposure)

        val record =
            ChildPrivacyBoundaryRecord.create(
                ageAppropriateTeaching = teaching,
                exposureAssessment = exposure,
                disclosureDecision = disclosure,
                privacyBoundaryFocus =
                    "  Protect bounded child-learning information.  ",
            )

        assertSame(teaching, record.ageAppropriateTeaching)
        assertSame(exposure, record.exposureAssessment)
        assertSame(disclosure, record.disclosureDecision)
        assertEquals(
            "Protect bounded child-learning information.",
            record.privacyBoundaryFocus,
        )
    }

    @Test
    fun `record rejects disclosure decision from another exposure assessment`() {
        val exposureOne =
            PrivacyExposureCoordinator().assess(
                PrivacyExposureRequest.create(
                    classification = PrivacyDataClassification.PUBLIC,
                    target = PrivacyExposureTarget.INTERNAL_PROCESSING,
                ),
            )
        val exposureTwo =
            PrivacyExposureCoordinator().assess(
                PrivacyExposureRequest.create(
                    classification = PrivacyDataClassification.PUBLIC,
                    target = PrivacyExposureTarget.OWNER_PRESENTATION,
                ),
            )
        val disclosureTwo =
            PrivacyDisclosureCoordinator().evaluate(exposureTwo)

        assertFailsWith<IllegalArgumentException> {
            ChildPrivacyBoundaryRecord.create(
                ageAppropriateTeaching = ageAppropriateTeaching(),
                exposureAssessment = exposureOne,
                disclosureDecision = disclosureTwo,
                privacyBoundaryFocus = "Child privacy boundary",
            )
        }
    }

    @Test
    fun `record rejects blank privacy boundary focus`() {
        val exposure =
            PrivacyExposureCoordinator().assess(
                PrivacyExposureRequest.create(
                    classification = PrivacyDataClassification.PUBLIC,
                    target = PrivacyExposureTarget.INTERNAL_PROCESSING,
                ),
            )

        assertFailsWith<IllegalArgumentException> {
            ChildPrivacyBoundaryRecord.create(
                ageAppropriateTeaching = ageAppropriateTeaching(),
                exposureAssessment = exposure,
                disclosureDecision =
                    PrivacyDisclosureCoordinator().evaluate(exposure),
                privacyBoundaryFocus = "   ",
            )
        }
    }

    private fun ageAppropriateTeaching(): AgeAppropriateTeachingRecord {
        val subjectIdentity =
            IdentityId.from("identity:stage146-model-child")

        val childContext =
            ChildGuardianContext.create(
                subjectIdentityId = subjectIdentity,
                classification = ChildSubjectClassification.CHILD,
            )

        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage146-model",
                    ),
                subjectIdentityId = subjectIdentity,
                objective =
                    EducationObjective.create(
                        subject = "Child learning",
                        objective = "Support bounded child learning.",
                    )
            )

        val childEducation =
            ChildEducationRecord.create(
                educationSession = educationSession,
                childGuardianContext = childContext,
                childEducationFocus = "Foundational learning",
                childEducationObjective = "Support understanding",
            )

        val policyDecision =
            ChildPolicyDecision.create(
                status =
                    ChildPolicyDecisionStatus.ALLOWED_BY_CHILD_POLICY,
                context = childContext,
                requirement = ChildPolicyRequirement.CHILD_ALLOWED,
                rationale = "Child policy permits this bounded activity.",
            )

        val satisfactionRequest =
            ChildPolicySatisfactionRequest.create(
                policyDecision = policyDecision,
            )

        val policySatisfaction =
            ChildPolicySatisfactionResult.create(
                status = ChildPolicySatisfactionStatus.SATISFIED,
                request = satisfactionRequest,
                rationale =
                    "The bounded child-policy requirement is satisfied.",
            )

        val guardianPolicy =
            GuardianEducationPolicyRecord.create(
                childEducation = childEducation,
                policyDecision = policyDecision,
                policySatisfaction = policySatisfaction,
                guardianPolicyFocus = "Bounded education governance",
            )

        return AgeAppropriateTeachingRecord.create(
            guardianEducationPolicy = guardianPolicy,
            teachingLevel = "Foundational",
            teachingApproach = "Clear and age-appropriate",
            teachingObjective = "Support comprehension",
        )
    }
}
