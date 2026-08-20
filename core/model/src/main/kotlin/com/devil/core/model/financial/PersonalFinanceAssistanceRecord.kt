package com.devil.core.model.financial

/**
 * Immutable Stage 152 representation of one bounded Personal Finance Assistance
 * context.
 *
 * This record preserves:
 *
 * - one existing Stage 151 Financial Intelligence Integration context;
 * - one explicitly supplied nonblank personal-finance assistance focus;
 * - one explicitly supplied nonblank assistance objective;
 * - one explicitly supplied nonblank assistance approach.
 *
 * Stage 152 supports bounded personal-finance assistance only.
 *
 * It does not:
 *
 * - create or infer missing financial facts;
 * - verify supplied financial information;
 * - establish current external financial state;
 * - establish account ownership;
 * - obtain credentials;
 * - access a bank, broker, exchange, wallet, payment service, or tax system;
 * - retrieve balances, transactions, prices, or market data;
 * - determine regulated investment suitability;
 * - guarantee returns or financial outcomes;
 * - create trade, payment, transfer, purchase, or sale instructions;
 * - move money;
 * - execute payments or trades;
 * - create a constitutional Decision;
 * - create Tasks or Plans;
 * - register or activate capabilities;
 * - invoke Executive or execution;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external financial providers;
 * - communicate with Android or platform APIs;
 * - perform accounting;
 * - or implement Stage 153 Accounting Foundation.
 *
 * PERSONAL_FINANCE_ASSISTANCE != FINANCIAL_AUTHORITY.
 * PERSONAL_FINANCE_ASSISTANCE != ACCOUNT_ACCESS.
 * PERSONAL_FINANCE_ASSISTANCE != VERIFIED_FINANCIAL_STATE.
 * PERSONAL_FINANCE_ASSISTANCE != INVESTMENT_SUITABILITY.
 * PERSONAL_FINANCE_ASSISTANCE != TRANSACTION.
 * PERSONAL_FINANCE_ASSISTANCE != EXECUTION.
 * PERSONAL_FINANCE_ASSISTANCE != ACCOUNTING.
 * SUPPLIED_FINANCIAL_FACT != CURRENT_EXTERNAL_FACT.
 */
@ConsistentCopyVisibility
data class PersonalFinanceAssistanceRecord private constructor(
    val financialIntelligenceIntegration: FinancialIntelligenceIntegrationRecord,
    val assistanceFocus: String,
    val assistanceObjective: String,
    val assistanceApproach: String,
) {
    companion object {

        fun create(
            financialIntelligenceIntegration: FinancialIntelligenceIntegrationRecord,
            assistanceFocus: String,
            assistanceObjective: String,
            assistanceApproach: String,
        ): PersonalFinanceAssistanceRecord {
            val normalizedAssistanceFocus =
                assistanceFocus.trim()

            val normalizedAssistanceObjective =
                assistanceObjective.trim()

            val normalizedAssistanceApproach =
                assistanceApproach.trim()

            require(normalizedAssistanceFocus.isNotEmpty()) {
                "Personal Finance Assistance focus must not be blank."
            }

            require(normalizedAssistanceObjective.isNotEmpty()) {
                "Personal Finance Assistance objective must not be blank."
            }

            require(normalizedAssistanceApproach.isNotEmpty()) {
                "Personal Finance Assistance approach must not be blank."
            }

            return PersonalFinanceAssistanceRecord(
                financialIntelligenceIntegration =
                    financialIntelligenceIntegration,
                assistanceFocus = normalizedAssistanceFocus,
                assistanceObjective = normalizedAssistanceObjective,
                assistanceApproach = normalizedAssistanceApproach,
            )
        }
    }
}
