package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.memory.MemoryPersistenceRequest

/**
 * Represents the stable operational result of constitutional logical-memory
 * persistence evaluation.
 *
 * A persistable result preserves one MemoryPersistenceRequest for which genuine
 * constitutional persistence eligibility was established.
 *
 * Preserving that request does not create, persist, store, expose, recall,
 * delete, or commit logical memory.
 *
 * A deferred result contains neither request nor error.
 *
 * A failed result contains one matching error.
 */
@ConsistentCopyVisibility
data class MemoryPersistenceResult private constructor(
    val traceId: TraceId,
    val status: MemoryPersistenceStatus,
    val request: MemoryPersistenceRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: MemoryPersistenceStatus,
            request: MemoryPersistenceRequest? = null,
            error: UniversalErrorRecord? = null,
        ): MemoryPersistenceResult {
            when (status) {
                MemoryPersistenceStatus.PERSISTABLE -> {
                    require(request != null && error == null) {
                        "Persistable memory persistence results require a request and must not contain an error."
                    }
                }

                MemoryPersistenceStatus.DEFERRED -> {
                    require(request == null && error == null) {
                        "Deferred memory persistence results must not contain a request or error."
                    }
                }

                MemoryPersistenceStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed memory persistence results require an error and must not contain a request."
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
                "Memory persistence result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Memory persistence result and error must use the same trace identity."
            }

            return MemoryPersistenceResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
