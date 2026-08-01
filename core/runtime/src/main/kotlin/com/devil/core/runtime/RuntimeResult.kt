package com.devil.core.runtime

import com.devil.core.model.common.TraceId
import com.devil.core.model.error.UniversalErrorRecord

/**
 * Represents the immediate constitutional result of submitting work to the
 * Devil runtime.
 *
 * This result does not represent execution progress or verified outcomes.
 */
@ConsistentCopyVisibility
data class RuntimeResult private constructor(
    val traceId: TraceId,
    val status: RuntimeStatus,
    val error: UniversalErrorRecord?,
) {
    companion object {
        fun create(
            traceId: TraceId,
            status: RuntimeStatus,
            error: UniversalErrorRecord? = null,
        ): RuntimeResult {
            require(
                (status == RuntimeStatus.REJECTED) == (error != null),
            ) {
                "Rejected runtime results must contain an error and non-rejected results must not."
            }

            require(error == null || error.traceId == traceId) {
                "Runtime result and error must use the same trace identity."
            }

            return RuntimeResult(
                traceId = traceId,
                status = status,
                error = error,
            )
        }
    }
}
