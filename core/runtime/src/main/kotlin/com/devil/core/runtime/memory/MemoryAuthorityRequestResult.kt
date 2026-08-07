package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.memory.MemoryAuthorityRequest

/**
 * Represents the bounded result produced by the Memory Authority request
 * provider.
 */
@ConsistentCopyVisibility
data class MemoryAuthorityRequestResult private constructor(
    val traceId: TraceId,
    val status: MemoryAuthorityRequestStatus,
    val request: MemoryAuthorityRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: MemoryAuthorityRequestStatus,
            request: MemoryAuthorityRequest? = null,
            error: UniversalErrorRecord? = null,
        ): MemoryAuthorityRequestResult {

            when (status) {
                MemoryAuthorityRequestStatus.AVAILABLE ->
                    require(request != null && error == null) {
                        "Available Memory Authority request results require a request and must not contain an error."
                    }

                MemoryAuthorityRequestStatus.UNAVAILABLE ->
                    require(request == null && error == null) {
                        "Unavailable Memory Authority request results must not contain a request or error."
                    }

                MemoryAuthorityRequestStatus.FAILED ->
                    require(request == null && error != null) {
                        "Failed Memory Authority request results require an error and must not contain a request."
                    }
            }

            require(
                request == null ||
                    request.proposal.learning
                        .worldModelUpdate
                        .outcome
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
                "Memory Authority request result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Memory Authority request result and error must use the same trace identity."
            }

            return MemoryAuthorityRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
