package com.devil.core.model.legal

/**
 * Immutable Stage 159 representation of one bounded Legal Intelligence
 * Foundation context.
 *
 * This record preserves only explicitly supplied legal-domain context:
 *
 * - one nonblank legal subject;
 * - one nonblank legal objective;
 * - one nonempty ordered collection of explicitly supplied legal context items.
 *
 * Stage 159 establishes structural Legal Intelligence context only.
 *
 * It does not:
 *
 * - infer jurisdiction;
 * - verify current law;
 * - provide legal advice;
 * - determine legal rights or obligations;
 * - determine liability;
 * - establish a legal conclusion;
 * - establish legal authority;
 * - authenticate legal documents;
 * - access courts, tribunals, registries, government portals, or legal systems;
 * - create legal citations or perform legal research;
 * - create a constitutional Decision;
 * - create Tasks or Plans;
 * - invoke Executive or execution;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external legal providers;
 * - communicate with Android or platform APIs;
 * - or implement Stage 160 Legal Research.
 *
 * LEGAL_INTELLIGENCE_FOUNDATION != LEGAL_ADVICE.
 * LEGAL_INTELLIGENCE_FOUNDATION != LEGAL_AUTHORITY.
 * LEGAL_INTELLIGENCE_FOUNDATION != VERIFIED_CURRENT_LAW.
 * LEGAL_INTELLIGENCE_FOUNDATION != JURISDICTION_DETERMINATION.
 * LEGAL_INTELLIGENCE_FOUNDATION != RIGHTS_DETERMINATION.
 * LEGAL_INTELLIGENCE_FOUNDATION != LIABILITY_DETERMINATION.
 * LEGAL_INTELLIGENCE_FOUNDATION != LEGAL_CONCLUSION.
 * LEGAL_INTELLIGENCE_FOUNDATION != COURT_ACCESS.
 * LEGAL_INTELLIGENCE_FOUNDATION != DOCUMENT_AUTHENTICITY.
 * LEGAL_INTELLIGENCE_FOUNDATION != CONSTITUTIONAL_VERIFICATION.
 * SUPPLIED_LEGAL_CONTEXT != VERIFIED_LEGAL_FACT.
 */
@ConsistentCopyVisibility
data class LegalIntelligenceFoundationRecord private constructor(
    val legalSubject: String,
    val legalObjective: String,
    val suppliedLegalContext: List<String>,
) {
    companion object {

        fun create(
            legalSubject: String,
            legalObjective: String,
            suppliedLegalContext: List<String>,
        ): LegalIntelligenceFoundationRecord {
            val normalizedLegalSubject =
                legalSubject.trim()

            val normalizedLegalObjective =
                legalObjective.trim()

            val normalizedSuppliedLegalContext =
                suppliedLegalContext.map { it.trim() }

            require(normalizedLegalSubject.isNotEmpty()) {
                "Legal Intelligence Foundation subject must not be blank."
            }

            require(normalizedLegalObjective.isNotEmpty()) {
                "Legal Intelligence Foundation objective must not be blank."
            }

            require(normalizedSuppliedLegalContext.isNotEmpty()) {
                "Legal Intelligence Foundation requires supplied legal context."
            }

            require(normalizedSuppliedLegalContext.none { it.isEmpty() }) {
                "Legal Intelligence Foundation context items must not be blank."
            }

            return LegalIntelligenceFoundationRecord(
                legalSubject = normalizedLegalSubject,
                legalObjective = normalizedLegalObjective,
                suppliedLegalContext =
                    normalizedSuppliedLegalContext.toList(),
            )
        }
    }
}
