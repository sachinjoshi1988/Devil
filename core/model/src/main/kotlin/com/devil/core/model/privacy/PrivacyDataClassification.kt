package com.devil.core.model.privacy

/**
 * Stage 46 bounded classification of information according to privacy exposure
 * sensitivity.
 *
 * PUBLIC
 *     Information explicitly suitable for unrestricted presentation.
 *
 * PRIVATE
 *     Information intended for bounded user-facing or internal use but not
 *     unrestricted disclosure.
 *
 * SENSITIVE
 *     Information whose disclosure requires explicit privacy protection.
 *
 * HIGHLY_SENSITIVE
 *     Information whose unnecessary disclosure must fail closed.
 *
 * Classification
 * != identity
 * != authentication
 * != trust
 * != authorization
 * != permission
 * != memory eligibility
 * != execution approval.
 */
enum class PrivacyDataClassification {
    PUBLIC,
    PRIVATE,
    SENSITIVE,
    HIGHLY_SENSITIVE,
}
