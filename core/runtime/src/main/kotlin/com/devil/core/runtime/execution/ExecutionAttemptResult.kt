package com.devil.core.runtime.execution

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.execution.ExecutionRequest

/**
 * Represents one bounded execution-attempt result after constitutional execution
 * evaluation.
 *
 * ATTEMPTED preserves the exact approved ExecutionRequest that an authorized
 * embodiment genuinely attempted.
 *
 * Preserving an attempted request does not establish observation, verification,
 * Outcome, task completion, plan completion, World Model update, learning, or
 * memory.
 *
 * DEFERRED contains neither request nor error because no justified execution
 * attempt occurred.
 *
 * FAILED contains one matching operational error and no request.
 */
@ConsistentCopyVisibility
data class ExecutionAttemptResult private constructor(
    val traceId: TraceId,
    val status: ExecutionAttemptStatus,
    val request: ExecutionRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: ExecutionAttemptStatus,
            request: ExecutionRequest? = null,
            error: UniversalErrorRecord? = null,
        ): ExecutionAttemptResult {
            when (status) {
                ExecutionAttemptStatus.ATTEMPTED -> {
                    require(request != null && error == null) {
                        "Attempted execution results require an execution request and must not contain an error."
                    }
                }

                ExecutionAttemptStatus.DEFERRED -> {
                    require(request == null && error == null) {
                        "Deferred execution-attempt results must not contain a request or error."
                    }
                }

                ExecutionAttemptStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed execution-attempt results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.plan.task.decision.understanding.context.traceId ==
                    traceId,
            ) {
                "Execution-attempt result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Execution-attempt result and error must use the same trace identity."
            }

            return ExecutionAttemptResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
