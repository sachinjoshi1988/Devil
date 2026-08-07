package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.memory.MemoryCommitmentRequest

/**
 * Represents the bounded result of constitutional logical-memory commitment
 * evaluation.
 *
 * A committable result preserves one evaluated MemoryCommitmentRequest.
 * Preserving that request does not create, persist, store, expose, recall, or
 * commit logical memory.
 *
 * An unavailable result contains neither request nor error. A failed result
 * contains one matching error.
 */
@ConsistentCopyVisibility
data class MemoryCommitmentEvaluationResult private constructor(
    val traceId: TraceId,
    val status: MemoryCommitmentEvaluationStatus,
    val request: MemoryCommitmentRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: MemoryCommitmentEvaluationStatus,
            request: MemoryCommitmentRequest? = null,
            error: UniversalErrorRecord? = null,
        ): MemoryCommitmentEvaluationResult {
            when (status) {
                MemoryCommitmentEvaluationStatus.COMMITTABLE -> {
                    require(request != null && error == null) {
                        "Committable memory commitment evaluation results require a request and must not contain an error."
                    }
                }

                MemoryCommitmentEvaluationStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable memory commitment evaluation results must not contain a request or error."
                    }
                }

                MemoryCommitmentEvaluationStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed memory commitment evaluation results require an error and must not contain a request."
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
                "Memory commitment evaluation result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Memory commitment evaluation result and error must use the same trace identity."
            }

            return MemoryCommitmentEvaluationResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
