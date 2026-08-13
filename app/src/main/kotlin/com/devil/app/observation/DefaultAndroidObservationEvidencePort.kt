package com.devil.app.observation

import com.devil.app.execution.AndroidExecutionAttemptResult
import com.devil.app.execution.AndroidExecutionAttemptStatus
import com.devil.core.runtime.execution.ExecutionAttemptResult
import com.devil.core.runtime.execution.ExecutionAttemptStatus
import com.devil.core.runtime.observation.ObservationEvidencePort
import com.devil.core.runtime.observation.ObservationEvidenceResult
import com.devil.core.runtime.observation.ObservationEvidenceStatus

/**
 * Android implementation of the neutral constitutional ObservationEvidencePort.
 *
 * This boundary is reached only after the single Unified Devil Runtime has
 * established one genuine ExecutionAttemptResult.
 *
 * ATTEMPTED is necessary but remains insufficient for OBSERVED.
 *
 * For one genuine ATTEMPTED result this port approaches the existing bounded
 * AndroidObservationAdapter using only the preserved trace and selected
 * capability identity.
 *
 * Android observation evidence becomes neutral constitutional observation
 * evidence only when trace identity and capability identity remain consistent.
 *
 * This port grants no authority, performs no Verification, establishes no
 * Outcome, updates no World Model state, performs no Learning, commits no
 * Memory, and creates no alternate runtime, Brain, Executive, Planner,
 * Security Authority, or Observation Authority.
 *
 * ATTEMPTED != OBSERVED.
 * OBSERVED != VERIFIED.
 * VERIFIED != OUTCOME.
 */
class DefaultAndroidObservationEvidencePort(
    private val observationAdapter: AndroidObservationAdapter,
) : ObservationEvidencePort {

    override fun observe(
        executionAttempt: ExecutionAttemptResult,
    ): ObservationEvidenceResult {
        return when (executionAttempt.status) {
            ExecutionAttemptStatus.DEFERRED ->
                ObservationEvidenceResult.create(
                    traceId = executionAttempt.traceId,
                    status = ObservationEvidenceStatus.DEFERRED,
                )

            ExecutionAttemptStatus.FAILED ->
                ObservationEvidenceResult.create(
                    traceId = executionAttempt.traceId,
                    status = ObservationEvidenceStatus.FAILED,
                    error = requireNotNull(executionAttempt.error),
                )

            ExecutionAttemptStatus.ATTEMPTED -> {
                val request =
                    requireNotNull(executionAttempt.request)

                val capabilityId =
                    request.capability.capabilityId

                val androidObservation =
                    observationAdapter.observe(
                        executionAttempt =
                            AndroidExecutionAttemptResult.create(
                                traceId = executionAttempt.traceId,
                                status =
                                    AndroidExecutionAttemptStatus.ATTEMPTED,
                                capabilityId = capabilityId,
                            ),
                    )

                require(
                    androidObservation.traceId ==
                        executionAttempt.traceId,
                ) {
                    "Android observation and constitutional execution attempt must use the same trace identity."
                }

                require(
                    androidObservation.evidence == null ||
                        androidObservation.evidence.capabilityId ==
                        capabilityId,
                ) {
                    "Android observation evidence and constitutional execution attempt must refer to the same capability identity."
                }

                when (androidObservation.status) {
                    AndroidObservationStatus.OBSERVED -> {
                        val evidence =
                            requireNotNull(
                                androidObservation.evidence,
                            )

                        ObservationEvidenceResult.create(
                            traceId = executionAttempt.traceId,
                            status =
                                ObservationEvidenceStatus.OBSERVED,
                            capabilityId =
                                evidence.capabilityId,
                            description =
                                evidence.description,
                        )
                    }

                    AndroidObservationStatus.DEFERRED ->
                        ObservationEvidenceResult.create(
                            traceId = executionAttempt.traceId,
                            status =
                                ObservationEvidenceStatus.DEFERRED,
                        )

                    AndroidObservationStatus.FAILED ->
                        ObservationEvidenceResult.create(
                            traceId = executionAttempt.traceId,
                            status =
                                ObservationEvidenceStatus.FAILED,
                            error =
                                requireNotNull(
                                    androidObservation.error,
                                ),
                        )
                }
            }
        }
    }
}
