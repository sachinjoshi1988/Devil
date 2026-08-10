package com.devil.core.model.child

/**
 * Stage 44 bounded result of child-policy evaluation.
 *
 * ALLOWED_BY_CHILD_POLICY means only that the supplied child-policy requirement
 * does not block the activity for the supplied CHILD context.
 *
 * GUARDIAN_APPROVAL_REQUIRED means a separate guardian-approval mechanism is
 * required. It does not mean approval has already been obtained.
 *
 * BLOCKED_BY_CHILD_POLICY means the supplied child policy prohibits the activity.
 *
 * NOT_APPLICABLE means the supplied subject is explicitly classified NOT_CHILD.
 *
 * UNAVAILABLE means Stage 44 cannot safely determine a child-policy decision
 * from the supplied context.
 *
 * Every status remains distinct from constitutional authorization and execution.
 */
enum class ChildPolicyDecisionStatus {
    ALLOWED_BY_CHILD_POLICY,
    GUARDIAN_APPROVAL_REQUIRED,
    BLOCKED_BY_CHILD_POLICY,
    NOT_APPLICABLE,
    UNAVAILABLE,
}
