package com.devil.core.model.education

import com.devil.core.model.child.ChildPolicyDecision
import com.devil.core.model.child.ChildPolicySatisfactionResult

/**
 * Immutable Stage 144 representation of one bounded Guardian Policy Foundation
 * context for Child Education.
 *
 * This record preserves:
 *
 * - one existing Stage 143 Child Education context;
 * - one existing Stage 44 ChildPolicyDecision;
 * - one existing Stage 44 ChildPolicySatisfactionResult;
 * - one explicitly supplied nonblank guardian-policy focus.
 *
 * Stage 144 does not create or replace Stage 44 child/guardian policy.
 *
 * The supplied policy decision and satisfaction result must describe the exact
 * ChildGuardianContext already preserved by the Child Education context.
 *
 * It does not:
 *
 * - infer child status;
 * - authenticate a child or guardian;
 * - establish guardian authority;
 * - create guardian approval;
 * - alter a Stage 44 policy decision;
 * - turn guardian authority into guardian approval;
 * - grant constitutional authorization;
 * - grant Android permission;
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
 * CHILD_POLICY_SATISFIED != EXECUTION_APPROVED.
 */
@ConsistentCopyVisibility
data class GuardianEducationPolicyRecord private constructor(
    val childEducation: ChildEducationRecord,
    val policyDecision: ChildPolicyDecision,
    val policySatisfaction: ChildPolicySatisfactionResult,
    val guardianPolicyFocus: String,
) {
    companion object {

        fun create(
            childEducation: ChildEducationRecord,
            policyDecision: ChildPolicyDecision,
            policySatisfaction: ChildPolicySatisfactionResult,
            guardianPolicyFocus: String,
        ): GuardianEducationPolicyRecord {
            require(
                policyDecision.context ===
                    childEducation.childGuardianContext,
            ) {
                "Guardian Policy Foundation requires the exact Child Education child/guardian context."
            }

            require(
                policySatisfaction.request.policyDecision ===
                    policyDecision,
            ) {
                "Guardian Policy Foundation satisfaction must belong to the supplied child-policy decision."
            }

            val normalizedGuardianPolicyFocus =
                guardianPolicyFocus.trim()

            require(normalizedGuardianPolicyFocus.isNotEmpty()) {
                "Guardian Policy Foundation focus must not be blank."
            }

            return GuardianEducationPolicyRecord(
                childEducation = childEducation,
                policyDecision = policyDecision,
                policySatisfaction = policySatisfaction,
                guardianPolicyFocus = normalizedGuardianPolicyFocus,
            )
        }
    }
}
