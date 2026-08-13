package com.devil.core.runtime.observation

import com.devil.core.runtime.execution.ExecutionAttemptResult
import com.devil.core.runtime.execution.ExecutionAttemptStatus

/**
 * Default fail-closed core observation-evidence port.
 *
 * No production observation embodiment is configured inside core runtime.
 *
 * Therefore:
 *
 * - ATTEMPTED remains DEFERRED rather than being fabricated as OBSERVED;
 * - DEFERRED remains DEFERRED;
 * - FAILED preserves its matching operational error.
 *
 * A platform embodiment may implement ObservationEvidencePort outside core and
 * be injected through the normal Unified Devil Runtime composition boundary.
 *
 * This default performs no platform observation and invents no evidence.
 */
class DefaultObservationEvidencePort : ObservationEvidencePort {

    override fun observe(
        executionAttempt: ExecutionAttemptResult,
    ): ObservationEvidenceResult {
        return when (executionAttempt.status) {
            ExecutionAttemptStatus.ATTEMPTED ->
                ObservationEvidenceResult.create(
                    traceId = executionAttempt.traceId,
                    status = ObservationEvidenceStatus.DEFERRED,
                )

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
        }
    }
}
