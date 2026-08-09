package com.devil.app.observation

import com.devil.app.execution.AndroidExecutionAttemptResult

/**
 * Android embodiment boundary for observation after a Stage 30 execution
 * attempt.
 *
 * Only a genuine ATTEMPTED execution result may approach the bounded Android
 * observation source.
 *
 * A DEFERRED execution attempt produces no observation attempt.
 *
 * A FAILED execution attempt preserves its matching operational failure.
 *
 * Execution attempt != observed effect.
 * Observed effect != verified outcome.
 */
fun interface AndroidObservationAdapter {

    fun observe(
        executionAttempt: AndroidExecutionAttemptResult,
    ): AndroidObservationResult
}
