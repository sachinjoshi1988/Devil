package com.devil.core.model.privacy

/**
 * Stage 46 bounded coordinator for privacy-disclosure treatment.
 *
 * Flow:
 *
 * PrivacyExposureAssessment
 * -> PrivacyDisclosureRequest
 * -> PrivacyDisclosurePolicy
 * -> PrivacyDisclosureDecision.
 *
 * No protected representation is accepted by this coordinator.
 *
 * It therefore cannot itself disclose, redact, transmit, log, present, or
 * persist sensitive content.
 *
 * This coordinator is not another Brain, Security Authority, Authorization
 * Authority, Memory Authority, Executive, runtime, or execution mechanism.
 */
class PrivacyDisclosureCoordinator(
    private val policy: PrivacyDisclosurePolicy =
        PrivacyDisclosurePolicy(),
) {

    fun evaluate(
        exposureAssessment: PrivacyExposureAssessment,
    ): PrivacyDisclosureDecision {
        return policy.evaluate(
            request =
                PrivacyDisclosureRequest.create(
                    exposureAssessment = exposureAssessment,
                ),
        )
    }
}
