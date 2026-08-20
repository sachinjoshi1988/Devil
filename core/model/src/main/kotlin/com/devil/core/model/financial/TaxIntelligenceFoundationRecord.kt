package com.devil.core.model.financial

/**
 * Immutable Stage 155 representation of one bounded Tax Intelligence Foundation
 * context.
 *
 * This record preserves:
 *
 * - one existing Stage 151 Financial Intelligence Integration context;
 * - one explicitly supplied nonblank tax focus;
 * - one explicitly supplied nonblank tax objective;
 * - one explicitly supplied nonblank tax-context description.
 *
 * Stage 155 establishes structural tax-intelligence context only.
 *
 * It does not:
 *
 * - infer jurisdiction;
 * - determine tax residency;
 * - calculate authoritative tax liability;
 * - determine deductions, exemptions, allowances, or credits;
 * - prepare a tax return;
 * - file or submit a tax return;
 * - access a tax portal;
 * - submit forms or declarations;
 * - verify current tax law;
 * - establish tax compliance;
 * - establish filing authority;
 * - execute tax payments;
 * - create a constitutional Decision;
 * - create Tasks or Plans;
 * - invoke Executive or execution;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external tax or financial providers;
 * - communicate with Android or platform APIs;
 * - or implement Stage 156 Indian Tax Assistance.
 *
 * TAX_INTELLIGENCE_FOUNDATION != TAX_AUTHORITY.
 * TAX_INTELLIGENCE_FOUNDATION != TAX_LIABILITY_CALCULATION.
 * TAX_INTELLIGENCE_FOUNDATION != TAX_RETURN_PREPARATION.
 * TAX_INTELLIGENCE_FOUNDATION != TAX_FILING.
 * TAX_INTELLIGENCE_FOUNDATION != TAX_PORTAL_ACCESS.
 * TAX_INTELLIGENCE_FOUNDATION != VERIFIED_TAX_COMPLIANCE.
 * TAX_INTELLIGENCE_FOUNDATION != PAYMENT_EXECUTION.
 * TAX_INTELLIGENCE_FOUNDATION != INDIAN_TAX_ASSISTANCE.
 * SUPPLIED_TAX_CONTEXT != VERIFIED_CURRENT_TAX_LAW.
 */
@ConsistentCopyVisibility
data class TaxIntelligenceFoundationRecord private constructor(
    val financialIntelligenceIntegration: FinancialIntelligenceIntegrationRecord,
    val taxFocus: String,
    val taxObjective: String,
    val taxContextDescription: String,
) {
    companion object {

        fun create(
            financialIntelligenceIntegration: FinancialIntelligenceIntegrationRecord,
            taxFocus: String,
            taxObjective: String,
            taxContextDescription: String,
        ): TaxIntelligenceFoundationRecord {
            val normalizedTaxFocus =
                taxFocus.trim()

            val normalizedTaxObjective =
                taxObjective.trim()

            val normalizedTaxContextDescription =
                taxContextDescription.trim()

            require(normalizedTaxFocus.isNotEmpty()) {
                "Tax Intelligence Foundation focus must not be blank."
            }

            require(normalizedTaxObjective.isNotEmpty()) {
                "Tax Intelligence Foundation objective must not be blank."
            }

            require(normalizedTaxContextDescription.isNotEmpty()) {
                "Tax Intelligence Foundation context description must not be blank."
            }

            return TaxIntelligenceFoundationRecord(
                financialIntelligenceIntegration =
                    financialIntelligenceIntegration,
                taxFocus = normalizedTaxFocus,
                taxObjective = normalizedTaxObjective,
                taxContextDescription =
                    normalizedTaxContextDescription,
            )
        }
    }
}
