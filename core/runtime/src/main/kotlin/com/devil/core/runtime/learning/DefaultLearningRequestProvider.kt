package com.devil.core.runtime.learning

import com.devil.core.model.learning.LearningRequest
import com.devil.core.runtime.worldmodel.WorldModelUpdateResult
import com.devil.core.runtime.worldmodel.WorldModelUpdateStatus

/**
 * Default Stage 17 constitutional learning-request provider.
 *
 * A request is available only when constitutional World Model update evaluation
 * produced an APPLICABLE WorldModelUpdateResult containing one bounded
 * WorldModelUpdateRequest.
 *
 * Deferred World Model update evaluation remains unavailable. World Model
 * update failure preserves its matching error.
 *
 * This implementation does not create learning, create or commit memory,
 * mutate world state, change task or plan state, communicate externally, or
 * produce a runtime result.
 */
class DefaultLearningRequestProvider :
    LearningRequestProvider {

    override fun provide(
        worldModelUpdate: WorldModelUpdateResult,
    ): LearningRequestResult {
        return when (worldModelUpdate.status) {
            WorldModelUpdateStatus.APPLICABLE ->
                LearningRequestResult.create(
                    traceId = worldModelUpdate.traceId,
                    status = LearningRequestStatus.AVAILABLE,
                    request = LearningRequest.create(
                        worldModelUpdate =
                            requireNotNull(worldModelUpdate.request),
                    ),
                )

            WorldModelUpdateStatus.DEFERRED ->
                LearningRequestResult.create(
                    traceId = worldModelUpdate.traceId,
                    status = LearningRequestStatus.UNAVAILABLE,
                )

            WorldModelUpdateStatus.FAILED ->
                LearningRequestResult.create(
                    traceId = worldModelUpdate.traceId,
                    status = LearningRequestStatus.FAILED,
                    error = requireNotNull(worldModelUpdate.error),
                )
        }
    }
}
