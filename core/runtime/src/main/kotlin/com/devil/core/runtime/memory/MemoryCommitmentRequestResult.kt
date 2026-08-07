package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.memory.MemoryCommitmentRequest

/**
 * Represents the bounded result produced while preparing one controlled
 * logical-memory commitment request.
 *
 * An available result preserves one MemoryCommitmentRequest. Preserving that
 * request does not create, persist, store, expose, recall, or commit logical
 * memory.
 *
 * An unavailable result contains neither request nor error. A failed result
 * contains one matching error.
 */
@ConsistentCopyVisibility
data class MemoryCommitmentRequestResult private constructor(
    val traceId: TraceId,
    val status: MemoryCommitmentRequestStatus,
    val request: MemoryCommitmentRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: MemoryCommitmentRequestStatus,
            request: MemoryCommitmentRequest? = null,
            error: UniversalErrorRecord? = null,
        ): MemoryCommitmentRequestResult {
            when (status) {
                MemoryCommitmentRequestStatus.AVAILABLE -> {
                    require(request != null && error == null) {
                        "Available memory commitment request results require a request and must not contain an error."
                    }
                }

                MemoryCommitmentRequestStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable memory commitment request results must not contain a request or error."
                    }
                }

                MemoryCommitmentRequestStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed memory commitment request results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.authorityRequest
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
                "Memory commitment request result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Memory commitment request result and error must use the same trace identity."
            }

            return MemoryCommitmentRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
