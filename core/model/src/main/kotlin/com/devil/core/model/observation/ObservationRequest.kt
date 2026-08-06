package com.devil.core.model.observation

import com.devil.core.model.execution.ExecutionRequest

/**
 * Represents one structured request for bounded constitutional observation.
 *
 * The request preserves one approved ExecutionRequest without claiming that a
 * capability was activated, an action was attempted, execution occurred, or an
 * observable result exists.
 *
 * This request does not create observations, infer execution results, verify
 * outcomes, report success, change task or plan state, or produce final outcomes.
 */
@ConsistentCopyVisibility
data class ObservationRequest private constructor(
    val execution: ExecutionRequest,
) {
    companion object {
        fun create(
            execution: ExecutionRequest,
        ): ObservationRequest {
            return ObservationRequest(
                execution = execution,
            )
        }
    }
}
