package com.devil.core.runtime.child

import com.devil.core.model.child.ChildGuardianContext
import com.devil.core.model.child.ChildPolicyDecisionStatus
import com.devil.core.model.child.ChildPolicyRequirement
import com.devil.core.model.child.ChildSubjectClassification
import com.devil.core.model.child.GuardianAuthorityRecord
import com.devil.core.model.child.GuardianAuthorityStatus
import com.devil.core.model.common.TraceId
import com.devil.core.model.identity.IdentityId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertSame

class Stage86ChildGuardianFoundationGovernanceTest {

    @Test
    fun `runtime foundation preserves existing stage 44 child allowance`() {
        val context =
            ChildGuardianContext.create(
                subjectIdentityId =
                    IdentityId.from(
                        "identity:stage86-child-001",
                    ),
                classification =
                    ChildSubjectClassification.CHILD,
            )

        val result =
            ChildGuardianRuntimeCoordinator().evaluate(
                traceId =
                    TraceId.from(
                        "trace-stage86-child-001",
                    ),
                context = context,
                requirement =
                    ChildPolicyRequirement.CHILD_ALLOWED,
            )

        assertEquals(
            ChildGuardianPolicyEvaluationStatus.EVALUATED,
            result.status,
        )

        val decision = requireNotNull(result.decision)

        assertEquals(
            ChildPolicyDecisionStatus.ALLOWED_BY_CHILD_POLICY,
            decision.status,
        )

        assertSame(
            context,
            decision.context,
        )
    }

    @Test
    fun `runtime foundation preserves child policy block`() {
        val result =
            ChildGuardianRuntimeCoordinator().evaluate(
                traceId =
                    TraceId.from(
                        "trace-stage86-child-002",
                    ),
                context =
                    childContext(
                        classification =
                            ChildSubjectClassification.CHILD,
                    ),
                requirement =
                    ChildPolicyRequirement.CHILD_BLOCKED,
            )

        assertEquals(
            ChildPolicyDecisionStatus.BLOCKED_BY_CHILD_POLICY,
            requireNotNull(result.decision).status,
        )
    }

    @Test
    fun `guardian authority does not become guardian approval`() {
        val childIdentityId =
            IdentityId.from(
                "identity:stage86-child-guardian-001",
            )

        val context =
            ChildGuardianContext.create(
                subjectIdentityId = childIdentityId,
                classification =
                    ChildSubjectClassification.CHILD,
                guardianAuthority =
                    GuardianAuthorityRecord.create(
                        childIdentityId = childIdentityId,
                        guardianIdentityId =
                            IdentityId.from(
                                "identity:stage86-guardian-001",
                            ),
                        status =
                            GuardianAuthorityStatus.ESTABLISHED,
                    ),
            )

        val result =
            ChildGuardianRuntimeCoordinator().evaluate(
                traceId =
                    TraceId.from(
                        "trace-stage86-child-003",
                    ),
                context = context,
                requirement =
                    ChildPolicyRequirement.GUARDIAN_APPROVAL_REQUIRED,
            )

        assertEquals(
            ChildPolicyDecisionStatus.GUARDIAN_APPROVAL_REQUIRED,
            requireNotNull(result.decision).status,
        )
    }

    @Test
    fun `unknown child classification remains unavailable`() {
        val result =
            ChildGuardianRuntimeCoordinator().evaluate(
                traceId =
                    TraceId.from(
                        "trace-stage86-child-004",
                    ),
                context =
                    childContext(
                        classification =
                            ChildSubjectClassification.UNKNOWN,
                    ),
                requirement =
                    ChildPolicyRequirement.CHILD_ALLOWED,
            )

        assertEquals(
            ChildPolicyDecisionStatus.UNAVAILABLE,
            requireNotNull(result.decision).status,
        )
    }

    @Test
    fun `not child classification preserves stage 44 not applicable semantics`() {
        val result =
            ChildGuardianRuntimeCoordinator().evaluate(
                traceId =
                    TraceId.from(
                        "trace-stage86-child-005",
                    ),
                context =
                    childContext(
                        classification =
                            ChildSubjectClassification.NOT_CHILD,
                    ),
                requirement =
                    ChildPolicyRequirement.CHILD_BLOCKED,
            )

        assertEquals(
            ChildPolicyDecisionStatus.NOT_APPLICABLE,
            requireNotNull(result.decision).status,
        )
    }

    @Test
    fun `evaluated runtime result requires one stage 44 decision`() {
        assertFailsWith<IllegalArgumentException> {
            ChildGuardianPolicyEvaluationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage86-result-001",
                    ),
                status =
                    ChildGuardianPolicyEvaluationStatus.EVALUATED,
            )
        }
    }

    @Test
    fun `deferred runtime result cannot smuggle child policy decision`() {
        val evaluated =
            ChildGuardianRuntimeCoordinator().evaluate(
                traceId =
                    TraceId.from(
                        "trace-stage86-result-source",
                    ),
                context =
                    childContext(
                        classification =
                            ChildSubjectClassification.CHILD,
                    ),
                requirement =
                    ChildPolicyRequirement.CHILD_ALLOWED,
            )

        assertFailsWith<IllegalArgumentException> {
            ChildGuardianPolicyEvaluationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage86-result-002",
                    ),
                status =
                    ChildGuardianPolicyEvaluationStatus.DEFERRED,
                decision =
                    requireNotNull(evaluated.decision),
            )
        }
    }

    @Test
    fun `deferred result contains no child policy decision`() {
        val result =
            ChildGuardianPolicyEvaluationResult.create(
                traceId =
                    TraceId.from(
                        "trace-stage86-result-003",
                    ),
                status =
                    ChildGuardianPolicyEvaluationStatus.DEFERRED,
            )

        assertNull(result.decision)
    }

    private fun childContext(
        classification: ChildSubjectClassification,
    ): ChildGuardianContext {
        return ChildGuardianContext.create(
            subjectIdentityId =
                IdentityId.from(
                    "identity:stage86-test-subject",
                ),
            classification = classification,
        )
    }
}
