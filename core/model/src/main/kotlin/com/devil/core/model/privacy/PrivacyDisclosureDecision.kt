package com.devil.core.model.privacy

/**
 * Immutable Stage 46 disclosure-treatment result.
 *
 * AVAILABLE requires exactly one disclosure treatment.
 *
 * BLOCKED and UNAVAILABLE contain no treatment.
 *
 * No protected raw value is stored in this result.
 *
 * This result is policy metadata only.
 *
 * A FULL treatment means only that Stage 46 disclosure reduction is not required
 * by this bounded policy decision. It does not grant constitutional
 * authorization or permission to transmit the representation.
 */
@ConsistentCopyVisibility
data class PrivacyDisclosureDecision private constructor(
    val status: PrivacyDisclosureStatus,
    val treatment: PrivacyDisclosureTreatment?,
    val request: PrivacyDisclosureRequest,
    val rationale: String,
) {
    companion object {

        fun create(
            status: PrivacyDisclosureStatus,
            treatment: PrivacyDisclosureTreatment?,
            request: PrivacyDisclosureRequest,
            rationale: String,
        ): PrivacyDisclosureDecision {
            val normalizedRationale =
                rationale.trim()

            require(normalizedRationale.isNotEmpty()) {
                "Privacy disclosure rationale must not be blank."
            }

            when (status) {
                PrivacyDisclosureStatus.AVAILABLE ->
                    require(treatment != null) {
                        "Available privacy disclosure decisions require a treatment."
                    }

                PrivacyDisclosureStatus.BLOCKED,
                PrivacyDisclosureStatus.UNAVAILABLE,
                ->
                    require(treatment == null) {
                        "Blocked or unavailable privacy disclosure decisions must not contain a treatment."
                    }
            }

            return PrivacyDisclosureDecision(
                status = status,
                treatment = treatment,
                request = request,
                rationale = normalizedRationale,
            )
        }
    }
}
