package com.devil.core.runtime.verification

import com.devil.core.model.verification.VerificationRequest
import com.devil.core.runtime.observation.ObservationResult
import com.devil.core.runtime.observation.ObservationStatus

/**
 * Default Stage 14 constitutional verification-request provider.
 *
 * A request is available only when constitutional observation produced an
 * OBSERVED ObservationResult containing one bounded ObservationRequest.
 *
 * Deferred observation remains unavailable. Observation failure preserves its
 * matching error.
 *
 * This implementation does not create verification evidence, determine whether
 * an intended outcome was achieved, update world state, report success, or
 * produce a final outcome.
 */
class DefaultVerificationRequestProvider :
    VerificationRequestProvider {

    override fun provide(
        observation: ObservationResult,
    ): VerificationRequestResult {
        return when (observation.status) {
            ObservationStatus.OBSERVED ->
                VerificationRequestResult.create(
                    traceId = observation.traceId,
                    status = VerificationRequestStatus.AVAILABLE,
                    request = VerificationRequest.create(
                        observation =
                            requireNotNull(observation.request),
                    ),
                )

            ObservationStatus.DEFERRED ->
                VerificationRequestResult.create(
                    traceId = observation.traceId,
                    status = VerificationRequestStatus.UNAVAILABLE,
                )

            ObservationStatus.FAILED ->
                VerificationRequestResult.create(
                    traceId = observation.traceId,
                    status = VerificationRequestStatus.FAILED,
                    error = requireNotNull(observation.error),
                )
        }
    }
}
