package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.memory.MemoryAuthorityRequest

/**
 * Represents the bounded result of constitutional Memory Authority evaluation.
 *
 * A committable result preserves the evaluated MemoryAuthorityRequest.
 * Preserving that request does not create, persist, or commit logical memory.
 *
 * An unavailable result contains neither request nor error. A failed result
 * contains one matching error.
 */
@ConsistentCopyVisibility
data class MemoryAuthorityEvaluationResult private constructor(
    val traceId: TraceId,
    val status: MemoryAuthorityEvaluationStatus,
    val request: MemoryAuthorityRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: MemoryAuthorityEvaluationStatus,
            request: MemoryAuthorityRequest? = null,
            error: UniversalErrorRecord? = null,
        ): MemoryAuthorityEvaluationResult {
            when (status) {
                MemoryAuthorityEvaluationStatus.COMMITTABLE -> {
                    require(request != null && error == null) {
                        "Committable Memory Authority evaluation results require a request and must not contain an error."
                    }
                }

                MemoryAuthorityEvaluationStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable Memory Authority evaluation results must not contain a request or error."
                    }
                }

                MemoryAuthorityEvaluationStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed Memory Authority evaluation results require an error and must not contain a request."
                    }
                }
            }

            require(
                request == null ||
                    request.proposal
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
                "Memory Authority evaluation result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Memory Authority evaluation result and error must use the same trace identity."
            }

            return MemoryAuthorityEvaluationResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
