package com.devil.core.runtime.observation

/**
 * Describes the bounded result of constitutional observation evaluation.
 *
 * OBSERVED means that genuine observation evidence was produced.
 * UNAVAILABLE means no justified observation could be established.
 * FAILED represents an operational observation-evaluation failure.
 *
 * This status does not verify outcomes, report success, update world state, or
 * produce a final outcome.
 */
enum class ObservationEvaluationStatus {
    OBSERVED,
    UNAVAILABLE,
    FAILED,
}
