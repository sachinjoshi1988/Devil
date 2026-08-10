package com.devil.core.model.privacy

/**
 * Immutable Stage 46 result of one bounded privacy exposure evaluation.
 *
 * The original request remains attached so the policy basis remains explicit.
 *
 * This assessment is privacy policy evidence only.
 *
 * It does not authenticate a subject, authorize an operation, perform I/O,
 * persist information, redact content, execute anything, or establish success.
 */
@ConsistentCopyVisibility
data class PrivacyExposureAssessment private constructor(
    val status: PrivacyExposureStatus,
    val request: PrivacyExposureRequest,
    val rationale: String,
) {
    companion object {

        fun create(
            status: PrivacyExposureStatus,
            request: PrivacyExposureRequest,
            rationale: String,
        ): PrivacyExposureAssessment {
            val normalizedRationale =
                rationale.trim()

            require(normalizedRationale.isNotEmpty()) {
                "Privacy exposure assessment rationale must not be blank."
            }

            return PrivacyExposureAssessment(
                status = status,
                request = request,
                rationale = normalizedRationale,
            )
        }
    }
}
