package com.devil.core.runtime.financial

import com.devil.core.model.common.TraceId
import com.devil.core.model.financial.IndianTaxAssistanceRecord

/**
 * Immutable Stage 156 preparation result.
 *
 * This result establishes no tax authority, verified current Indian tax law,
 * authoritative tax-liability calculation, regime selection, deduction
 * eligibility, return preparation, filing, portal access, verified compliance,
 * payment execution, constitutional Verification, execution authority, Memory
 * persistence, or Financial Document Intelligence.
 */
@ConsistentCopyVisibility
data class IndianTaxAssistancePreparationResult private constructor(
    val traceId: TraceId,
    val status: IndianTaxAssistancePreparationStatus,
    val assistance: IndianTaxAssistanceRecord?,
) {
    companion object {

        fun create(
            traceId: TraceId,
            status: IndianTaxAssistancePreparationStatus,
            assistance: IndianTaxAssistanceRecord? = null,
        ): IndianTaxAssistancePreparationResult {
            require(
                (status == IndianTaxAssistancePreparationStatus.PREPARED) ==
                    (assistance != null),
            ) {
                "Prepared Indian Tax Assistance requires assistance; deferred preparation must not contain assistance."
            }

            return IndianTaxAssistancePreparationResult(
                traceId = traceId,
                status = status,
                assistance = assistance,
            )
        }
    }
}
