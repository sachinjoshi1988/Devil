package com.devil.core.runtime.worldmodel

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.worldmodel.WorldModelUpdateRequest

/**
 * Represents the structured operational result of World Model update-request
 * preparation.
 *
 * An available result contains one WorldModelUpdateRequest. An unavailable
 * result contains neither request nor error. A failed result contains one
 * matching error.
 *
 * This result does not mutate world state, claim that world state changed,
 * change task or plan state, create memory or learning, communicate
 * externally, or produce a runtime result.
 */
@ConsistentCopyVisibility
data class WorldModelUpdateRequestResult private constructor(
    val traceId: TraceId,
    val status: WorldModelUpdateRequestStatus,
    val request: WorldModelUpdateRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: WorldModelUpdateRequestStatus,
            request: WorldModelUpdateRequest? = null,
            error: UniversalErrorRecord? = null,
        ): WorldModelUpdateRequestResult {
            when (status) {
                WorldModelUpdateRequestStatus.AVAILABLE -> {
                    require(request != null && error == null) {
                        "Available World Model update request results require a request and must not contain an error."
                    }
                }

                WorldModelUpdateRequestStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable World Model update request results must not contain a request or error."
                    }
                }

                WorldModelUpdateRequestStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed World Model update request results require an error and must not contain a request."
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
                "World Model update request result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "World Model update request result and error must use the same trace identity."
            }

            return WorldModelUpdateRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
