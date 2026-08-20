package com.devil.core.runtime.financial

import com.devil.core.model.common.TraceId
import com.devil.core.model.financial.FinancialIntelligenceIntegrationRecord
import com.devil.core.model.financial.PersonalFinanceAssistanceRecord

/**
 * Stage 152 bounded Personal Finance Assistance coordinator.
 *
 * This coordinator prepares one personal-finance assistance context from one
 * existing Stage 151 Financial Intelligence Integration context and explicitly
 * supplied assistance metadata.
 *
 * Stage 151 remains authoritative for Financial Intelligence Integration
 * provenance.
 *
 * This coordinator does not:
 *
 * - create or infer financial facts;
 * - verify supplied financial information;
 * - retrieve external financial state;
 * - establish account ownership or account access;
 * - obtain credentials;
 * - connect to banks, brokers, exchanges, wallets, or payment services;
 * - retrieve balances, transactions, prices, or market data;
 * - determine regulated investment suitability;
 * - guarantee returns or financial outcomes;
 * - create trade, payment, transfer, purchase, or sale instructions;
 * - move money;
 * - execute payments or trades;
 * - create Decisions, Tasks, or Plans;
 * - register, select, or activate capabilities;
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
class PersonalFinanceAssistanceCoordinator {

    fun prepare(
        traceId: TraceId,
        financialIntelligenceIntegration: FinancialIntelligenceIntegrationRecord,
        assistanceFocus: String,
        assistanceObjective: String,
        assistanceApproach: String,
    ): PersonalFinanceAssistancePreparationResult {
        if (
            assistanceFocus.isBlank() ||
            assistanceObjective.isBlank() ||
            assistanceApproach.isBlank()
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val assistance =
            PersonalFinanceAssistanceRecord.create(
                financialIntelligenceIntegration =
                    financialIntelligenceIntegration,
                assistanceFocus = assistanceFocus,
                assistanceObjective = assistanceObjective,
                assistanceApproach = assistanceApproach,
            )

        return PersonalFinanceAssistancePreparationResult.create(
            traceId = traceId,
            status = PersonalFinanceAssistancePreparationStatus.PREPARED,
            assistance = assistance,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): PersonalFinanceAssistancePreparationResult {
        return PersonalFinanceAssistancePreparationResult.create(
            traceId = traceId,
            status = PersonalFinanceAssistancePreparationStatus.DEFERRED,
        )
    }
}
