package com.devil.core.model.financial

/**
 * Immutable Stage 156 representation of one bounded Indian Tax Assistance
 * context.
 *
 * This record preserves:
 *
 * - one existing Stage 155 Tax Intelligence Foundation context;
 * - one explicitly supplied nonblank Indian-tax assistance focus;
 * - one explicitly supplied nonblank assistance objective;
 * - one explicitly supplied nonblank India-tax context description.
 *
 * Stage 156 supports bounded Indian-tax assistance only.
 *
 * It does not:
 *
 * - infer or verify current Indian tax law;
 * - infer jurisdiction or tax residency;
 * - calculate authoritative tax liability;
 * - select an old, new, or other tax regime;
 * - determine eligibility for deductions, exemptions, allowances, or credits;
 * - determine an authoritative financial year or assessment year;
 * - determine an authoritative ITR form or filing category;
 * - prepare, file, or submit a tax return;
 * - access Income Tax, GST, or other tax portals;
 * - submit forms, declarations, or registrations;
 * - establish GST, TDS, or other tax obligations;
 * - establish verified tax compliance;
 * - establish filing authority;
 * - create payment instructions or execute tax payments;
 * - create a constitutional Decision;
 * - create Tasks or Plans;
 * - invoke Executive or execution;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external tax or financial providers;
 * - communicate with Android or platform APIs;
 * - inspect or extract financial documents;
 * - or implement Stage 157 Financial Document Intelligence.
 *
 * INDIAN_TAX_ASSISTANCE != TAX_AUTHORITY.
 * INDIAN_TAX_ASSISTANCE != VERIFIED_CURRENT_INDIAN_TAX_LAW.
 * INDIAN_TAX_ASSISTANCE != TAX_LIABILITY_CALCULATION.
 * INDIAN_TAX_ASSISTANCE != REGIME_SELECTION.
 * INDIAN_TAX_ASSISTANCE != DEDUCTION_ELIGIBILITY.
 * INDIAN_TAX_ASSISTANCE != RETURN_PREPARATION.
 * INDIAN_TAX_ASSISTANCE != TAX_FILING.
 * INDIAN_TAX_ASSISTANCE != TAX_PORTAL_ACCESS.
 * INDIAN_TAX_ASSISTANCE != VERIFIED_TAX_COMPLIANCE.
 * INDIAN_TAX_ASSISTANCE != PAYMENT_EXECUTION.
 * INDIAN_TAX_ASSISTANCE != FINANCIAL_DOCUMENT_INTELLIGENCE.
 * SUPPLIED_INDIA_TAX_CONTEXT != VERIFIED_CURRENT_INDIAN_TAX_LAW.
 */
@ConsistentCopyVisibility
data class IndianTaxAssistanceRecord private constructor(
    val taxIntelligenceFoundation: TaxIntelligenceFoundationRecord,
    val indianTaxFocus: String,
    val assistanceObjective: String,
    val indiaTaxContextDescription: String,
) {
    companion object {

        fun create(
            taxIntelligenceFoundation: TaxIntelligenceFoundationRecord,
            indianTaxFocus: String,
            assistanceObjective: String,
            indiaTaxContextDescription: String,
        ): IndianTaxAssistanceRecord {
            val normalizedIndianTaxFocus =
                indianTaxFocus.trim()

            val normalizedAssistanceObjective =
                assistanceObjective.trim()

            val normalizedIndiaTaxContextDescription =
                indiaTaxContextDescription.trim()

            require(normalizedIndianTaxFocus.isNotEmpty()) {
                "Indian Tax Assistance focus must not be blank."
            }

            require(normalizedAssistanceObjective.isNotEmpty()) {
                "Indian Tax Assistance objective must not be blank."
            }

            require(normalizedIndiaTaxContextDescription.isNotEmpty()) {
                "Indian Tax Assistance context description must not be blank."
            }

            return IndianTaxAssistanceRecord(
                taxIntelligenceFoundation = taxIntelligenceFoundation,
                indianTaxFocus = normalizedIndianTaxFocus,
                assistanceObjective = normalizedAssistanceObjective,
                indiaTaxContextDescription =
                    normalizedIndiaTaxContextDescription,
            )
        }
    }
}
