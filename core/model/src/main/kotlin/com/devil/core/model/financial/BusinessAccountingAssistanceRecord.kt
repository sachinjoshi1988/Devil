package com.devil.core.model.financial

/**
 * Immutable Stage 154 representation of one bounded Business Accounting
 * Assistance context.
 *
 * This record preserves:
 *
 * - one existing Stage 153 Accounting Foundation context;
 * - one explicitly supplied nonblank business-accounting focus;
 * - one explicitly supplied nonblank assistance objective;
 * - one explicitly supplied nonblank assistance approach.
 *
 * Stage 154 supports bounded business-accounting assistance only.
 *
 * It does not:
 *
 * - create or infer financial facts;
 * - create or post journal entries;
 * - infer authoritative debit or credit treatment;
 * - maintain or mutate a ledger;
 * - reconcile accounts;
 * - calculate authoritative balances;
 * - generate verified financial statements;
 * - alter accounting records;
 * - establish verified accounting correctness;
 * - access accounting, banking, payment, or tax systems;
 * - establish account ownership or access;
 * - execute bookkeeping operations;
 * - execute payments or transfers;
 * - create a constitutional Decision;
 * - create Tasks or Plans;
 * - invoke Executive or execution;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external financial providers;
 * - communicate with Android or platform APIs;
 * - perform tax analysis;
 * - or implement Stage 155 Tax Intelligence Foundation.
 *
 * BUSINESS_ACCOUNTING_ASSISTANCE != BOOKKEEPING_EXECUTION.
 * BUSINESS_ACCOUNTING_ASSISTANCE != JOURNAL_POSTING.
 * BUSINESS_ACCOUNTING_ASSISTANCE != LEDGER_MUTATION.
 * BUSINESS_ACCOUNTING_ASSISTANCE != ACCOUNT_RECONCILIATION.
 * BUSINESS_ACCOUNTING_ASSISTANCE != VERIFIED_FINANCIAL_STATEMENT.
 * BUSINESS_ACCOUNTING_ASSISTANCE != ACCOUNTING_SYSTEM_ACCESS.
 * BUSINESS_ACCOUNTING_ASSISTANCE != PAYMENT_EXECUTION.
 * BUSINESS_ACCOUNTING_ASSISTANCE != TAX_INTELLIGENCE.
 */
@ConsistentCopyVisibility
data class BusinessAccountingAssistanceRecord private constructor(
    val accountingFoundation: AccountingFoundationRecord,
    val businessAccountingFocus: String,
    val assistanceObjective: String,
    val assistanceApproach: String,
) {
    companion object {

        fun create(
            accountingFoundation: AccountingFoundationRecord,
            businessAccountingFocus: String,
            assistanceObjective: String,
            assistanceApproach: String,
        ): BusinessAccountingAssistanceRecord {
            val normalizedBusinessAccountingFocus =
                businessAccountingFocus.trim()

            val normalizedAssistanceObjective =
                assistanceObjective.trim()

            val normalizedAssistanceApproach =
                assistanceApproach.trim()

            require(normalizedBusinessAccountingFocus.isNotEmpty()) {
                "Business Accounting Assistance focus must not be blank."
            }

            require(normalizedAssistanceObjective.isNotEmpty()) {
                "Business Accounting Assistance objective must not be blank."
            }

            require(normalizedAssistanceApproach.isNotEmpty()) {
                "Business Accounting Assistance approach must not be blank."
            }

            return BusinessAccountingAssistanceRecord(
                accountingFoundation = accountingFoundation,
                businessAccountingFocus =
                    normalizedBusinessAccountingFocus,
                assistanceObjective = normalizedAssistanceObjective,
                assistanceApproach = normalizedAssistanceApproach,
            )
        }
    }
}
