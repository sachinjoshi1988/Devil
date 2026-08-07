package com.devil.core.runtime.memory

/**
 * Describes the stable operational result of constitutional Memory Authority
 * evaluation.
 *
 * COMMITTABLE means genuine constitutional evidence established that one
 * bounded MemoryAuthorityRequest may proceed to a later logical-memory
 * commitment mechanism governed by the single Memory Authority.
 *
 * COMMITTABLE does not create, persist, or commit logical memory. It does not
 * assign memory class, sensitivity, confidence, retention policy, source,
 * owner-visible reason, or storage destination.
 *
 * DEFERRED means no justified logical-memory commitment is currently available.
 * FAILED represents an operational failure with one matching error.
 */
enum class MemoryAuthorityStatus {
    COMMITTABLE,
    DEFERRED,
    FAILED,
}
