package com.devil.core.runtime.identity

/**
 * Describes whether a structured identity-resolution request is available.
 *
 * This status does not resolve identity, authenticate a subject, establish
 * ownership, evaluate trust, or grant authorization.
 */
enum class IdentityResolutionRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
