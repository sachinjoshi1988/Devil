package com.devil.core.runtime.verification

import com.devil.core.runtime.observation.ObservationResult
import com.devil.core.runtime.observation.ObservationStatus

/**
 * Default fail-closed core verification-evidence port.
 *
 * No production verification embodiment is configured inside core runtime.
 *
 * Therefore:
 *
 * - OBSERVED remains DEFERRED rather than being fabricated as VERIFIED;
 * - DEFERRED remains DEFERRED;
 * - FAILED preserves its matching operational error.
 *
 * A platform embodiment may implement VerificationEvidencePort outside core and
 * be injected through the normal Unified Devil Runtime composition boundary.
 *
 * This default performs no platform verification and invents no evidence.
 */
class DefaultVerificationEvidencePort : VerificationEvidencePort {

    override fun verify(
        observation: ObservationResult,
    ): VerificationEvidenceResult {
        return when (observation.status) {
            ObservationStatus.OBSERVED ->
                VerificationEvidenceResult.create(
                    traceId = observation.traceId,
                    status = VerificationEvidenceStatus.DEFERRED,
                )

            ObservationStatus.DEFERRED ->
                VerificationEvidenceResult.create(
                    traceId = observation.traceId,
                    status = VerificationEvidenceStatus.DEFERRED,
                )

            ObservationStatus.FAILED ->
                VerificationEvidenceResult.create(
                    traceId = observation.traceId,
                    status = VerificationEvidenceStatus.FAILED,
                    error = requireNotNull(observation.error),
                )
        }
    }
}
