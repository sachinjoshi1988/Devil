package com.devil.core.runtime.financial

import com.devil.core.model.common.TraceId
import com.devil.core.model.financial.PersonalFinanceAssistanceRecord

/**
 * Stable Stage 152 result of bounded Personal Finance Assistance preparation.
 *
 * PREPARED requires exactly one PersonalFinanceAssistanceRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no financial authority, account access, verified
 * financial state, investment suitability, transaction, execution, accounting,
 * constitutional Verification, or Memory persistence.
 */
@ConsistentCopyVisibility
data class PersonalFinanceAssistancePreparationResult private constructor(
    val traceId: TraceId,
    val status: PersonalFinanceAssistancePreparationStatus,
    val assistance: PersonalFinanceAssistanceRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: PersonalFinanceAssistancePreparationStatus,
            assistance: PersonalFinanceAssistanceRecord? = null,
        ): PersonalFinanceAssistancePreparationResult {
            when (status) {
                PersonalFinanceAssistancePreparationStatus.PREPARED -> {
                    require(assistance != null) {
                        "Prepared Personal Finance Assistance results require one assistance context."
                    }
                }

                PersonalFinanceAssistancePreparationStatus.DEFERRED -> {
                    require(assistance == null) {
                        "Deferred Personal Finance Assistance results must not contain an assistance context."
                    }
                }
            }

            return PersonalFinanceAssistancePreparationResult(
                traceId = traceId,
                status = status,
                assistance = assistance,
            )
        }
    }
}
