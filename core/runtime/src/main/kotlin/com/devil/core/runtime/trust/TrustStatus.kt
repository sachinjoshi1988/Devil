package com.devil.core.runtime.trust

/**
 * Describes the result of trust evaluation.
 *
 * This status does not establish identity, grant authorization, perform
 * reasoning, create plans, or permit execution.
 */
enum class TrustStatus {
    EVALUATED,
    DEFERRED,
    FAILED,
}
