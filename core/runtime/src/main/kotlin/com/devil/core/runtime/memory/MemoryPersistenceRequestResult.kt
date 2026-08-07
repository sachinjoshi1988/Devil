package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.memory.MemoryPersistenceRequest

/**
 * Represents the bounded result produced while preparing one controlled
 * logical-memory persistence request.
 *
 * An available result preserves one MemoryPersistenceRequest.
 *
 * Preserving that request does not create, persist, store, expose, recall,
 * delete, or commit logical memory.
 *
 * An unavailable result contains neither request nor error.
 *
 * A failed result contains one matching error.
 */
@ConsistentCopyVisibility
data class MemoryPersistenceRequestResult private constructor(
    val traceId: TraceId,
    val status: MemoryPersistenceRequestStatus,
    val request: MemoryPersistenceRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: MemoryPersistenceRequestStatus,
            request: MemoryPersistenceRequest? = null,
            error: UniversalErrorRecord? = null,
        ): MemoryPersistenceRequestResult {
            when (status) {
                MemoryPersistenceRequestStatus.AVAILABLE -> {
                    require(request != null && error == null) {
                        "Available memory persistence request results require a request and must not contain an error."
                    }
                }

                MemoryPersistenceRequestStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable memory persistence request results must not contain a request or error."
                    }
                }

                MemoryPersistenceRequestStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed memory persistence request results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.commitmentRequest
                        .authorityRequest
                        .proposal
                        .learning
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
                "Memory persistence request result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Memory persistence request result and error must use the same trace identity."
            }

            return MemoryPersistenceRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
