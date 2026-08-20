package com.devil.core.runtime.financial

import com.devil.core.model.common.TraceId
import com.devil.core.model.financial.FinancialSafetyVerificationRecord

/**
 * Stable Stage 158 result of bounded Financial Safety & Verification
 * preparation.
 *
 * PREPARED requires exactly one FinancialSafetyVerificationRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no constitutional Verification, verified external
 * financial state, document authenticity, transaction authenticity, fraud,
 * account authentication, financial guarantee, execution authorization,
 * transaction, or Memory persistence.
 */
@ConsistentCopyVisibility
data class FinancialSafetyVerificationPreparationResult private constructor(
    val traceId: TraceId,
    val status: FinancialSafetyVerificationPreparationStatus,
    val safetyVerification: FinancialSafetyVerificationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: FinancialSafetyVerificationPreparationStatus,
            safetyVerification: FinancialSafetyVerificationRecord? = null,
        ): FinancialSafetyVerificationPreparationResult {
            when (status) {
                FinancialSafetyVerificationPreparationStatus.PREPARED -> {
                    require(safetyVerification != null) {
                        "Prepared Financial Safety & Verification results require one safety context."
                    }
                }

                FinancialSafetyVerificationPreparationStatus.DEFERRED -> {
                    require(safetyVerification == null) {
                        "Deferred Financial Safety & Verification results must not contain a safety context."
                    }
                }
            }

            return FinancialSafetyVerificationPreparationResult(
                traceId = traceId,
                status = status,
                safetyVerification = safetyVerification,
            )
        }
    }
}
