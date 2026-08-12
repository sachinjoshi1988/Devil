package com.devil.core.runtime.observation

import com.devil.core.runtime.execution.ExecutionAttemptResult

/**
 * Supplies one structured constitutional observation request only after a
 * genuine bounded execution attempt.
 *
 * Execution approval alone is insufficient.
 *
 * This provider does not perform execution, create observation evidence, verify
 * outcomes, or report final success.
 */
interface ObservationRequestProvider {

    fun provide(
        executionAttempt: ExecutionAttemptResult,
    ): ObservationRequestResult
}
