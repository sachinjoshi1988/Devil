package com.devil.core.model.child

/**
 * Stage 44 bounded policy requirement supplied for one requested activity.
 *
 * This contract deliberately does not classify Android capabilities, infer user
 * intent, inspect conversation text, or decide constitutional authorization.
 *
 * CHILD_ALLOWED means an approved child-policy rule permits the activity class
 * without a separate guardian approval step.
 *
 * GUARDIAN_APPROVAL_REQUIRED means child policy requires a distinct future
 * guardian-approval mechanism before the activity may proceed.
 *
 * CHILD_BLOCKED means the supplied child-policy rule prohibits the activity for
 * a subject classified CHILD.
 *
 * Requirement
 * != identity
 * != authentication
 * != guardian approval
 * != authorization
 * != Android permission
 * != Execution APPROVED.
 */
enum class ChildPolicyRequirement {
    CHILD_ALLOWED,
    GUARDIAN_APPROVAL_REQUIRED,
    CHILD_BLOCKED,
}
