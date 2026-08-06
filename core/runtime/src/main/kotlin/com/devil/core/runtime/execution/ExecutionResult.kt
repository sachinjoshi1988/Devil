package com.devil.core.runtime.execution

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.execution.ExecutionRequest

/**
 * Represents the stable operational result of constitutional execution
 * evaluation.
 *
 * An approved result preserves one ExecutionRequest that may approach a future
 * execution implementation. It does not claim that a capability was activated,
 * an action was attempted, or execution succeeded.
 *
 * A deferred result contains neither request nor error. A failed result contains
 * one matching error.
 */
@ConsistentCopyVisibility
data class ExecutionResult private constructor(
    val traceId: TraceId,
    val status: ExecutionStatus,
    val request: ExecutionRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: ExecutionStatus,
            request: ExecutionRequest? = null,
            error: UniversalErrorRecord? = null,
        ): ExecutionResult {
            when (status) {
                ExecutionStatus.APPROVED -> {
                    require(request != null && error == null) {
                        "Approved execution results require a request and must not contain an error."
                    }
                }

                ExecutionStatus.DEFERRED -> {
                    require(request == null && error == null) {
                        "Deferred execution results must not contain a request or error."
                    }
                }

                ExecutionStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed execution results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.plan.task.decision.understanding.context.traceId ==
                    traceId,
            ) {
                "Execution result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Execution result and error must use the same trace identity."
            }

            return ExecutionResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
