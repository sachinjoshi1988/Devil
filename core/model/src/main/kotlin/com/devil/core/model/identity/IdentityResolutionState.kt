package com.devil.core.model.identity

/**
 * Describes the established state of one identity-resolution process.
 *
 * This state does not authenticate a subject, prove ownership, evaluate trust,
 * grant authorization, or permit an action.
 */
enum class IdentityResolutionState {
    RESOLVED,
    UNRESOLVED,
    AMBIGUOUS,
}
