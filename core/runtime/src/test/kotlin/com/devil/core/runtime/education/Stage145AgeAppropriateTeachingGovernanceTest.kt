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
import com.devil.core.model.education.ChildEducationRecord
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.education.GuardianEducationPolicyRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage145AgeAppropriateTeachingGovernanceTest {

    @Test
    fun `guardian policy context may prepare bounded age appropriate teaching`() {
        val traceId =
            TraceId.from(
                "trace-stage145-age-appropriate-001",
            )

        val guardianPolicy =
            guardianEducationPolicy()

        val result =
            AgeAppropriateTeachingCoordinator().prepare(
                traceId = traceId,
                guardianEducationPolicy = guardianPolicy,
                teachingLevel = "Primary learner",
                teachingApproach =
                    "Concrete examples with short explanations",
                teachingObjective =
                    "Support comprehension without bypassing child policy",
            )

        assertEquals(
            AgeAppropriateTeachingPreparationStatus.PREPARED,
            result.status,
        )

        assertEquals(
            traceId,
            result.traceId,
        )

        val teaching =
            requireNotNull(result.teaching)

        assertSame(
            guardianPolicy,
            teaching.guardianEducationPolicy,
        )
    }

    @Test
    fun `blank teaching level remains deferred`() {
        val result =
            AgeAppropriateTeachingCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage145-age-appropriate-002",
                    ),
                guardianEducationPolicy =
                    guardianEducationPolicy(),
                teachingLevel = "   ",
                teachingApproach =
                    "Guided examples",
                teachingObjective =
                    "Support learning",
            )

        assertEquals(
            AgeAppropriateTeachingPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.teaching,
        )
    }

    @Test
    fun `blank teaching approach remains deferred`() {
        val result =
            AgeAppropriateTeachingCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage145-age-appropriate-003",
                    ),
                guardianEducationPolicy =
                    guardianEducationPolicy(),
                teachingLevel =
                    "Primary learner",
                teachingApproach = "   ",
                teachingObjective =
                    "Support learning",
            )

        assertEquals(
            AgeAppropriateTeachingPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.teaching,
        )
    }

    @Test
    fun `blank teaching objective remains deferred`() {
        val result =
            AgeAppropriateTeachingCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage145-age-appropriate-004",
                    ),
                guardianEducationPolicy =
                    guardianEducationPolicy(),
                teachingLevel =
                    "Primary learner",
                teachingApproach =
                    "Guided examples",
                teachingObjective = "   ",
            )

        assertEquals(
            AgeAppropriateTeachingPreparationStatus.DEFERRED,
            result.status,
        )

        assertNull(
            result.teaching,
        )
    }

    @Test
    fun `prepared result requires teaching context`() {
        assertFailsWith<IllegalArgumentException> {
            AgeAppropriateTeachingPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage145-result-001",
                    ),
                status =
                    AgeAppropriateTeachingPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred result cannot smuggle teaching context`() {
        val teaching =
            requireNotNull(
                AgeAppropriateTeachingCoordinator()
                    .prepare(
                        traceId =
                            TraceId.from(
                                "trace-stage145-result-source",
                            ),
                        guardianEducationPolicy =
                            guardianEducationPolicy(),
                        teachingLevel =
                            "Primary learner",
                        teachingApproach =
                            "Guided examples",
                        teachingObjective =
                            "Support learning",
                    )
                    .teaching,
            )

        assertFailsWith<IllegalArgumentException> {
            AgeAppropriateTeachingPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage145-result-002",
                    ),
                status =
                    AgeAppropriateTeachingPreparationStatus.DEFERRED,
                teaching = teaching,
            )
        }
    }

    private fun guardianEducationPolicy(): GuardianEducationPolicyRecord {
        val identity =
            IdentityId.from(
                "identity:stage145-runtime-child",
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
                                "education-session:stage145-runtime",
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
