package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.AgeAppropriateTeachingRecord

/**
 * Stable Stage 145 result of bounded Age-Appropriate Teaching preparation.
 *
 * PREPARED requires exactly one AgeAppropriateTeachingRecord.
 * DEFERRED contains none.
 *
 * This result establishes no age proof, authentication, guardian authority,
 * guardian approval, constitutional authorization, execution, Observation,
 * Verification, Outcome, Learning, or Memory.
 */
@ConsistentCopyVisibility
data class AgeAppropriateTeachingPreparationResult private constructor(
    val traceId: TraceId,
    val status: AgeAppropriateTeachingPreparationStatus,
    val teaching: AgeAppropriateTeachingRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: AgeAppropriateTeachingPreparationStatus,
            teaching: AgeAppropriateTeachingRecord? = null,
        ): AgeAppropriateTeachingPreparationResult {
            when (status) {
                AgeAppropriateTeachingPreparationStatus.PREPARED -> {
                    require(teaching != null) {
                        "Prepared Age-Appropriate Teaching results require one teaching context."
                    }
                }

                AgeAppropriateTeachingPreparationStatus.DEFERRED -> {
                    require(teaching == null) {
                        "Deferred Age-Appropriate Teaching results must not contain a teaching context."
                    }
                }
            }

            return AgeAppropriateTeachingPreparationResult(
                traceId = traceId,
                status = status,
                teaching = teaching,
            )
        }
    }
}
