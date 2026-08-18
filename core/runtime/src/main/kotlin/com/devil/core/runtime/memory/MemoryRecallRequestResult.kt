package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.memory.MemoryRecallRequest

/**
 * Immutable Stage 105 result produced while preparing one bounded constitutional
 * logical-memory recall request.
 *
 * AVAILABLE contains exactly one MemoryRecallRequest belonging to the same trace.
 *
 * UNAVAILABLE contains neither request nor error.
 *
 * FAILED contains one matching upstream error and no request.
 *
 * This result performs no storage read, retrieval, recall, exposure, disclosure,
 * persistence, deletion, synchronization, replication, decryption, execution, or
 * external communication.
 */
@ConsistentCopyVisibility
data class MemoryRecallRequestResult private constructor(
    val traceId: TraceId,
    val status: MemoryRecallRequestStatus,
    val request: MemoryRecallRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: MemoryRecallRequestStatus,
            request: MemoryRecallRequest? = null,
            error: UniversalErrorRecord? = null,
        ): MemoryRecallRequestResult {
            when (status) {
                MemoryRecallRequestStatus.AVAILABLE -> {
                    require(request != null) {
                        "Available memory recall request result requires one recall request."
                    }

                    require(error == null) {
                        "Available memory recall request result must not contain an error."
                    }
                }

                MemoryRecallRequestStatus.UNAVAILABLE -> {
                    require(request == null) {
                        "Unavailable memory recall request result must not contain a recall request."
                    }

                    require(error == null) {
                        "Unavailable memory recall request result must not contain an error."
                    }
                }

                MemoryRecallRequestStatus.FAILED -> {
                    require(request == null) {
                        "Failed memory recall request result must not contain a recall request."
                    }

                    require(error != null) {
                        "Failed memory recall request result requires one error."
                    }
                }
            }

            require(
                request == null ||
                    request.traceId == traceId,
            ) {
                "Memory recall request result and request must use the same trace identity."
            }

            require(
                error == null ||
                    error.traceId == traceId,
            ) {
                "Memory recall request result and error must use the same trace identity."
            }

            return MemoryRecallRequestResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
