package com.devil.core.runtime.financial

/**
 * Stage 153 bounded Accounting Foundation preparation status.
 *
 * PREPARED means one structurally valid accounting-domain context was prepared
 * from an existing Stage 151 Financial Intelligence Integration context and
 * explicitly supplied accounting metadata.
 *
 * PREPARED does not mean:
 *
 * - bookkeeping was performed;
 * - a journal entry exists;
 * - debit or credit treatment was established;
 * - a ledger was mutated;
 * - accounts were reconciled;
 * - balances were calculated;
 * - a financial statement was produced;
 * - accounting correctness was verified;
 * - execution was authorized;
 * - accounting state was persisted;
 * - or Stage 154 Business Accounting Assistance was implemented.
 *
 * DEFERRED means no truthful Accounting Foundation context was produced.
 */
enum class AccountingFoundationPreparationStatus {
    PREPARED,
    DEFERRED,
}
