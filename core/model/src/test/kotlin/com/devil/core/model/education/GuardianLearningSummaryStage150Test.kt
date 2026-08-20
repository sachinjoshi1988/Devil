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

class GuardianLearningSummaryStage150Test {

    @Test
    fun `record preserves exact learning progress and normalized summary context`() {
        val learningProgress = learningProgress()

        val record =
            GuardianLearningSummaryRecord.create(
                learningProgress = learningProgress,
                guardianSummaryFocus = "  Fractions progress  ",
                learnerProgressSummary =
                    "  Learner is showing greater independence with equivalent fractions.  ",
                guardianFacingInterpretation =
                    "  Continue guided practice without treating this as verified mastery.  ",
            )

        assertSame(learningProgress, record.learningProgress)
        assertEquals(
            "Fractions progress",
            record.guardianSummaryFocus,
        )
        assertEquals(
            "Learner is showing greater independence with equivalent fractions.",
            record.learnerProgressSummary,
        )
        assertEquals(
            "Continue guided practice without treating this as verified mastery.",
            record.guardianFacingInterpretation,
        )
    }

    @Test
    fun `record rejects blank guardian summary focus`() {
        assertFailsWith<IllegalArgumentException> {
            GuardianLearningSummaryRecord.create(
                learningProgress = learningProgress(),
                guardianSummaryFocus = "   ",
                learnerProgressSummary = "Bounded progress summary.",
                guardianFacingInterpretation = "Bounded interpretation.",
            )
        }
    }

    @Test
    fun `record rejects blank learner progress summary`() {
        assertFailsWith<IllegalArgumentException> {
            GuardianLearningSummaryRecord.create(
                learningProgress = learningProgress(),
                guardianSummaryFocus = "Fractions progress",
                learnerProgressSummary = "   ",
                guardianFacingInterpretation = "Bounded interpretation.",
            )
        }
    }

    @Test
    fun `record rejects blank guardian facing interpretation`() {
        assertFailsWith<IllegalArgumentException> {
            GuardianLearningSummaryRecord.create(
                learningProgress = learningProgress(),
                guardianSummaryFocus = "Fractions progress",
                learnerProgressSummary = "Bounded progress summary.",
                guardianFacingInterpretation = "   ",
            )
        }
    }

    private fun learningProgress(): LearningProgressRecord {
        return LearningProgressRecord.create(
            studyCompanion =
                StudyCompanionRecord.create(
                    homeworkAssistance =
                        HomeworkAssistanceRecord.create(
                            childPrivacyBoundary = childPrivacyBoundary(),
                            homeworkSubject = "Fractions",
                            assistanceObjective = "Support understanding",
                            assistanceApproach = "Use guided hints",
                        ),
                    studyFocus = "Fractions revision",
                    studyApproach = "Guided review and practice",
                    learnerSupportObjective =
                        "Reinforce understanding before independent practice.",
                ),
            progressFocus = "Equivalent fractions",
            learnerEvidenceDescription =
                "Learner explains two equivalent examples independently.",
            progressInterpretation =
                "Shows increased independence with the concept.",
        )
    }

    private fun childPrivacyBoundary(): ChildPrivacyBoundaryRecord {
        val subjectIdentity =
            IdentityId.from("identity:stage150-model-child")

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
                                "education-session:stage150-model",
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
