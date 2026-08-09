package com.devil.app.verification

import com.devil.app.observation.AndroidObservationResult
import com.devil.app.observation.AndroidObservationStatus

/**
 * Default Stage 32 Android Verification adapter.
 *
 * This adapter approaches AndroidVerificationSource only after Stage 31
 * produced one genuine OBSERVED result.
 *
 * It does not reinterpret OBSERVED as VERIFIED.
 *
 * The source must independently produce genuine verification evidence.
 *
 * The default source remains DEFERRED because no approved production Android
 * verification mechanism exists yet.
 */
class DefaultAndroidVerificationAdapter(
    private val verificationSource: AndroidVerificationSource =
        DefaultAndroidVerificationSource(),
) : AndroidVerificationAdapter {

    override fun verify(
        observation: AndroidObservationResult,
    ): AndroidVerificationResult {
        return when (observation.status) {
            AndroidObservationStatus.DEFERRED ->
                AndroidVerificationResult.create(
                    traceId = observation.traceId,
                    status = AndroidVerificationStatus.DEFERRED,
                )

            AndroidObservationStatus.FAILED ->
                AndroidVerificationResult.create(
                    traceId = observation.traceId,
                    status = AndroidVerificationStatus.FAILED,
                    error = requireNotNull(observation.error),
                )

            AndroidObservationStatus.OBSERVED -> {
                val observationEvidence =
                    requireNotNull(observation.evidence)

                val result =
                    verificationSource.verify(
                        traceId = observation.traceId,
                        observationEvidence = observationEvidence,
                    )

                require(
                    result.traceId == observation.traceId,
                ) {
                    "Android observation and verification result must use the same trace identity."
                }

                require(
                    result.evidence == null ||
                        result.evidence.capabilityId ==
                        observationEvidence.capabilityId,
                ) {
                    "Android observation and verification evidence must refer to the same capability identity."
                }

                result
            }
        }
    }
}
