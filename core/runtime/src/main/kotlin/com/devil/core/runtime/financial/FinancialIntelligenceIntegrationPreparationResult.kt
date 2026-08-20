package com.devil.core.runtime.financial

import com.devil.core.model.common.TraceId
import com.devil.core.model.financial.FinancialIntelligenceIntegrationRecord

/**
 * Stable Stage 151 result of bounded Financial Intelligence Integration
 * preparation.
 *
 * PREPARED requires exactly one FinancialIntelligenceIntegrationRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no financial authority, account access, financial
 * advice, transaction, payment, trade, constitutional Verification, execution,
 * Memory persistence, or Personal Finance Assistance.
 */
@ConsistentCopyVisibility
data class FinancialIntelligenceIntegrationPreparationResult private constructor(
    val traceId: TraceId,
    val status: FinancialIntelligenceIntegrationPreparationStatus,
    val integration: FinancialIntelligenceIntegrationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: FinancialIntelligenceIntegrationPreparationStatus,
            integration: FinancialIntelligenceIntegrationRecord? = null,
        ): FinancialIntelligenceIntegrationPreparationResult {
            when (status) {
                FinancialIntelligenceIntegrationPreparationStatus.PREPARED -> {
                    require(integration != null) {
                        "Prepared Financial Intelligence Integration results require one integration context."
                    }
                }

                FinancialIntelligenceIntegrationPreparationStatus.DEFERRED -> {
                    require(integration == null) {
                        "Deferred Financial Intelligence Integration results must not contain an integration context."
                    }
                }
            }

            return FinancialIntelligenceIntegrationPreparationResult(
                traceId = traceId,
                status = status,
                integration = integration,
            )
        }
    }
}
