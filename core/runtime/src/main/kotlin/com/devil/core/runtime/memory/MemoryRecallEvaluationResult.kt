package com.devil.core.runtime.memory

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord
import com.devil.core.model.memory.MemoryRecallRequest

/**
 * Immutable Stage 106 result of bounded constitutional logical-memory recall
 * evaluation.
 *
 * RECALLABLE preserves exactly one already-existing Stage 105 MemoryRecallRequest.
 *
 * UNAVAILABLE contains neither request nor error.
 *
 * FAILED contains one matching error and no recall request.
 *
 * This result performs no storage read, retrieval, restoration, decryption,
 * recall, exposure, disclosure, presentation, persistence, deletion,
 * synchronization, replication, execution, or external communication.
 */
@ConsistentCopyVisibility
data class MemoryRecallEvaluationResult private constructor(
    val traceId: TraceId,
    val status: MemoryRecallEvaluationStatus,
    val request: MemoryRecallRequest?,
    val error: UniversalErrorRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: MemoryRecallEvaluationStatus,
            request: MemoryRecallRequest? = null,
            error: UniversalErrorRecord? = null,
        ): MemoryRecallEvaluationResult {
            when (status) {
                MemoryRecallEvaluationStatus.RECALLABLE -> {
                    require(request != null) {
                        "Recallable memory recall evaluation result requires one recall request."
                    }

                    require(error == null) {
                        "Recallable memory recall evaluation result must not contain an error."
                    }
                }

                MemoryRecallEvaluationStatus.UNAVAILABLE -> {
                    require(request == null) {
                        "Unavailable memory recall evaluation result must not contain a recall request."
                    }

                    require(error == null) {
                        "Unavailable memory recall evaluation result must not contain an error."
                    }
                }

                MemoryRecallEvaluationStatus.FAILED -> {
                    require(request == null) {
                        "Failed memory recall evaluation result must not contain a recall request."
                    }

                    require(error != null) {
                        "Failed memory recall evaluation result requires one error."
                    }
                }
            }

            require(
                request == null ||
                    request.traceId == traceId,
            ) {
                "Memory recall evaluation result and request must use the same trace identity."
            }

            require(
                error == null ||
                    error.traceId == traceId,
            ) {
                "Memory recall evaluation result and error must use the same trace identity."
            }

            return MemoryRecallEvaluationResult(
                traceId = traceId,
                status = status,
                request = request,
                error = error,
            )
        }
    }
}
