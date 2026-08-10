package com.devil.core.model.privacy

/**
 * Stage 46 pure disclosure-treatment policy.
 *
 * This policy consumes privacy assessment metadata only.
 *
 * It does not receive or inspect the protected representation.
 *
 * Therefore it performs no content redaction itself.
 *
 * It decides only the treatment later representation handling must obey.
 *
 * Privacy disclosure treatment
 * != authorization
 * != transmission permission
 * != execution permission.
 *
 * An UNAVAILABLE exposure assessment remains unavailable.
 *
 * Unknown privacy eligibility must never be converted into an affirmative
 * disclosure treatment.
 */
class PrivacyDisclosurePolicy {

    fun evaluate(
        request: PrivacyDisclosureRequest,
    ): PrivacyDisclosureDecision {
        val exposure =
            request.exposureAssessment

        return when (exposure.status) {
            PrivacyExposureStatus.ALLOWED ->
                evaluateAllowed(
                    request = request,
                )

            PrivacyExposureStatus.RESTRICTED ->
                PrivacyDisclosureDecision.create(
                    status = PrivacyDisclosureStatus.AVAILABLE,
                    treatment =
                        PrivacyDisclosureTreatment.METADATA_ONLY,
                    request = request,
                    rationale =
                        "Restricted privacy exposure permits only bounded non-content metadata through this disclosure boundary.",
                )

            PrivacyExposureStatus.BLOCKED ->
                PrivacyDisclosureDecision.create(
                    status = PrivacyDisclosureStatus.BLOCKED,
                    treatment = null,
                    request = request,
                    rationale =
                        "Blocked privacy exposure does not permit a disclosure treatment.",
                )

            PrivacyExposureStatus.UNAVAILABLE ->
                PrivacyDisclosureDecision.create(
                    status = PrivacyDisclosureStatus.UNAVAILABLE,
                    treatment = null,
                    request = request,
                    rationale =
                        "Unavailable privacy exposure cannot safely establish a disclosure treatment.",
                )
        }
    }

    private fun evaluateAllowed(
        request: PrivacyDisclosureRequest,
    ): PrivacyDisclosureDecision {
        val exposureRequest =
            request.exposureAssessment.request

        val treatment =
            when (exposureRequest.classification) {
                PrivacyDataClassification.PUBLIC ->
                    PrivacyDisclosureTreatment.FULL

                PrivacyDataClassification.PRIVATE ->
                    when (exposureRequest.target) {
                        PrivacyExposureTarget.INTERNAL_PROCESSING,
                        PrivacyExposureTarget.OWNER_PRESENTATION,
                        ->
                            PrivacyDisclosureTreatment.FULL

                        PrivacyExposureTarget.SUBJECT_PRESENTATION ->
                            PrivacyDisclosureTreatment.METADATA_ONLY

                        PrivacyExposureTarget.EXTERNAL_SYSTEM ->
                            PrivacyDisclosureTreatment.SUPPRESSED
                    }

                PrivacyDataClassification.SENSITIVE ->
                    when (exposureRequest.target) {
                        PrivacyExposureTarget.INTERNAL_PROCESSING ->
                            PrivacyDisclosureTreatment.FULL

                        PrivacyExposureTarget.OWNER_PRESENTATION ->
                            PrivacyDisclosureTreatment.REDACTED

                        PrivacyExposureTarget.SUBJECT_PRESENTATION,
                        PrivacyExposureTarget.EXTERNAL_SYSTEM,
                        ->
                            PrivacyDisclosureTreatment.SUPPRESSED
                    }

                PrivacyDataClassification.HIGHLY_SENSITIVE ->
                    when (exposureRequest.target) {
                        PrivacyExposureTarget.INTERNAL_PROCESSING ->
                            PrivacyDisclosureTreatment.METADATA_ONLY

                        PrivacyExposureTarget.OWNER_PRESENTATION,
                        PrivacyExposureTarget.SUBJECT_PRESENTATION,
                        PrivacyExposureTarget.EXTERNAL_SYSTEM,
                        ->
                            PrivacyDisclosureTreatment.SUPPRESSED
                    }
            }

        return PrivacyDisclosureDecision.create(
            status = PrivacyDisclosureStatus.AVAILABLE,
            treatment = treatment,
            request = request,
            rationale =
                "A bounded disclosure treatment was derived from the already evaluated privacy exposure assessment.",
        )
    }
}
