package com.devil.core.runtime.legal

import com.devil.core.model.common.TraceId
import com.devil.core.model.legal.LegalIntelligenceFoundationRecord
import com.devil.core.model.legal.LegalResearchRecord

/**
 * Stage 160 bounded Legal Research coordinator.
 *
 * This coordinator prepares one supplied legal-research context from one
 * existing Stage 159 Legal Intelligence Foundation context and explicitly
 * supplied research metadata.
 *
 * Stage 159 remains authoritative for preserved Legal Intelligence Foundation
 * provenance.
 *
 * This coordinator does not:
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
 * - create Decisions, Tasks, or Plans;
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
class LegalResearchCoordinator {

    fun prepare(
        traceId: TraceId,
        legalFoundation: LegalIntelligenceFoundationRecord,
        researchFocus: String,
        suppliedLegalSourceDescription: String,
        researchObjective: String,
    ): LegalResearchPreparationResult {
        if (
            researchFocus.isBlank() ||
            suppliedLegalSourceDescription.isBlank() ||
            researchObjective.isBlank()
        ) {
            return deferred(
                traceId = traceId,
            )
        }

        val legalResearch =
            LegalResearchRecord.create(
                legalFoundation = legalFoundation,
                researchFocus = researchFocus,
                suppliedLegalSourceDescription =
                    suppliedLegalSourceDescription,
                researchObjective = researchObjective,
            )

        return LegalResearchPreparationResult.create(
            traceId = traceId,
            status = LegalResearchPreparationStatus.PREPARED,
            legalResearch = legalResearch,
        )
    }

    private fun deferred(
        traceId: TraceId,
    ): LegalResearchPreparationResult {
        return LegalResearchPreparationResult.create(
            traceId = traceId,
            status = LegalResearchPreparationStatus.DEFERRED,
        )
    }
}
