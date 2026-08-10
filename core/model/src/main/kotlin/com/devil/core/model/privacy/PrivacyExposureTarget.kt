package com.devil.core.model.privacy

/**
 * Stage 46 bounded destination class for one proposed information exposure.
 *
 * INTERNAL_PROCESSING represents bounded in-process use.
 *
 * OWNER_PRESENTATION represents presentation intended for the authenticated or
 * otherwise constitutionally validated owner context. This enum does not itself
 * prove that such validation exists.
 *
 * SUBJECT_PRESENTATION represents presentation to the current non-owner subject
 * context.
 *
 * EXTERNAL_SYSTEM represents disclosure to a remote, platform, service, app, or
 * other system outside the bounded Devil process.
 *
 * Exposure target
 * != authentication
 * != owner identity
 * != authorization
 * != Android permission
 * != permission to disclose.
 */
enum class PrivacyExposureTarget {
    INTERNAL_PROCESSING,
    OWNER_PRESENTATION,
    SUBJECT_PRESENTATION,
    EXTERNAL_SYSTEM,
}
