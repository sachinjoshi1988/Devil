package com.devil.core.model.privacy

/**
 * Stage 46 bounded treatment describing how an already classified representation
 * may be handled after privacy-exposure evaluation.
 *
 * FULL means the representation may remain intact with respect to this bounded
 * privacy-disclosure treatment only.
 *
 * REDACTED means the protected representation must not be preserved in disclosed
 * form and may be replaced only by an explicit redaction marker.
 *
 * METADATA_ONLY means protected representation content must not be disclosed.
 * Only bounded non-content metadata represented by the disclosure result may
 * remain.
 *
 * SUPPRESSED means no representation derived from the protected value may be
 * disclosed through this boundary.
 *
 * Treatment
 * != authentication
 * != trust
 * != authorization
 * != Android permission
 * != permission to transmit
 * != execution approval.
 */
enum class PrivacyDisclosureTreatment {
    FULL,
    REDACTED,
    METADATA_ONLY,
    SUPPRESSED,
}
