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

class Stage147HomeworkAssistanceGovernanceTest {

    @Test
    fun `coordinator prepares bounded homework assistance`() {
        val traceId =
            TraceId.from("trace:stage147-prepared")

        val childPrivacyBoundary =
            childPrivacyBoundary()

        val result =
            HomeworkAssistanceCoordinator().prepare(
                traceId = traceId,
                childPrivacyBoundary = childPrivacyBoundary,
                homeworkSubject = "Fractions",
                assistanceObjective =
                    "Help the learner understand equivalent fractions.",
                assistanceApproach =
                    "Use guided explanation, decomposition, and hints.",
            )

        assertEquals(
            HomeworkAssistancePreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(
            traceId,
            result.traceId,
        )

        val assistance =
            requireNotNull(result.homeworkAssistance)

        assertSame(
            childPrivacyBoundary,
            assistance.childPrivacyBoundary,
        )
        assertEquals(
            "Fractions",
            assistance.homeworkSubject,
        )
        assertEquals(
            "Help the learner understand equivalent fractions.",
            assistance.assistanceObjective,
        )
        assertEquals(
            "Use guided explanation, decomposition, and hints.",
            assistance.assistanceApproach,
        )
    }

    @Test
    fun `blank homework subject defers`() {
        val result =
            HomeworkAssistanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage147-subject"),
                childPrivacyBoundary = childPrivacyBoundary(),
                homeworkSubject = "   ",
                assistanceObjective = "Support understanding.",
                assistanceApproach = "Use guided hints.",
            )

        assertEquals(
            HomeworkAssistancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.homeworkAssistance)
    }

    @Test
    fun `blank assistance objective defers`() {
        val result =
            HomeworkAssistanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage147-objective"),
                childPrivacyBoundary = childPrivacyBoundary(),
                homeworkSubject = "Fractions",
                assistanceObjective = "   ",
                assistanceApproach = "Use guided hints.",
            )

        assertEquals(
            HomeworkAssistancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.homeworkAssistance)
    }

    @Test
    fun `blank assistance approach defers`() {
        val result =
            HomeworkAssistanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage147-approach"),
                childPrivacyBoundary = childPrivacyBoundary(),
                homeworkSubject = "Fractions",
                assistanceObjective = "Support understanding.",
                assistanceApproach = "   ",
            )

        assertEquals(
            HomeworkAssistancePreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.homeworkAssistance)
    }

    @Test
    fun `prepared result requires homework assistance context`() {
        assertFailsWith<IllegalArgumentException> {
            HomeworkAssistancePreparationResult.create(
                traceId = TraceId.from("trace:stage147-invalid-prepared"),
                status = HomeworkAssistancePreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot contain homework assistance context`() {
        val prepared =
            HomeworkAssistanceCoordinator().prepare(
                traceId = TraceId.from("trace:stage147-source"),
                childPrivacyBoundary = childPrivacyBoundary(),
                homeworkSubject = "Fractions",
                assistanceObjective = "Support understanding.",
                assistanceApproach = "Use guided hints.",
            )

        val assistance =
            requireNotNull(prepared.homeworkAssistance)

        assertFailsWith<IllegalArgumentException> {
            HomeworkAssistancePreparationResult.create(
                traceId = TraceId.from("trace:stage147-invalid-deferred"),
                status = HomeworkAssistancePreparationStatus.DEFERRED,
                homeworkAssistance = assistance,
            )
        }
    }

    private fun childPrivacyBoundary(): ChildPrivacyBoundaryRecord {
        val subjectIdentity =
            IdentityId.from("identity:stage147-runtime-child")

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
                                "education-session:stage147-runtime",
                            ),
                        subjectIdentityId = subjectIdentity,
                        objective =
                            EducationObjective.create(
                                subject = "Child learning",
                                objective =
                                    "Support bounded child learning.",
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
