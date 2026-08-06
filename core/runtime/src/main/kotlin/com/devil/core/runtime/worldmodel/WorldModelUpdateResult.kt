package com.devil.core.runtime.worldmodel

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.worldmodel.WorldModelUpdateRequest

/**
 * Represents the stable operational result of constitutional World Model
 * update evaluation.
 *
 * An applicable result preserves one WorldModelUpdateRequest for which genuine
 * constitutional update evidence was established. Preserving that request
 * does not mutate world state, claim that state changed, change task or plan
 * state, create memory or learning, communicate externally, or bypass unified
 * runtime handling.
 *
 * A deferred result contains neither request nor error. A failed result
 * contains one matching error.
 */
@ConsistentCopyVisibility
data class WorldModelUpdateResult private constructor(
    val traceId: TraceId,
    val status: WorldModelUpdateStatus,
    val request: WorldModelUpdateRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: WorldModelUpdateStatus,
            request: WorldModelUpdateRequest? = null,
            error: UniversalErrorRecord? = null,
        ): WorldModelUpdateResult {
            when (status) {
                WorldModelUpdateStatus.APPLICABLE -> {
                    require(request != null && error == null) {
                        "Applicable World Model update results require a request and must not contain an error."
                    }
                }

                WorldModelUpdateStatus.DEFERRED -> {
                    require(request == null && error == null) {
                        "Deferred World Model update results must not contain a request or error."
                    }
                }

                WorldModelUpdateStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed World Model update results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.outcome
                        .verification
                        .observation
                        .execution
                        .plan
                        .task
                        .decision
                        .understanding
                        .context
                        .traceId == traceId,
            ) {
                "World Model update result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "World Model update result and error must use the same trace identity."
            }

            return WorldModelUpdateResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
