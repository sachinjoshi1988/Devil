package com.devil.core.runtime.task

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.task.TaskCreationRequest

/**
 * Represents the structured operational result of task-creation request
 * preparation.
 *
 * An available result contains one TaskCreationRequest. An unavailable result
 * contains neither request nor error. A failed result contains the matching
 * error.
 */
@ConsistentCopyVisibility
data class TaskCreationRequestResult private constructor(
    val traceId: TraceId,
    val status: TaskCreationRequestStatus,
    val request: TaskCreationRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: TaskCreationRequestStatus,
            request: TaskCreationRequest? = null,
            error: UniversalErrorRecord? = null,
        ): TaskCreationRequestResult {
            when (status) {
                TaskCreationRequestStatus.AVAILABLE -> {
                    require(request != null && error == null) {
                        "Available task creation request results require a request and must not contain an error."
                    }
                }

                TaskCreationRequestStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable task creation request results must not contain a request or error."
                    }
                }

                TaskCreationRequestStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed task creation request results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.decision.understanding.context.traceId == traceId,
            ) {
                "Task creation request result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Task creation request result and error must use the same trace identity."
            }

            return TaskCreationRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
