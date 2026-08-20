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

class Stage149LearningProgressGovernanceTest {

    @Test
    fun `coordinator prepares bounded learning progress`() {
        val studyCompanion = studyCompanion()
        val traceId = TraceId.from("trace:stage149-prepared")

        val result =
            LearningProgressCoordinator().prepare(
                traceId = traceId,
                studyCompanion = studyCompanion,
                progressFocus = "Equivalent fractions",
                learnerEvidenceDescription =
                    "Learner explains two equivalent examples independently.",
                progressInterpretation =
                    "Shows increased independence with the concept.",
            )

        assertEquals(
            LearningProgressPreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(traceId, result.traceId)

        val progress = requireNotNull(result.learningProgress)

        assertSame(studyCompanion, progress.studyCompanion)
        assertEquals("Equivalent fractions", progress.progressFocus)
        assertEquals(
            "Learner explains two equivalent examples independently.",
            progress.learnerEvidenceDescription,
        )
        assertEquals(
            "Shows increased independence with the concept.",
            progress.progressInterpretation,
        )
    }

    @Test
    fun `blank progress focus defers`() {
        val result =
            LearningProgressCoordinator().prepare(
                traceId = TraceId.from("trace:stage149-focus"),
                studyCompanion = studyCompanion(),
                progressFocus = "   ",
                learnerEvidenceDescription = "Learner supplied evidence.",
                progressInterpretation = "Bounded interpretation.",
            )

        assertEquals(
            LearningProgressPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.learningProgress)
    }

    @Test
    fun `blank learner evidence defers`() {
        val result =
            LearningProgressCoordinator().prepare(
                traceId = TraceId.from("trace:stage149-evidence"),
                studyCompanion = studyCompanion(),
                progressFocus = "Equivalent fractions",
                learnerEvidenceDescription = "   ",
                progressInterpretation = "Bounded interpretation.",
            )

        assertEquals(
            LearningProgressPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.learningProgress)
    }

    @Test
    fun `blank progress interpretation defers`() {
        val result =
            LearningProgressCoordinator().prepare(
                traceId = TraceId.from("trace:stage149-interpretation"),
                studyCompanion = studyCompanion(),
                progressFocus = "Equivalent fractions",
                learnerEvidenceDescription = "Learner supplied evidence.",
                progressInterpretation = "   ",
            )

        assertEquals(
            LearningProgressPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.learningProgress)
    }

    @Test
    fun `prepared result requires learning progress context`() {
        assertFailsWith<IllegalArgumentException> {
            LearningProgressPreparationResult.create(
                traceId = TraceId.from("trace:stage149-invalid-prepared"),
                status = LearningProgressPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain learning progress context`() {
        val prepared =
            LearningProgressCoordinator().prepare(
                traceId = TraceId.from("trace:stage149-source"),
                studyCompanion = studyCompanion(),
                progressFocus = "Equivalent fractions",
                learnerEvidenceDescription = "Learner supplied evidence.",
                progressInterpretation = "Bounded interpretation.",
            )

        val progress = requireNotNull(prepared.learningProgress)

        assertFailsWith<IllegalArgumentException> {
            LearningProgressPreparationResult.create(
                traceId = TraceId.from("trace:stage149-invalid-deferred"),
                status = LearningProgressPreparationStatus.DEFERRED,
                learningProgress = progress,
            )
        }
    }

    private fun studyCompanion(): StudyCompanionRecord {
        return StudyCompanionRecord.create(
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
        )
    }

    private fun childPrivacyBoundary(): ChildPrivacyBoundaryRecord {
        val subjectIdentity =
            IdentityId.from("identity:stage149-runtime-child")

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
                                "education-session:stage149-runtime",
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
