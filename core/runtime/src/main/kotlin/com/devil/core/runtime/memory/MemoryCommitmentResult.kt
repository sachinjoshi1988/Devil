package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.memory.MemoryCommitmentRequest

/**
 * Represents the stable operational result of constitutional logical-memory
 * commitment evaluation.
 *
 * A committable result preserves one MemoryCommitmentRequest for which genuine
 * constitutional evidence was established. Preserving that request does not
 * create, persist, store, expose, recall, or commit logical memory.
 *
 * A deferred result contains neither request nor error. A failed result contains
 * one matching error.
 */
@ConsistentCopyVisibility
data class MemoryCommitmentResult private constructor(
    val traceId: TraceId,
    val status: MemoryCommitmentStatus,
    val request: MemoryCommitmentRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: MemoryCommitmentStatus,
            request: MemoryCommitmentRequest? = null,
            error: UniversalErrorRecord? = null,
        ): MemoryCommitmentResult {
            when (status) {
                MemoryCommitmentStatus.COMMITTABLE -> {
                    require(request != null && error == null) {
                        "Committable memory commitment results require a request and must not contain an error."
                    }
                }

                MemoryCommitmentStatus.DEFERRED -> {
                    require(request == null && error == null) {
                        "Deferred memory commitment results must not contain a request or error."
                    }
                }

                MemoryCommitmentStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed memory commitment results require an error and must not contain a request."
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
                "Memory commitment result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Memory commitment result and error must use the same trace identity."
            }

            return MemoryCommitmentResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
