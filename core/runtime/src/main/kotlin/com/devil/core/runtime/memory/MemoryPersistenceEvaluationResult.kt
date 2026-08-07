package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.memory.MemoryPersistenceRequest

/**
 * Represents the bounded result of constitutional logical-memory persistence
 * evaluation.
 *
 * A persistable result preserves one evaluated MemoryPersistenceRequest.
 *
 * Preserving that request does not create, persist, store, expose, recall,
 * delete, or commit logical memory.
 *
 * An unavailable result contains neither request nor error.
 *
 * A failed result contains one matching error.
 */
@ConsistentCopyVisibility
data class MemoryPersistenceEvaluationResult private constructor(
    val traceId: TraceId,
    val status: MemoryPersistenceEvaluationStatus,
    val request: MemoryPersistenceRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: MemoryPersistenceEvaluationStatus,
            request: MemoryPersistenceRequest? = null,
            error: UniversalErrorRecord? = null,
        ): MemoryPersistenceEvaluationResult {
            when (status) {
                MemoryPersistenceEvaluationStatus.PERSISTABLE -> {
                    require(request != null && error == null) {
                        "Persistable memory persistence evaluation results require a request and must not contain an error."
                    }
                }

                MemoryPersistenceEvaluationStatus.UNAVAILABLE -> {
                    require(request == null && error == null) {
                        "Unavailable memory persistence evaluation results must not contain a request or error."
                    }
                }

                MemoryPersistenceEvaluationStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed memory persistence evaluation results require an error and must not contain a request."
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
                "Memory persistence evaluation result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Memory persistence evaluation result and error must use the same trace identity."
            }

            return MemoryPersistenceEvaluationResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
