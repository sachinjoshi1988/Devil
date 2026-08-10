package com.devil.core.model.privacy

/**
 * Stage 46 result status for bounded privacy-disclosure evaluation.
 *
 * AVAILABLE means a bounded disclosure treatment was derived.
 *
 * BLOCKED means the supplied privacy exposure assessment does not permit
 * disclosure through this boundary.
 *
 * UNAVAILABLE means disclosure treatment cannot safely be derived from the
 * supplied information.
 *
 * AVAILABLE
 * != Devil authorization
 * != permission to disclose externally
 * != transmission performed
 * != execution approval.
 */
enum class PrivacyDisclosureStatus {
    AVAILABLE,
    BLOCKED,
    UNAVAILABLE,
}
