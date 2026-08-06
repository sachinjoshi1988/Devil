package com.devil.core.runtime.learning

import com.devil.core.runtime.worldmodel.WorldModelUpdateResult

/**
 * Supplies one structured constitutional learning request when genuine
 * constitutional evidence established that a bounded World Model update may be
 * applicable.
 *
 * This provider does not create learning, create or commit memory, mutate world
 * state, change task or plan state, communicate externally, or produce a
 * runtime result.
 */
interface LearningRequestProvider {

    fun provide(
        worldModelUpdate: WorldModelUpdateResult,
    ): LearningRequestResult
}
