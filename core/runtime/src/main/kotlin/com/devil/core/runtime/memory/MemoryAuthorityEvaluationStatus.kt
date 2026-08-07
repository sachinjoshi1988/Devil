package com.devil.core.runtime.memory

/**
 * Describes the bounded result of constitutional Memory Authority evaluation.
 *
 * COMMITTABLE means genuine constitutional evidence established that one
 * bounded memory proposal may proceed to a later logical-memory commitment
 * mechanism governed by the single Memory Authority.
 *
 * UNAVAILABLE means no justified logical-memory commitment can currently be
 * established.
 *
 * FAILED represents an operational evaluation failure.
 *
 * This status does not create, persist, or commit logical memory. It does not
 * assign memory class, sensitivity, confidence, retention policy, source,
 * owner-visible reason, or storage destination.
 *
 * It does not mutate world state, change task or plan state, communicate
 * externally, bypass constitutional security review, or produce a runtime
 * result.
 */
enum class MemoryAuthorityEvaluationStatus {
    COMMITTABLE,
    UNAVAILABLE,
    FAILED,
}
