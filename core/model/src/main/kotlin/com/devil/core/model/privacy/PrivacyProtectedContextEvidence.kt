package com.devil.core.model.privacy

/**
 * Immutable Stage 46 evidence describing one bounded protected-context
 * determination for privacy policy.
 *
 * The record preserves only the determination and its rationale.
 *
 * It deliberately does not contain credentials, authentication secrets,
 * conversation content, notification content, raw protected representation,
 * Android permission state, or executable authority.
 *
 * The evidence is descriptive input to privacy policy only.
 *
 * PrivacyProtectedContextEvidence
 * != authentication evidence
 * != owner identity proof
 * != session creation
 * != Owner Mode entry
 * != authorization
 * != permission to disclose
 * != execution approval.
 */
@ConsistentCopyVisibility
data class PrivacyProtectedContextEvidence private constructor(
    val status: PrivacyProtectedContextStatus,
    val rationale: String,
) {
    companion object {

        fun create(
            status: PrivacyProtectedContextStatus,
            rationale: String,
        ): PrivacyProtectedContextEvidence {
            val normalizedRationale =
                rationale.trim()

            require(normalizedRationale.isNotEmpty()) {
                "Privacy protected-context evidence rationale must not be blank."
            }

            return PrivacyProtectedContextEvidence(
                status = status,
                rationale = normalizedRationale,
            )
        }
    }
}
