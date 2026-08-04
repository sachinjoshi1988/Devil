package com.devil.core.model.identity

/**
 * Represents one candidate selected by a future identity-resolution authority.
 *
 * This record preserves the selected candidate, confidence, and concise
 * rationale. It does not perform candidate selection, authenticate the subject,
 * prove ownership, evaluate trust, or grant authorization.
 */
@ConsistentCopyVisibility
data class IdentityResolutionSelection private constructor(
    val candidate: IdentityResolutionCandidate,
    val confidence: IdentityConfidence,
    val rationale: String,
) {
    companion object {
        fun create(
            candidate: IdentityResolutionCandidate,
            confidence: IdentityConfidence,
            rationale: String,
        ): IdentityResolutionSelection {
            val normalizedRationale = rationale.trim()

            require(normalizedRationale.isNotEmpty()) {
                "Identity resolution selection rationale must not be blank."
            }

            return IdentityResolutionSelection(
                candidate = candidate,
                confidence = confidence,
                rationale = normalizedRationale,
            )
        }
    }
}
