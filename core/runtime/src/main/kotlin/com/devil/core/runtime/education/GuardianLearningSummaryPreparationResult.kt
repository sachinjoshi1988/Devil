package com.devil.core.runtime.education

import com.devil.core.model.common.TraceId
import com.devil.core.model.education.GuardianLearningSummaryRecord

/**
 * Stable Stage 150 result of bounded Guardian Learning Summary preparation.
 *
 * PREPARED requires exactly one GuardianLearningSummaryRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no guardian authentication, guardian authority,
 * disclosure authorization, disclosure occurrence, verified mastery,
 * constitutional Verification, execution, or Memory persistence.
 */
@ConsistentCopyVisibility
data class GuardianLearningSummaryPreparationResult private constructor(
    val traceId: TraceId,
    val status: GuardianLearningSummaryPreparationStatus,
    val guardianLearningSummary: GuardianLearningSummaryRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: GuardianLearningSummaryPreparationStatus,
            guardianLearningSummary: GuardianLearningSummaryRecord? = null,
        ): GuardianLearningSummaryPreparationResult {
            when (status) {
                GuardianLearningSummaryPreparationStatus.PREPARED -> {
                    require(guardianLearningSummary != null) {
                        "Prepared Guardian Learning Summary results require one summary context."
                    }
                }

                GuardianLearningSummaryPreparationStatus.DEFERRED -> {
                    require(guardianLearningSummary == null) {
                        "Deferred Guardian Learning Summary results must not contain a summary context."
                    }
                }
            }

            return GuardianLearningSummaryPreparationResult(
                traceId = traceId,
                status = status,
                guardianLearningSummary = guardianLearningSummary,
            )
        }
    }
}
