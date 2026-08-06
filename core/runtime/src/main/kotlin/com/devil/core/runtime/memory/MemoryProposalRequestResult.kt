package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.memory.MemoryProposalRequest

/**
 * Represents the structured operational result of constitutional
 * memory-proposal-request preparation.
 *
 * An available result contains one MemoryProposalRequest. An unavailable
 * result contains neither request nor error. A failed result contains one
 * matching error.
 *
 * This result does not create a memory proposal, approve or commit logical
 * memory, mutate world state, change task or plan state, communicate
 * externally, bypass the single Memory Authority, or produce a runtime result.
 */
@ConsistentCopyVisibility
data class MemoryProposalRequestResult private constructor(
    val traceId: TraceId,
    val status: MemoryProposalRequestStatus,
    val request: MemoryProposalRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: MemoryProposalRequestStatus,
            request: MemoryProposalRequest? = null,
            error: UniversalErrorRecord? = null,
        ): MemoryProposalRequestResult {
            when (status) {
                MemoryProposalRequestStatus.AVAILABLE -> {
                    require(request != null && error == null) {
                        "Available memory proposal request results require a request and must not contain an error."
                    }
                }

                MemoryProposalRequestStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable memory proposal request results must not contain a request or error."
                    }
                }

                MemoryProposalRequestStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed memory proposal request results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.learning
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
                "Memory proposal request result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Memory proposal request result and error must use the same trace identity."
            }

            return MemoryProposalRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
