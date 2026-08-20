package com.devil.core.runtime.legal

/**
 * Stage 160 bounded Legal Research preparation status.
 *
 * PREPARED means one structurally valid supplied legal-research context was
 * prepared from one existing Stage 159 Legal Intelligence Foundation context
 * and explicitly supplied research metadata.
 *
 * PREPARED does not mean:
 *
 * - legal sources were fetched;
 * - jurisdiction was determined;
 * - current law was verified;
 * - authoritative citations were created or verified;
 * - precedent was established;
 * - legal advice was produced;
 * - legal rights or liability were determined;
 * - constitutional Verification occurred;
 * - legal documents were understood;
 * - or Stage 161 Legal Document Understanding was implemented.
 *
 * DEFERRED means no truthful Legal Research context was produced.
 */
enum class LegalResearchPreparationStatus {
    PREPARED,
    DEFERRED,
}
