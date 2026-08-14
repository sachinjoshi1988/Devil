package com.devil.core.model.financial

/**
 * Immutable Stage 89 representation of one explicitly supplied financial fact.
 *
 * label identifies the supplied financial field.
 *
 * value preserves its supplied bounded value.
 *
 * Stage 89 does not independently establish that the supplied value is true,
 * current, complete, externally verified, or suitable for a financial decision.
 *
 * A FinancialFact does not:
 *
 * - query a bank;
 * - query a broker;
 * - query an exchange;
 * - query a wallet;
 * - retrieve market prices;
 * - verify an account balance;
 * - verify a transaction;
 * - infer ownership;
 * - authenticate anyone;
 * - establish trust;
 * - grant authorization;
 * - move money;
 * - place an order;
 * - execute a trade;
 * - execute a payment;
 * - create a financial recommendation;
 * - create a constitutional Decision;
 * - create a Task or Plan;
 * - establish Observation, Verification, or Outcome;
 * - perform constitutional Learning;
 * - or create or commit Memory.
 *
 * SUPPLIED_FINANCIAL_FACT != VERIFIED_EXTERNAL_FINANCIAL_STATE.
 * FINANCIAL_FACT != TRANSACTION.
 * FINANCIAL_FACT != AUTHORITY.
 */
@ConsistentCopyVisibility
data class FinancialFact private constructor(
    val label: String,
    val value: String,
) {
    companion object {

        fun create(
            label: String,
            value: String,
        ): FinancialFact {
            val normalizedLabel =
                label.trim()

            val normalizedValue =
                value.trim()

            require(normalizedLabel.isNotEmpty()) {
                "Financial fact label must not be blank."
            }

            require(normalizedValue.isNotEmpty()) {
                "Financial fact value must not be blank."
            }

            return FinancialFact(
                label = normalizedLabel,
                value = normalizedValue,
            )
        }
    }
}
