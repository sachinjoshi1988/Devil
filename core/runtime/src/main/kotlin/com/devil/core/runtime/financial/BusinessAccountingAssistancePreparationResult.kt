package com.devil.core.runtime.financial

import com.devil.core.model.common.TraceId
import com.devil.core.model.financial.BusinessAccountingAssistanceRecord

/**
 * Stable Stage 154 result of bounded Business Accounting Assistance preparation.
 *
 * PREPARED requires exactly one BusinessAccountingAssistanceRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no bookkeeping execution, journal posting, ledger
 * mutation, reconciliation, verified financial statement, accounting-system
 * access, payment execution, tax intelligence, constitutional Verification,
 * execution authority, or Memory persistence.
 */
@ConsistentCopyVisibility
data class BusinessAccountingAssistancePreparationResult private constructor(
    val traceId: TraceId,
    val status: BusinessAccountingAssistancePreparationStatus,
    val assistance: BusinessAccountingAssistanceRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: BusinessAccountingAssistancePreparationStatus,
            assistance: BusinessAccountingAssistanceRecord? = null,
        ): BusinessAccountingAssistancePreparationResult {
            when (status) {
                BusinessAccountingAssistancePreparationStatus.PREPARED -> {
                    require(assistance != null) {
                        "Prepared Business Accounting Assistance results require one assistance context."
                    }
                }

                BusinessAccountingAssistancePreparationStatus.DEFERRED -> {
                    require(assistance == null) {
                        "Deferred Business Accounting Assistance results must not contain an assistance context."
                    }
                }
            }

            return BusinessAccountingAssistancePreparationResult(
                traceId = traceId,
                status = status,
                assistance = assistance,
            )
        }
    }
}
