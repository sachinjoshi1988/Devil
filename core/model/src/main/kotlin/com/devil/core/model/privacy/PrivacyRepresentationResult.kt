package com.devil.core.model.privacy

/**
 * Stage 46 bounded transformed representation.
 *
 * representation is present only for FULL or REDACTED status.
 *
 * METADATA_ONLY and SUPPRESSED deliberately contain no protected representation.
 *
 * The result carries classification metadata so later constitutional processing
 * retains the privacy context without reproducing protected content.
 */
@ConsistentCopyVisibility
data class PrivacyRepresentationResult private constructor(
    val status: PrivacyRepresentationStatus,
    val classification: PrivacyDataClassification,
    val representation: String?,
) {
    companion object {

        fun create(
            status: PrivacyRepresentationStatus,
            classification: PrivacyDataClassification,
            representation: String?,
        ): PrivacyRepresentationResult {
            val normalizedRepresentation =
                representation
                    ?.trim()
                    ?.takeIf {
                        it.isNotEmpty()
                    }

            when (status) {
                PrivacyRepresentationStatus.FULL,
                PrivacyRepresentationStatus.REDACTED,
                ->
                    require(normalizedRepresentation != null) {
                        "Full or redacted privacy representation requires non-blank representation."
                    }

                PrivacyRepresentationStatus.METADATA_ONLY,
                PrivacyRepresentationStatus.SUPPRESSED,
                ->
                    require(normalizedRepresentation == null) {
                        "Metadata-only or suppressed privacy representation must not contain protected representation."
                    }
            }

            return PrivacyRepresentationResult(
                status = status,
                classification = classification,
                representation = normalizedRepresentation,
            )
        }
    }
}
