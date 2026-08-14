package com.devil.core.runtime.financial

import com.devil.core.model.common.TraceId
import com.devil.core.model.financial.FinancialAnalysisRecord

/**
 * Stable Stage 89 result of bounded Financial Intelligence preparation.
 *
 * PREPARED requires exactly one FinancialAnalysisRecord.
 *
 * DEFERRED must not contain a record.
 *
 * This result creates no identity authority, authentication, trust,
 * authorization, security session, financial-account authority, financial
 * recommendation, Decision, Task, Plan, capability, execution request,
 * transaction, payment, transfer, order, trade, Observation, Verification,
 * Outcome, World Model mutation, constitutional Learning, Memory, or
 * persistence authority.
 */
@ConsistentCopyVisibility
data class FinancialAnalysisPreparationResult private constructor(
    val traceId: TraceId,
    val status: FinancialAnalysisPreparationStatus,
    val record: FinancialAnalysisRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: FinancialAnalysisPreparationStatus,
            record: FinancialAnalysisRecord? = null,
        ): FinancialAnalysisPreparationResult {
            when (status) {
                FinancialAnalysisPreparationStatus.PREPARED -> {
                    require(record != null) {
                        "Prepared financial-analysis results require one record."
                    }
                }

                FinancialAnalysisPreparationStatus.DEFERRED -> {
                    require(record == null) {
                        "Deferred financial-analysis results must not contain a record."
                    }
                }
            }

            return FinancialAnalysisPreparationResult(
                traceId = traceId,
                status = status,
                record = record,
            )
        }
    }
}
