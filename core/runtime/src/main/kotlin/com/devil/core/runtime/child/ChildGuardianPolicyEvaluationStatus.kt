package com.devil.core.runtime.child

/**
 * Stage 86 bounded runtime status for child/guardian policy evaluation.
 *
 * EVALUATED means one existing Stage 44 ChildPolicyDecision was produced from
 * explicitly supplied child/guardian context and policy requirement.
 *
 * DEFERRED means no truthful bounded child-policy evaluation was produced.
 *
 * EVALUATED does not mean:
 *
 * - the subject is a child unless the supplied Stage 44 context says so;
 * - subject identity was resolved;
 * - authentication succeeded;
 * - trust was established;
 * - guardian authority exists;
 * - guardian approval exists;
 * - Devil authorization exists;
 * - Owner Mode exists;
 * - a security session exists;
 * - Android permission exists;
 * - a capability is available;
 * - execution is approved;
 * - an Outcome occurred;
 * - constitutional Learning occurred;
 * - or Memory was committed.
 *
 * CHILD_POLICY_EVALUATED != AUTHENTICATED.
 * CHILD_POLICY_EVALUATED != AUTHORIZED.
 * CHILD_POLICY_EVALUATED != EXECUTION_APPROVED.
 */
enum class ChildGuardianPolicyEvaluationStatus {
    EVALUATED,
    DEFERRED,
}
