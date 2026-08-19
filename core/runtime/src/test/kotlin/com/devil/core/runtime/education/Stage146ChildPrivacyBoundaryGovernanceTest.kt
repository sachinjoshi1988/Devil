package com.devil.core.runtime.education

import com.devil.core.model.child.ChildGuardianContext
import com.devil.core.model.child.ChildPolicyDecision
import com.devil.core.model.child.ChildPolicyDecisionStatus
import com.devil.core.model.child.ChildPolicyRequirement
import com.devil.core.model.child.ChildPolicySatisfactionRequest
import com.devil.core.model.child.ChildPolicySatisfactionResult
import com.devil.core.model.child.ChildPolicySatisfactionStatus
import com.devil.core.model.child.ChildSubjectClassification
import com.devil.core.model.common.TraceId
import com.devil.core.model.education.AgeAppropriateTeachingRecord
import com.devil.core.model.education.ChildEducationRecord
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.GuardianEducationPolicyRecord
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.privacy.PrivacyDataClassification
import com.devil.core.model.privacy.PrivacyDisclosureCoordinator
import com.devil.core.model.privacy.PrivacyExposureCoordinator
import com.devil.core.model.privacy.PrivacyExposureRequest
import com.devil.core.model.privacy.PrivacyExposureTarget
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage146ChildPrivacyBoundaryGovernanceTest {

    @Test
    fun `coordinator prepares bounded child privacy context from exact evidence`() {
        val traceId = TraceId.from("trace:stage146-prepared")
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

        val result =
            ChildPrivacyBoundaryCoordinator().prepare(
                traceId = traceId,
                ageAppropriateTeaching = teaching,
                exposureAssessment = exposure,
                disclosureDecision = disclosure,
                privacyBoundaryFocus =
                    "Protect bounded child-learning information",
            )

        assertEquals(
            ChildPrivacyBoundaryPreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(traceId, result.traceId)

        val boundary = requireNotNull(result.childPrivacyBoundary)

        assertSame(teaching, boundary.ageAppropriateTeaching)
        assertSame(exposure, boundary.exposureAssessment)
        assertSame(disclosure, boundary.disclosureDecision)
    }

    @Test
    fun `coordinator defers mismatched privacy provenance`() {
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

        val result =
            ChildPrivacyBoundaryCoordinator().prepare(
                traceId = TraceId.from("trace:stage146-mismatch"),
                ageAppropriateTeaching = ageAppropriateTeaching(),
                exposureAssessment = exposureOne,
                disclosureDecision =
                    PrivacyDisclosureCoordinator().evaluate(exposureTwo),
                privacyBoundaryFocus = "Child privacy boundary",
            )

        assertEquals(
            ChildPrivacyBoundaryPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.childPrivacyBoundary)
    }

    @Test
    fun `coordinator defers blank boundary focus`() {
        val exposure =
            PrivacyExposureCoordinator().assess(
                PrivacyExposureRequest.create(
                    classification = PrivacyDataClassification.PUBLIC,
                    target = PrivacyExposureTarget.INTERNAL_PROCESSING,
                ),
            )

        val result =
            ChildPrivacyBoundaryCoordinator().prepare(
                traceId = TraceId.from("trace:stage146-blank"),
                ageAppropriateTeaching = ageAppropriateTeaching(),
                exposureAssessment = exposure,
                disclosureDecision =
                    PrivacyDisclosureCoordinator().evaluate(exposure),
                privacyBoundaryFocus = "   ",
            )

        assertEquals(
            ChildPrivacyBoundaryPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.childPrivacyBoundary)
    }

    @Test
    fun `prepared result requires boundary and deferred result forbids boundary`() {
        val traceId = TraceId.from("trace:stage146-result")
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

        val preparedBoundary =
            requireNotNull(
                ChildPrivacyBoundaryCoordinator().prepare(
                    traceId = traceId,
                    ageAppropriateTeaching = teaching,
                    exposureAssessment = exposure,
                    disclosureDecision = disclosure,
                    privacyBoundaryFocus = "Bounded privacy",
                ).childPrivacyBoundary,
            )

        kotlin.test.assertFailsWith<IllegalArgumentException> {
            ChildPrivacyBoundaryPreparationResult.create(
                traceId = traceId,
                status = ChildPrivacyBoundaryPreparationStatus.PREPARED,
            )
        }

        kotlin.test.assertFailsWith<IllegalArgumentException> {
            ChildPrivacyBoundaryPreparationResult.create(
                traceId = traceId,
                status = ChildPrivacyBoundaryPreparationStatus.DEFERRED,
                childPrivacyBoundary = preparedBoundary,
            )
        }
    }

    private fun ageAppropriateTeaching(): AgeAppropriateTeachingRecord {
        val subjectIdentity =
            IdentityId.from("identity:stage146-runtime-child")

        val childContext =
            ChildGuardianContext.create(
                subjectIdentityId = subjectIdentity,
                classification = ChildSubjectClassification.CHILD,
            )

        val childEducation =
            ChildEducationRecord.create(
                educationSession =
                    EducationSessionRecord.create(
                        sessionId =
                            EducationSessionId.from(
                                "education-session:stage146-runtime",
                            ),
                        subjectIdentityId = subjectIdentity,
                        objective =
                            EducationObjective.create(
                                subject = "Child learning",
                                objective = "Support bounded child learning.",
                            )
                    ),
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

        val policySatisfaction =
            ChildPolicySatisfactionResult.create(
                status = ChildPolicySatisfactionStatus.SATISFIED,
                request =
                    ChildPolicySatisfactionRequest.create(
                        policyDecision = policyDecision,
                    ),
                rationale =
                    "The bounded child-policy requirement is satisfied.",
            )

        return AgeAppropriateTeachingRecord.create(
            guardianEducationPolicy =
                GuardianEducationPolicyRecord.create(
                    childEducation = childEducation,
                    policyDecision = policyDecision,
                    policySatisfaction = policySatisfaction,
                    guardianPolicyFocus =
                        "Bounded education governance",
                ),
            teachingLevel = "Foundational",
            teachingApproach = "Clear and age-appropriate",
            teachingObjective = "Support comprehension",
        )
    }
}
