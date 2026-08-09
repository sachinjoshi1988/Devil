package com.devil.app.verification

import com.devil.app.observation.AndroidObservationResult

/**
 * Android embodiment boundary for verification after Stage 31 observation.
 *
 * Only a genuine OBSERVED result may approach the bounded Android verification
 * source.
 *
 * A DEFERRED observation produces no verification attempt.
 *
 * A FAILED observation preserves its matching operational failure.
 *
 * Observed effect != verified outcome.
 * Verified outcome != completed task.
 */
fun interface AndroidVerificationAdapter {

    fun verify(
        observation: AndroidObservationResult,
    ): AndroidVerificationResult
}
