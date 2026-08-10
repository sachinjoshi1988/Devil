package com.devil.core.model.child

/**
 * Stage 44 bounded status describing whether one already-evaluated child-policy
 * requirement has been satisfied.
 *
 * SATISFIED means only that Stage 44 child/guardian policy no longer blocks the
 * supplied bounded activity.
 *
 * UNSATISFIED means the supplied child/guardian requirement has not been met.
 *
 * BLOCKED means child policy explicitly prohibits the activity.
 *
 * NOT_APPLICABLE means the supplied subject is explicitly NOT_CHILD.
 *
 * UNAVAILABLE means Stage 44 lacks sufficient bounded evidence to determine
 * satisfaction safely.
 *
 * SATISFIED
 * != Devil authorization
 * != Android permission
 * != Executive readiness
 * != Execution APPROVED
 * != action attempted
 * != verified Outcome.
 */
enum class ChildPolicySatisfactionStatus {
    SATISFIED,
    UNSATISFIED,
    BLOCKED,
    NOT_APPLICABLE,
    UNAVAILABLE,
}
