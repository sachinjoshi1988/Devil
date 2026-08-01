package com.devil.core.runtime.executive

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the structured result of Executive readiness evaluation.
 *
 * Ready and deferred results contain no error. A failed result requires a
 * matching UniversalErrorRecord from the same trace.
 */
@ConsistentCopyVisibility
data class ExecutiveReadinessResult private constructor(
    val traceId: TraceId,
    val status: ExecutiveReadinessStatus,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: ExecutiveReadinessStatus,
            error: UniversalErrorRecord? = null,
        ): ExecutiveReadinessResult {
            require(
                (status == ExecutiveReadinessStatus.FAILED) == (error != null),
            ) {
                "Failed readiness results must contain an error and non-failed results must not."
            }

            require(error == null || error.traceId == traceId) {
                "Executive readiness result and error must use the same trace identity."
            }

            return ExecutiveReadinessResult(
                traceId = traceId,
                status = status,
                error = error,
            )
        }
    }
}
