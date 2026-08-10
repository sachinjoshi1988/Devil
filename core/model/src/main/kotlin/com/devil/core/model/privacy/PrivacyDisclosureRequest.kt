package com.devil.core.model.privacy

/**
 * One explicit Stage 46 request for bounded disclosure-treatment evaluation.
 *
 * The request preserves one existing PrivacyExposureAssessment.
 *
 * It deliberately does not contain the protected content itself.
 *
 * Therefore this policy layer can decide disclosure treatment without copying,
 * inspecting, logging, transmitting, or persisting sensitive representation
 * values.
 *
 * Creating this request does not:
 *
 * - authenticate anyone;
 * - establish Owner Mode;
 * - grant authorization;
 * - grant Android permission;
 * - transmit data;
 * - present data;
 * - persist memory;
 * - invoke UnifiedDevilRuntime;
 * - or execute an action.
 */
@ConsistentCopyVisibility
data class PrivacyDisclosureRequest private constructor(
    val exposureAssessment: PrivacyExposureAssessment,
) {
    companion object {

        fun create(
            exposureAssessment: PrivacyExposureAssessment,
        ): PrivacyDisclosureRequest {
            return PrivacyDisclosureRequest(
                exposureAssessment = exposureAssessment,
            )
        }
    }
}
