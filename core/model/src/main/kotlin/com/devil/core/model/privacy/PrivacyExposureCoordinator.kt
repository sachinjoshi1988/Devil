package com.devil.core.model.privacy

/**
 * Stage 46 bounded coordinator for privacy exposure assessment.
 *
 * Flow:
 *
 * PrivacyExposureRequest
 * -> PrivacyExposurePolicy
 * -> PrivacyExposureAssessment.
 *
 * This coordinator is not another Brain, Security Authority, Authorization
 * Authority, Memory Authority, Executive, Planner, runtime, redaction engine,
 * transport mechanism, or disclosure mechanism.
 *
 * ALLOWED means only that this Stage 46 privacy gate does not block the supplied
 * exposure request.
 *
 * ALLOWED
 * != constitutional authorization
 * != execution approval
 * != disclosure performed.
 */
class PrivacyExposureCoordinator(
    private val policy: PrivacyExposurePolicy =
        PrivacyExposurePolicy(),
) {

    fun assess(
        request: PrivacyExposureRequest,
    ): PrivacyExposureAssessment {
        return policy.assess(
            request = request,
        )
    }
}
