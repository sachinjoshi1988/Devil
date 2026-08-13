package com.devil.core.runtime.learning

import com.devil.core.runtime.worldmodel.WorldModelUpdateResult
import com.devil.core.runtime.worldmodel.WorldModelUpdateStatus

/**
 * Default fail-closed core Learning-evidence port.
 *
 * No production Learning-evidence mechanism is configured inside core runtime.
 *
 * Therefore:
 *
 * - APPLICABLE World Model update remains DEFERRED rather than being fabricated
 *   as Learning evidence;
 * - DEFERRED World Model update remains DEFERRED;
 * - FAILED World Model update preserves its matching operational error.
 *
 * A platform or other authorized embodiment may implement LearningEvidencePort
 * outside core and be injected through the normal Unified Devil Runtime
 * composition boundary.
 *
 * This default creates no Learning, proposes no Memory, invokes no Memory
 * Authority, commits no Memory, persists no Memory, mutates no world state, and
 * invents no evidence.
 */
class DefaultLearningEvidencePort :
    LearningEvidencePort {

    override fun establish(
        worldModelUpdate: WorldModelUpdateResult,
    ): LearningEvidenceResult {
        return when (worldModelUpdate.status) {
            WorldModelUpdateStatus.APPLICABLE -> {
                val request =
                    requireNotNull(worldModelUpdate.request)

                val capabilityId =
                    request.outcome
                        .verification
                        .observation
                        .execution
                        .capability
                        .capabilityId

                require(
                    capabilityId.value.isNotBlank(),
                ) {
                    "Applicable constitutional World Model update must preserve one capability identity before Learning evidence may be attempted."
                }

                LearningEvidenceResult.create(
                    traceId = worldModelUpdate.traceId,
                    status = LearningEvidenceStatus.DEFERRED,
                )
            }

            WorldModelUpdateStatus.DEFERRED ->
                LearningEvidenceResult.create(
                    traceId = worldModelUpdate.traceId,
                    status = LearningEvidenceStatus.DEFERRED,
                )

            WorldModelUpdateStatus.FAILED ->
                LearningEvidenceResult.create(
                    traceId = worldModelUpdate.traceId,
                    status = LearningEvidenceStatus.FAILED,
                    error = requireNotNull(worldModelUpdate.error),
                )
        }
    }
}
