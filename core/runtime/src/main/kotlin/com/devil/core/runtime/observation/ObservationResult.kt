package com.devil.core.runtime.observation

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.observation.ObservationRequest

/**
 * Represents the stable operational result of constitutional observation.
 *
 * An observed result preserves one ObservationRequest for which genuine
 * observation evidence was established. Preserving that request does not verify
 * an outcome, prove success, or change task, plan, execution, or world state.
 *
 * A deferred result contains neither request nor error. A failed result contains
 * one matching error.
 */
@ConsistentCopyVisibility
data class ObservationResult private constructor(
    val traceId: TraceId,
    val status: ObservationStatus,
    val request: ObservationRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: ObservationStatus,
            request: ObservationRequest? = null,
            error: UniversalErrorRecord? = null,
        ): ObservationResult {
            when (status) {
                ObservationStatus.OBSERVED -> {
                    require(request != null && error == null) {
                        "Observed results require a request and must not contain an error."
                    }
                }

                ObservationStatus.DEFERRED -> {
                    require(request == null && error == null) {
                        "Deferred observation results must not contain a request or error."
                    }
                }

                ObservationStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed observation results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.execution.plan.task.decision.understanding.context.traceId ==
                    traceId,
            ) {
                "Observation result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Observation result and error must use the same trace identity."
            }

            return ObservationResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
