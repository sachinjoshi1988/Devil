package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.ChildEducationRecord

/**
 * Stable Stage 143 result of bounded Child Education integration preparation.
 *
 * PREPARED requires one ChildEducationRecord.
 * DEFERRED must not contain one.
 *
 * This result establishes no authentication, guardian authority, guardian
 * approval, constitutional authorization, teaching result, Task, Plan,
 * execution, Observation, Verification, Outcome, constitutional Learning,
 * Memory commitment, persistence, or verified learner progress.
 */
@ConsistentCopyVisibility
data class ChildEducationPreparationResult private constructor(
    val traceId: TraceId,
    val status: ChildEducationPreparationStatus,
    val childEducation: ChildEducationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: ChildEducationPreparationStatus,
            childEducation: ChildEducationRecord? = null,
        ): ChildEducationPreparationResult {
            when (status) {
                ChildEducationPreparationStatus.PREPARED -> {
                    require(childEducation != null) {
                        "Prepared Child Education results require one Child Education context."
                    }
                }

                ChildEducationPreparationStatus.DEFERRED -> {
                    require(childEducation == null) {
                        "Deferred Child Education results must not contain a Child Education context."
                    }
                }
            }

            return ChildEducationPreparationResult(
                traceId = traceId,
                status = status,
                childEducation = childEducation,
            )
        }
    }
}
