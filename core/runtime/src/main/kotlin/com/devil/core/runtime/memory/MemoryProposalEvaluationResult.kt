package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.memory.MemoryProposalRequest

/**
 * Represents the bounded result of constitutional memory-proposal evaluation.
 *
 * A proposable result preserves the evaluated MemoryProposalRequest. An
 * unavailable result contains neither request nor error. A failed result
 * contains one matching error.
 *
 * Preserving a request does not create a memory proposal, approve or commit
 * logical memory, mutate world state, change task or plan state, communicate
 * externally, bypass the single Memory Authority, or produce a runtime result.
 */
@ConsistentCopyVisibility
data class MemoryProposalEvaluationResult private constructor(
    val traceId: TraceId,
    val status: MemoryProposalEvaluationStatus,
    val request: MemoryProposalRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: MemoryProposalEvaluationStatus,
            request: MemoryProposalRequest? = null,
            error: UniversalErrorRecord? = null,
        ): MemoryProposalEvaluationResult {
            when (status) {
                MemoryProposalEvaluationStatus.PROPOSABLE -> {
                    require(request != null && error == null) {
                        "Proposable memory evaluation results require a request and must not contain an error."
                    }
                }

                MemoryProposalEvaluationStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable memory proposal evaluation results must not contain a request or error."
                    }
                }

                MemoryProposalEvaluationStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed memory proposal evaluation results require an error and must not contain a request."
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
                "Memory proposal evaluation result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Memory proposal evaluation result and error must use the same trace identity."
            }

            return MemoryProposalEvaluationResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
