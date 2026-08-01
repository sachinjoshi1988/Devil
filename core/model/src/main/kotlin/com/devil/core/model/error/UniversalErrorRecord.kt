package com.devil.core.model.error

import com.devil.core.model.common.DevilTimestamp
import com.devil.core.model.common.TraceId

/**
 * Represents one constitutionally recognized Devil error.
 *
 * This record captures only the stable constitutional representation of a
 * failure. It does not contain exceptions, stack traces, logging details,
 * platform information, retry state, or runtime diagnostics.
 */
@ConsistentCopyVisibility
data class UniversalErrorRecord private constructor(
    val errorCode: ErrorCode,
    val traceId: TraceId,
    val occurredAt: DevilTimestamp,
    val summary: String,
) {
    companion object {
        fun create(
            errorCode: ErrorCode,
            traceId: TraceId,
            occurredAt: DevilTimestamp,
            summary: String,
        ): UniversalErrorRecord {
            val normalizedSummary = summary.trim()

            require(normalizedSummary.isNotEmpty()) {
                "Error summary must not be blank."
            }

            return UniversalErrorRecord(
                errorCode = errorCode,
                traceId = traceId,
                occurredAt = occurredAt,
                summary = normalizedSummary,
            )
        }
    }
}
