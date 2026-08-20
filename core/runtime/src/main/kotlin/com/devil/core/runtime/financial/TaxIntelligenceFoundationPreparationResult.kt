package com.devil.core.runtime.financial

import com.devil.core.model.common.TraceId
import com.devil.core.model.financial.TaxIntelligenceFoundationRecord

/**
 * Stable Stage 155 result of bounded Tax Intelligence Foundation preparation.
 *
 * PREPARED requires exactly one TaxIntelligenceFoundationRecord.
 * DEFERRED must contain none.
 *
 * This result establishes no tax authority, tax-liability calculation, return
 * preparation, filing, portal access, verified compliance, payment execution,
 * constitutional Verification, execution authority, or Memory persistence.
 */
@ConsistentCopyVisibility
data class TaxIntelligenceFoundationPreparationResult private constructor(
    val traceId: TraceId,
    val status: TaxIntelligenceFoundationPreparationStatus,
    val taxFoundation: TaxIntelligenceFoundationRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: TaxIntelligenceFoundationPreparationStatus,
            taxFoundation: TaxIntelligenceFoundationRecord? = null,
        ): TaxIntelligenceFoundationPreparationResult {
            when (status) {
                TaxIntelligenceFoundationPreparationStatus.PREPARED -> {
                    require(taxFoundation != null) {
                        "Prepared Tax Intelligence Foundation results require one tax context."
                    }
                }

                TaxIntelligenceFoundationPreparationStatus.DEFERRED -> {
                    require(taxFoundation == null) {
                        "Deferred Tax Intelligence Foundation results must not contain a tax context."
                    }
                }
            }

            return TaxIntelligenceFoundationPreparationResult(
                traceId = traceId,
                status = status,
                taxFoundation = taxFoundation,
            )
        }
    }
}
