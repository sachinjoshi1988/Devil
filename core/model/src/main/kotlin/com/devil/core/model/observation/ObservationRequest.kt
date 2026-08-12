package com.devil.core.model.observation

import com.devil.core.model.execution.ExecutionRequest

/**
 * Represents one structured request for bounded constitutional observation.
 *
 * The request preserves one ExecutionRequest for which a genuine bounded
 * execution attempt has already been established. The execution attempt itself
 * is not observation evidence.
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
