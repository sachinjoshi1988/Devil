package com.devil.core.model.legal

/**
 * Immutable Stage 160 representation of one bounded Legal Research context.
 *
 * This record preserves:
 *
 * - one existing Stage 159 Legal Intelligence Foundation context;
 * - one explicitly supplied nonblank legal-research focus;
 * - one explicitly supplied nonblank supplied legal-source description;
 * - one explicitly supplied nonblank research objective.
 *
 * Stage 160 represents supplied Legal Research context only.
 *
 * It does not:
 *
 * - fetch statutes, regulations, cases, precedents, or court records;
 * - access courts, tribunals, registries, government portals, or legal systems;
 * - infer jurisdiction;
 * - verify current law;
 * - create or verify authoritative legal citations;
 * - establish precedent;
 * - provide legal advice;
 * - establish legal authority;
 * - determine legal rights or obligations;
 * - determine liability;
 * - establish a legal conclusion;
 * - parse or authenticate legal documents;
 * - create a constitutional Decision;
 * - create Tasks or Plans;
 * - invoke Executive or execution;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external legal providers;
 * - communicate with Android or platform APIs;
 * - or implement Stage 161 Legal Document Understanding.
 *
 * LEGAL_RESEARCH != VERIFIED_CURRENT_LAW.
 * LEGAL_RESEARCH != JURISDICTION_DETERMINATION.
 * LEGAL_RESEARCH != LEGAL_ADVICE.
 * LEGAL_RESEARCH != LEGAL_AUTHORITY.
 * LEGAL_RESEARCH != AUTHORITATIVE_CITATION.
 * LEGAL_RESEARCH != PRECEDENT_ESTABLISHED.
 * LEGAL_RESEARCH != RIGHTS_DETERMINATION.
 * LEGAL_RESEARCH != LIABILITY_DETERMINATION.
 * LEGAL_RESEARCH != COURT_ACCESS.
 * LEGAL_RESEARCH != CONSTITUTIONAL_VERIFICATION.
 * SUPPLIED_LEGAL_SOURCE_DESCRIPTION != VERIFIED_LEGAL_SOURCE.
 * RESEARCH_INTERPRETATION != LEGAL_CONCLUSION.
 */
@ConsistentCopyVisibility
data class LegalResearchRecord private constructor(
    val legalFoundation: LegalIntelligenceFoundationRecord,
    val researchFocus: String,
    val suppliedLegalSourceDescription: String,
    val researchObjective: String,
) {
    companion object {

        fun create(
            legalFoundation: LegalIntelligenceFoundationRecord,
            researchFocus: String,
            suppliedLegalSourceDescription: String,
            researchObjective: String,
        ): LegalResearchRecord {
            val normalizedResearchFocus =
                researchFocus.trim()

            val normalizedSuppliedLegalSourceDescription =
                suppliedLegalSourceDescription.trim()

            val normalizedResearchObjective =
                researchObjective.trim()

            require(normalizedResearchFocus.isNotEmpty()) {
                "Legal Research focus must not be blank."
            }

            require(normalizedSuppliedLegalSourceDescription.isNotEmpty()) {
                "Legal Research supplied source description must not be blank."
            }

            require(normalizedResearchObjective.isNotEmpty()) {
                "Legal Research objective must not be blank."
            }

            return LegalResearchRecord(
                legalFoundation = legalFoundation,
                researchFocus = normalizedResearchFocus,
                suppliedLegalSourceDescription =
                    normalizedSuppliedLegalSourceDescription,
                researchObjective = normalizedResearchObjective,
            )
        }
    }
}
