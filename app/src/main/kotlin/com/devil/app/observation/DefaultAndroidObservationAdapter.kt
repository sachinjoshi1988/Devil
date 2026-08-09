package com.devil.app.observation

import com.devil.app.execution.AndroidExecutionAttemptResult
import com.devil.app.execution.AndroidExecutionAttemptStatus

/**
 * Default Stage 31 Android Observation adapter.
 *
 * This adapter approaches AndroidObservationSource only after Stage 30 produced
 * one genuine ATTEMPTED result.
 *
 * It does not reinterpret ATTEMPTED as OBSERVED.
 *
 * The source must independently produce genuine observation evidence.
 *
 * The default source remains DEFERRED because no approved production Android
 * observation mechanism exists yet.
 */
class DefaultAndroidObservationAdapter(
    private val observationSource: AndroidObservationSource =
        DefaultAndroidObservationSource(),
) : AndroidObservationAdapter {

    override fun observe(
        executionAttempt: AndroidExecutionAttemptResult,
    ): AndroidObservationResult {
        return when (executionAttempt.status) {
            AndroidExecutionAttemptStatus.DEFERRED ->
                AndroidObservationResult.create(
                    traceId = executionAttempt.traceId,
                    status = AndroidObservationStatus.DEFERRED,
                )

            AndroidExecutionAttemptStatus.FAILED ->
                AndroidObservationResult.create(
                    traceId = executionAttempt.traceId,
                    status = AndroidObservationStatus.FAILED,
                    error = requireNotNull(
                        executionAttempt.error,
                    ),
                )

            AndroidExecutionAttemptStatus.ATTEMPTED -> {
                val capabilityId =
                    requireNotNull(
                        executionAttempt.capabilityId,
                    )

                val result =
                    observationSource.observe(
                        traceId = executionAttempt.traceId,
                        capabilityId = capabilityId,
                    )

                require(
                    result.traceId == executionAttempt.traceId,
                ) {
                    "Android execution attempt and observation result must use the same trace identity."
                }

                require(
                    result.evidence == null ||
                        result.evidence.capabilityId == capabilityId,
                ) {
                    "Android execution attempt and observation evidence must refer to the same capability identity."
                }

                result
            }
        }
    }
}
