package com.devil.core.runtime.financial

/**
 * Stage 155 bounded Tax Intelligence Foundation preparation status.
 *
 * PREPARED means one structurally valid tax-intelligence context was prepared
 * from an existing Stage 151 Financial Intelligence Integration context and
 * explicitly supplied tax metadata.
 *
 * PREPARED does not mean:
 *
 * - jurisdiction was inferred;
 * - tax residency was determined;
 * - tax liability was calculated;
 * - deductions, exemptions, or credits were established;
 * - a tax return was prepared or filed;
 * - a tax portal was accessed;
 * - current tax law was verified;
 * - tax compliance was established;
 * - a tax payment was executed;
 * - or Stage 156 Indian Tax Assistance was implemented.
 *
 * DEFERRED means no truthful Tax Intelligence Foundation context was produced.
 */
enum class TaxIntelligenceFoundationPreparationStatus {
    PREPARED,
    DEFERRED,
}
