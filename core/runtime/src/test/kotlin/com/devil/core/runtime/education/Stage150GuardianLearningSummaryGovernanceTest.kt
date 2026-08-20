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
import com.devil.core.model.education.ChildPrivacyBoundaryRecord
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.GuardianEducationPolicyRecord
import com.devil.core.model.education.HomeworkAssistanceRecord
import com.devil.core.model.education.LearningProgressRecord
import com.devil.core.model.education.StudyCompanionRecord
import com.devil.core.model.identity.IdentityId
import com.devil.core.model.privacy.PrivacyDataClassification
import com.devil.core.model.privacy.PrivacyDisclosureCoordinator
import com.devil.core.model.privacy.PrivacyExposureCoordinator
import com.devil.core.model.privacy.PrivacyExposureRequest
import com.devil.core.model.privacy.PrivacyExposureTarget
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage150GuardianLearningSummaryGovernanceTest {

    @Test
    fun `coordinator prepares bounded guardian learning summary`() {
        val learningProgress = learningProgress()
        val traceId = TraceId.from("trace:stage150-prepared")

        val result =
            GuardianLearningSummaryCoordinator().prepare(
                traceId = traceId,
                learningProgress = learningProgress,
                guardianSummaryFocus = "Fractions progress",
                learnerProgressSummary =
                    "Learner is showing greater independence with equivalent fractions.",
                guardianFacingInterpretation =
                    "Continue guided practice without treating this as verified mastery.",
            )

        assertEquals(
            GuardianLearningSummaryPreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(traceId, result.traceId)

        val summary = requireNotNull(result.guardianLearningSummary)

        assertSame(learningProgress, summary.learningProgress)
        assertEquals(
            "Fractions progress",
            summary.guardianSummaryFocus,
        )
        assertEquals(
            "Learner is showing greater independence with equivalent fractions.",
            summary.learnerProgressSummary,
        )
        assertEquals(
            "Continue guided practice without treating this as verified mastery.",
            summary.guardianFacingInterpretation,
        )
    }

    @Test
    fun `blank guardian summary focus defers`() {
        val result =
            GuardianLearningSummaryCoordinator().prepare(
                traceId = TraceId.from("trace:stage150-focus"),
                learningProgress = learningProgress(),
                guardianSummaryFocus = "   ",
                learnerProgressSummary = "Bounded summary.",
                guardianFacingInterpretation = "Bounded interpretation.",
            )

        assertEquals(
            GuardianLearningSummaryPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.guardianLearningSummary)
    }

    @Test
    fun `blank learner progress summary defers`() {
        val result =
            GuardianLearningSummaryCoordinator().prepare(
                traceId = TraceId.from("trace:stage150-summary"),
                learningProgress = learningProgress(),
                guardianSummaryFocus = "Fractions progress",
                learnerProgressSummary = "   ",
                guardianFacingInterpretation = "Bounded interpretation.",
            )

        assertEquals(
            GuardianLearningSummaryPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.guardianLearningSummary)
    }

    @Test
    fun `blank guardian facing interpretation defers`() {
        val result =
            GuardianLearningSummaryCoordinator().prepare(
                traceId = TraceId.from("trace:stage150-interpretation"),
                learningProgress = learningProgress(),
                guardianSummaryFocus = "Fractions progress",
                learnerProgressSummary = "Bounded summary.",
                guardianFacingInterpretation = "   ",
            )

        assertEquals(
            GuardianLearningSummaryPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.guardianLearningSummary)
    }

    @Test
    fun `prepared result requires guardian learning summary context`() {
        assertFailsWith<IllegalArgumentException> {
            GuardianLearningSummaryPreparationResult.create(
                traceId = TraceId.from("trace:stage150-invalid-prepared"),
                status = GuardianLearningSummaryPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain guardian learning summary context`() {
        val prepared =
            GuardianLearningSummaryCoordinator().prepare(
                traceId = TraceId.from("trace:stage150-source"),
                learningProgress = learningProgress(),
                guardianSummaryFocus = "Fractions progress",
                learnerProgressSummary = "Bounded summary.",
                guardianFacingInterpretation = "Bounded interpretation.",
            )

        val summary =
            requireNotNull(prepared.guardianLearningSummary)

        assertFailsWith<IllegalArgumentException> {
            GuardianLearningSummaryPreparationResult.create(
                traceId = TraceId.from("trace:stage150-invalid-deferred"),
                status = GuardianLearningSummaryPreparationStatus.DEFERRED,
                guardianLearningSummary = summary,
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
            IdentityId.from("identity:stage150-runtime-child")

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
                                "education-session:stage150-runtime",
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
