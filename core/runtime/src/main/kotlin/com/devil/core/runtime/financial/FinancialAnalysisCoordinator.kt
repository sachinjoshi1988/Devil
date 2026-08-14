package com.devil.core.runtime.financial

import com.devil.core.model.common.TraceId
import com.devil.core.model.financial.FinancialAnalysisRecord
import com.devil.core.model.financial.FinancialAnalysisSubject
import com.devil.core.model.financial.FinancialFact

/**
 * Stage 89 bounded Financial Intelligence Foundation coordinator.
 *
 * This coordinator prepares one financial-analysis context from explicitly
 * supplied structured financial-domain inputs.
 *
 * Inputs are:
 *
 * - constitutional TraceId;
 * - explicit financial subject;
 * - explicitly supplied financial facts.
 *
 * It does not infer financial facts from unavailable sources.
 *
 * It does not:
 *
 * - create another Devil intelligence;
 * - create another Brain;
 * - create another Constitution;
 * - create another Executive;
 * - create another Planner;
 * - create another Unified Devil Runtime;
 * - create Financial-specific Memory or Security authorities;
 * - resolve or infer identity;
 * - authenticate a subject;
 * - establish trust;
 * - grant authorization;
 * - establish or validate a security session;
 * - enter Owner Mode;
 * - establish financial-account ownership;
 * - obtain account credentials;
 * - connect to banks;
 * - connect to brokers;
 * - connect to exchanges;
 * - connect to wallets;
 * - retrieve live market data;
 * - retrieve balances;
 * - retrieve transactions;
 * - fabricate missing financial information;
 * - verify supplied financial facts;
 * - produce financial advice;
 * - determine investment suitability;
 * - guarantee a return;
 * - predict a guaranteed market outcome;
 * - create a constitutional Decision;
 * - create Tasks or Plans;
 * - register, select, authorize, or activate capabilities;
 * - invoke UnifiedDevilRuntime;
 * - create execution requests;
 * - execute actions;
 * - transfer money;
 * - make payments;
 * - place orders;
 * - buy or sell assets;
 * - execute trades;
 * - create or alter financial accounts;
 * - establish Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - propose, commit, or persist Memory;
 * - persist financial state;
 * - or communicate with platform or financial APIs.
 *
 * SUPPLIED_FINANCIAL_FACT != VERIFIED_EXTERNAL_FINANCIAL_STATE.
 * FINANCIAL_INFORMATION != FINANCIAL_AUTHORITY.
 * FINANCIAL_ANALYSIS != FINANCIAL_ADVICE_GUARANTEE.
 * ANALYSIS != AUTHORIZATION.
 * AUTHORIZATION != PLATFORM_PERMISSION.
 * ANALYSIS != TRANSACTION.
 * PREPARATION != EXECUTION.
 * FINANCIAL_DOMAIN != ANOTHER_INTELLIGENCE.
 */
class FinancialAnalysisCoordinator {

    fun prepare(
        traceId: TraceId,
        subject: String,
        facts: List<Pair<String, String>>,
    ): FinancialAnalysisPreparationResult {
        if (
            subject.isBlank() ||
            facts.isEmpty() ||
            facts.any {
                it.first.isBlank() ||
                    it.second.isBlank()
            }
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val financialFacts =
            facts.map { (label, value) ->
                FinancialFact.create(
                    label = label,
                    value = value,
                )
            }

        val normalizedLabels =
            financialFacts.map {
                it.label.lowercase()
            }

        if (
            normalizedLabels.distinct().size !=
            normalizedLabels.size
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val record =
            FinancialAnalysisRecord.create(
                subject =
                    FinancialAnalysisSubject.from(
                        subject,
                    ),
                facts = financialFacts,
            )

        return FinancialAnalysisPreparationResult.create(
            traceId = traceId,
            status =
                FinancialAnalysisPreparationStatus.PREPARED,
            record = record,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): FinancialAnalysisPreparationResult {
        return FinancialAnalysisPreparationResult.create(
            traceId = traceId,
            status =
                FinancialAnalysisPreparationStatus.DEFERRED,
        )
    }
}
