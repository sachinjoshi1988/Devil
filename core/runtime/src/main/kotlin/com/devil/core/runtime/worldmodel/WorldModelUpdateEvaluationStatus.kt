package com.devil.core.runtime.worldmodel

/**
 * Describes the bounded result of constitutional World Model update evaluation.
 *
 * APPLICABLE means genuine constitutional World Model update evidence
 * established that one bounded update may be applied.
 *
 * UNAVAILABLE means no justified World Model update could currently be
 * established.
 *
 * FAILED represents an operational World Model update-evaluation failure.
 *
 * This status does not mutate world state, claim that world state changed,
 * change task or plan state, create memory or learning, communicate
 * externally, or produce a runtime result.
 */
enum class WorldModelUpdateEvaluationStatus {
    APPLICABLE,
    UNAVAILABLE,
    FAILED,
}
