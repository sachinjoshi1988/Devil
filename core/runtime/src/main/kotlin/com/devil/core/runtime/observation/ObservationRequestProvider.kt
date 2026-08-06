package com.devil.core.runtime.observation

import com.devil.core.runtime.execution.ExecutionResult

/**
 * Supplies one structured constitutional observation request when bounded
 * execution evaluation produced an approved ExecutionRequest.
 *
 * This provider does not claim that execution occurred, activate capabilities,
 * create observation evidence, verify outcomes, or report final success.
 */
interface ObservationRequestProvider {

    fun provide(
        execution: ExecutionResult,
    ): ObservationRequestResult
}
