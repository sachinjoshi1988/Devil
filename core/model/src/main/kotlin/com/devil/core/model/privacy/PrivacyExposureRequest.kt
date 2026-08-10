package com.devil.core.model.privacy

/**
 * Immutable Stage 46 request for one bounded privacy exposure assessment.
 *
 * The request describes only:
 *
 * - the privacy classification of the information;
 * - the proposed exposure target;
 * - whether the caller supplied explicit protected-context evidence.
 *
 * protectedContextEstablished is supplied context only.
 *
 * It does not authenticate anyone, establish Owner Mode, grant authorization,
 * grant Android permission, execute an action, persist memory, or perform the
 * disclosure.
 */
@ConsistentCopyVisibility
data class PrivacyExposureRequest private constructor(
    val classification: PrivacyDataClassification,
    val target: PrivacyExposureTarget,
    val protectedContextEstablished: Boolean,
) {
    companion object {

        fun create(
            classification: PrivacyDataClassification,
            target: PrivacyExposureTarget,
            protectedContextEstablished: Boolean = false,
        ): PrivacyExposureRequest {
            return PrivacyExposureRequest(
                classification = classification,
                target = target,
                protectedContextEstablished = protectedContextEstablished,
            )
        }
    }
}
