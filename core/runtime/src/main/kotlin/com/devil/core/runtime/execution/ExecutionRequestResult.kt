package com.devil.core.runtime.execution

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.execution.ExecutionRequest

/**
 * Represents the structured operational result of execution-request
 * preparation.
 *
 * An available result contains one ExecutionRequest. An unavailable result
 * contains neither request nor error. A failed result contains one matching
 * error.
 */
@ConsistentCopyVisibility
data class ExecutionRequestResult private constructor(
    val traceId: TraceId,
    val status: ExecutionRequestStatus,
    val request: ExecutionRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: ExecutionRequestStatus,
            request: ExecutionRequest? = null,
            error: UniversalErrorRecord? = null,
        ): ExecutionRequestResult {
            when (status) {
                ExecutionRequestStatus.AVAILABLE -> {
                    require(request != null && error == null) {
                        "Available execution request results require a request and must not contain an error."
                    }
                }

                ExecutionRequestStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable execution request results must not contain a request or error."
                    }
                }

                ExecutionRequestStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed execution request results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.plan.task.decision.understanding.context.traceId ==
                    traceId,
            ) {
                "Execution request result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Execution request result and error must use the same trace identity."
            }

            return ExecutionRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
