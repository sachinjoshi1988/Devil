package com.devil.core.model.financial

/**
 * Immutable Stage 158 representation of one bounded Financial Safety &
 * Verification context.
 *
 * This record preserves:
 *
 * - one existing Stage 151 Financial Intelligence Integration context;
 * - one explicitly supplied nonblank financial-safety focus;
 * - one explicitly supplied nonblank verification-basis description;
 * - one explicitly supplied nonblank bounded safety interpretation.
 *
 * Stage 158 represents Financial Domain safety and verification interpretation
 * only.
 *
 * It does not:
 *
 * - access banks, brokers, exchanges, wallets, payment services, tax portals,
 *   or accounting systems;
 * - retrieve balances, transactions, prices, or market data;
 * - perform OCR or document extraction;
 * - authenticate users, accounts, or documents;
 * - establish document authenticity;
 * - establish transaction authenticity;
 * - establish fraud as fact;
 * - establish verified external financial state;
 * - certify correctness or guarantee financial safety;
 * - calculate authoritative financial or tax results;
 * - create a constitutional Decision;
 * - create Tasks or Plans;
 * - invoke Executive or execution;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external providers;
 * - communicate with Android or platform APIs;
 * - authorize financial action;
 * - or implement Stage 159 Legal Intelligence Foundation.
 *
 * FINANCIAL_SAFETY_VERIFICATION != CONSTITUTIONAL_VERIFICATION.
 * FINANCIAL_SAFETY_VERIFICATION != VERIFIED_EXTERNAL_FINANCIAL_STATE.
 * FINANCIAL_SAFETY_VERIFICATION != DOCUMENT_AUTHENTICITY.
 * FINANCIAL_SAFETY_VERIFICATION != FRAUD_ESTABLISHED.
 * FINANCIAL_SAFETY_VERIFICATION != TRANSACTION_AUTHENTICITY.
 * FINANCIAL_SAFETY_VERIFICATION != ACCOUNT_AUTHENTICATION.
 * FINANCIAL_SAFETY_VERIFICATION != FINANCIAL_GUARANTEE.
 * FINANCIAL_SAFETY_VERIFICATION != EXECUTION_AUTHORIZATION.
 * FINANCIAL_SAFETY_VERIFICATION != TRANSACTION.
 * SUPPLIED_VERIFICATION_BASIS != CONSTITUTIONAL_OBSERVATION.
 * SAFETY_INTERPRETATION != VERIFIED_OUTCOME.
 */
@ConsistentCopyVisibility
data class FinancialSafetyVerificationRecord private constructor(
    val financialIntelligenceIntegration: FinancialIntelligenceIntegrationRecord,
    val safetyFocus: String,
    val verificationBasisDescription: String,
    val safetyInterpretation: String,
) {
    companion object {

        fun create(
            financialIntelligenceIntegration: FinancialIntelligenceIntegrationRecord,
            safetyFocus: String,
            verificationBasisDescription: String,
            safetyInterpretation: String,
        ): FinancialSafetyVerificationRecord {
            val normalizedSafetyFocus =
                safetyFocus.trim()

            val normalizedVerificationBasisDescription =
                verificationBasisDescription.trim()

            val normalizedSafetyInterpretation =
                safetyInterpretation.trim()

            require(normalizedSafetyFocus.isNotEmpty()) {
                "Financial Safety & Verification focus must not be blank."
            }

            require(normalizedVerificationBasisDescription.isNotEmpty()) {
                "Financial Safety & Verification basis description must not be blank."
            }

            require(normalizedSafetyInterpretation.isNotEmpty()) {
                "Financial Safety & Verification interpretation must not be blank."
            }

            return FinancialSafetyVerificationRecord(
                financialIntelligenceIntegration =
                    financialIntelligenceIntegration,
                safetyFocus = normalizedSafetyFocus,
                verificationBasisDescription =
                    normalizedVerificationBasisDescription,
                safetyInterpretation =
                    normalizedSafetyInterpretation,
            )
        }
    }
}
