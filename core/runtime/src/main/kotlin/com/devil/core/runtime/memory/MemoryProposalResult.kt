package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.memory.MemoryProposalRequest

/**
 * Represents the stable operational result of constitutional memory-proposal
 * evaluation.
 *
 * A proposable result preserves one MemoryProposalRequest for which genuine
 * constitutional proposal evidence was established. Preserving that request
 * does not create, approve, or commit logical memory, mutate world state, change
 * task or plan state, communicate externally, or bypass the single Memory
 * Authority.
 *
 * A deferred result contains neither request nor error. A failed result contains
 * one matching error.
 */
@ConsistentCopyVisibility
data class MemoryProposalResult private constructor(
    val traceId: TraceId,
    val status: MemoryProposalStatus,
    val request: MemoryProposalRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: MemoryProposalStatus,
            request: MemoryProposalRequest? = null,
            error: UniversalErrorRecord? = null,
        ): MemoryProposalResult {
            when (status) {
                MemoryProposalStatus.PROPOSABLE -> {
                    require(request != null && error == null) {
                        "Proposable memory proposal results require a request and must not contain an error."
                    }
                }

                MemoryProposalStatus.DEFERRED -> {
                    require(request == null && error == null) {
                        "Deferred memory proposal results must not contain a request or error."
                    }
                }

                MemoryProposalStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed memory proposal results require an error and must not contain a request."
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
                "Memory proposal result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Memory proposal result and error must use the same trace identity."
            }

            return MemoryProposalResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
