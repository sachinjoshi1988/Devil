package com.devil.core.model.privacy

/**
 * Stage 46 deterministic in-memory representation reduction boundary.
 *
 * This component performs no I/O.
 *
 * It accepts a representation only after an AVAILABLE PrivacyDisclosureDecision.
 *
 * REDACTED never attempts partial masking or reversible transformation.
 *
 * It replaces the entire protected representation with one fixed marker so the
 * original value cannot accidentally survive through this bounded result.
 *
 * METADATA_ONLY and SUPPRESSED return no representation.
 *
 * This reducer does not log, transmit, persist, authenticate, authorize, invoke
 * UnifiedDevilRuntime, or execute any external action.
 */
class PrivacyRepresentationReducer {

    fun reduce(
        decision: PrivacyDisclosureDecision,
        representation: String,
    ): PrivacyRepresentationResult {
        require(
            decision.status == PrivacyDisclosureStatus.AVAILABLE,
        ) {
            "Privacy representation reduction requires an AVAILABLE disclosure decision."
        }

        val normalizedRepresentation =
            representation.trim()

        require(normalizedRepresentation.isNotEmpty()) {
            "Privacy representation must not be blank."
        }

        val classification =
            decision
                .request
                .exposureAssessment
                .request
                .classification

        return when (requireNotNull(decision.treatment)) {
            PrivacyDisclosureTreatment.FULL ->
                PrivacyRepresentationResult.create(
                    status = PrivacyRepresentationStatus.FULL,
                    classification = classification,
                    representation = normalizedRepresentation,
                )

            PrivacyDisclosureTreatment.REDACTED ->
                PrivacyRepresentationResult.create(
                    status = PrivacyRepresentationStatus.REDACTED,
                    classification = classification,
                    representation = REDACTION_MARKER,
                )

            PrivacyDisclosureTreatment.METADATA_ONLY ->
                PrivacyRepresentationResult.create(
                    status = PrivacyRepresentationStatus.METADATA_ONLY,
                    classification = classification,
                    representation = null,
                )

            PrivacyDisclosureTreatment.SUPPRESSED ->
                PrivacyRepresentationResult.create(
                    status = PrivacyRepresentationStatus.SUPPRESSED,
                    classification = classification,
                    representation = null,
                )
        }
    }

    private companion object {
        const val REDACTION_MARKER =
            "[REDACTED]"
    }
}
