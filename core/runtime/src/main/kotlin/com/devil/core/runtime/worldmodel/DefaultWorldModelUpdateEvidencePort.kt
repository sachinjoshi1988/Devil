package com.devil.core.runtime.worldmodel

import com.devil.core.runtime.outcome.OutcomeResult
import com.devil.core.runtime.outcome.OutcomeStatus

/**
 * Default fail-closed core World Model update-evidence port.
 *
 * No production World Model update-evidence embodiment is configured inside
 * core runtime.
 *
 * Therefore:
 *
 * - ESTABLISHED Outcome remains DEFERRED rather than being fabricated as World
 *   Model update evidence;
 * - DEFERRED Outcome remains DEFERRED;
 * - FAILED Outcome preserves its matching operational error.
 *
 * A platform embodiment may implement WorldModelUpdateEvidencePort outside
 * core and be injected through the normal Unified Devil Runtime composition
 * boundary.
 *
 * This default performs no World Model mutation and invents no evidence.
 */
class DefaultWorldModelUpdateEvidencePort :
    WorldModelUpdateEvidencePort {

    override fun establish(
        outcome: OutcomeResult,
    ): WorldModelUpdateEvidenceResult {
        return when (outcome.status) {
            OutcomeStatus.ESTABLISHED -> {
                val request =
                    requireNotNull(outcome.request)

                val capabilityId =
                    request.verification
                        .observation
                        .execution
                        .capability
                        .capabilityId

                require(
                    capabilityId.value.isNotBlank(),
                ) {
                    "Established constitutional Outcome must preserve one capability identity before World Model update evidence may be attempted."
                }

                WorldModelUpdateEvidenceResult.create(
                    traceId = outcome.traceId,
                    status =
                        WorldModelUpdateEvidenceStatus.DEFERRED,
                )
            }

            OutcomeStatus.DEFERRED ->
                WorldModelUpdateEvidenceResult.create(
                    traceId = outcome.traceId,
                    status =
                        WorldModelUpdateEvidenceStatus.DEFERRED,
                )

            OutcomeStatus.FAILED ->
                WorldModelUpdateEvidenceResult.create(
                    traceId = outcome.traceId,
                    status =
                        WorldModelUpdateEvidenceStatus.FAILED,
                    error = requireNotNull(outcome.error),
                )
        }
    }
}
