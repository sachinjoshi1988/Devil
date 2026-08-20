package com.devil.core.model.financial

/**
 * Immutable Stage 151 representation of one bounded Financial Intelligence
 * Integration context.
 *
 * This record preserves:
 *
 * - one existing Stage 89 FinancialAnalysisRecord exactly;
 * - one explicitly supplied nonblank financial-integration focus;
 * - one explicitly supplied nonblank financial-integration objective.
 *
 * Stage 151 integrates the existing bounded Financial Intelligence domain into
 * the post-150 architecture without replacing Stage 89.
 *
 * It does not:
 *
 * - create or infer financial facts;
 * - verify supplied financial information;
 * - establish current external financial state;
 * - establish account ownership;
 * - obtain credentials;
 * - access a bank, broker, exchange, wallet, payment service, or tax system;
 * - retrieve balances, transactions, prices, or market data;
 * - produce financial advice or investment suitability;
 * - implement Personal Finance Assistance;
 * - perform accounting or tax analysis;
 * - create a constitutional Decision;
 * - create Tasks or Plans;
 * - register or select capabilities;
 * - invoke Executive or execution;
 * - move money;
 * - make payments;
 * - place orders or execute trades;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external financial providers;
 * - communicate with Android or platform APIs;
 * - or implement Stage 152 Personal Finance Assistance.
 *
 * FINANCIAL_INTELLIGENCE_INTEGRATION != FINANCIAL_AUTHORITY.
 * FINANCIAL_INTELLIGENCE_INTEGRATION != ACCOUNT_ACCESS.
 * SUPPLIED_FINANCIAL_ANALYSIS != VERIFIED_EXTERNAL_STATE.
 * FINANCIAL_INTEGRATION != FINANCIAL_ADVICE.
 * FINANCIAL_INTEGRATION != TRANSACTION.
 * FINANCIAL_INTEGRATION != EXECUTION.
 * FINANCIAL_INTEGRATION != PERSONAL_FINANCE_ASSISTANCE.
 */
@ConsistentCopyVisibility
data class FinancialIntelligenceIntegrationRecord private constructor(
    val financialAnalysis: FinancialAnalysisRecord,
    val integrationFocus: String,
    val integrationObjective: String,
) {
    companion object {

        fun create(
            financialAnalysis: FinancialAnalysisRecord,
            integrationFocus: String,
            integrationObjective: String,
        ): FinancialIntelligenceIntegrationRecord {
            val normalizedIntegrationFocus =
                integrationFocus.trim()

            val normalizedIntegrationObjective =
                integrationObjective.trim()

            require(normalizedIntegrationFocus.isNotEmpty()) {
                "Financial Intelligence Integration focus must not be blank."
            }

            require(normalizedIntegrationObjective.isNotEmpty()) {
                "Financial Intelligence Integration objective must not be blank."
            }

            return FinancialIntelligenceIntegrationRecord(
                financialAnalysis = financialAnalysis,
                integrationFocus = normalizedIntegrationFocus,
                integrationObjective = normalizedIntegrationObjective,
            )
        }
    }
}
