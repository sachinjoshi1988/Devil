package com.devil.core.runtime.financial

/**
 * Stage 156 preparation status for bounded Indian Tax Assistance.
 *
 * PREPARED means only that explicitly supplied Stage 156 context was accepted.
 *
 * It does not mean:
 *
 * - current Indian tax law was verified;
 * - tax liability was calculated;
 * - a tax regime was selected;
 * - deduction eligibility was established;
 * - a tax return was prepared or filed;
 * - a tax portal was accessed;
 * - tax compliance was established;
 * - a tax payment was executed;
 * - financial documents were inspected or extracted;
 * - or Stage 157 Financial Document Intelligence was implemented.
 */
enum class IndianTaxAssistancePreparationStatus {
    PREPARED,
    DEFERRED,
}
