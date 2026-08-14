package com.devil.core.runtime.child

import com.devil.core.model.child.ChildGuardianContext
import com.devil.core.model.child.ChildPolicyCoordinator
import com.devil.core.model.child.ChildPolicyDecision
import com.devil.core.model.child.ChildPolicyRequest
import com.devil.core.model.child.ChildPolicyRequirement
import com.devil.core.model.common.TraceId

/**
 * Stage 86 bounded Child / Guardian Foundation runtime coordinator.
 *
 * Stage 44 already owns the constitutional child/guardian model:
 *
 * ChildGuardianContext
 * -> ChildPolicyRequest
 * -> ChildGuardianPolicy
 * -> ChildPolicyDecision.
 *
 * Stage 86 does not replace, duplicate, broaden, or reinterpret that policy.
 *
 * This coordinator provides only a bounded runtime-layer entry around the existing
 * Stage 44 policy contracts.
 *
 * It accepts already-supplied:
 *
 * - constitutional TraceId;
 * - ChildGuardianContext;
 * - ChildPolicyRequirement.
 *
 * It does not:
 *
 * - infer age;
 * - infer child classification;
 * - infer guardian relationships;
 * - establish guardian authority;
 * - obtain guardian approval;
 * - infer subject identity;
 * - authenticate a child or guardian;
 * - establish trust;
 * - grant Devil authorization;
 * - create or validate a security session;
 * - enter Owner Mode;
 * - create another Brain;
 * - create another Devil intelligence;
 * - create another Unified Devil Runtime;
 * - create child-specific Memory or Security authorities;
 * - alter Stage 44 policy semantics;
 * - invoke UnifiedDevilRuntime;
 * - create Tasks or Plans;
 * - select capabilities;
 * - grant Android permission;
 * - execute actions;
 * - establish Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - propose or commit Memory;
 * - persist child or guardian state;
 * - apply education policy;
 * - create an education session;
 * - or communicate with a platform API.
 *
 * Child classification remains supplied evidence only.
 *
 * CHILD_CLASSIFICATION != AUTHENTICATION.
 * GUARDIAN_AUTHORITY != GUARDIAN_APPROVAL.
 * GUARDIAN_APPROVAL != DEVIL_AUTHORIZATION.
 * CHILD_POLICY_ALLOWANCE != EXECUTION_AUTHORITY.
 * EDUCATION != CHILD_POLICY.
 * CHILD_DOMAIN != ANOTHER_INTELLIGENCE.
 */
class ChildGuardianRuntimeCoordinator(
    private val policyCoordinator: ChildPolicyCoordinator =
        ChildPolicyCoordinator(),
) {

    fun evaluate(
        traceId: TraceId,
        context: ChildGuardianContext,
        requirement: ChildPolicyRequirement,
    ): ChildGuardianPolicyEvaluationResult {
        val decision: ChildPolicyDecision =
            policyCoordinator.evaluate(
                request =
                    ChildPolicyRequest.create(
                        context = context,
                        requirement = requirement,
                    ),
            )

        return ChildGuardianPolicyEvaluationResult.create(
            traceId = traceId,
            status = ChildGuardianPolicyEvaluationStatus.EVALUATED,
            decision = decision,
        )
    }
}
