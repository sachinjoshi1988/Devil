package com.devil.core.model.legal

/**
 * Immutable Stage 164 representation of one bounded Legal Evidence & Citation
 * context.
 *
 * This record preserves:
 *
 * - one existing Stage 159 Legal Intelligence Foundation context;
 * - one explicitly supplied nonblank evidence/citation focus;
 * - one explicitly supplied nonblank legal source/evidence description;
 * - one explicitly supplied nonblank citation objective.
 *
 * Stage 164 represents supplied legal evidence-and-citation context only.
 *
 * It does not:
 *
 * - verify current law;
 * - infer jurisdiction;
 * - provide legal advice;
 * - determine legal rights;
 * - determine legal obligations;
 * - determine liability;
 * - establish authoritative legal procedure;
 * - authenticate supplied evidence;
 * - determine evidence admissibility;
 * - determine evidence weight;
 * - establish that a supplied source is legally authoritative;
 * - verify that a citation is correct, current, or authoritative;
 * - establish precedent or controlling authority;
 * - access courts, tribunals, registries, government portals, legal databases,
 *   or external legal systems;
 * - file or submit documents;
 * - create a constitutional Decision;
 * - create Tasks or Plans;
 * - invoke Executive or execution;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external legal providers;
 * - communicate with Android or platform APIs;
 * - duplicate Stage 108 Source & Evidence Intelligence;
 * - or implement Stage 165 High-Stakes Legal Safety.
 *
 * SUPPLIED_LEGAL_EVIDENCE != VERIFIED_EVIDENCE.
 * SUPPLIED_CITATION != AUTHORITATIVE_CITATION.
 * LEGAL_EVIDENCE_CITATION_CONTEXT != LEGAL_CONCLUSION.
 * PREPARED_CONTEXT != VERIFIED_LEGAL_FACT.
 * LEGAL_EVIDENCE_CITATION != HIGH_STAKES_LEGAL_SAFETY.
 */
@ConsistentCopyVisibility
data class LegalEvidenceCitationRecord private constructor(
    val legalFoundation: LegalIntelligenceFoundationRecord,
    val evidenceCitationFocus: String,
    val suppliedLegalSourceEvidenceDescription: String,
    val citationObjective: String,
) {
    companion object {

        fun create(
            legalFoundation: LegalIntelligenceFoundationRecord,
            evidenceCitationFocus: String,
            suppliedLegalSourceEvidenceDescription: String,
            citationObjective: String,
        ): LegalEvidenceCitationRecord {
            val normalizedEvidenceCitationFocus =
                evidenceCitationFocus.trim()

            val normalizedSuppliedLegalSourceEvidenceDescription =
                suppliedLegalSourceEvidenceDescription.trim()

            val normalizedCitationObjective =
                citationObjective.trim()

            require(normalizedEvidenceCitationFocus.isNotEmpty()) {
                "Legal Evidence & Citation focus must not be blank."
            }

            require(
                normalizedSuppliedLegalSourceEvidenceDescription.isNotEmpty(),
            ) {
                "Legal source/evidence description must not be blank."
            }

            require(normalizedCitationObjective.isNotEmpty()) {
                "Legal citation objective must not be blank."
            }

            return LegalEvidenceCitationRecord(
                legalFoundation = legalFoundation,
                evidenceCitationFocus = normalizedEvidenceCitationFocus,
                suppliedLegalSourceEvidenceDescription =
                    normalizedSuppliedLegalSourceEvidenceDescription,
                citationObjective = normalizedCitationObjective,
            )
        }
    }
}
