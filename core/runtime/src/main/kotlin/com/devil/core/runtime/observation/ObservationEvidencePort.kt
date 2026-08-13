package com.devil.core.runtime.observation

import com.devil.core.runtime.execution.ExecutionAttemptResult

/**
 * Neutral observation-embodiment port between constitutional execution attempt
 * and constitutional Observation.
 *
 * The core runtime may approach this port only with the genuine
 * ExecutionAttemptResult produced by its configured execution-attempt boundary.
 *
 * Implementations may obtain bounded observation evidence only through their
 * authorized embodiment-specific mechanisms.
 *
 * This port grants no authority of its own.
 *
 * ExecutionAttemptStatus.ATTEMPTED is necessary for observation evidence but
 * does not itself establish OBSERVED.
 *
 * This contract contains no Android dependency and creates no alternate Brain,
 * Executive, Planner, Security Authority, Observation Authority, or runtime.
 */
fun interface ObservationEvidencePort {

    fun observe(
        executionAttempt: ExecutionAttemptResult,
    ): ObservationEvidenceResult
}
