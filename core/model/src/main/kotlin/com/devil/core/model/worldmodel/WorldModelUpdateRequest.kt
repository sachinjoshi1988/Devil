package com.devil.core.model.worldmodel

import com.devil.core.model.outcome.OutcomeRequest

/**
 * Represents one structured request for bounded constitutional World Model
 * update evaluation.
 *
 * The request preserves one existing OutcomeRequest after genuine
 * constitutional outcome evidence has been established by the Outcome
 * Authority. It does not reinterpret outcome, verification, observation,
 * execution, planning, or capability selection.
 *
 * This request does not mutate world state, claim that world state changed,
 * change task or plan state, create memory or learning, communicate
 * externally, or produce a runtime result.
 */
@ConsistentCopyVisibility
data class WorldModelUpdateRequest private constructor(
    val outcome: OutcomeRequest,
) {
    companion object {
        fun create(
            outcome: OutcomeRequest,
        ): WorldModelUpdateRequest {
            return WorldModelUpdateRequest(
                outcome = outcome,
            )
        }
    }
}
