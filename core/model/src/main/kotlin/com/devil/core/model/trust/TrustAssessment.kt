package com.devil.core.model.trust

import com.devil.core.model.identity.IdentityId

/**
 * Records one bounded trust assessment for a resolved subject identity.
 *
 * The assessment preserves a subject, trust classification, and concise
 * rationale. It does not perform identity resolution or authentication,
 * prove ownership, grant authorization, enter Owner Mode, or permit execution.
 */
@ConsistentCopyVisibility
data class TrustAssessment private constructor(
    val subjectIdentityId: IdentityId,
    val level: SubjectTrustLevel,
    val rationale: String,
) {
    companion object {
        fun create(
            subjectIdentityId: IdentityId,
            level: SubjectTrustLevel,
            rationale: String,
        ): TrustAssessment {
            val normalizedRationale = rationale.trim()

            require(normalizedRationale.isNotEmpty()) {
                "Trust assessment rationale must not be blank."
            }

            return TrustAssessment(
                subjectIdentityId = subjectIdentityId,
                level = level,
                rationale = normalizedRationale,
            )
        }
    }
}
