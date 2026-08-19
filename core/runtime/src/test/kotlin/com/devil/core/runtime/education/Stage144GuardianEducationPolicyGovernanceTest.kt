package com.devil.core.runtime.education

import com.devil.core.model.child.ChildGuardianContext
import com.devil.core.model.child.ChildGuardianPolicy
import com.devil.core.model.child.ChildPolicyDecision
import com.devil.core.model.child.ChildPolicyRequirement
import com.devil.core.model.child.ChildPolicyRequest
import com.devil.core.model.child.ChildPolicySatisfactionPolicy
import com.devil.core.model.child.ChildPolicySatisfactionRequest
import com.devil.core.model.child.ChildPolicySatisfactionResult
import com.devil.core.model.child.ChildSubjectClassification
import com.devil.core.model.common.TraceId
import com.devil.core.model.education.ChildEducationRecord
import com.devil.core.model.education.EducationObjective
import com.devil.core.model.education.EducationSessionId
import com.devil.core.model.education.EducationSessionRecord
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage144GuardianEducationPolicyGovernanceTest {

    @Test
    fun `existing child education and Stage 44 policy evidence may prepare guardian policy foundation`() {
        val traceId =
            TraceId.from(
                "trace-stage144-guardian-policy-001",
            )

        val childEducation =
            childEducation()

        val policyDecision =
            policyDecision(
                childEducation = childEducation,
            )

        val satisfaction =
            policySatisfaction(
                policyDecision = policyDecision,
            )

        val result =
            GuardianEducationPolicyCoordinator().prepare(
                traceId = traceId,
                childEducation = childEducation,
                policyDecision = policyDecision,
                policySatisfaction = satisfaction,
                guardianPolicyFocus =
                    "Bounded education guardian policy",
            )

        assertEquals(
            GuardianEducationPolicyPreparationStatus.PREPARED,
            result.status,
        )
        assertEquals(
            traceId,
            result.traceId,
        )

        val policy =
            requireNotNull(result.guardianPolicy)

        assertSame(
            childEducation,
            policy.childEducation,
        )
        assertSame(
            policyDecision,
            policy.policyDecision,
        )
        assertSame(
            satisfaction,
            policy.policySatisfaction,
        )
    }

    @Test
    fun `different child guardian context remains deferred`() {
        val childEducation = childEducation()
        val otherChildEducation =
            childEducation(
                identity =
                    IdentityId.from(
                        "identity:stage144-runtime-other",
                    ),
            )

        val policyDecision =
            policyDecision(
                childEducation = otherChildEducation,
            )

        val result =
            GuardianEducationPolicyCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage144-guardian-policy-002",
                    ),
                childEducation = childEducation,
                policyDecision = policyDecision,
                policySatisfaction =
                    policySatisfaction(
                        policyDecision = policyDecision,
                    ),
                guardianPolicyFocus =
                    "Guardian policy",
            )

        assertEquals(
            GuardianEducationPolicyPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.guardianPolicy)
    }

    @Test
    fun `unrelated policy satisfaction remains deferred`() {
        val childEducation = childEducation()

        val policyDecision =
            policyDecision(
                childEducation = childEducation,
            )

        val secondDecision =
            policyDecision(
                childEducation = childEducation,
            )

        val result =
            GuardianEducationPolicyCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage144-guardian-policy-003",
                    ),
                childEducation = childEducation,
                policyDecision = policyDecision,
                policySatisfaction =
                    policySatisfaction(
                        policyDecision = secondDecision,
                    ),
                guardianPolicyFocus =
                    "Guardian policy",
            )

        assertEquals(
            GuardianEducationPolicyPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.guardianPolicy)
    }

    @Test
    fun `blank guardian policy focus remains deferred`() {
        val childEducation = childEducation()
        val policyDecision =
            policyDecision(
                childEducation = childEducation,
            )

        val result =
            GuardianEducationPolicyCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage144-guardian-policy-004",
                    ),
                childEducation = childEducation,
                policyDecision = policyDecision,
                policySatisfaction =
                    policySatisfaction(
                        policyDecision = policyDecision,
                    ),
                guardianPolicyFocus = "   ",
            )

        assertEquals(
            GuardianEducationPolicyPreparationStatus.DEFERRED,
            result.status,
        )
        assertNull(result.guardianPolicy)
    }

    @Test
    fun `prepared guardian policy result requires context`() {
        assertFailsWith<IllegalArgumentException> {
            GuardianEducationPolicyPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage144-result-001",
                    ),
                status =
                    GuardianEducationPolicyPreparationStatus.PREPARED,
            )
        }
    }

    @Test
    fun `deferred guardian policy result cannot smuggle context`() {
        val childEducation = childEducation()

        val policyDecision =
            policyDecision(
                childEducation = childEducation,
            )

        val satisfaction =
            policySatisfaction(
                policyDecision = policyDecision,
            )

        val prepared =
            GuardianEducationPolicyCoordinator().prepare(
                traceId =
                    TraceId.from(
                        "trace-stage144-result-source",
                    ),
                childEducation = childEducation,
                policyDecision = policyDecision,
                policySatisfaction = satisfaction,
                guardianPolicyFocus =
                    "Guardian policy",
            )

        val guardianPolicy =
            requireNotNull(prepared.guardianPolicy)

        assertFailsWith<IllegalArgumentException> {
            GuardianEducationPolicyPreparationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage144-result-002",
                    ),
                status =
                    GuardianEducationPolicyPreparationStatus.DEFERRED,
                guardianPolicy = guardianPolicy,
            )
        }
    }

    private fun childEducation(
        identity: IdentityId =
            IdentityId.from(
                "identity:stage144-runtime-child",
            ),
    ): ChildEducationRecord {
        val educationSession =
            EducationSessionRecord.create(
                sessionId =
                    EducationSessionId.from(
                        "education-session:stage144-runtime:${identity.value}",
                    ),
                subjectIdentityId = identity,
                objective =
                    EducationObjective.create(
                        subject = "Child Education",
                        objective =
                            "Prepare bounded Guardian Policy Foundation context.",
                    ),
            )

        val context =
            ChildGuardianContext.create(
                subjectIdentityId = identity,
                classification =
                    ChildSubjectClassification.CHILD,
            )

        return ChildEducationRecord.create(
            educationSession = educationSession,
            childGuardianContext = context,
            childEducationFocus =
                "Age-bounded learning support",
            childEducationObjective =
                "Prepare child education context",
        )
    }

    private fun policyDecision(
        childEducation: ChildEducationRecord,
    ): ChildPolicyDecision {
        return ChildGuardianPolicy().evaluate(
            ChildPolicyRequest.create(
                context =
                    childEducation.childGuardianContext,
                requirement =
                    ChildPolicyRequirement.CHILD_ALLOWED,
            ),
        )
    }

    private fun policySatisfaction(
        policyDecision: ChildPolicyDecision,
    ): ChildPolicySatisfactionResult {
        return ChildPolicySatisfactionPolicy().evaluate(
            ChildPolicySatisfactionRequest.create(
                policyDecision = policyDecision,
            ),
        )
    }
}
