package com.devil.core.model.privacy

/**
 * Stage 46 status of bounded representation reduction.
 *
 * FULL means the supplied representation remained intact.
 *
 * REDACTED means the supplied representation was replaced by the fixed Stage 46
 * redaction marker.
 *
 * METADATA_ONLY means no protected representation content remains.
 *
 * SUPPRESSED means no representation is returned.
 *
 * This status describes transformation only.
 *
 * It is not authorization, transmission, execution, or verified privacy.
 */
enum class PrivacyRepresentationStatus {
    FULL,
    REDACTED,
    METADATA_ONLY,
    SUPPRESSED,
}
