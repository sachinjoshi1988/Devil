package com.devil.core.runtime.trust

/**
 * Describes whether a trust-evaluation request could be constructed.
 *
 * This status does not evaluate trust, authenticate a subject, prove
 * ownership, grant authorization, or permit execution.
 */
enum class TrustEvaluationRequestStatus {
    AVAILABLE,
    UNAVAILABLE,
    FAILED,
}
