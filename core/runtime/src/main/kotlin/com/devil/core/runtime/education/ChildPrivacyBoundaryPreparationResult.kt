package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.ChildPrivacyBoundaryRecord

/**
 * Stable Stage 146 result of bounded Child Privacy Boundary preparation.
 *
 * PREPARED requires exactly one ChildPrivacyBoundaryRecord.
 * DEFERRED contains no boundary record.
 */
@ConsistentCopyVisibility
data class ChildPrivacyBoundaryPreparationResult private constructor(
    val traceId: TraceId,
    val status: ChildPrivacyBoundaryPreparationStatus,
    val childPrivacyBoundary: ChildPrivacyBoundaryRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: ChildPrivacyBoundaryPreparationStatus,
            childPrivacyBoundary: ChildPrivacyBoundaryRecord? = null,
        ): ChildPrivacyBoundaryPreparationResult {
            when (status) {
                ChildPrivacyBoundaryPreparationStatus.PREPARED -> {
                    require(childPrivacyBoundary != null) {
                        "Prepared Child Privacy Boundary results require one boundary context."
                    }
                }

                ChildPrivacyBoundaryPreparationStatus.DEFERRED -> {
                    require(childPrivacyBoundary == null) {
                        "Deferred Child Privacy Boundary results must not contain a boundary context."
                    }
                }
            }

            return ChildPrivacyBoundaryPreparationResult(
                traceId = traceId,
                status = status,
                childPrivacyBoundary = childPrivacyBoundary,
            )
        }
    }
}
