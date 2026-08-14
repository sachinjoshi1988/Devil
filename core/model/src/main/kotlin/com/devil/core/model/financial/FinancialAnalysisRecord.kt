package com.devil.core.model.financial

/**
 * Immutable Stage 89 representation of one bounded Financial Intelligence
 * analysis context.
 *
 * The record preserves only explicitly supplied financial-domain information:
 *
 * - one bounded FinancialAnalysisSubject;
 * - and one nonempty ordered collection of supplied FinancialFact values.
 *
 * Duplicate normalized fact labels are rejected so one bounded record cannot
 * silently preserve conflicting fields under the same label.
 *
 * This record deliberately contains no:
 *
 * - another Devil intelligence;
 * - another Brain;
 * - another Constitution;
 * - another Executive;
 * - another Planner;
 * - another Unified Devil Runtime;
 * - Financial-specific Memory Authority;
 * - Financial-specific Security Authority;
 * - financial-account authority;
 * - identity authority;
 * - authentication;
 * - trust;
 * - authorization;
 * - security session;
 * - account credentials;
 * - bank connection;
 * - broker connection;
 * - exchange connection;
 * - wallet connection;
 * - live market feed;
 * - independently verified balance;
 * - independently verified transaction;
 * - financial recommendation;
 * - investment suitability determination;
 * - constitutional Decision;
 * - Task;
 * - Plan;
 * - capability binding;
 * - execution request;
 * - payment instruction;
 * - transfer instruction;
 * - order;
 * - trade;
 * - purchase;
 * - sale;
 * - Observation;
 * - Verification;
 * - Outcome;
 * - World Model mutation;
 * - constitutional Learning result;
 * - Memory commitment;
 * - or persistence authority.
 *
 * FINANCIAL_INTELLIGENCE = DOMAIN_OF_ONE_DEVIL_INTELLIGENCE.
 * FINANCIAL_INTELLIGENCE != ANOTHER_INTELLIGENCE.
 * FINANCIAL_ANALYSIS != FINANCIAL_AUTHORITY.
 * FINANCIAL_ANALYSIS != TRANSACTION.
 * FINANCIAL_ANALYSIS != EXECUTION.
 */
@ConsistentCopyVisibility
data class FinancialAnalysisRecord private constructor(
    val subject: FinancialAnalysisSubject,
    val facts: List<FinancialFact>,
) {
    companion object {

        fun create(
            subject: FinancialAnalysisSubject,
            facts: List<FinancialFact>,
        ): FinancialAnalysisRecord {
            require(facts.isNotEmpty()) {
                "Financial analysis requires at least one supplied financial fact."
            }

            val normalizedLabels =
                facts.map {
                    it.label.lowercase()
                }

            require(normalizedLabels.distinct().size == normalizedLabels.size) {
                "Financial analysis must not contain duplicate financial fact labels."
            }

            return FinancialAnalysisRecord(
                subject = subject,
                facts = facts.toList(),
            )
        }
    }
}
