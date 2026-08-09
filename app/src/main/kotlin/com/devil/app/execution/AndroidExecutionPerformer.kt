package com.devil.app.execution

import com.devil.core.model.common.TraceId
import com.devil.core.model.execution.ExecutionRequest

/**
 * Performs one explicitly supported Android platform action after all bounded
 * Stage 30 execution gates have been satisfied.
 *
 * Implementations must never infer Devil authorization from Android permission,
 * bypass capability availability or health, invent execution approval, or claim
 * successful effect merely because a platform call was attempted.
 *
 * A performer reports ATTEMPTED only after a genuine platform-action attempt.
 * Observation and Verification remain separate later responsibilities.
 */
fun interface AndroidExecutionPerformer {

    fun perform(
        traceId: TraceId,
        request: ExecutionRequest,
    ): AndroidExecutionAttemptResult
}
