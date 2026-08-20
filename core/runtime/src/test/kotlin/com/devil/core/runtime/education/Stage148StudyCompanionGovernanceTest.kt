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

class Stage148StudyCompanionGovernanceTest {

    @Test
    fun `coordinator prepares bounded study companion`() {
        val homeworkAssistance = homeworkAssistance()
        val traceId = TraceId.from("trace:stage148-prepared")

        val result =
            StudyCompanionCoordinator().prepare(
                traceId = traceId,
                homeworkAssistance = homeworkAssistance,
                studyFocus = "Fractions revision",
                studyApproach = "Guided review and practice",
                learnerSupportObjective =
                    "Reinforce understanding before independent practice.",
            )

        assertEquals(StudyCompanionPreparationStatus.PREPARED, result.status)
        assertEquals(traceId, result.traceId)

        val companion = requireNotNull(result.studyCompanion)

        assertSame(homeworkAssistance, companion.homeworkAssistance)
        assertEquals("Fractions revision", companion.studyFocus)
        assertEquals("Guided review and practice", companion.studyApproach)
        assertEquals(
            "Reinforce understanding before independent practice.",
            companion.learnerSupportObjective,
        )
    }

    @Test
    fun `blank study focus defers`() {
        val result =
            StudyCompanionCoordinator().prepare(
                traceId = TraceId.from("trace:stage148-focus"),
                homeworkAssistance = homeworkAssistance(),
                studyFocus = "   ",
                studyApproach = "Guided review",
                learnerSupportObjective = "Support understanding",
            )

        assertEquals(StudyCompanionPreparationStatus.DEFERRED, result.status)
        assertNull(result.studyCompanion)
    }

    @Test
    fun `blank study approach defers`() {
        val result =
            StudyCompanionCoordinator().prepare(
                traceId = TraceId.from("trace:stage148-approach"),
                homeworkAssistance = homeworkAssistance(),
                studyFocus = "Fractions revision",
                studyApproach = "   ",
                learnerSupportObjective = "Support understanding",
            )

        assertEquals(StudyCompanionPreparationStatus.DEFERRED, result.status)
        assertNull(result.studyCompanion)
    }

    @Test
    fun `blank learner support objective defers`() {
        val result =
            StudyCompanionCoordinator().prepare(
                traceId = TraceId.from("trace:stage148-objective"),
                homeworkAssistance = homeworkAssistance(),
                studyFocus = "Fractions revision",
                studyApproach = "Guided review",
                learnerSupportObjective = "   ",
            )

        assertEquals(StudyCompanionPreparationStatus.DEFERRED, result.status)
        assertNull(result.studyCompanion)
    }

    @Test
    fun `prepared result requires study companion context`() {
        assertFailsWith<IllegalArgumentException> {
            StudyCompanionPreparationResult.create(
                traceId = TraceId.from("trace:stage148-invalid-prepared"),
                status = StudyCompanionPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain study companion context`() {
        val prepared =
            StudyCompanionCoordinator().prepare(
                traceId = TraceId.from("trace:stage148-source"),
                homeworkAssistance = homeworkAssistance(),
                studyFocus = "Fractions revision",
                studyApproach = "Guided review",
                learnerSupportObjective = "Support understanding",
            )

        val companion = requireNotNull(prepared.studyCompanion)

        assertFailsWith<IllegalArgumentException> {
            StudyCompanionPreparationResult.create(
                traceId = TraceId.from("trace:stage148-invalid-deferred"),
                status = StudyCompanionPreparationStatus.DEFERRED,
                studyCompanion = companion,
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
            IdentityId.from("identity:stage148-runtime-child")

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
                                "education-session:stage148-runtime",
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
