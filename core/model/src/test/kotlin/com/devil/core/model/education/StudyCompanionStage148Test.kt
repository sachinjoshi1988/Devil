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

class StudyCompanionStage148Test {

    @Test
    fun `record preserves exact homework assistance and normalized study context`() {
        val homeworkAssistance = homeworkAssistance()

        val record =
            StudyCompanionRecord.create(
                homeworkAssistance = homeworkAssistance,
                studyFocus = "  Fractions revision  ",
                studyApproach = "  Guided review and practice  ",
                learnerSupportObjective =
                    "  Reinforce understanding before independent practice.  ",
            )

        assertSame(homeworkAssistance, record.homeworkAssistance)
        assertEquals("Fractions revision", record.studyFocus)
        assertEquals("Guided review and practice", record.studyApproach)
        assertEquals(
            "Reinforce understanding before independent practice.",
            record.learnerSupportObjective,
        )
    }

    @Test
    fun `record rejects blank study focus`() {
        assertFailsWith<IllegalArgumentException> {
            StudyCompanionRecord.create(
                homeworkAssistance = homeworkAssistance(),
                studyFocus = "   ",
                studyApproach = "Guided review",
                learnerSupportObjective = "Support understanding",
            )
        }
    }

    @Test
    fun `record rejects blank study approach`() {
        assertFailsWith<IllegalArgumentException> {
            StudyCompanionRecord.create(
                homeworkAssistance = homeworkAssistance(),
                studyFocus = "Fractions revision",
                studyApproach = "   ",
                learnerSupportObjective = "Support understanding",
            )
        }
    }

    @Test
    fun `record rejects blank learner support objective`() {
        assertFailsWith<IllegalArgumentException> {
            StudyCompanionRecord.create(
                homeworkAssistance = homeworkAssistance(),
                studyFocus = "Fractions revision",
                studyApproach = "Guided review",
                learnerSupportObjective = "   ",
            )
        }
    }

    private fun homeworkAssistance(): HomeworkAssistanceRecord {
        return HomeworkAssistanceRecord.create(
            childPrivacyBoundary = childPrivacyBoundary(),
            homeworkSubject = "Fractions",
            assistanceObjective = "Support understanding",
            assistanceApproach = "Use guided hints",
        )
    }

    private fun childPrivacyBoundary(): ChildPrivacyBoundaryRecord {
        val subjectIdentity =
            IdentityId.from("identity:stage148-model-child")

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
                                "education-session:stage148-model",
                            ),
                        subjectIdentityId = subjectIdentity,
                        objective =
                            EducationObjective.create(
                                subject = "Child learning",
                                objective = "Support bounded child learning.",
                            ),
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
                rationale =
                    "Child policy permits this bounded activity.",
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

        val ageAppropriateTeaching =
            AgeAppropriateTeachingRecord.create(
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

        val exposureAssessment =
            PrivacyExposureCoordinator().assess(
                PrivacyExposureRequest.create(
                    classification = PrivacyDataClassification.PUBLIC,
                    target = PrivacyExposureTarget.INTERNAL_PROCESSING,
                ),
            )

        val disclosureDecision =
            PrivacyDisclosureCoordinator().evaluate(
                exposureAssessment = exposureAssessment,
            )

        return ChildPrivacyBoundaryRecord.create(
            ageAppropriateTeaching = ageAppropriateTeaching,
            exposureAssessment = exposureAssessment,
            disclosureDecision = disclosureDecision,
            privacyBoundaryFocus =
                "Preserve bounded child-learning privacy.",
        )
    }
}
