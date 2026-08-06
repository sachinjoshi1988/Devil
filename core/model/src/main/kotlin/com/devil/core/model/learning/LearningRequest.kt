package com.devil.core.model.learning

import com.devil.core.model.worldmodel.WorldModelUpdateRequest

/**
 * Represents one structured request for bounded constitutional learning
 * evaluation.
 *
 * The request preserves one existing WorldModelUpdateRequest after genuine
 * constitutional evidence established that one bounded World Model update may
 * be applicable. It does not claim that world state was mutated or reinterpret
 * outcome, verification, observation, execution, planning, or capability
 * selection.
 *
 * This request does not create learning, create or commit memory, mutate world
 * state, change task or plan state, communicate externally, or produce a
 * runtime result.
 */
@ConsistentCopyVisibility
data class LearningRequest private constructor(
    val worldModelUpdate: WorldModelUpdateRequest,
) {
    companion object {
        fun create(
            worldModelUpdate: WorldModelUpdateRequest,
        ): LearningRequest {
            return LearningRequest(
                worldModelUpdate = worldModelUpdate,
            )
        }
    }
}
