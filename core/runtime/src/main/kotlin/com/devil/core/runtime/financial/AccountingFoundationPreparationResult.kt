package com.devil.core.runtime.financial

import com.devil.core.model.common.TraceId
import com.devil.core.model.financial.AccountingFoundationRecord

/**
 * Stable Stage 153 result of bounded Accounting Foundation preparation.
 *
 * PREPARED requires exactly one AccountingFoundationRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no bookkeeping, journal entry, ledger mutation,
 * reconciliation, financial statement, verified accounting correctness,
 * transaction, execution, constitutional Verification, or Memory persistence.
 */
@ConsistentCopyVisibility
data class AccountingFoundationPreparationResult private constructor(
    val traceId: TraceId,
    val status: AccountingFoundationPreparationStatus,
    val accountingFoundation: AccountingFoundationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: AccountingFoundationPreparationStatus,
            accountingFoundation: AccountingFoundationRecord? = null,
        ): AccountingFoundationPreparationResult {
            when (status) {
                AccountingFoundationPreparationStatus.PREPARED -> {
                    require(accountingFoundation != null) {
                        "Prepared Accounting Foundation results require one accounting context."
                    }
                }

                AccountingFoundationPreparationStatus.DEFERRED -> {
                    require(accountingFoundation == null) {
                        "Deferred Accounting Foundation results must not contain an accounting context."
                    }
                }
            }

            return AccountingFoundationPreparationResult(
                traceId = traceId,
                status = status,
                accountingFoundation = accountingFoundation,
            )
        }
    }
}
