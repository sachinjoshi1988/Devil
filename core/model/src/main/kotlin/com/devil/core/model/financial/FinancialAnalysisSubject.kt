package com.devil.core.model.financial

/**
 * Immutable Stage 89 representation of one explicitly supplied bounded
 * financial-analysis subject.
 *
 * Examples may include:
 *
 * - household budget;
 * - monthly expenses;
 * - savings plan;
 * - investment portfolio;
 * - business revenue;
 * - loan comparison;
 * - or another bounded financial subject.
 *
 * This value is descriptive metadata only.
 *
 * It does not establish:
 *
 * - ownership of a financial account;
 * - identity;
 * - authentication;
 * - trust;
 * - authorization;
 * - access to a bank, broker, exchange, wallet, payment service, or tax system;
 * - correctness of financial information;
 * - current market state;
 * - account balance;
 * - transaction existence;
 * - financial advice;
 * - investment suitability;
 * - a constitutional Decision;
 * - a Task or Plan;
 * - capability availability;
 * - execution authority;
 * - payment authority;
 * - trading authority;
 * - Observation;
 * - Verification;
 * - Outcome;
 * - constitutional Learning;
 * - Memory eligibility;
 * - or persistence authority.
 *
 * FINANCIAL_SUBJECT != FINANCIAL_AUTHORITY.
 * FINANCIAL_SUBJECT != ACCOUNT_ACCESS.
 * FINANCIAL_SUBJECT != EXECUTION.
 */
@ConsistentCopyVisibility
data class FinancialAnalysisSubject private constructor(
    val value: String,
) {
    companion object {

        fun from(rawValue: String): FinancialAnalysisSubject {
            val normalizedValue =
                rawValue.trim()

            require(normalizedValue.isNotEmpty()) {
                "Financial analysis subject must not be blank."
            }

            return FinancialAnalysisSubject(
                value = normalizedValue,
            )
        }
    }
}
