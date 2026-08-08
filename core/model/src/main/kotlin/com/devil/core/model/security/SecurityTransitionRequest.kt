package com.devil.core.model.security

import com.devil.core.model.context.ContextEnvelope

/**
 * Represents one bounded request to evaluate a constitutional security-stage
 * transition.
 *
 * The request binds authoritative constitutional context, the currently
 * established SecurityStateRecord, a distinct requested SecurityStage, and a
 * concise rationale.
 *
 * Creating this request does not establish that the requested transition is
 * valid, sequential, authorized, authenticated, session-safe, owner-approved,
 * or permitted by constitutional security policy.
 *
 * In particular, requesting AUTHENTICATION does not authenticate a subject,
 * requesting SESSION does not create a session, requesting OWNER_MODE does not
 * establish owner authority, and requesting HIGH_SECURITY_CONFIRMATION does not
 * approve a protected action.
 *
 * Transition evaluation belongs to the Security Authority.
 */
@ConsistentCopyVisibility
data class SecurityTransitionRequest private constructor(
    val context: ContextEnvelope,
    val currentState: SecurityStateRecord,
    val requestedStage: SecurityStage,
    val rationale: String,
) {
    companion object {
        fun create(
            context: ContextEnvelope,
            currentState: SecurityStateRecord,
            requestedStage: SecurityStage,
            rationale: String,
        ): SecurityTransitionRequest {
            require(requestedStage != currentState.stage) {
                "Security transition request must target a different security stage."
            }

            val normalizedRationale = rationale.trim()

            require(normalizedRationale.isNotEmpty()) {
                "Security transition request rationale must not be blank."
            }

            return SecurityTransitionRequest(
                context = context,
                currentState = currentState,
                requestedStage = requestedStage,
                rationale = normalizedRationale,
            )
        }
    }
}
