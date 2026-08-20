package com.devil.core.model.legal

/**
 * Immutable Stage 162 representation of one bounded Legal Drafting Assistance
 * context.
 *
 * This record preserves:
 *
 * - one existing Stage 159 Legal Intelligence Foundation context;
 * - one explicitly supplied nonblank drafting focus;
 * - one explicitly supplied nonblank requested draft purpose;
 * - one explicitly supplied nonblank drafting objective.
 *
 * Stage 162 represents bounded drafting-assistance context only.
 *
 * It does not:
 *
 * - verify current law;
 * - provide legal advice;
 * - establish legal authority;
 * - determine legal effect;
 * - determine enforceability;
 * - determine legal rights or obligations;
 * - determine liability;
 * - create an authoritative legal document;
 * - establish that any draft is legally sufficient, complete, or valid;
 * - establish a legal conclusion;
 * - file or submit a legal document;
 * - access courts, tribunals, registries, government portals, or legal systems;
 * - sign or execute a legal document;
 * - create a constitutional Decision;
 * - create Tasks or Plans;
 * - invoke Executive or execution;
 * - establish constitutional Observation, Verification, or Outcome;
 * - mutate World Model state;
 * - perform constitutional Learning;
 * - create or persist Memory;
 * - invoke external legal providers;
 * - communicate with Android or platform APIs;
 * - determine rights or procedure under Stage 163;
 * - or implement Stage 163 Rights & Procedure Guidance.
 *
 * LEGAL_DRAFTING_ASSISTANCE != LEGAL_ADVICE.
 * LEGAL_DRAFTING_ASSISTANCE != LEGAL_AUTHORITY.
 * LEGAL_DRAFTING_ASSISTANCE != VERIFIED_CURRENT_LAW.
 * LEGAL_DRAFTING_ASSISTANCE != LEGAL_EFFECT_DETERMINATION.
 * LEGAL_DRAFTING_ASSISTANCE != ENFORCEABILITY_DETERMINATION.
 * LEGAL_DRAFTING_ASSISTANCE != RIGHTS_DETERMINATION.
 * LEGAL_DRAFTING_ASSISTANCE != LIABILITY_DETERMINATION.
 * LEGAL_DRAFTING_ASSISTANCE != AUTHORITATIVE_LEGAL_DOCUMENT.
 * LEGAL_DRAFTING_ASSISTANCE != FILING.
 * LEGAL_DRAFTING_ASSISTANCE != SIGNATURE.
 * LEGAL_DRAFTING_ASSISTANCE != EXECUTION.
 * LEGAL_DRAFTING_ASSISTANCE != CONSTITUTIONAL_VERIFICATION.
 * SUPPLIED_DRAFTING_CONTEXT != VERIFIED_LEGAL_FACT.
 * DRAFTING_ASSISTANCE != LEGAL_CONCLUSION.
 */
@ConsistentCopyVisibility
data class LegalDraftingAssistanceRecord private constructor(
    val legalFoundation: LegalIntelligenceFoundationRecord,
    val draftingFocus: String,
    val requestedDraftPurpose: String,
    val draftingObjective: String,
) {
    companion object {

        fun create(
            legalFoundation: LegalIntelligenceFoundationRecord,
            draftingFocus: String,
            requestedDraftPurpose: String,
            draftingObjective: String,
        ): LegalDraftingAssistanceRecord {
            val normalizedDraftingFocus =
                draftingFocus.trim()

            val normalizedRequestedDraftPurpose =
                requestedDraftPurpose.trim()

            val normalizedDraftingObjective =
                draftingObjective.trim()

            require(normalizedDraftingFocus.isNotEmpty()) {
                "Legal Drafting Assistance focus must not be blank."
            }

            require(normalizedRequestedDraftPurpose.isNotEmpty()) {
                "Legal Drafting Assistance requested draft purpose must not be blank."
            }

            require(normalizedDraftingObjective.isNotEmpty()) {
                "Legal Drafting Assistance objective must not be blank."
            }

            return LegalDraftingAssistanceRecord(
                legalFoundation = legalFoundation,
                draftingFocus = normalizedDraftingFocus,
                requestedDraftPurpose =
                    normalizedRequestedDraftPurpose,
                draftingObjective = normalizedDraftingObjective,
            )
        }
    }
}
