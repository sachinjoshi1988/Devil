package com.devil.core.model.financial

/**
 * Immutable Stage 153 representation of one bounded Accounting Foundation
 * context.
 *
 * This record preserves:
 *
 * - one existing Stage 151 Financial Intelligence Integration context;
 * - one explicitly supplied nonblank accounting focus;
 * - one explicitly supplied nonblank accounting objective;
 * - one explicitly supplied nonblank accounting basis description.
 *
 * Stage 153 establishes structural accounting-domain context only.
 *
 * It does not:
 *
 * - create or infer financial facts;
 * - create journal entries;
 * - infer debit or credit treatment;
 * - maintain or mutate a ledger;
 * - reconcile accounts;
 * - calculate balances;
 * - classify transactions automatically;
 * - perform bookkeeping;
 * - generate financial statements;
 * - apply or verify accounting standards;
 * - establish verified accounting correctness;
 * - establish account ownership or access;
 * - access accounting, banking, payment, or tax systems;
 * - alter financial records;
 * - create a constitutional Decision;
 * - create Tasks or Plans;
 * - invoke Executive or execution;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external financial providers;
 * - communicate with Android or platform APIs;
 * - or implement Stage 154 Business Accounting Assistance.
 *
 * ACCOUNTING_FOUNDATION != BOOKKEEPING_PERFORMED.
 * ACCOUNTING_FOUNDATION != JOURNAL_ENTRY.
 * ACCOUNTING_FOUNDATION != LEDGER_MUTATION.
 * ACCOUNTING_FOUNDATION != ACCOUNT_RECONCILIATION.
 * ACCOUNTING_FOUNDATION != VERIFIED_ACCOUNTING.
 * ACCOUNTING_FOUNDATION != FINANCIAL_STATEMENT.
 * ACCOUNTING_FOUNDATION != TRANSACTION.
 * ACCOUNTING_FOUNDATION != EXECUTION.
 * ACCOUNTING_FOUNDATION != BUSINESS_ACCOUNTING_ASSISTANCE.
 * SUPPLIED_FINANCIAL_FACT != VERIFIED_ACCOUNTING_FACT.
 */
@ConsistentCopyVisibility
data class AccountingFoundationRecord private constructor(
    val financialIntelligenceIntegration: FinancialIntelligenceIntegrationRecord,
    val accountingFocus: String,
    val accountingObjective: String,
    val accountingBasisDescription: String,
) {
    companion object {

        fun create(
            financialIntelligenceIntegration: FinancialIntelligenceIntegrationRecord,
            accountingFocus: String,
            accountingObjective: String,
            accountingBasisDescription: String,
        ): AccountingFoundationRecord {
            val normalizedAccountingFocus =
                accountingFocus.trim()

            val normalizedAccountingObjective =
                accountingObjective.trim()

            val normalizedAccountingBasisDescription =
                accountingBasisDescription.trim()

            require(normalizedAccountingFocus.isNotEmpty()) {
                "Accounting Foundation focus must not be blank."
            }

            require(normalizedAccountingObjective.isNotEmpty()) {
                "Accounting Foundation objective must not be blank."
            }

            require(normalizedAccountingBasisDescription.isNotEmpty()) {
                "Accounting Foundation basis description must not be blank."
            }

            return AccountingFoundationRecord(
                financialIntelligenceIntegration =
                    financialIntelligenceIntegration,
                accountingFocus = normalizedAccountingFocus,
                accountingObjective = normalizedAccountingObjective,
                accountingBasisDescription =
                    normalizedAccountingBasisDescription,
            )
        }
    }
}
