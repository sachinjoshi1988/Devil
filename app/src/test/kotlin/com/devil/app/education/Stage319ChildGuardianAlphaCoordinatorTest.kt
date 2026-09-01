package com.devil.app.education

import com.devil.core.model.child.ChildGuardianContext
import com.devil.core.model.child.ChildGuardianPolicy
import com.devil.core.model.child.ChildPolicyDecision
import com.devil.core.model.child.ChildPolicyRequest
import com.devil.core.model.child.ChildPolicyRequirement
import com.devil.core.model.child.ChildPolicySatisfactionPolicy
import com.devil.core.model.child.ChildPolicySatisfactionRequest
import com.devil.core.model.child.ChildPolicySatisfactionResult
import com.devil.core.model.child.ChildSubjectClassification
import com.devil.core.model.common.TraceId
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.privacy.PrivacyDataClassification
import com.devil.core.model.privacy.PrivacyDisclosureCoordinator
import com.devil.core.model.privacy.PrivacyDisclosureDecision
import com.devil.core.model.privacy.PrivacyExposureAssessment
import com.devil.core.model.privacy.PrivacyExposureCoordinator
import com.devil.core.model.privacy.PrivacyExposureRequest
import com.devil.core.model.privacy.PrivacyExposureTarget
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage319ChildGuardianAlphaCoordinatorTest {

    @Test
    fun `Child Guardian Alpha preserves Stage 143 through 146 provenance`() {
        val fixture = fixture()

        val result =
            Stage319ChildGuardianAlphaCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace:stage319-child-guardian-alpha",
                    ),
                educationSession = fixture.educationSession,
                childGuardianContext = fixture.childGuardianContext,
                policyDecision = fixture.policyDecision,
                policySatisfaction = fixture.policySatisfaction,
                exposureAssessment = fixture.exposureAssessment,
                disclosureDecision = fixture.disclosureDecision,
                childEducationFocus =
                    "Age-bounded learning support",
                childEducationObjective =
                    "Prepare bounded child education Alpha context.",
                guardianPolicyFocus =
                    "Preserve bounded guardian policy provenance.",
                teachingLevel =
                    "Explicitly supplied learner level",
                teachingApproach =
                    "Age-appropriate bounded explanation",
                teachingObjective =
                    "Prepare bounded age-appropriate teaching context.",
                privacyBoundaryFocus =
                    "Protect bounded child-learning information",
            )

        assertEquals(
            Stage319ChildGuardianAlphaStatus.AVAILABLE,
            result.status,
        )

        val childEducation =
            requireNotNull(result.childEducation)
        val guardianPolicy =
            requireNotNull(result.guardianEducationPolicy)
        val teaching =
            requireNotNull(result.ageAppropriateTeaching)
        val privacyBoundary =
            requireNotNull(result.childPrivacyBoundary)

        assertSame(
            fixture.educationSession,
            childEducation.educationSession,
        )
        assertSame(
            fixture.childGuardianContext,
            childEducation.childGuardianContext,
        )
        assertSame(
            childEducation,
            guardianPolicy.childEducation,
        )
        assertSame(
            fixture.policyDecision,
            guardianPolicy.policyDecision,
        )
        assertSame(
            fixture.policySatisfaction,
            guardianPolicy.policySatisfaction,
        )
        assertSame(
            guardianPolicy,
            teaching.guardianEducationPolicy,
        )
        assertSame(
            teaching,
            privacyBoundary.ageAppropriateTeaching,
        )
        assertSame(
            fixture.exposureAssessment,
            privacyBoundary.exposureAssessment,
        )
        assertSame(
            fixture.disclosureDecision,
            privacyBoundary.disclosureDecision,
        )
    }

    @Test
    fun `non child context fails closed at Stage 143`() {
        val fixture = fixture()

        val nonChildContext =
            ChildGuardianContext.create(
                subjectIdentityId =
                    fixture.educationSession.subjectIdentityId,
                classification =
                    ChildSubjectClassification.NOT_CHILD,
            )

        val result =
            Stage319ChildGuardianAlphaCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace:stage319-non-child",
                    ),
                educationSession = fixture.educationSession,
                childGuardianContext = nonChildContext,
                policyDecision = fixture.policyDecision,
                policySatisfaction = fixture.policySatisfaction,
                exposureAssessment = fixture.exposureAssessment,
                disclosureDecision = fixture.disclosureDecision,
                childEducationFocus = "Child education",
                childEducationObjective = "Bounded objective",
                guardianPolicyFocus = "Guardian policy",
                teachingLevel = "Supplied level",
                teachingApproach = "Bounded approach",
                teachingObjective = "Bounded teaching objective",
                privacyBoundaryFocus = "Child privacy",
            )

        assertDeferred(result)
    }

    @Test
    fun `mismatched Stage 44 policy provenance fails closed`() {
        val fixture = fixture()

        val otherIdentity =
            IdentityId.from(
                "identity:stage319-other-child",
            )

        val otherContext =
            ChildGuardianContext.create(
                subjectIdentityId = otherIdentity,
                classification =
                    ChildSubjectClassification.CHILD,
            )

        val otherDecision =
            ChildGuardianPolicy().evaluate(
                ChildPolicyRequest.create(
                    context = otherContext,
                    requirement =
                        ChildPolicyRequirement.CHILD_ALLOWED,
                ),
            )

        val otherSatisfaction =
            ChildPolicySatisfactionPolicy().evaluate(
                ChildPolicySatisfactionRequest.create(
                    policyDecision = otherDecision,
                ),
            )

        val result =
            Stage319ChildGuardianAlphaCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace:stage319-policy-mismatch",
                    ),
                educationSession = fixture.educationSession,
                childGuardianContext = fixture.childGuardianContext,
                policyDecision = otherDecision,
                policySatisfaction = otherSatisfaction,
                exposureAssessment = fixture.exposureAssessment,
                disclosureDecision = fixture.disclosureDecision,
                childEducationFocus = "Child education",
                childEducationObjective = "Bounded objective",
                guardianPolicyFocus = "Guardian policy",
                teachingLevel = "Supplied level",
                teachingApproach = "Bounded approach",
                teachingObjective = "Bounded teaching objective",
                privacyBoundaryFocus = "Child privacy",
            )

        assertDeferred(result)
    }

    @Test
    fun `mismatched Stage 46 privacy provenance fails closed`() {
        val fixture = fixture()

        val otherExposure =
            PrivacyExposureCoordinator().assess(
                PrivacyExposureRequest.create(
                    classification =
                        PrivacyDataClassification.PUBLIC,
                    target =
                        PrivacyExposureTarget.OWNER_PRESENTATION,
                ),
            )

        val mismatchedDisclosure =
            PrivacyDisclosureCoordinator().evaluate(
                otherExposure,
            )

        val result =
            Stage319ChildGuardianAlphaCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace:stage319-privacy-mismatch",
                    ),
                educationSession = fixture.educationSession,
                childGuardianContext = fixture.childGuardianContext,
                policyDecision = fixture.policyDecision,
                policySatisfaction = fixture.policySatisfaction,
                exposureAssessment = fixture.exposureAssessment,
                disclosureDecision = mismatchedDisclosure,
                childEducationFocus = "Child education",
                childEducationObjective = "Bounded objective",
                guardianPolicyFocus = "Guardian policy",
                teachingLevel = "Supplied level",
                teachingApproach = "Bounded approach",
                teachingObjective = "Bounded teaching objective",
                privacyBoundaryFocus = "Child privacy",
            )

        assertDeferred(result)
    }

    @Test
    fun `blank age appropriate teaching context fails closed`() {
        val fixture = fixture()

        val result =
            Stage319ChildGuardianAlphaCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace:stage319-blank-teaching",
                    ),
                educationSession = fixture.educationSession,
                childGuardianContext = fixture.childGuardianContext,
                policyDecision = fixture.policyDecision,
                policySatisfaction = fixture.policySatisfaction,
                exposureAssessment = fixture.exposureAssessment,
                disclosureDecision = fixture.disclosureDecision,
                childEducationFocus = "Child education",
                childEducationObjective = "Bounded objective",
                guardianPolicyFocus = "Guardian policy",
                teachingLevel = "   ",
                teachingApproach = "Bounded approach",
                teachingObjective = "Bounded teaching objective",
                privacyBoundaryFocus = "Child privacy",
            )

        assertDeferred(result)
    }

    private fun fixture(): Fixture {
        val identity =
            IdentityId.from(
                "identity:stage319-alpha-child",
            )

        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage319-alpha-child",
                    ),
                subjectIdentityId = identity,
                objective =
                    EducationObjective.create(
                        subject = "Child Education",
                        objective =
                            "Support bounded Child/Guardian Alpha validation.",
                    ),
            )

        val childGuardianContext =
            ChildGuardianContext.create(
                subjectIdentityId = identity,
                classification =
                    ChildSubjectClassification.CHILD,
            )

        val policyDecision =
            ChildGuardianPolicy().evaluate(
                ChildPolicyRequest.create(
                    context = childGuardianContext,
                    requirement =
                        ChildPolicyRequirement.CHILD_ALLOWED,
                ),
            )

        val policySatisfaction =
            ChildPolicySatisfactionPolicy().evaluate(
                ChildPolicySatisfactionRequest.create(
                    policyDecision = policyDecision,
                ),
            )

        val exposureAssessment =
            PrivacyExposureCoordinator().assess(
                PrivacyExposureRequest.create(
                    classification =
                        PrivacyDataClassification.PUBLIC,
                    target =
                        PrivacyExposureTarget.INTERNAL_PROCESSING,
                ),
            )

        val disclosureDecision =
            PrivacyDisclosureCoordinator().evaluate(
                exposureAssessment,
            )

        return Fixture(
            educationSession = educationSession,
            childGuardianContext = childGuardianContext,
            policyDecision = policyDecision,
            policySatisfaction = policySatisfaction,
            exposureAssessment = exposureAssessment,
            disclosureDecision = disclosureDecision,
        )
    }

    private fun assertDeferred(
        result: Stage319ChildGuardianAlphaResult,
    ) {
        assertEquals(
            Stage319ChildGuardianAlphaStatus.DEFERRED,
            result.status,
        )
        assertNull(result.childEducation)
        assertNull(result.guardianEducationPolicy)
        assertNull(result.ageAppropriateTeaching)
        assertNull(result.childPrivacyBoundary)
    }

    private data class Fixture(
        val educationSession: EducationSessionRecord,
        val childGuardianContext: ChildGuardianContext,
        val policyDecision: ChildPolicyDecision,
        val policySatisfaction: ChildPolicySatisfactionResult,
        val exposureAssessment: PrivacyExposureAssessment,
        val disclosureDecision: PrivacyDisclosureDecision,
    )
}
