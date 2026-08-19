package com.devil.core.runtime.education

import com.devil.core.model.child.ChildPolicyDecision
import com.devil.core.model.child.ChildPolicySatisfactionResult
import com.devil.core.model.common.TraceId
import com.devil.core.model.education.ChildEducationRecord
import com.devil.core.model.education.GuardianEducationPolicyRecord

/**
 * Stage 144 bounded Guardian Policy Foundation coordinator.
 *
 * This coordinator binds an existing Stage 143 Child Education context to
 * already-existing Stage 44 child-policy decision and satisfaction evidence.
 *
 * Stage 44 remains sovereign over child/guardian policy semantics.
 *
 * It does not:
 *
 * - infer child status;
 * - authenticate a child or guardian;
 * - create ChildGuardianContext;
 * - establish GuardianAuthorityRecord;
 * - obtain GuardianApprovalDecision;
 * - evaluate or replace ChildGuardianPolicy;
 * - evaluate or replace ChildPolicySatisfactionPolicy;
 * - treat guardian authority as guardian approval;
 * - grant constitutional authorization;
 * - invoke Executive or execution;
 * - establish Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - teach the learner;
 * - or implement Stage 145 Age-Appropriate Teaching.
 *
 * GUARDIAN_POLICY_FOUNDATION != GUARDIAN_AUTHORITY.
 * GUARDIAN_POLICY_FOUNDATION != GUARDIAN_APPROVAL.
 * CHILD_POLICY_SATISFIED != DEVIL_AUTHORIZATION.
 */
class GuardianEducationPolicyCoordinator {

    fun prepare(
        traceId: TraceId,
        childEducation: ChildEducationRecord,
        policyDecision: ChildPolicyDecision,
        policySatisfaction: ChildPolicySatisfactionResult,
        guardianPolicyFocus: String,
    ): GuardianEducationPolicyPreparationResult {
        if (
            policyDecision.context !==
            childEducation.childGuardianContext ||
            policySatisfaction.request.policyDecision !==
            policyDecision ||
            guardianPolicyFocus.isBlank()
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val guardianPolicy =
            GuardianEducationPolicyRecord.create(
                childEducation = childEducation,
                policyDecision = policyDecision,
                policySatisfaction = policySatisfaction,
                guardianPolicyFocus = guardianPolicyFocus,
            )

        return GuardianEducationPolicyPreparationResult.create(
            traceId = traceId,
            status =
                GuardianEducationPolicyPreparationStatus.PREPARED,
            guardianPolicy = guardianPolicy,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): GuardianEducationPolicyPreparationResult {
        return GuardianEducationPolicyPreparationResult.create(
            traceId = traceId,
            status =
                GuardianEducationPolicyPreparationStatus.DEFERRED,
        )
    }
}
