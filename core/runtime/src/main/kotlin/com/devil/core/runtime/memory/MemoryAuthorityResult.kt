package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.memory.MemoryAuthorityRequest

/**
 * Represents the stable operational result of constitutional Memory Authority
 * evaluation.
 *
 * A committable result preserves one MemoryAuthorityRequest for which genuine
 * constitutional evidence was established. Preserving that request does not
 * create, persist, or commit logical memory.
 *
 * A deferred result contains neither request nor error. A failed result contains
 * one matching error.
 */
@ConsistentCopyVisibility
data class MemoryAuthorityResult private constructor(
    val traceId: TraceId,
    val status: MemoryAuthorityStatus,
    val request: MemoryAuthorityRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: MemoryAuthorityStatus,
            request: MemoryAuthorityRequest? = null,
            error: UniversalErrorRecord? = null,
        ): MemoryAuthorityResult {
            when (status) {
                MemoryAuthorityStatus.COMMITTABLE -> {
                    require(request != null && error == null) {
                        "Committable Memory Authority results require a request and must not contain an error."
                    }
                }

                MemoryAuthorityStatus.DEFERRED -> {
                    require(request == null && error == null) {
                        "Deferred Memory Authority results must not contain a request or error."
                    }
                }

                MemoryAuthorityStatus.FAILED -> {
                    require(request == null && error != null) {
                        "Failed Memory Authority results require an error and must not contain a request."
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
                "Memory Authority result and request must use the same trace identity."
            }

            require(error == null || error.traceId == traceId) {
                "Memory Authority result and error must use the same trace identity."
            }

            return MemoryAuthorityResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
